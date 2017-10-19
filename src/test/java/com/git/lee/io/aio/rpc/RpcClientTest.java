package com.git.lee.io.aio.rpc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author LISHUAIWEI
 * @date 2017/9/27 16:36
 */
public class RpcClientTest {
    private final static Executor executor = Executors.newFixedThreadPool(10);
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
//        for (int i = 0; i < 10; i++) {
//            final int j = i;
//            executor.execute(() -> {
//                RpcClient client = new RpcClient();
//                try {
//                    client.connect(new InetSocketAddress("localhost", 8888));
//                    IService service = client.getService("test", IService.class);
//                    System.out.println(service.say("say hello " + j));
//                    client.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
        RpcClient client = new RpcClient();
        try {
            client.connect(new InetSocketAddress("localhost", 8888));
            IService service = client.getService("test", IService.class);
            System.out.println(service.say("say hello " ));
//            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
