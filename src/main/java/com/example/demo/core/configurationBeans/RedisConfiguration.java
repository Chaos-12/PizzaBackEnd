package com.example.demo.core.configurationBeans;

import com.example.demo.application.userApplication.AutenticationUser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

  	@Bean
  	ReactiveRedisOperations<String, byte[]> redisImageOperations(ReactiveRedisConnectionFactory factory) {
    	ByteSerializer byteSerializer = new ByteSerializer();
    	RedisSerializationContext.RedisSerializationContextBuilder<String, byte[]> builder = 
				RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
    	RedisSerializationContext<String, byte[]> context = builder.value(byteSerializer).build();
    	return new ReactiveRedisTemplate<>(factory, context);
  	}

  	@Bean
  	ReactiveRedisOperations<String, AutenticationUser> redisOperations(ReactiveRedisConnectionFactory factory) {
    	Jackson2JsonRedisSerializer<AutenticationUser> serializer = new Jackson2JsonRedisSerializer<>(AutenticationUser.class);
    	RedisSerializationContext.RedisSerializationContextBuilder<String, AutenticationUser> builder =
        		RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
    	RedisSerializationContext<String, AutenticationUser> context = builder.value(serializer).build();
    	return new ReactiveRedisTemplate<>(factory, context);
	}
}