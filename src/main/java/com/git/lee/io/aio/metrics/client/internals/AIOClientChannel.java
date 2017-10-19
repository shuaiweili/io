package com.git.lee.io.aio.metrics.client.internals;

import com.git.lee.io.aio.metrics.client.ClientRequest;
import com.git.lee.io.aio.metrics.client.ClientResponse;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 17:20
 */
public class AIOClientChannel {

    private List<ClientResponse> completedReceives;

    public AIOClientChannel() {
        completedReceives = new ArrayList<>();
    }

    public void send(ClientRequest request) {
        try {
            List<MetricsBatch> batches = request.getBatches();
            for (MetricsBatch batch : batches) {
                ByteBuffer buffer = batch.buffer();
                buffer.flip();
                request.getNode().channel.write(buffer).get();
            }
            completedReceives.add(new ClientResponse(request.getCallback()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ClientResponse> completedReceives() {
        return completedReceives;
    }
}
