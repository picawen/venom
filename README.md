# Venom

Venom 是一个基于 **Netty** + **Disruptor** 构建的高性能网络通信框架，采用客户端 - 服务端架构，支持高效的消息传输和处理。

## 项目结构

```
venom
├── venom-common    # 公共模块（消息实体、编解码器、序列化等）
├── venom-server    # 服务端模块
└── venom-client    # 客户端模块
```

## 技术栈

- **Java 1.8+**
- **Netty 4.1.50.Final** - 异步事件驱动的网络框架
- **Disruptor 3.4.2** - 高性能无锁队列
- **Kryo 5.0.0-RC8** - 高效序列化框架
- **Hutool 5.3.10** - Java 工具库

## 模块说明

### venom-common

公共模块，包含：

- **消息实体** (`Message`, `MessageWrapper`)
- **编解码器** (`MessageEncoder`, `MessageDecoder`)
- **序列化接口及实现** (`Serializer`, `KryoSerializer`)
- **消息生产/消费接口** (`MessageProducer`, `MessageConsumer`)
- **Disruptor 工作池工厂** (`VenomWorkerPoolFactory`)

### venom-server

服务端模块，提供：

- **VenomServer** - 服务端启动类，基于 Netty 构建
- **VenomServerHandler** - 服务端消息处理器
- **ServerMessageConsumer** - 服务端消息消费者

### venom-client

客户端模块，提供：

- **VenomClient** - 客户端启动类，支持连接服务端并发送消息
- **VenomClientHandler** - 客户端消息处理器
- **ClientMessageConsumer** - 客户端消息消费者

## 快速开始

### 编译项目

```bash
mvn clean install
```

### 启动服务端

```bash
cd venom-server
# 运行 VenomServer 的 main 方法
```

服务端默认监听端口：**9152**

### 启动客户端

```bash
cd venom-client
# 运行 VenomClient 的 main 方法
```

客户端会自动连接服务端并发送测试消息。

## 核心特性

- ✅ 基于 Netty 的异步非阻塞 I/O
- ✅ 集成 Disruptor 实现高性能消息队列
- ✅ 支持 Kryo 高效序列化
- ✅ 自适应接收缓冲区分配
- ✅ 内存池化 (PooledByteBufAllocator)
- ✅ 多消费者并行处理

## 配置参数

| 参数 | 默认值 | 说明 |
|------|--------|------|
| 服务端端口 | 9152 | 服务端监听端口 |
| Disruptor 缓冲区大小 | 1024 * 1024 | 环形缓冲区大小 |
| 消费者数量 | 4 | 并行消费者数量 |
| 等待策略 | YieldingWaitStrategy | Disruptor 等待策略 |

## 使用示例

### 自定义消息消费者

```java
public class MyMessageConsumer implements MessageConsumer {
    @Override
    public void consume(Message message) {
        // 处理消息逻辑
        System.out.println("收到消息：" + message);
    }
}
```

### 启动服务端

```java
public static void main(String[] args) {
    // 初始化消费者
    MessageConsumer[] consumers = new MessageConsumer[4];
    for (int i = 0; i < consumers.length; i++) {
        consumers[i] = new MyMessageConsumer();
    }
    
    // 启动 Disruptor 工作池
    VenomWorkerPoolFactory.getInstance()
        .initAndStart(ProducerType.MULTI, 1024 * 1024, 
                     new YieldingWaitStrategy(), consumers);
    
    // 启动服务端
    new VenomServer(9152).start();
}
```

## 作者

- **Sven.Fu** (fuxw)

## 许可证

本项目采用开源许可证。

## 注意事项

- 确保已安装 JDK 1.8 或更高版本
- 服务端启动前请确保端口 9152 未被占用
- 生产环境建议根据实际需求调整 Disruptor 缓冲区大小和消费者数量
