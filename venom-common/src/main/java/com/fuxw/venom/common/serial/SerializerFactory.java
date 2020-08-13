package com.fuxw.venom.common.serial;

import com.fuxw.venom.common.serial.impl.KryoSerializer;

/**
 * 序列化工厂
 *
 * @author: Sven.Fu
 * @create: 2020-08-12 14:10
 **/
public class SerializerFactory {
    /**
     * 获取序列化工具类
     *
     * @param cls
     * @return
     */
    public static Serializer getSerializer(Class<?> cls) {
        return new KryoSerializer(cls);
    }
}
