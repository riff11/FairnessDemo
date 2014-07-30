/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.concurrency.fairness.embed;

import test.concurrency.fairness.Consumer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author igor
 */
public class Main {
    private static final int COUNT = 100;
    private static final CountDownLatch latch = new CountDownLatch(COUNT);
    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = null;
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < COUNT; i++) {
            consumer = new Consumer(producer, consumer, latch);
            exec.execute(consumer);
        }
        latch.await();
        Thread.sleep(100);
        producer.setContinueFlag(true);
        exec.awaitTermination(5, TimeUnit.SECONDS);
        exec.shutdownNow();
    }
}
