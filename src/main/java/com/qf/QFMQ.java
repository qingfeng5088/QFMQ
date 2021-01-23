package com.qf;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 同步阻塞式消息队列
 */
public class QFMQ {
    private Executor executor;
    private ConsumerRegistry registry = new ConsumerRegistry();

    public QFMQ() {
        this(Executors.newSingleThreadExecutor());
    }

    public QFMQ(Executor executor) {
        this.executor = executor;
    }

    /**
     * 添加观察者
     *
     * @param object
     */
    public void register(Object object) {
        registry.register(object);
    }

    /**
     * 删除观察者
     *
     * @param object
     */
    public void unregister(Object object) {
    }

    /**
     * 给观察者发送消息
     * @param message
     */
    public void post(Object message) {
        List<ConsumerAction> matchedConsumers = registry.getMatchedConsumerActions(message);
        for (ConsumerAction matchedConsumer : matchedConsumers) {
            executor.execute(() -> matchedConsumer.execute(message));
        }
    }
}
