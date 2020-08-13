package com.fuxw.venom.common.serial;

/**
 * 自定义序列化接口
 *
 * @author: Sven.Fu
 * @create: 2020-08-12 13:40
 **/
public interface Serializer {
    /**
     * 序列化
     *
     * @param obj
     * @return
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     *
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] bytes);
}
