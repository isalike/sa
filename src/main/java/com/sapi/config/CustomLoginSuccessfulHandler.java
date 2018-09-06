package com.sapi.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.sapi.service.UserRedisSessionService;

@Component
public class CustomLoginSuccessfulHandler implements AuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomLoginSuccessfulHandler.class);

	@Autowired
	private UserRedisSessionService userRedisSessionService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		String username = request.getParameter("username");

		logger.info("CustomLoginSuccessfulHandler : " + username);
		logger.info("sessionID : " + request.getSession().getId());

		// deletePreviousSessionInRedis(username, request.getSession().getId());
		userRedisSessionService.logoutUserSessions(username, request.getSession().getId());

		request.getSession().setAttribute("username", username);

		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write("{\"message\":\"LOGIN_SUCCESSFUL\"}");
	}

	/*public void deletePreviousSessionInRedis(String username, String sessionId) {
		RedisClient redisClient = RedisClient.create("redis://localhost:6379/1");
		StatefulRedisConnection<String, String> connection = redisClient.connect();

		RedisCommands<String, String> syncCommands = connection.sync();

		List<String> PRINCIPAL_NAME_INDEX_NAME = syncCommands.keys("sapi:*.PRINCIPAL_NAME_INDEX_NAME:" + username);
		if (PRINCIPAL_NAME_INDEX_NAME.size() > 0) {
			String redisIndex = PRINCIPAL_NAME_INDEX_NAME.get(0);
			logger.info(redisIndex);

			Object[] allSession = syncCommands.smembers(redisIndex).toArray();

			for (int i = 0; i < allSession.length; i++) {
				String[] raw = allSession[i].toString().split(java.util.regex.Pattern.quote("$"));
				if (raw.length > 1) {
					logger.info("sessionRepository.deleteById(" + raw[1] + ");");
					// sessionRepository.deleteById(raw[1]);

					
					  String expireToBeDelete = "sapi:sessions:expires:" + raw[1];
					  logger.info(expireToBeDelete); syncCommands.del(expireToBeDelete);
					  
					  String sessionToBeDelete = "sapi:sessions:" + raw[1];
					  logger.info(sessionToBeDelete); syncCommands.del(sessionToBeDelete);
					 
				}
			}
			// syncCommands.del(redisIndex);
		}

		connection.close();
	}*/

}
