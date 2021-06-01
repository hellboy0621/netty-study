package com.xtransformers.netty.chapter01.client;

import com.alibaba.fastjson.JSONObject;
import com.xtransformers.netty.chapter01.multithread.RequestFuture;
import com.xtransformers.netty.chapter01.multithread.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author daniel
 * @date 2021-05-31
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 由于经过了 StringDecoder 解码器，所以 msg 为 String 类型
        Response response = JSONObject.parseObject(msg.toString(), Response.class);
        RequestFuture.received(response);
    }
}
