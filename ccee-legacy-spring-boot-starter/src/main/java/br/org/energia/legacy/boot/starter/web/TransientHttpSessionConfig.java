package br.org.energia.legacy.boot.starter.web;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "spring.session", name = "store-type", havingValue = "hash_map")
@EnableSpringHttpSession
public class TransientHttpSessionConfig {

	@Value("${server.servlet.session.timeout}")
	private Integer defaultMaxInactiveInterval;

	@Bean
	public ConcurrentHashMap<String, ExpiringSession> getSessionsMap() {
		return new ConcurrentHashMap<>();
	}
	
	@Bean
	public SessionRepository<ExpiringSession> sessionRepository(
			@Autowired ConcurrentHashMap<String, ExpiringSession> sessionsMap) {
		MapSessionRepository mapSessionRepository = new MapSessionRepository(sessionsMap);
		if (defaultMaxInactiveInterval != null) {
			mapSessionRepository.setDefaultMaxInactiveInterval(defaultMaxInactiveInterval);
		}
		return mapSessionRepository;
	}
	
	@Bean
	public TransientSessionCleaner sessionCleaner() {
		return new TransientSessionCleaner();
	}

}