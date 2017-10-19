package com.git.lee.io.aio.metrics.client.internals;

import com.git.lee.io.aio.metrics.Common.Cluster;
import com.git.lee.io.aio.metrics.Common.Node;
import com.git.lee.io.aio.metrics.client.ClientRequest;
import com.git.lee.io.aio.metrics.client.ClientResponse;
import com.git.lee.io.aio.metrics.client.MetricsClient;
import com.git.lee.io.aio.metrics.client.RequestCompletionHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 12:02
 */
public class Sender implements Runnable {

    private MetricsAccumulator accumulator;
    private MetricsClient client;
    private Cluster cluster;
    private volatile boolean running;

    public Sender(Cluster cluster, MetricsAccumulator accumulator, MetricsClient client) {
        this.cluster = cluster;
        this.accumulator = accumulator;
        this.client = client;
        running = true;
    }

    @Override
    public void run() {
        while (running) {
            List<MetricsBatch> batchs = accumulator.drain(cluster);
            sendProduceRequests(batchs);
            client.poll();
        }
    }

    private void sendProduceRequests(List<MetricsBatch> batches) {
        Map<Node, List<MetricsBatch>> recordsByNode = batches.stream().collect(Collectors.groupingBy(mb -> mb.getNode()));
        RequestCompletionHandler callback = new RequestCompletionHandler() {
            @Override
            public void onComplete(ClientResponse response) {
                handleProduceResponse(recordsByNode);
            }
        };
        for (Map.Entry<Node, List<MetricsBatch>> entry : recordsByNode.entrySet()) {
            ClientRequest request = client.newClientRequest(entry.getKey(), batches, callback);
            client.send(request);
        }
    }

    private void handleProduceResponse(Map<Node, List<MetricsBatch>> recordsByNode) {
        for (Map.Entry<Node, List<MetricsBatch>> entry : recordsByNode.entrySet()) {
            entry.getValue().forEach(MetricsBatch::done);
        }
    }

    public void close() {
        accumulator.close();
        running = false;
    }
}
