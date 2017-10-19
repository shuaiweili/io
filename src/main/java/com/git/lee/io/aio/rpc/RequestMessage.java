package com.git.lee.io.aio.rpc;

/**
 * @author LISHUAIWEI
 * @date 2017/9/27 15:12
 */
public class RequestMessage implements IMessage {

    public String server;
    public String methodName;
    public Object[] args;
    public Class[] parameterTypes;
}
