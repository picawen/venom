package com.fuxw.venom.common.codec;

import com.fuxw.venom.common.entity.Message;
import com.fuxw.venom.common.serial.Serializer;
import com.fuxw.venom.common.serial.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义消息编码器
 *
 * @author: Sven.Fu
 * @create: 2020-08-12 14:14
 **/
public class MessageEncoder extends MessageToByteEncoder<Message> {

    private Serializer serializer = SerializerFactory.getSerializer(Message.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        byte[] bytes = serializer.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
