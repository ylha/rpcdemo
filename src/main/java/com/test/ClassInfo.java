package com.test;

import java.io.Serializable;

/**
 * serversub
 */
public class ClassInfo implements Serializable {
    /**
     * 序列化id
     */
    /**
     * 服务端存根就是根据收到的请求信息执行相应的业务逻辑调用并把结果返回客户端
     */
    private static final long serialVersionUID = -7821682294197810003L;

    private String className;//类名

    private String methodName;//返回值

    private Class<?>[] types; //参数类型

    private Object[] objects; //参数列表

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getTypes() {
        return types;
    }

    public void setTypes(Class<?>[] types) {
        this.types = types;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }
}
