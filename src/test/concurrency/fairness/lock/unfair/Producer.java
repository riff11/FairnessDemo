/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.concurrency.fairness.lock.unfair;

import static java.lang.Thread.sleep;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.concurrency.fairness.Consumer;
import test.concurrency.fairness.IProducer;

/**
 *
 * @author igor
 */
public class Producer implements IProducer {

    private volatile Consumer prevConsumer;
    private volatile boolean continueFlag;
    private final Lock lock = new ReentrantLock(false);

    @Override
    public void produce(Consumer consumer) {
        lock.lock();
        try {
            System.out.println(consumer + " entered producer's lock");
            synchronized (consumer) {
//                System.out.println(consumer + " is sending continue signal to next");
                consumer.notify();
            }
            if (prevConsumer == null) {
                System.out.println(consumer + " is waiting for continue flag");
                while (!continueFlag) {
                    try {
                        sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println("continue");
            } else if (prevConsumer != consumer.getPrevConsumer()) {
                throw new RuntimeException(consumer + ": порядок нарушен!!! Предыдущий: " + prevConsumer);
            }
        } finally {
            prevConsumer = consumer;
            lock.unlock();
        }

    }

    public boolean isContinueFlag() {
        return continueFlag;
    }

    @Override
    public void setContinueFlag(boolean continueFlag) {
        this.continueFlag = continueFlag;
    }

}
