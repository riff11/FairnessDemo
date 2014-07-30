/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.concurrency.fairness;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author igor
 */
public class Consumer implements Runnable {

    private static int counter = 0;
    private final int id = counter++;

    private final Consumer prevConsumer;
    private final IProducer producer;
    private final CountDownLatch latch;

    public Consumer(IProducer producer, Consumer prevConsumer, CountDownLatch latch) {
        this.producer = producer;
        this.prevConsumer = prevConsumer;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {

            if (prevConsumer != null) {
                synchronized (prevConsumer) {
                    System.out.println(this + " is waiting for " + prevConsumer);
                    prevConsumer.wait();
                }
                Thread.sleep(10);
                synchronized (this) {
                    notify();
                }
            }
            System.out.println(this + " is ready");
            latch.countDown();
            producer.produce(this);
        } catch (InterruptedException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return "Consumer{" + "id=" + id + '}';
    }

    public int getId() {
        return id;
    }

    public Consumer getPrevConsumer() {
        return prevConsumer;
    }

}
