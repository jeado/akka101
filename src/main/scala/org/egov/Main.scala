package org.egov


import akka.actor.{ActorSystem, PoisonPill, Props}

import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}

import akka.util.Timeout

/**
  * Created by gojaedo on 2016. 12. 19..
  */
object Main extends App {
  import scala.concurrent.duration._
  implicit val timeout = Timeout(1.seconds)

  val actorSystem: ActorSystem = ActorSystem("event-processor")

  //#create-singleton-manager
  actorSystem.actorOf(ClusterSingletonManager.props(
    singletonProps = Props[RuleEngineManager],
    terminationMessage = PoisonPill,
    settings = ClusterSingletonManagerSettings(actorSystem).withRole("engine")),
    name = "rule-engine-manager")

  //#create-singleton-manager
  val ruleManagerProxy = actorSystem.actorOf(ClusterSingletonProxy.props(singletonManagerPath = "/user/rule-engine-manager",
    settings = ClusterSingletonProxySettings(actorSystem).withRole("engine")),
    name = "rule-engine-manager-proxy")

}
