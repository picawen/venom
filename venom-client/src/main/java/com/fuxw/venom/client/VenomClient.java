package com.fuxw.venom.client;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.RandomUtil;
import com.fuxw.venom.common.MessageConsumer;
import com.fuxw.venom.common.VenomWorkerPoolFactory;
import com.fuxw.venom.common.codec.MessageDecoder;
import com.fuxw.venom.common.codec.MessageEncoder;
import com.fuxw.venom.common.entity.Message;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Venom客户端
 *
 * @author: Sven.Fu
 * @create: 2020-08-12 16:44
 **/
public class VenomClient {
    private final String host;
    private final int port;

    public VenomClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void connect() {
        EventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new MessageEncoder());
                            ch.pipeline().addLast(new MessageDecoder());
                            ch.pipeline().addLast(new VenomClientHandler());
                        }
                    });
            ChannelFuture cf = bootstrap.connect(this.host, this.port).sync();
            Channel channel = cf.channel();
            Console.log("venom client connected");
            // 模拟发送数据
            for (int i = 1; i <= 10; i++) {
                Message message = new Message();
                message.setId(String.valueOf(i));
                message.setName(RandomUtil.randomString(12));
                message.setMessage(RandomUtil.randomStringUpper(28));
                channel.writeAndFlush(message);
            }
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
            Console.log("venom client shutdown");
        }
    }

    public static void main(String[] args) {
        MessageConsumer[] consumers = new MessageConsumer[4];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new ClientMessageConsumer("clientId:" + i);
        }
        VenomWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI, 1024 * 1024, new YieldingWaitStrategy(), consumers);
        new VenomClient("127.0.0.1", 9152).connect();
    }
}
