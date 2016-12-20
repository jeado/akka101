package org.egov

import java.util.UUID

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import akka.kafka.ConsumerSettings
import akka.kafka.scaladsl.Consumer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import akka.util.Timeout
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.egov.RuleEngine.{DeviceData, _}

/**
  * Created by gojaedo on 2016. 12. 19..
  */
object Main extends App {
  import scala.concurrent.duration._
  implicit val timeout = Timeout(1.seconds)

  implicit val actorSystem: ActorSystem = ActorSystem("event-processor")
  implicit val materializer = ActorMaterializer()

  val kafkaSetting =
  ConsumerSettings(actorSystem, new StringDeserializer, new StringDeserializer, Set("device-event"))
    .withBootstrapServers("192.168.99.100:9091")
    .withGroupId("event-engine")
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
    .withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
    .withProperty(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "750000")
    .withClientId("engine"+UUID.randomUUID().toString)

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

  Consumer.plainSource(kafkaSetting)
    .map(_.value().split(","))
    .map(v => DeviceData(v(0),Integer.parseInt(v(1))))
    .to(Sink.actorRef(ruleManagerProxy, None))
    .run()
}
