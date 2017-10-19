package com.git.lee.io.aio.metrics.client.internals;

import com.git.lee.io.aio.metrics.Common.Cluster;
import com.git.lee.io.aio.metrics.Common.Node;
import com.git.lee.io.aio.metrics.Metrics;
import com.git.lee.io.aio.metrics.utils.CopyOnWriteMap;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 14:25
 */
public class MetricsAccumulator {

    private final ConcurrentMap<Node, Deque<MetricsBatch>> batchs;
    private int batchSize;
    private volatile boolean closed;

    public MetricsAccumulator(int batchSize) {
        this.batchSize = batchSize;
        batchs = new CopyOnWriteMap<>();
    }

    public FutureMetricsNodeData append(Node node, Metrics metrics) throws InterruptedException{
        if (closed) {
            throw new IllegalStateException("Cannot send after the producer is closed.");
        }
        Deque<MetricsBatch> deque = getOrCreateDeque(node);
        synchronized (deque) {
            MetricsBatch batch = deque.peekLast();
            if (batch == null) {
                batch = new MetricsBatch(node, batchSize);
                deque.addLast(batch);
            }
            if (!batch.isFull(metrics.sizeInBytes())) {
                return batch.append(metrics);
            } else {
                MetricsBatch newBatch = new MetricsBatch(node, batchSize);
                deque.addLast(newBatch);
                return newBatch.append(metrics);
            }
        }
    }

    public List<MetricsBatch> drain(Cluster cluster) {
        List<Node> nodes = cluster.getNodes();
        if (nodes == null) return Collections.emptyList();

        List<MetricsBatch> metricsBatches = new ArrayList<>();
        for (Node node : nodes) {
            Deque<MetricsBatch> deque = batchs.get(node);
            if (deque != null) {
                synchronized (deque) {
                    do {
                        MetricsBatch batch = deque.pollFirst();
                        if (batch != null) {
                            metricsBatches.add(batch);
                        }
                    } while (!deque.isEmpty());
                }
            }
        }
        return metricsBatches;
    }

    public void close() {
        closed = true;
    }

    private Deque<MetricsBatch> getOrCreateDeque(Node node) {
        Deque<MetricsBatch> deque = batchs.get(node);
        if (deque != null) {
            return deque;
        }
        deque = new ArrayDeque<>();
        Deque<MetricsBatch> previous = batchs.putIfAbsent(node, deque);
        if (previous != null) {
            return previous;
        }
        return deque;
    }
}
