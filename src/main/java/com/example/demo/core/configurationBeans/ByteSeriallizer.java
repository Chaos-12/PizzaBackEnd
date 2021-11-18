package com.example.demo.core.configurationBeans;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ByteSeriallizer implements RedisSerializer<byte[]>{
    @Override
    public byte[] deserialize(byte[] bytes) throws SerializationException {
        return bytes;
    }

    @Override
    public byte[] serialize(byte[] bytes) throws SerializationException {
        return bytes;
    }
}
