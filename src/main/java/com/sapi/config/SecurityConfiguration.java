package com.sapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapi.repository.UsersRepository;
import com.sapi.service.CustomUserDetailsService;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UsersRepository.class)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private CustomLoginSuccessfulHandler customLoginSuccessfulHandler;

	@Autowired
	private CustomLoginFailureHandler customLoginFailureHandler;

	@Autowired
	private CustomLogoutSuccessfulHandler customLogoutSuccessfulHandler;

	@Autowired
	private CustomAccessDeniedHandler customAccessDeniedHandler;

	@Autowired
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
	}

	public LoginAuthenticationFilter loginAuthenticationFilter() throws Exception {
		LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter();

		loginAuthenticationFilter.setAuthenticationManager(authenticationManager());
		loginAuthenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
		loginAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessfulHandler);
		loginAuthenticationFilter.setAuthenticationFailureHandler(customLoginFailureHandler);

		return loginAuthenticationFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        
        http.addFilter(loginAuthenticationFilter());
        
        http
        	.authorizeRequests()
        	.antMatchers("**/secured/**").authenticated()
        	.anyRequest().permitAll()
        .and()
        	.formLogin()
        		.successHandler(customLoginSuccessfulHandler)
        		.failureHandler(customLoginFailureHandler)
        		.permitAll()
        .and()
        	.logout()
        		.logoutSuccessHandler(customLogoutSuccessfulHandler)
        .and()
        	.exceptionHandling()
        		.accessDeniedHandler(customAccessDeniedHandler)
        		.authenticationEntryPoint(customAuthenticationEntryPoint);
	}

	private PasswordEncoder getPasswordEncoder() {
		return new PasswordEncoder() {
			@Override
			public String encode(CharSequence charSequence) {
				logger.info("encode");
				logger.info(charSequence.toString());
				return charSequence.toString();
			}

			@Override
			public boolean matches(CharSequence charSequence, String s) {
				//Todo match password
				logger.info("matches");
				logger.info(charSequence.toString());
				logger.info(s);
				return true;
			}
		};
	}
}
