package com.qf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 消费者注册表类
 * 主要利用java的反射语法
 */
public class ConsumerRegistry {
    private ConcurrentHashMap<Class<?>, CopyOnWriteArraySet<ConsumerAction>> registry = new ConcurrentHashMap<>();

    public void register(Object consumer) {
        Map<Class<?>, List<ConsumerAction>> consumerActions = getAllConsumerActions(consumer);
        for (Class<?> eventType : consumerActions.keySet()) {
            registry.putIfAbsent(eventType, new CopyOnWriteArraySet<>());
            registry.get(eventType).addAll(consumerActions.get(eventType));
        }
    }

    public List<ConsumerAction> getMatchedConsumerActions(Object event) {
        List<ConsumerAction> matchedConsumers = new ArrayList<>();
        registry.forEach((k, v) -> {
            if (k.isAssignableFrom(event.getClass())) {
                matchedConsumers.addAll(v);
            }
        });

        return matchedConsumers;
    }

    private Map<Class<?>, List<ConsumerAction>> getAllConsumerActions(Object consumer) {
        Map<Class<?>, List<ConsumerAction>> consumerActions = new HashMap<>();
        for (Method m : getAnnotatedMethods(consumer.getClass())) {
            Class<?>[] parameterTypes = m.getParameterTypes();
            Class<?> eventType = parameterTypes[0];
            consumerActions.putIfAbsent(eventType, new ArrayList<>());
            consumerActions.get(eventType).add(new ConsumerAction(consumer, m));
        }

        return consumerActions;
    }

    private List<Method> getAnnotatedMethods(  Class<?> clazz) {
        List<Method> annotatedMethods = new ArrayList<>();

        Method[] ms = clazz.getMethods();
        for (Method m : ms) {
            Annotation[] annotations = m.getAnnotations();
            for (Annotation annotation : annotations) {
                if (!(annotation instanceof Consumer)) continue;
                Class<?>[] parameterTypes = m.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException("方法" + m + "有注解@Consumer,但参数多于一个。给定参数有" + parameterTypes.length + "个");
                }
                annotatedMethods.add(m);
            }
        }

        return annotatedMethods;
    }

}
