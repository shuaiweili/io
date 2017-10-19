package com.git.lee.io.aio.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author LISHUAIWEI
 * @date 2017/9/27 15:32
 */
public class RpcChannel implements IChannel {
    private Logger logger = LoggerFactory.getLogger(RpcChannel.class);
    private AsynchronousSocketChannel channel;
    private Serializer serializer;

    public RpcChannel(AsynchronousSocketChannel channel) {
        this.channel = channel;
        serializer = new JdkSerializer();
    }

    @Override
    public void write(IMessage message) {
        try {
            if (isOpen()) {
                logger.info("writing ...");
                byte[] data = serializer.encode(message);
                ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
                buffer.putInt(data.length);
                buffer.put(data);
                buffer.flip();
                channel.write(buffer).get(5, TimeUnit.SECONDS);
                logger.info("writed.");
            }
        } catch (Exception e) {
            logger.error("write error", e);
            close();
        }
    }

    @Override
    public <M extends IMessage> M read() {
        try {
            if (isOpen()) {
                logger.info("reading...");
                ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
                Integer rs = channel.read(lengthBuffer).get();
                if (-1 == rs) {
                    logger.info("连接断开了, localAddress={}, remoteAddress={}", channel.getLocalAddress(), channel.getRemoteAddress());
                }
                lengthBuffer.flip();
                int length = lengthBuffer.getInt();
                ByteBuffer messageBuffer = ByteBuffer.allocate(length);
                channel.read(messageBuffer).get();
                messageBuffer.flip();
                return serializer.decode(messageBuffer.array());
            }
        } catch (Exception e) {
            logger.error("read error", e);
            close();
        }
        return null;
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    @Override
    public void close() {
        try {
            channel.close();
        } catch (IOException e) {
            logger.error("close error", e);
        }
    }
}
