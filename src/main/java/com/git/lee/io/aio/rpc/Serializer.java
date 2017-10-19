package com.git.lee.io.aio.rpc;

import java.io.IOException;

/**
 * @author LISHUAIWEI
 * @date 2017/9/27 15:17
 */
public interface Serializer {

    byte[] encode(IMessage message) throws IOException;
    <M extends IMessage> M decode(byte[] data) throws IOException, ClassNotFoundException;
}
