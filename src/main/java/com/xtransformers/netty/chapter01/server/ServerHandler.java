package com.xtransformers.netty.chapter01.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

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
        if (msg instanceof ByteBuf) {
            System.out.println(((ByteBuf) msg).toString(Charset.defaultCharset()));
        }
        ctx.channel().writeAndFlush("msg has recived!");
    }
}
