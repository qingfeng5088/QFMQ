package com.qf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 用来执行Consumer注解的方法
 */
public class ConsumerAction {
    // 观察者类
    private final Object target;

    // 要执行的方法
    private final Method method;

    public ConsumerAction(Object target, Method method) {
        if (target == null) {
            throw new NullPointerException("观察者对象为空！");
        }

        this.target = target;
        this.method = method;
        this.method.setAccessible(true);
    }

    /**
     * 执行方法
     *
     * @param event 方法的参数
     */
    public void execute(Object event) {
        try {
            method.invoke(target, event);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
