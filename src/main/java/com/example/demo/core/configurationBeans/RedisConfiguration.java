package com.example.demo.core.configurationBeans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

  @Bean
  public ReactiveRedisTemplate<String, byte[]> stringReactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory){
      RedisSerializationContext<String, byte[]> serializationContext = RedisSerializationContext
          .<String, byte[]>newSerializationContext(new StringRedisSerializer())
          .hashKey(new StringRedisSerializer())
          .hashValue(new ByteSeriallizer())
          .build();
      return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
  }

    
}
