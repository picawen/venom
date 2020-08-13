package com.fuxw.venom.common;

import com.fuxw.venom.common.entity.Message;
import com.fuxw.venom.common.entity.MessageWrapper;
import com.lmax.disruptor.RingBuffer;
import io.netty.channel.ChannelHandlerContext;

/**
 * Disruptor消息生产者
 *
 * @author: Sven.Fu
 * @create: 2020-08-12 14:55
 **/
public class MessageProducer {
    private String producerId;
    private RingBuffer<MessageWrapper> ringBuffer;

    public MessageProducer(String producerId, RingBuffer<MessageWrapper> ringBuffer) {
        this.producerId = producerId;
        this.ringBuffer = ringBuffer;
    }

    public void sendMessage(ChannelHandlerContext ctx, Message message) {
        long sequence = ringBuffer.next();
        try {
            MessageWrapper wrapper = ringBuffer.get(sequence);
            wrapper.setMessage(message);
            wrapper.setCtx(ctx);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
