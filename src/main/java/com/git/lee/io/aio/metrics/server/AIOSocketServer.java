package com.git.lee.io.aio.metrics.server;

import com.git.lee.io.aio.metrics.Metrics;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 17:58
 */
public class AIOSocketServer {
    final static int port = 8888;
    private static AtomicInteger metricsCount = new AtomicInteger(0);

    public static void main(String[] args) throws IOException {
        final AsynchronousServerSocketChannel channel = AsynchronousServerSocketChannel.open(AsynchronousChannelGroup.withFixedThreadPool(3, Executors.defaultThreadFactory()))
                                                            .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                                                            .bind(new InetSocketAddress(port));
        channel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel mchannel, Void attachment) {
                channel.accept(null, this);
                try {
                    if (mchannel.isOpen()) {
                        while (true) {
                            ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
                            mchannel.read(sizeBuffer).get();
                            sizeBuffer.flip();
                            int size = sizeBuffer.getInt();
                            if (size <= 0) break;

                            ByteBuffer buffer = ByteBuffer.allocate(size);
                            mchannel.read(buffer).get();
                            buffer.flip();
                            Metrics metrics = Metrics.readFrom(buffer);
                            System.out.println(metricsCount.incrementAndGet() + " " + metrics.getName());
                        }
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                    if (mchannel.isOpen()) {
                        try {
                            mchannel.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                System.out.println("accept error:" + exc.getMessage());
            }
        });
        System.out.println("aio server start in port="+ port);
        System.in.read();
    }
}
