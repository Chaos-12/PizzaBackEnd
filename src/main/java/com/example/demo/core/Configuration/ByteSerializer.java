package com.example.demo.core.Configuration;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ByteSerializer implements RedisSerializer<byte[]>{
    @Override
    public byte[] deserialize(byte[] bytes) throws SerializationException {
        return bytes;
    }

    @Override
    public byte[] serialize(byte[] bytes) throws SerializationException {
        return bytes;
    }
}
