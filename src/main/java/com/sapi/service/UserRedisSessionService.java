package com.sapi.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

@Service
public class UserRedisSessionService {
	private static final Logger logger = LoggerFactory.getLogger(UserRedisSessionService.class);

	@Autowired
	private FindByIndexNameSessionRepository<?> sessionRepository;

	public void logoutUserSessions(String username, String sessionIdCannotBeDelete) {
		Collection<?> userSessions = sessionRepository
				.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, username)
				.values();

		System.out.println("userSessions.size():\t" + userSessions.size());
		for (Object userSession : userSessions) {
			Session session = (Session) userSession;
			System.out.println(session.getAttribute("username").toString() + "\t\t"
					+ sessionRepository.findById(session.getId()).getAttribute("username").toString());
			if (!sessionIdCannotBeDelete.equals(session.getId())) {
				logger.info(username + ", sessionRepository.deleteById(" + session.getId() + ");");
				sessionRepository.deleteById(session.getId());
			}
		}
	}

	public void logoutAllUserSession() {
	}
}
