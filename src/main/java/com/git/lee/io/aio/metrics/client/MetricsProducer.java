package com.git.lee.io.aio.metrics.client;

import com.git.lee.io.aio.metrics.Common.Cluster;
import com.git.lee.io.aio.metrics.Common.Node;
import com.git.lee.io.aio.metrics.Metrics;
import com.git.lee.io.aio.metrics.client.internals.FutureMetricsNodeData;
import com.git.lee.io.aio.metrics.client.internals.MetricsAccumulator;
import com.git.lee.io.aio.metrics.client.internals.Sender;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 12:02
 */
public class MetricsProducer {

    private static final AtomicInteger PRODUCER_CLIENT_ID_SEQUENCE = new AtomicInteger(1);
    private Cluster cluster;
    private Random random;
    private MetricsAccumulator accumulator;
    private Sender sender;

    public MetricsProducer(String ... bootstrapServers) {
        cluster = Cluster.bootstrap(bootstrapServers);
        random = new Random();
        accumulator = new MetricsAccumulator(16 * 1024);
        String clientId = "clientId-" + PRODUCER_CLIENT_ID_SEQUENCE.getAndIncrement();
        NetworkClient client = new NetworkClient(clientId);
        sender = new Sender(cluster, accumulator, client);
        Thread thread = new Thread(sender, "metrics-producer-network-thread" + "|" + clientId);
        thread.setDaemon(true);
        thread.start();
    }

    public FutureMetricsNodeData send(Metrics metrics) {
        try {
            List<Node> nodes = cluster.getNodes();
            int nodeCount = nodes.size();
            Node node  = nodes.get(random.nextInt(nodeCount));
            return accumulator.append(node, metrics);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        cluster.close();
        sender.close();
    }
}
