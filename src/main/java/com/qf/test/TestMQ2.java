package com.qf.test;

import com.qf.Consumer;

import java.util.HashMap;
import java.util.Map;

public class TestMQ2 {
    private Map<Integer, String> messges = new HashMap<>();

    public TestMQ2() {
        messges.put(1, "清风真有才！");
        messges.put(2, "清风真聪明！");
        messges.put(3, "清风真开悟！");
        messges.put(4, "清风真能干！");
    }

    @Consumer
    public void sendMessage(Integer id) {
        System.out.println("发送消息:" + messges.get(id));
    }

}
