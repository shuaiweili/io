package com.git.lee.io.aio.metrics.client;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 12:11
 */
public interface RequestCompletionHandler {

    void onComplete(ClientResponse response);
}
