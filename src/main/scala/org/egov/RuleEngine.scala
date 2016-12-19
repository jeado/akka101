package org.egov

import akka.actor.{Actor, ActorLogging, Props}
import org.egov.RuleEngine.DeviceData

/**
  * Created by gojaedo on 2016. 12. 19..
  */
case class RuleDescription(deviceId: String, threshold: Int)

object RuleEngine {
  case class DeviceData(deviceId: String, co2: Int)

  case class InsertRule(rule: RuleDescription)
}

class RuleEngine extends Actor with ActorLogging {
  import org.egov.RuleEngine._
  override def receive: Receive = {
    case data@ DeviceData(deviceId, co2) =>
      log.info(s"got device data $deviceId, $co2")
      context.child(s"threshold-$deviceId") match {
        case Some(actorRef) =>
          actorRef ! data
        case None => log.info(s"can not find $deviceId rule so discard it")
      }
    case InsertRule(rule) =>
      log.info(s"create rule $rule")
      context.actorOf(ThresholdRule.props(rule.threshold), s"threshold-${rule.deviceId}")

  }
}


object ThresholdRule {
  def props(threshold: Int): Props = Props(new ThresholdRule(threshold))
}
class ThresholdRule(threshold: Int) extends Actor with ActorLogging {
  override def receive = {
    case DeviceData(_, co2) =>
      if(co2 > threshold) log.info("Event occurred!")
  }
}
