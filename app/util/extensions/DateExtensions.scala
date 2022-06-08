package util.extensions

import com.typesafe.config.ConfigFactory
import java.time.ZoneId

object DateExtensions {
  def getZone = {
    val conf = ConfigFactory.load()
    val zoneId = conf.getString("zoneId")
    ZoneId.of(zoneId).normalized()
  }
}