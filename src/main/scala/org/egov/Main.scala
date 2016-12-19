package org.egov

import akka.actor.{ActorRef, ActorSystem, Props}
import org.egov.RuleEngine._

/**
  * Created by gojaedo on 2016. 12. 19..
  */
object Main extends App {

  val actorSystem: ActorSystem = ActorSystem("event-processor")

  val ruleEngineActor: ActorRef = actorSystem.actorOf(Props[RuleEngine], "rule-engine")

  ruleEngineActor ! DeviceData("1", 200)
  ruleEngineActor ! DeviceData("1", 200)
  ruleEngineActor ! DeviceData("1", 200)

  ruleEngineActor ! InsertRule(RuleDescription("1", 200))

  Thread.sleep(1000)

  ruleEngineActor ! DeviceData("1", 200)
  ruleEngineActor ! DeviceData("1", 201)
  ruleEngineActor ! DeviceData("2", 201)
}
