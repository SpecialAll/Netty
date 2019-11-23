package com.zxh.netty.firstExample;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @version 1.0
 * @Author ningque
 * @Date 2019/11/21
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    //初始化管道
   @Override
    protected void initChannel(SocketChannel ch) throws Exception {
       ChannelPipeline pipeline = ch.pipeline();

       /**
        * 这里的HttpServerCodec包含两个类 HttpRequestDecoder（代表客户端向服务端发送内容的解码类） and  HttpResponseEncoder（代表服务端向客户端发送内容 的编码）
        */
       pipeline.addLast("httpServerCodec", new HttpServerCodec());

       pipeline.addLast("testHttpServerHandler", new TestHttpServerHandler());
    }
}
