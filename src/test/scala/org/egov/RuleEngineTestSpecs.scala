package org.egov

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.egov.RuleEngine._
import org.scalatest.{FlatSpecLike, FunSuite}

/**
  * Created by gojaedo on 2016. 12. 19..
  */
class RuleEngineTestSpecs extends TestKit(ActorSystem()) with FlatSpecLike with ImplicitSender {

  it should "insert rule and process event processing by that rule" in {
    val actor = system.actorOf(Props[RuleEngine])

    actor ! InsertRule(RuleDescription("1", 1))

    expectMsg(RuleInserted)

    actor ! DeviceData("1", 2)

    expectMsg(EventOccurred(2))
  }
}
