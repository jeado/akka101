package org.egov

import akka.actor.{Actor, ActorLogging}

/**
  * Created by gojaedo on 2016. 12. 19..
  */
case class RuleDescription(deviceId: String, threshold: Int)

object RuleEngine {
  case class DeviceData(deviceId: String, co2: Int)

  case class InsertRule(rule: RuleDescription)

  case object RuleInserted

  case class EventOccurred(co2: Int)
}

class RuleEngine extends Actor with ActorLogging {
  import org.egov.RuleEngine._

  var map: Map[String, Int] = Map[String, Int]()

  log.info("Created RuleEngine!")
  override def receive: Receive = {
    case DeviceData(deviceId, co2) =>
      log.info(s"got device data $deviceId, $co2")
      val threshold = map.get(deviceId)
      threshold match {
        case Some(t) if co2 > t =>
          log.info("Event occurred!")
          sender ! EventOccurred(co2)
        case Some(t) =>
          log.info(s"$co2 is not greater than $t so discard it")
        case None =>
          log.info(s"can not find $deviceId rule so discard it")
      }
    case InsertRule(rule) =>
      log.info(s"add new $rule")
      map = map + (rule.deviceId -> rule.threshold)
      sender ! RuleInserted
  }
}