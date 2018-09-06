package com.sapi.resource;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	@RequestMapping(value = "/profile", produces = { MediaType.APPLICATION_JSON_VALUE })
	public String profile(HttpSession session) {
		if (session.getAttribute("username") == null) {
			return "{\"username\":\"\",\"logon\":false,\"upline\":\"\"}";
		} else {
			return "{\"username\":\"" + ((String) session.getAttribute("username"))
					+ "\",\"logon\":true,\"upline\":\"\"}";
		}
	}
}
