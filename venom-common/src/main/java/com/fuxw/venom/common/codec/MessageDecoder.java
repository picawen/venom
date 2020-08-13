package com.fuxw.venom.common.codec;

import com.fuxw.venom.common.entity.Message;
import com.fuxw.venom.common.serial.Serializer;
import com.fuxw.venom.common.serial.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义消息解码器
 *
 * @author: Sven.Fu
 * @create: 2020-08-12 14:14
 **/
public class MessageDecoder extends ByteToMessageDecoder {

    private Serializer serializer = SerializerFactory.getSerializer(Message.class);

    /**
     * 数据流头部消息字节数
     */
    private final static int HEAD_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < HEAD_LENGTH) {
            return;
        }
        in.markReaderIndex();
        int length = in.readInt();
        if (length <= 0) {
            ctx.close();
        }
        if (in.readableBytes() < HEAD_LENGTH) {
            in.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        Message message = serializer.deserialize(bytes);
        out.add(message);
    }
}
