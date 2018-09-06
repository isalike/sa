package com.sapi.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

public class CaptchaService {
	public static byte[] generateImage(int width, int height, int codeCount, int fontHeight, int codeX, int codeY,
			char[] codeSequence, String sessionName, HttpSession session) throws Exception {

		// Define the image buffer
		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D gd = buffImg.createGraphics();
		// Create a random number generator
		Random random = new Random();
		// Fill the image with white
		// gd.setColor(Color.LIGHT_GRAY);
		gd.fillRect(0, 0, width, height);
		// Create font, font size should be based on the height of the PIC
		Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
		// Set the font
		gd.setFont(font);
		// Set rim
		// gd.setColor(Color.BLACK);
		gd.drawRect(0, 0, width - 1, height - 1);
		// Randomly generated 160 lines of interference, let the image of the authentication code is not easily detected by other programs
		gd.setColor(Color.lightGray);
		for (int i = 0; i < 5; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			gd.drawLine(x, y, x + xl, y + yl);
		}
		// RandomCode is use to save a random verification code, so that user can login after verification.
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;
		// Random verification code by codeCount
		for (int i = 0; i < codeCount; i++) {
			// Get randomly generated codes.
			String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
			// Generate a random color component to construct the color value so each digit output has a different color value.
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			// The code is drawn into the image with those randomly generated color.
			gd.setColor(new Color(red, green, blue));

			int tmpCodeY = codeY;
			// if(random.nextInt(1) == 0){
			// tmpCodeY += random.nextInt(7);
			// }else{
			tmpCodeY -= random.nextInt(7);
			// }
			gd.drawString(strRand, (i + 1) * codeX, tmpCodeY);
			// gd.drawString(strRand, (i + 1) * codeX, codeY);

			// Resulting four random numbers together.
			randomCode.append(strRand);
		}

		// Make BufferImage to byte[]
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(buffImg, "jpg", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();

		// Save the four-digit verification code in the Session.
		session.setAttribute(sessionName, randomCode.toString());

		return imageInByte;	
	}
}
