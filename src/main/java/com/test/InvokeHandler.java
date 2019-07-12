package com.test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

public class InvokeHandler extends ChannelInboundHandlerAdapter {
    /**
     * 触发读事件，重写读取消息的方法，
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClassInfo classInfo = (ClassInfo) msg;

        Object clazz = Class.forName(getImplClassName(classInfo)).newInstance();
        Method method = clazz.getClass().getMethod(classInfo.getMethodName(), classInfo.getTypes());
        //通过反射调用实现类的方法
        Object result = method.invoke(clazz, classInfo.getObjects());
        ctx.writeAndFlush(result);
    }

    /**
     * 获取实现类，调用实现类方法， 写回response
     */
    private String getImplClassName(ClassInfo classInfo) throws ClassNotFoundException {

        String interfacePath = "com.test";

        int lastDot = classInfo.getClassName().lastIndexOf(".");
        String interfaceName = classInfo.getClassName().substring(lastDot);


        Class<?> aClass = Class.forName(interfacePath + interfaceName);

        Reflections reflections = new Reflections(interfacePath);

        //得到某接口下的所有实现类
        Set<Class<?>> ImplClassSet = (Set<Class<?>>) reflections.getSubTypesOf(aClass);
        if (ImplClassSet.size() == 0) {
            System.out.println("未找到实现类");
            return null;
        } else if (ImplClassSet.size() > 1) {
            System.out.println("找到多个实现类，未明确使用哪一个");
            return null;
        } else {
            //把集合转换为数组
            Class[] classes = ImplClassSet.toArray(new Class[0]);
            return classes[0].getName();//得到实现类的名字
        }

    }
}
