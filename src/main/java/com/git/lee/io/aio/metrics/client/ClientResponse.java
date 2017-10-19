package com.git.lee.io.aio.metrics.client;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 12:08
 */
public final class ClientResponse {

    private String clientId;
    private RequestCompletionHandler callback;

    public ClientResponse(RequestCompletionHandler callback) {
        this.callback = callback;
    }

    public void onCompete() {
        if (callback != null) {
            callback.onComplete(this);
        }
    }
}
