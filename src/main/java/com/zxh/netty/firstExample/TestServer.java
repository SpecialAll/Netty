package com.zxh.netty.firstExample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @version 1.0
 * @Author ningque
 * @Date 2019/11/21
 *
 * 服务端的简单demo
 */
public class TestServer {
    public static void main(String[] args) throws InterruptedException {
        //建立两个事件循环组
        EventLoopGroup bossGroup = new NioEventLoopGroup();    //相当于一个死循环，从客户端不断接收连接  仅仅接受连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();   //该事件组用于处理上面boss发送来的连接请求  处理连接

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();   //用于启动服务端
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).
                    childHandler(new TestServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            //优雅关闭事件组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
