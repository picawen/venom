package com.fuxw.venom.common.serial.impl;

import cn.hutool.core.io.IoUtil;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.BeanSerializer;
import com.fuxw.venom.common.serial.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 基于 Kryo 的序列化
 *
 * @author: Sven.Fu
 * @create: 2020-08-12 13:45
 **/
public class KryoSerializer implements Serializer {

    private final Class<?> cls;

    public KryoSerializer(Class<?> cls) {
        this.cls = cls;
    }

    public Class<?> getCls() {
        return cls;
    }

    /**
     * Kryo为非线程安全类
     */
    final ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.register(cls, new BeanSerializer(kryo, cls));
            return kryo;
        }
    };

    public Kryo getKryo() {
        return kryoLocal.get();
    }

    @Override
    public byte[] serialize(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);
        try {
            Kryo kryo = this.getKryo();
            kryo.writeObjectOrNull(output, obj, obj.getClass());
            output.flush();
            return bos.toByteArray();
        } finally {
            IoUtil.close(output);
            IoUtil.close(bos);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Input input = new Input(bis);
        try {
            Kryo kryo = this.getKryo();
            return (T) kryo.readObjectOrNull(input, cls);
        } finally {
            IoUtil.close(input);
            IoUtil.close(bis);
        }
    }
}
