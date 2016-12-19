package org.egov.java;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.concurrent.CountDownLatch;

/**
 * Created by gojaedo on 2016. 12. 19..
 */
public class RuleEngine extends UntypedActor {

    static public class Incr {
        final CountDownLatch doneSignal;

        public Incr(CountDownLatch doneSignal) {
            this.doneSignal = doneSignal;
        }

        public void done() {
            doneSignal.countDown();
        }
    }

    private int count = 0;
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof Incr) {
            count++;
            System.out.println("count => "+count);
//            log.info("count => {} {}", count, ((Incr) message).doneSignal.getCount());
            ((Incr) message).done();
        }
    }
}
