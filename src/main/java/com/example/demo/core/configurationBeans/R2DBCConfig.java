package com.example.demo.core.configurationBeans;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
public class R2DBCConfig {
   @Bean
   public PostgresqlConnectionFactory connectionFactory() {
       return new PostgresqlConnectionFactory(
               PostgresqlConnectionConfiguration.builder()
                       .host("localhost")
                       .database("mydb")
                       .username("postgres")
                       .password("admin").build());
   }    
}
