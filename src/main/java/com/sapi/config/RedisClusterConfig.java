package com.sapi.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisClusterConfig {
	private Logger logger = LoggerFactory.getLogger(RedisClusterConfig.class);

	@Value("${redis.cluster.nodes}")
	private String[] redisClusterNodes;

	private List<String> nodes = new ArrayList<String>();

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		if (nodes.size() == 0) {
			logger.info("Initialize redis cluster nodes (" + redisClusterNodes.length + ") : ");
			nodes = new ArrayList<String>();
			for (int i = 0; i < redisClusterNodes.length; i++) {
				if (redisClusterNodes[i].length() < 7 || !redisClusterNodes[i].contains(":")) {
					try {
						String errorMsg = "RedisConfig exception : invalid format of redis.cluster.nodes";
						logger.error(errorMsg);
						throw new Exception(errorMsg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				nodes.add(redisClusterNodes[i]);
			}

			for (int i = 0; i < nodes.size(); i++) {
				logger.info(nodes.get(i));
			}
		} else {
			logger.info("Get redis connection.");
		}

		return new LettuceConnectionFactory(new RedisClusterConfiguration(nodes));
	}
}
