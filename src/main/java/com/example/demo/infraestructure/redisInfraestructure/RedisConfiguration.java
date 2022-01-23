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
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

	@Bean
	RedisRepository<byte[], String> createImageRedisRepository(ReactiveRedisConnectionFactory factory){
    	return RedisConfiguration.createRedisRepository(factory, new ByteSerializer());
	}

	@Bean
	RedisRepository<UserLogInfo, String> createOAuthInfoRedisRepository(ReactiveRedisConnectionFactory factory){
		return RedisConfiguration.createRedisRepository(factory, UserLogInfo.class);
	}

	@Bean
	RedisRepository<UUID, String> createRefreshRedisRepository(ReactiveRedisConnectionFactory factory) {
		return RedisConfiguration.createRedisRepository(factory, UUID.class);
	}

	private static <T> RedisRepository<T, String> createRedisRepository(ReactiveRedisConnectionFactory factory, RedisSerializer<T> objSerializer){
    	RedisSerializationContext.RedisSerializationContextBuilder<String, T> builder =
        		RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
    	RedisSerializationContext<String, T> context = builder.value(objSerializer).build();
		return new RedisRepository<T, String>(new ReactiveRedisTemplate<>(factory, context));
	}

	private static <T> RedisRepository<T, String> createRedisRepository(ReactiveRedisConnectionFactory factory, Class objClass) {
    	return RedisConfiguration.createRedisRepository(
			factory, new Jackson2JsonRedisSerializer<T>(objClass)
		);
	}
}