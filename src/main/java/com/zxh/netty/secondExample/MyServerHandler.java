package com.zxh.netty.secondExample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
 * @version 1.0
 * @Author ningque
 * @Date 2019/11/22
 */
public class MyServerHandler extends SimpleChannelInboundHandler<String> {
    /**
     *
     * @param ctx       //表示上下文信息
     * @param msg       //接收到的消息
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+", "+msg);

        //接受到消息之后需要给客户端返回结果
        ctx.writeAndFlush("from server: "+ UUID.randomUUID());
    }

    /**
     * 出现异常的处理方法
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}
