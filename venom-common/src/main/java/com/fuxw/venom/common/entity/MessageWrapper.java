package com.fuxw.venom.common.entity;

import io.netty.channel.ChannelHandlerContext;

/**
 * 消息包装类
 *
 * @author: Sven.Fu
 * @create: 2020-08-12 14:52
 **/
public class MessageWrapper {
    private Message message;
    private ChannelHandlerContext ctx;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
