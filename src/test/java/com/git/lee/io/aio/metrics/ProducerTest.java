package com.git.lee.io.aio.metrics;

import com.git.lee.io.aio.metrics.Common.Node;
import com.git.lee.io.aio.metrics.client.MetricsProducer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 17:51
 */
public class ProducerTest extends AbstraceTest{

    @Test
    public void sendOnce() {
        Metrics metrics = buildMetrics();

        MetricsProducer producer = new MetricsProducer("localhost:8888");
        Future<Node> future = producer.send(metrics);
        try {
            Node node = future.get();
            Assert.assertEquals(node.getPort(), 8888);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendBatch() throws IOException {
        MetricsProducer producer = new MetricsProducer("localhost:8888");
        int batchSize = 10000;
        for (int i = 0; i < batchSize; i++) {
            Metrics metrics = buildMetrics2("metrcs_name_" + i, i);
            producer.send(metrics);
            System.out.println("send " + metrics.getName());
        }
        System.in.read();
    }
}
