package models

import akka.http.scaladsl.model.DateTime
import graphql.types.ScalarTypes.SafeZonedDateTime
import models.traits.Identifiable

case class Order(
                  id: Int = 0,
                  name: String,
                  userId: Int,
                  expectedDeliveryDate: DateTime,
                  createdAt: Option[SafeZonedDateTime],
                  updatedAt: Option[SafeZonedDateTime],
                  isDeleted: Boolean
                ) extends Identifiable