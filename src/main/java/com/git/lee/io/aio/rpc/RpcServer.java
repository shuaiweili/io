package com.git.lee.io.aio.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;

/**
 * @author LISHUAIWEI
 * @date 2017/9/27 15:55
 */
public class RpcServer {
    private Logger logger = LoggerFactory.getLogger(RpcServer.class);
    private AsynchronousChannelGroup group;
    private AsynchronousServerSocketChannel serverSocketChannel;
    private int nThreads = Runtime.getRuntime().availableProcessors() * 2;
    private int port;
    private Map<String, Object> sm = new HashMap<>();

    public RpcServer bind(int port) {
        this.port = port;
        return this;
    }

    public RpcServer register(String server, Object service) {
        sm.put(server, service);
        return this;
    }

    public void start() throws IOException {
        group = AsynchronousChannelGroup.withFixedThreadPool(nThreads, Executors.defaultThreadFactory());
        serverSocketChannel = AsynchronousServerSocketChannel.open(group)
                                .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                                .bind(new InetSocketAddress("localhost", port));
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel result, Void attachment) {
                serverSocketChannel.accept(null, this);
                String localAddress = null;
                String remoteAddress = null;
                try {
                    localAddress = result.getLocalAddress().toString();
                    remoteAddress = result.getRemoteAddress().toString();
                    logger.info("创建连接{} -> {}", localAddress, remoteAddress);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                IChannel channel = new RpcChannel(result);
                while (channel.isOpen()) {
                    handle(channel);
                }
                logger.info("断开连接 {} -> {}", localAddress, remoteAddress);
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                logger.error("accept failed", exc);
            }
        });
    }

    private void handle(IChannel channel) {
        try {
            RequestMessage message = channel.read();
            if (Objects.nonNull(message)) {
                String server = message.server;
                Object obj = sm.get(server);
                if (Objects.isNull(obj)) {
                    logger.info("没有发现对应的service!");
                } else {
                    Method method = obj.getClass().getMethod(message.methodName, message.parameterTypes);
                    Object result = method.invoke(obj, message.args);
                    ResponseMessage responseMessage = new ResponseMessage();
                    responseMessage.result = result;
                    channel.write(responseMessage);
                }
            }
        } catch (Exception e) {
            logger.error("server handle error", e);
            if (channel.isOpen()) {
                channel.close();
            }
        }
    }
}

