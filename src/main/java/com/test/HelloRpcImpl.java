package com.test;


/**
 * 接口实现类
 */
public class HelloRpcImpl implements HelloRPC {
    @Override
    public String hello(String name) {
        return "hello,myfriends"+name;
    }
}
