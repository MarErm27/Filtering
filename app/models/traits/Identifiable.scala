package models.traits

import sangria.execution.deferred.HasId

trait Identifiable {
  val id: Int
}

object Identifiable {
  implicit def hasId[T <: Identifiable]: HasId[T, Int] = HasId(_.id)
}