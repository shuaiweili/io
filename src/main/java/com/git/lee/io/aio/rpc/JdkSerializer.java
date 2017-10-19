package com.git.lee.io.aio.rpc;


import java.io.*;

/**
 * @author LISHUAIWEI
 * @date 2017/9/27 15:19
 */
public class JdkSerializer implements Serializer {
    @Override
    public byte[] encode(IMessage message) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(message);
        byte[] data = outputStream.toByteArray();
        objectOutputStream.close();
        outputStream.close();
        return data;
    }

    @Override
    public <M extends IMessage> M decode(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        M message = (M)objectInputStream.readObject();
        objectInputStream.close();
        inputStream.close();
        return message;
    }
}
