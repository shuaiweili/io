package com.git.lee.io.aio.metrics.client.internals;

import com.git.lee.io.aio.metrics.Common.Node;
import com.git.lee.io.aio.metrics.Metrics;

import java.nio.ByteBuffer;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 14:24
 */
public class MetricsBatch {

    private int sizeLimit;
    private ByteBuffer buffer;
    private ProduceRequestResult result;

    public MetricsBatch(Node node, int sizeLimit) {
        this.sizeLimit = sizeLimit;
        buffer = ByteBuffer.allocate(sizeLimit);
        result = new ProduceRequestResult(node);
    }

    public FutureMetricsNodeData append(Metrics metrics) {
        buffer.putInt(metrics.sizeInBytes());
        metrics.writeTo(buffer);
        return new FutureMetricsNodeData(result);
    }

    public boolean isFull(int appendSize) {
        return buffer.position() + appendSize >= sizeLimit;
    }

    public Node getNode() {
        return result.getNode();
    }

    public ByteBuffer buffer() {
        return buffer.duplicate();
    }

    public void done() {
        result.done();
    }
}
