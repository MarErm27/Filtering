package graphql

import database.DAO
import org.slf4j.LoggerFactory
import play.api.mvc.{Cookie, Cookies, Headers}

import scala.collection.mutable.ListBuffer

case class GraphQLContext(requestHeaders: Headers,
                          requestCookies: Cookies,
                          newHeaders: ListBuffer[(String, String)] = ListBuffer.empty,
                          newCookies: ListBuffer[Cookie] = ListBuffer.empty,
                          dao: DAO
                         ) {
  val logger = LoggerFactory.getLogger("GraphQLContext")

}