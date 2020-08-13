package com.fuxw.venom.client;

import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONUtil;
import com.fuxw.venom.common.MessageConsumer;
import com.fuxw.venom.common.entity.Message;
import com.fuxw.venom.common.entity.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * 客户端消息消费者
 *
 * @author: Sven.Fu
 * @create: 2020-08-12 15:50
 **/
public class ClientMessageConsumer extends MessageConsumer {

    public ClientMessageConsumer(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(MessageWrapper event) throws Exception {
        ChannelHandlerContext ctx = event.getCtx();
        Message message = event.getMessage();
        try {
            Console.log("client --> " + JSONUtil.toJsonStr(message));
        } finally {
            ReferenceCountUtil.release(message);
        }
    }
}
