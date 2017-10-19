package com.git.lee.io.aio.metrics.client;

import com.git.lee.io.aio.metrics.Common.Node;
import com.git.lee.io.aio.metrics.client.internals.MetricsBatch;

import java.util.List;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 12:19
 */
public interface MetricsClient {

    void send(ClientRequest request);

    List<ClientResponse> poll();

    ClientRequest newClientRequest(Node node, List<MetricsBatch> batches, RequestCompletionHandler callback);
}
