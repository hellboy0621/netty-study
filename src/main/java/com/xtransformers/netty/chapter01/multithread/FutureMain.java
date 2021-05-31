package com.xtransformers.netty.chapter01.multithread;

import java.util.ArrayList;
import java.util.List;

/**
 * @author daniel
 * @date 2021-05-30
 */
public class FutureMain {

    public static void main(String[] args) {
        List<RequestFuture> reqs = new ArrayList<>();
        for (int index = 1; index < 10; index++) {
            RequestFuture req = new RequestFuture();
            req.setId(index);
            req.setRequest("hello world[" + (long) index + "]");
            RequestFuture.addFuture(req);
            reqs.add(req);

            sendMsg(req);

            SubThread subThread = new SubThread(req);
            subThread.start();
        }

        for (RequestFuture req : reqs) {
            Object result = req.get();
            System.out.println(result.toString());
        }
    }

    private static void sendMsg(RequestFuture req) {
        System.out.println("client send msg, id is [" + req.getId() + "] Thread id["
                + Thread.currentThread().getId() + "]");
    }
}
