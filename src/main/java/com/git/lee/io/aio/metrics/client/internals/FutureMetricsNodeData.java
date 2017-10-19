package com.git.lee.io.aio.metrics.client.internals;

import com.git.lee.io.aio.metrics.Common.Node;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 15:24
 */
public class FutureMetricsNodeData implements Future<Node> {

    final ProduceRequestResult result;

    public FutureMetricsNodeData(ProduceRequestResult result) {
        this.result = result;
    }

    @Override
    public Node get() throws InterruptedException, ExecutionException {
        result.await();
        return value();
    }

    @Override
    public Node get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        result.await(timeout, unit);
        return value();
    }

    private Node value() {
        return result.getNode();
    }

    @Override
    public boolean isDone() {
        return result.completed();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
