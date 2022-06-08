package controllers

import database.DAO
import errors.Forbidden
import graphql.{GraphQL, GraphQLContext}
import graphql.fetchers.{OrderFetchers, UserFetchers}
import graphql.violations.{TooComplexQueryError, UnsupportedBodyTypeError}
import org.slf4j.LoggerFactory
import play.api.libs.json.{JsArray, JsObject, JsString, JsValue, Json}
import play.api.mvc.{AbstractController, ControllerComponents, Request}
import sangria.execution.deferred.DeferredResolver
import sangria.execution.{ErrorWithResolver, ExceptionHandler, Executor, HandledException, MaxQueryDepthReachedError, QueryAnalysisError}
import sangria.marshalling.MarshallingUtil._
import sangria.marshalling.playJson._
import sangria.parser.{QueryParser, SyntaxError}
import sangria.validation.UndefinedFieldViolation
import java.sql.SQLException
import javax.inject.Inject
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}


class GraphQLController @Inject()(
                                   dao: DAO,
                                   graphQL: GraphQL
                                 )(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  val logger = LoggerFactory.getLogger("GraphQLController")

  var requestObj: Request[JsValue] = _

  def graphiql = Action {
    Ok(views.html.graphiql())
  }

  def graphql = Action.async(parse.json) { implicit request =>
    requestObj = request
    val extract: JsValue => (String, Option[String], Option[JsObject]) = query => (
      (query \ "query").as[String],
      (query \ "operationName").asOpt[String],
      (query \ "variables").toOption.flatMap {
        case JsString(vars) => Some(parseVariables(vars))
        case obj: JsObject => Some(obj)
        case _ => None
      }
    )

    val maybeQuery: Try[(String, Option[String], Option[JsObject])] = Try {
      request.body match {
        case arrayBody@JsArray(_) => extract(arrayBody.value(0))
        case objectBody@JsObject(_) => extract(objectBody)
        case otherType => throw UnsupportedBodyTypeError(s"/graphql endpoint does not support request body of type [${otherType.getClass.getSimpleName}]")
      }
    }

    maybeQuery match {
      case Success((query, operationName, variables)) =>
        val httpContext = GraphQLContext(request.headers, request.cookies, ListBuffer.empty, ListBuffer.empty, dao)
        executeQuery(query, variables, operationName, httpContext)
      case Failure(error) => Future.successful(BadRequest(error.getMessage))
    }
  }

  def parseVariables(variables: String): JsObject =
    if (variables.trim.isEmpty || variables.trim == "null") Json.obj()
    else Json.parse(variables).as[JsObject]

  /** All error handling logic goes in here */
  private val ErrorHandler = ExceptionHandler(
    onException = {
      case (_, e: SQLException) =>
        logError("ErrorHandle", e)
        HandledException(e.getMessage)
      case (_, e: Forbidden) =>
        logError("ErrorHandle", e)
        HandledException(e.getMessage)
      case (_, e: NoSuchElementException) =>
        logError("ErrorHandle", e)
        HandledException("The specified element could not be found")
      case (_, e: TooComplexQueryError) =>
        logError("ErrorHandle", e)
        HandledException(e.getMessage)
      case (_, e: MaxQueryDepthReachedError) =>
        logError("ErrorHandle", e)
        HandledException(e.getMessage)
      case (m, e: Exception) =>
        logError("ErrorHandle", e)
        HandledException(e.getMessage, Map("code" -> m.scalarNode(1, "Int", Set.empty)))
    },
    onViolation = {
      case (resultMarshaller, violation: UndefinedFieldViolation) =>
        HandledException("Field is missing!",
          Map(
            "fieldName" -> resultMarshaller.fromString(violation.fieldName),
            "errorCode" -> resultMarshaller.fromString("FIELD_MISSING"))
        )
    }
  )

  private def logError(funcName: String, e: Throwable): Unit = {
    val req = requestObj.body.asInstanceOf[JsObject]

    val nonLoggableFields = Seq("password", "creditCard")

    val variables = (req \ "variables").asOpt[JsObject].map(
      allVariables => nonLoggableFields.foldLeft(allVariables)((variables, field) => variables - field)
    ).getOrElse(JsObject.empty)
    logger.error(
      "funcName={}\n" +
        "name={}\nmessage={}\n" +
        "uri={}\nquery={}\nvariables={}\n",
      funcName, e.getClass.getName, e.getMessage,
      requestObj.uri,
      StringContext.processEscapes((req \ "query").get.toString()),
      Json.prettyPrint(variables)
    )
  }

  /** Add all deferred fetchers here */
  private val resolver = DeferredResolver.fetchers(
    OrderFetchers.Fetchers
      ++ UserFetchers.Fetchers
      : _*
  )

  private def executeQuery(query: String, variables: Option[JsObject], operation: Option[String], context: GraphQLContext) =
    QueryParser.parse(query) match {
      case Success(queryAst) =>
        Executor.execute(
          schema = graphQL.Schema,
          queryAst = queryAst,
          userContext = context,
          operationName = operation,
          variables = variables getOrElse Json.obj(),
          exceptionHandler = ErrorHandler,
          deferredResolver = resolver
        ).map(Ok(_))
          .recover {
            case error: QueryAnalysisError =>
              logError("executeQuery", error)
              BadRequest(error.resolveError)
            case error: ErrorWithResolver =>
              logger.error(
                "executeQuery ErrorWithResolver message={}",
                error.getMessage
              )
              InternalServerError(error.resolveError)
          }
      case Failure(error: SyntaxError) =>
        logger.error(
          "executeQuery SyntaxError message={} line={} column={}",
          error.getMessage, error.originalError.position.line, error.originalError.position.column
        )
        Future.successful(BadRequest(Json.obj(
          "syntaxError" -> error.getMessage,
          "locations" -> Json.arr(Json.obj(
            "line" -> error.originalError.position.line,
            "column" -> error.originalError.position.column
          ))
        )))
      case Failure(error) =>
        logger.error(
          "executeQuery Failure message={}",
          error.getMessage
        )
        throw error
    }
}