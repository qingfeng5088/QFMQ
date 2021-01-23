package com.qf.test;

import com.qf.Consumer;
import com.qf.QFMQ;

import java.util.HashMap;
import java.util.Map;

public class TestMQ {
    private Map<String, String> messges = new HashMap<>();

    public TestMQ() {
        messges.put("1", "邱羽彤真可爱！");
        messges.put("2", "邱羽彤真漂亮！");
        messges.put("3", "邱羽彤真聪明！");
        messges.put("4", "邱羽彤真能干！");
    }

    @Consumer
    public String getMessage(String id) {
        System.out.println("发送消息:" + messges.get(id));
        return messges.get(id);
    }

    @Consumer
    public void getMoney(Integer money) {
        System.out.println("今年赚了:" + money +"万！");
    }


    public static void main(String[] args) {
        TestMQ testMQ = new TestMQ();
        TestMQ2 testMQ2 = new TestMQ2();
        QFMQ mq = new QFMQ();
        mq.register(testMQ);
        mq.register(testMQ2);

        mq.post("3");
        mq.post(1);
    }
}
