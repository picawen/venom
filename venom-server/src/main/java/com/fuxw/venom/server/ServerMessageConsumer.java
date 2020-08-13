package com.fuxw.venom.server;

import cn.hutool.json.JSONUtil;
import com.fuxw.venom.common.MessageConsumer;
import com.fuxw.venom.common.entity.Message;
import com.fuxw.venom.common.entity.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;

/**
 * 服务端消息消费者
 *
 * @author: Sven.Fu
 * @create: 2020-08-12 15:07
 **/
public class ServerMessageConsumer extends MessageConsumer {

    public ServerMessageConsumer(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(MessageWrapper event) throws Exception {
        ChannelHandlerContext ctx = event.getCtx();
        Message message = event.getMessage();
        // 处理具体的业务逻辑，这里只做了简单的打印输出
        System.out.println("server --> " + JSONUtil.toJsonStr(message));
        // 处理完之后回送消息
        Message result = new Message();
        result.setId("R --> " + message.getId());
        result.setName("R --> " + message.getName());
        result.setMessage("R --> " + message.getMessage());
        ctx.writeAndFlush(result);
    }
}
