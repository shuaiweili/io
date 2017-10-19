package com.git.lee.io.aio.metrics.client.internals;

import com.git.lee.io.aio.metrics.Common.Node;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 15:22
 */
public final class ProduceRequestResult {

    private CountDownLatch latch = new CountDownLatch(1);
    private Node node;

    public ProduceRequestResult(Node node) {
        this.node = node;
    }

    public void await() throws InterruptedException {
        latch.await();
    }

    public void await(long timeout, TimeUnit unit) throws InterruptedException {
        latch.await(timeout, unit);
    }

    public void done() {
        latch.countDown();
    }

    public boolean completed() {
        return latch.getCount() == 0L;
    }

    public Node getNode() {
        return node;
    }
}
