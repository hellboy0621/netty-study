package com.xtransformers.netty.chapter01.server;

import com.alibaba.fastjson.JSONObject;
import com.xtransformers.netty.chapter01.multithread.RequestFuture;
import com.xtransformers.netty.chapter01.multithread.Response;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 注解 @ChannelHandler.Sharable 表示此 Handler 对所有 Channel 共享，无状态，注意多线程并发
 *
 * @author daniel
 * @date 2021-05-30
 */
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取客户端发送的数据
     *
     * @param ctx ChannelHandlerContext
     * @param msg msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        if (msg instanceof ByteBuf) {
//            System.out.println(((ByteBuf) msg).toString(Charset.defaultCharset()));
//        }
//        ctx.channel().writeAndFlush("msg has recived!");
        RequestFuture request = JSONObject.parseObject(msg.toString(), RequestFuture.class);
        long id = request.getId();
        System.out.println("request : " + msg);

        // 构建响应结果
        Response response = new Response();
        response.setId(id);
        response.setResult("server response ok.");
        // 把响应结果返回客户端
        ctx.channel().writeAndFlush(JSONObject.toJSONString(response));
    }
}
