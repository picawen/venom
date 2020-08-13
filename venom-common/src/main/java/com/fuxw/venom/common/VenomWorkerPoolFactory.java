package com.fuxw.venom.common;

import cn.hutool.core.lang.Console;
import com.fuxw.venom.common.entity.MessageWrapper;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * Venom工作线程池工厂类
 *
 * @author: Sven.Fu
 * @create: 2020-08-12 15:36
 **/
public class VenomWorkerPoolFactory {

    private static class SingletonHolder {
        static final VenomWorkerPoolFactory instance = new VenomWorkerPoolFactory();
    }

    private VenomWorkerPoolFactory() {
    }

    public static VenomWorkerPoolFactory getInstance() {
        return SingletonHolder.instance;
    }

    private static Map<String, MessageProducer> producers = new ConcurrentHashMap<>();
    private static Map<String, MessageConsumer> consumers = new ConcurrentHashMap<>();

    private RingBuffer<MessageWrapper> ringBuffer;
    private SequenceBarrier sequenceBarrier;
    private WorkerPool<MessageWrapper> workerPool;

    public void initAndStart(ProducerType type, int bufferSize, WaitStrategy waitStrategy, MessageConsumer[] messageConsumers) {
        this.ringBuffer = RingBuffer.create(type, new EventFactory<MessageWrapper>() {
            @Override
            public MessageWrapper newInstance() {
                return new MessageWrapper();
            }
        }, bufferSize, waitStrategy);
        this.sequenceBarrier = this.ringBuffer.newBarrier();
        this.workerPool = new WorkerPool<MessageWrapper>(this.ringBuffer, this.sequenceBarrier, new EventExceptionHandler(), messageConsumers);
        for (MessageConsumer consumer : messageConsumers) {
            consumers.put(consumer.getConsumerId(), consumer);
        }
        this.ringBuffer.addGatingSequences(this.workerPool.getWorkerSequences());
        this.workerPool.start(Executors.newFixedThreadPool(10));
    }

    public MessageProducer getMessageProducer(String producerId) {
        MessageProducer producer = producers.get(producerId);
        if (producer == null) {
            producer = new MessageProducer(producerId, this.ringBuffer);
            producers.put(producerId, producer);
        }
        return producer;
    }

    private static class EventExceptionHandler implements ExceptionHandler<MessageWrapper> {
        @Override
        public void handleEventException(Throwable throwable, long l, MessageWrapper messageWrapper) {
            Console.error("handleEventException...");
        }

        @Override
        public void handleOnStartException(Throwable throwable) {
            Console.error("handleOnStartException...");
        }

        @Override
        public void handleOnShutdownException(Throwable throwable) {
            Console.error("handleOnShutdownException...");
        }
    }
}
