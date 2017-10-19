package com.git.lee.io.aio.rpc;

/**
 * @author LISHUAIWEI
 * @date 2017/9/27 16:19
 */
public class IServiceImpl implements IService {
    @Override
    public String say(String content) {
        return content;
    }
}
