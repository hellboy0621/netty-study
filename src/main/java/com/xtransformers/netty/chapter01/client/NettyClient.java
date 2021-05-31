package com.xtransformers.netty.chapter01.client;

import com.alibaba.fastjson.JSONObject;
import com.xtransformers.netty.chapter01.multithread.RequestFuture;
import com.xtransformers.netty.chapter01.multithread.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

/**
 * @author daniel
 * @date 2021-05-31
 */
public class NettyClient {
    public static EventLoopGroup group;
    public static Bootstrap bootstrap;

    static {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建一个 primise 对象
        Promise<Response> promise = new DefaultPromise<>(group.next());
        final ClientHandler handler = new ClientHandler();
        handler.setPromise(promise);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) {
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(handler);
                ch.pipeline().addLast(new LengthFieldPrepender(4, false));
                ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
            }
        });

        // 连接服务器
        ChannelFuture future = bootstrap.connect("127.0.0.1", 8080).sync();
        RequestFuture request = new RequestFuture();
        request.setId(1);
        request.setRequest("Hello World!");
        String requestStr = JSONObject.toJSONString(request);
        future.channel().writeAndFlush(requestStr);

        Response response = promise.get();
        System.out.println(JSONObject.toJSONString(response));
    }
}
