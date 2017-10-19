package com.git.lee.io.aio.metrics.client;

import com.git.lee.io.aio.metrics.Common.Node;
import com.git.lee.io.aio.metrics.client.internals.MetricsBatch;

import java.util.List;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 12:05
 */
public final class ClientRequest {

    private String clientId;
    private Node node;
    private List<MetricsBatch> batches;
    private RequestCompletionHandler callback;

    public ClientRequest(String clientId, Node node, List<MetricsBatch> batches) {
        this(clientId, node, batches, null);
    }

    public ClientRequest(String clientId, Node node, List<MetricsBatch> batches, RequestCompletionHandler callback) {
        this.clientId = clientId;
        this.node = node;
        this.batches = batches;
        this.callback = callback;
    }

    public Node getNode() {
        return node;
    }

    public List<MetricsBatch> getBatches() {
        return batches;
    }

    public RequestCompletionHandler getCallback() {
        return callback;
    }
}
