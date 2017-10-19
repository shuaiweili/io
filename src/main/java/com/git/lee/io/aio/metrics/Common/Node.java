package com.git.lee.io.aio.metrics.Common;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Executors;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 14:20
 */
public class Node {
    private final static int nThreads = 3;
    private int id;
    private String host;
    private int port;
    public AsynchronousSocketChannel channel;

    public Node(int id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
        connect();
    }

    public boolean connect() {
        try {
            channel = AsynchronousSocketChannel.open(AsynchronousChannelGroup.withFixedThreadPool(nThreads, Executors.defaultThreadFactory()));
            channel.setOption(StandardSocketOptions.TCP_NODELAY, true).setOption(StandardSocketOptions.SO_REUSEADDR, true).setOption(StandardSocketOptions.SO_KEEPALIVE, true);
            channel.connect(new InetSocketAddress(host, port)).get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void close() {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (id != node.id) return false;
        if (port != node.port) return false;
        return host != null ? host.equals(node.host) : node.host == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + port;
        return result;
    }
}
