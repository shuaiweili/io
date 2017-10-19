package com.git.lee.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author LISHUAIWEI
 * @date 2017/9/26 14:37
 */
public class SocketClient {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
        channel.connect(new InetSocketAddress("127.0.0.1", 8888)).get();
//        ByteBuffer buffer = ByteBuffer.wrap("你好".getBytes());
//        Future<Integer> future = channel.write(buffer);
//        future.get();
//        System.out.println("send ok.");
    }
}
