package org.egov

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope
import akka.routing.FromConfig
import akka.util.Timeout
import org.egov.RuleEngine._

import scala.concurrent.Await

/**
  * Created by gojaedo on 2016. 12. 19..
  */
object Main extends App {
  import akka.pattern.ask
  import scala.concurrent.duration._

  val actorSystem: ActorSystem = ActorSystem("event-processor")
  val ruleEngineActor: ActorRef =  actorSystem.actorOf(FromConfig.props(Props[RuleEngine]), "rule-engine")

  implicit val timeout = Timeout(1 seconds)

  Thread.sleep(1000)
  ruleEngineActor ! ConsistentHashableEnvelope(DeviceData("1", 200), "1")
  Await.ready(ruleEngineActor ? ConsistentHashableEnvelope(InsertRule(RuleDescription("1", 200)), "1"), 1 seconds)

  ruleEngineActor ! ConsistentHashableEnvelope(DeviceData("1", 200), "1")
  ruleEngineActor ! ConsistentHashableEnvelope(DeviceData("1", 201), "1")
  Await.ready(ruleEngineActor ? ConsistentHashableEnvelope(InsertRule(RuleDescription("2", 200)), "2"), 1 seconds)

  ruleEngineActor ! ConsistentHashableEnvelope(DeviceData("2", 201), "2")
}
