package com.sofudev.trackapptrl.Util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class Worker implements Runnable {

    private AtomicBoolean started;
    private Runnable task;
    private Thread thread;
    private CountDownLatch latch;

    public Worker(Runnable task, CountDownLatch latch) {
        this.task = task;
        this.latch = latch;
        started = new AtomicBoolean();
        thread  = new Thread();
    }

    public void start(){
        if (!started.getAndSet(true))
        {
            thread.start();
        }
    }

    @Override
    public void run() {
        task.run();
        latch.countDown();
    }
}
