package br.org.energia.legacy.boot.starter.web;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransientSessionCleaner {

	@Autowired
	private MapSessionRepository repository;

	@Autowired
	private ConcurrentHashMap<String, ExpiringSession> sessions;

	@Scheduled(fixedDelayString = "${ccee.legacy.web.clean-period:60000}")
    public void clean() {
		log.debug("clean - start");
		sessions.entrySet().forEach(session  -> {
				if (session.getValue().isExpired()) {
					repository.delete(session.getKey()) ; 
					log.debug(session.getKey() + " - expired ");
				}
			});
		log.debug("clean - end");
	}

}