package com.git.lee.io.aio.metrics;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 17:52
 */
public class AbstraceTest {

    protected Metrics buildMetrics() {
        Metrics metrics = new Metrics("my_metrics", System.currentTimeMillis());
        metrics.addTag("tag1_key", "tag1_value");
        metrics.addTag("tag2_key", "tag2_value");
        metrics.addField("field_key", 1.1);
        return metrics;
    }

    protected Metrics buildMetrics2(String metricsName, double fieldValue) {
        Metrics metrics = new Metrics(metricsName, System.currentTimeMillis());
        metrics.addTag("tag1_key1", "tag1_value");
        metrics.addTag("tag2_key2", "tag2_value");
        metrics.addField("field_key1", fieldValue);
        return metrics;
    }
}
