package com.git.lee.io.aio.rpc;

/**
 * @author LISHUAIWEI
 * @date 2017/9/27 15:14
 */
public interface IChannel {

    void write(IMessage message);

    <M extends IMessage> M read();

    boolean isOpen();

    void close();
}
