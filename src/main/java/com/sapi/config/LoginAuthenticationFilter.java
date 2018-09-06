package com.sapi.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private static final Logger logger = LoggerFactory.getLogger(LoginAuthenticationFilter.class);

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		if(request.getParameter("code") == null) {
			throw new BadCredentialsException("CODE_ERROR");
		}
		
		String verification = request.getParameter("code");

		logger.info("attemptAuthentication : " + verification);
		logger.info("session code_image : " + request.getSession().getAttribute("code_image"));

		if (verification.equals("error")) {
			throw new BadCredentialsException("CODE_ERROR");
		}

		return super.attemptAuthentication(request, response);
	}
}