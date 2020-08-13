package com.fuxw.venom.client;

import cn.hutool.core.util.IdUtil;
import com.fuxw.venom.common.MessageProducer;
import com.fuxw.venom.common.VenomWorkerPoolFactory;
import com.fuxw.venom.common.entity.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Venom客户端处理类
 *
 * @author: Sven.Fu
 * @create: 2020-08-12 17:00
 **/
public class VenomClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        String producerId = IdUtil.fastSimpleUUID();
        MessageProducer messageProducer = VenomWorkerPoolFactory.getInstance().getMessageProducer(producerId);
        messageProducer.sendMessage(ctx, message);
    }
}
