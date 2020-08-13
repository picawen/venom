package com.fuxw.venom.common;

import com.fuxw.venom.common.entity.MessageWrapper;
import com.lmax.disruptor.WorkHandler;

/**
 * Disruptor消息消费者-抽象类
 *
 * @author: Sven.Fu
 * @create: 2020-08-12 15:02
 **/
public abstract class MessageConsumer implements WorkHandler<MessageWrapper> {

    private String consumerId;

    public MessageConsumer(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }
}
