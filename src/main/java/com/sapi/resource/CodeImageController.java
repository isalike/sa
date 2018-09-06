package com.sapi.resource;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sapi.service.CaptchaService;


@Controller
public class CodeImageController {
	private int width = 130; // The width of the captcha picture.
	private int height = 50; // The height of the captcha image.
	private int codeCount = 5; // The number of verification code characters
	private int fontHeight = height - 10; // Font height

	// The x-axis value of the first character, since the character coordinates in
	// the following increments, therefore their x-axis value is a multiple of codeX
	private int codeX = (width - 4) / (codeCount + 1);

	// codeY ,Verify that y-axis values of the characters are the same since they
	// are parallel
	private int codeY = height - 7;

	// codeSequence, The sequence of characters allowed to appear
	private char[] codeSequence = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A' };

	private String sessionName = "code_image";

	@RequestMapping(value = "/code_image", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getCodeImage(HttpSession session) {

		try {
			return CaptchaService.generateImage(width, height, codeCount, fontHeight, codeX, codeY, codeSequence,
					sessionName, session);
		} catch (Exception ex) {
			return null;
		}
	}
}
