package com.fuxw.venom.server;

import cn.hutool.core.lang.Console;
import com.fuxw.venom.common.MessageConsumer;
import com.fuxw.venom.common.VenomWorkerPoolFactory;
import com.fuxw.venom.common.codec.MessageDecoder;
import com.fuxw.venom.common.codec.MessageEncoder;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Venom服务端
 *
 * @author: Sven.Fu
 * @create: 2020-08-12 15:15
 **/
public class VenomServer {

    private final int port;

    public VenomServer(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new MessageEncoder());
                    ch.pipeline().addLast(new MessageDecoder());
                    ch.pipeline().addLast(new VenomServerHandler());
                }
            });
            ChannelFuture cf = serverBootstrap.bind(this.port).sync();
            Console.log("venom server startup");
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            Console.log("venom server shutdown");
        }
    }

    public static void main(String[] args) {
        MessageConsumer[] consumers = new MessageConsumer[4];
        for (int i = 0; i < consumers.length; i++) {
            MessageConsumer messageConsumer = new ServerMessageConsumer("serverId:" + i);
            consumers[i] = messageConsumer;
        }
        VenomWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI, 1024 * 1024, new YieldingWaitStrategy(), consumers);
        new VenomServer(9152).start();
    }
}
