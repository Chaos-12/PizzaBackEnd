package com.example.demo.infraestructure.redisInfraestructure;

import java.util.UUID;

import com.example.demo.core.configurationBeans.ByteSerializer;
import com.example.demo.security.UserLogInfo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

	@Bean
	RedisRepository<byte[], String> createImageRedisRepository(ReactiveRedisConnectionFactory factory){
    	ByteSerializer byteSerializer = new ByteSerializer();
    	RedisSerializationContext.RedisSerializationContextBuilder<String, byte[]> builder = 
				RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
    	RedisSerializationContext<String, byte[]> context = builder.value(byteSerializer).build();
		return new RedisRepository<byte[], String>(new ReactiveRedisTemplate<>(factory, context));
	}

	@Bean
	RedisRepository<UserLogInfo, UUID> createLogInfoRedisRepository(ReactiveRedisConnectionFactory factory){
    	Jackson2JsonRedisSerializer<UserLogInfo> logInfoSerializer = new Jackson2JsonRedisSerializer<>(UserLogInfo.class);
		Jackson2JsonRedisSerializer<UUID> uuidSerializer = new Jackson2JsonRedisSerializer<>(UUID.class);
    	RedisSerializationContext.RedisSerializationContextBuilder<UUID, UserLogInfo> builder =
        		RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
    	RedisSerializationContext<UUID, UserLogInfo> context = builder.key(uuidSerializer).value(logInfoSerializer).build();
		return new RedisRepository<UserLogInfo, UUID>(new ReactiveRedisTemplate<>(factory, context));
	}

	@Bean
	RedisRepository<UUID, String> createRefreshRedisRepository(ReactiveRedisConnectionFactory factory) {
    	Jackson2JsonRedisSerializer<UUID> serializer = new Jackson2JsonRedisSerializer<>(UUID.class);
    	RedisSerializationContext.RedisSerializationContextBuilder<String, UUID> builder =
        		RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
    	RedisSerializationContext<String, UUID> context = builder.value(serializer).build();
    	return new RedisRepository<UUID, String>(new ReactiveRedisTemplate<>(factory, context));
	}
}