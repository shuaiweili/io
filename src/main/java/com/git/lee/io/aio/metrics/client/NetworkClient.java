package com.git.lee.io.aio.metrics.client;

import com.git.lee.io.aio.metrics.Common.Node;
import com.git.lee.io.aio.metrics.client.internals.AIOClientChannel;
import com.git.lee.io.aio.metrics.client.internals.MetricsBatch;

import java.util.List;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 14:10
 */
public class NetworkClient implements MetricsClient {

    private String clientId;
    private AIOClientChannel clientChannel;

    public NetworkClient(String clientId) {
        this.clientId = clientId;
        clientChannel = new AIOClientChannel();
    }

    @Override
    public void send(ClientRequest request) {
        clientChannel.send(request);
    }

    @Override
    public List<ClientResponse> poll() {
        handleCompletedReceives();
        return null;
    }

    @Override
    public ClientRequest newClientRequest(Node node, List<MetricsBatch> batches, RequestCompletionHandler callback) {
        return new ClientRequest(clientId, node, batches, callback);
    }

    private void handleCompletedReceives() {
        for (ClientResponse response : clientChannel.completedReceives()) {
            response.onCompete();
        }
        clientChannel.completedReceives().clear();
    }
}
