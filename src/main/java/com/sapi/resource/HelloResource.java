package com.sapi.resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sapi.util.HttpClientUtil;

@RequestMapping("/rest/hello")
@RestController
public class HelloResource {

	@GetMapping("/all")
	public String hello() {
		String url = "https://jsonplaceholder.typicode.com/posts";
		String ret = HttpClientUtil.get(url);

		System.out.println(ret.length());

		return "Hello Youtube";
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/secured/all")
	public String securedHello() {
		return "Secured Hello";
	}

	@GetMapping("/secured/alternate")
	public String alternate() {
		return "alternate";
	}
}
