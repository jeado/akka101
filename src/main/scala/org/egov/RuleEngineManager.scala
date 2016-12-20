package org.egov

import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope
import akka.routing.FromConfig
import org.egov.RuleEngine.{RuleInserted, _}

/**
  * Created by gojaedo on 2016. 12. 20..
  */
class RuleEngineManager extends Actor with ActorLogging {
  val engineRouter = context.actorOf(FromConfig.props(Props[RuleEngine]), "rule-engine-router")

  override def receive: Receive = {
    case msg: InsertRule =>
      engineRouter ! ConsistentHashableEnvelope(msg, msg.rule.deviceId)
    case msg: DeviceData =>
      engineRouter ! ConsistentHashableEnvelope(msg, msg.deviceId)
    case RuleInserted(deviceId) =>
      log.info(s"rule of $deviceId inserted")
  }
}
