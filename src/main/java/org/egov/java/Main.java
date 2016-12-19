package org.egov.java;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Created by gojaedo on 2016. 12. 19..
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        int N = 500;
        int M = 1000;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(N*M);

        ActorSystem actorSystem = ActorSystem.create("event-processor");

        ActorRef actorRef = actorSystem.actorOf(Props.create(RuleEngine.class));

        Counter c = new Counter();
        Date now = new Date();
        for (int i = 0; i < N; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        startSignal.await();
                        for (int j = 0; j < M; j++) {
//                            c.incr();
//                            doneSignal.countDown();
                            actorRef.tell(new RuleEngine.Incr(doneSignal), ActorRef.noSender());

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
            t.start();
        }
        startSignal.countDown();
        doneSignal.await();
        Date finished = new Date();
        System.out.println("~~~" + now + ", "+ finished);
    }
}


class Counter {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private int count = 0;
    public void incr() {
        count++;
        System.out.println("count => "+ count);
//        logger.info("count => {}", count);
    }
}