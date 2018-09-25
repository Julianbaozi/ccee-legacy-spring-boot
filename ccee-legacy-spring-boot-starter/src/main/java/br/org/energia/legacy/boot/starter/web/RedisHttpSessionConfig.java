package br.org.energia.legacy.boot.starter.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import br.org.energia.portal.dto.ModuloDTO;
import redis.clients.jedis.JedisPoolConfig;

@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "ccee.legacy.web", name = "enabled-session", havingValue = "redis")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60, redisNamespace="energia")
public class RedisHttpSessionConfig {

	@Value("${spring.redis.host:localhost}")
	private String redisHostName;

	@Value("${spring.redis.port:6379}")
	private Integer redisPort;
	
	public String getRedisHostName() {
		return redisHostName;
	}

	public void setRedisHostName(String redisHostName) {
		this.redisHostName = redisHostName;
	}

	public Integer getRedisPort() {
		return redisPort;
	}

	public void setRedisPort(Integer redisPort) {
		this.redisPort = redisPort;
	}
	
	@Bean
	public JedisConnectionFactory jedisConnectionFactory(@Autowired JedisPoolConfig poolConfig) {
		
		JedisConnectionFactory factory = new JedisConnectionFactory(poolConfig);
		factory.setHostName(getRedisHostName());
		factory.setPort(getRedisPort());
		factory.setUsePool(true);
		
		return factory;
	}

	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
	    poolConfig.setMaxTotal(128);
	    poolConfig.setBlockWhenExhausted(false);
		return poolConfig;
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
		RedisTemplate<String, String> redis = new RedisTemplate<String, String>();
		redis.setConnectionFactory(cf);
		redis.setKeySerializer(new StringRedisSerializer());
		redis.setValueSerializer(new Jackson2JsonRedisSerializer<String>(String.class));
		return redis;
	}
	
	@Bean("redisModulo")
	public RedisTemplate<String, ModuloDTO> redisTemplateModulo(RedisConnectionFactory cf) {
		RedisTemplate<String, ModuloDTO> redis = new RedisTemplate<String, ModuloDTO>();
		redis.setConnectionFactory(cf);
		redis.setKeySerializer(new StringRedisSerializer());
		redis.setValueSerializer(new Jackson2JsonRedisSerializer<ModuloDTO>(ModuloDTO.class));
		return redis;
	}
	
}