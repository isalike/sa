package com.sapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=60, redisNamespace=RedisSessionConfig.redisNamespace)
public class RedisSessionConfig {
	public static final String redisNamespace="sapi";
    
}