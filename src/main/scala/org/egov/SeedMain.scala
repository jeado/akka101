package org.egov

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

/**
  * Created by gojaedo on 2016. 12. 19..
  */
object SeedMain extends App {
  ActorSystem("event-processor", ConfigFactory.parseString(
    """
      |akka {
      |  actor {
      |    provider = "akka.cluster.ClusterActorRefProvider"
      |    debug {
      |      receive = off
      |      lifecycle = off
      |    }
      |  }
      |
      |  remote {
      |    netty.tcp {
      |      hostname = 127.0.0.1
      |      port = 2551
      |    }
      |  }
      |
      |  cluster {
      |    seed-nodes = [
      |      "akka.tcp://event-processor@127.0.0.1:2551"
      |    ]
      |    roles = [seed]
      |    failure-detector {
      |      threshold = 12
      |    }
      |    metrics.enabled=off
      |  }
      |}
    """.stripMargin))
}
