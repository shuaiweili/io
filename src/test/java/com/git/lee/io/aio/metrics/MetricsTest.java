package com.git.lee.io.aio.metrics;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 11:49
 */
public class MetricsTest extends AbstraceTest{

    @Test
    public void test() {
        Metrics metrics = buildMetrics();
        int size = metrics.sizeInBytes();
        ByteBuffer buffer = ByteBuffer.allocate(size);
        metrics.writeTo(buffer);

        buffer.flip();
        Metrics copy = Metrics.readFrom(buffer);
        Assert.assertEquals(metrics.getName(), copy.getName());
        Assert.assertEquals(metrics.getTags().size(), copy.getTags().size());
    }


}
