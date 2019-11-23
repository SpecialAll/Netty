package com.zxh.netty.thirdExample;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @version 1.0
 * @Author ningque
 * @Date 2019/11/22
 *
 * 需求：
 *  多个客户端与服务器端连接
 *  当客户端连接时发现已经有客户端连接，就打印出来此时连接的客户端名称；
 *  并且当有一个客户端连接时，会广播给其他客户端“我已上线”；
 *
 *  当客户端下线时，需要通知其他客户端“我已下线”
 */
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {
    //channelGroup就是存储channel的对象或者容器
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //服务器接受到任意消息需要做的操作
        //该channel就是向服务端发送消息的channel
        Channel channel = ctx.channel();

        channelGroup.forEach(ch -> {
            if(channel != ch){
                ch.writeAndFlush(channel.remoteAddress()+", 发送的消息： "+msg+"\n");
            } else {
                ch.writeAndFlush("[自己] "+msg+"\n");
            }
        });
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //向channelGroup中的所有channel都发送一条消息
        channelGroup.writeAndFlush("[服务器】 - "+channel.remoteAddress() + " 加入\n");
        //然后将要加入的channel加入到group
        channelGroup.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        //有客户端离开，就会给所有channel发送一条离开消息
        channelGroup.writeAndFlush("[服务器] - "+channel.remoteAddress() + " 离开\n");

        //netty会自动将断掉连接的channel自动删除
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+" 上线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+" 离线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
