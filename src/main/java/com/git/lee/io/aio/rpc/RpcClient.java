package com.git.lee.io.aio.rpc;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * @author LISHUAIWEI
 * @date 2017/9/27 15:23
 */
public class RpcClient {

    private AsynchronousChannelGroup group;
    private AsynchronousSocketChannel asynchronousSocketChannel;
    private int nThreads = Runtime.getRuntime().availableProcessors() * 2;
    private IChannel channel;

    public void connect(SocketAddress address) throws IOException, ExecutionException, InterruptedException {
        group = AsynchronousChannelGroup.withFixedThreadPool(nThreads, Executors.defaultThreadFactory());
        asynchronousSocketChannel = AsynchronousSocketChannel.open(group);
        asynchronousSocketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true).setOption(StandardSocketOptions.SO_REUSEADDR, true).setOption(StandardSocketOptions.SO_KEEPALIVE, true);
        asynchronousSocketChannel.connect(address).get();
        channel = new RpcChannel(asynchronousSocketChannel);
    }

    public <T> T getService(final String server, Class<T> cls) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{cls}, ((proxy, method, args) -> {
            RequestMessage requestMessage = new RequestMessage();
            requestMessage.server = server;
            requestMessage.methodName = method.getName();
            requestMessage.args = args;
            requestMessage.parameterTypes = method.getParameterTypes();
//            Thread.sleep(10000);
            channel.write(requestMessage);
            ResponseMessage responseMessage = channel.read();
            return responseMessage.result;
        }));
    }

    public void close() {
        if (channel != null) {
            channel.close();
        }
        if (group != null) {
            try {
                group.shutdownNow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
