package com.git.lee.io.aio.rpc;


/**
 * @author LISHUAIWEI
 * @date 2017/9/27 16:18
 */
public class RpcServerTest {

    public static void main(String[] args) throws Exception{
        new RpcServer().register("test", new IServiceImpl())
                .bind(8888)
                .start();
    }
}
