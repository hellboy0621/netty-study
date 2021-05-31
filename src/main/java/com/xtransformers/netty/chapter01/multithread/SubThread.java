package com.xtransformers.netty.chapter01.multithread;

import java.util.concurrent.TimeUnit;

/**
 * 子线程 模拟 Netty 异步响应结果
 *
 * @author daniel
 * @date 2021-05-30
 */
public class SubThread extends Thread {

    private RequestFuture request;

    public SubThread(RequestFuture request) {
        this.request = request;
    }

    @Override
    public void run() {
        Response resp = new Response();
        resp.setId(request.getId());
        resp.setResult("server response: id[" + request.getId()
                + "] - Thread id[" + Thread.currentThread().getId() + "]");

        // 子线程模拟睡眠1s
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 将响应结果返回主线程
        RequestFuture.received(resp);
    }
}
