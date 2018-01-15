package com.mmec.business;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * 图形验证类
 * 
 * @author Administrator
 * 
 */
public class ValidateCodeServlet extends HttpServlet {
	private Logger log = Logger.getLogger(ValidateCodeServlet.class);
	private static final long serialVersionUID = 1L;
	private static int WIDTH = 80;// 设置图片的宽度
	private static int HEIGHT = 35;// 设置图片的高度

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		response.setContentType("image/jpeg");
		ServletOutputStream sos = response.getOutputStream();

		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();

		// 获取随机验证码
		char[] rands = generateCheckCode();
		drawBackground(g);
		drawRands(g, rands);
		g.dispose();
		// 将验证码生成IO形式图片
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(image, "JPEG", bos);
		byte[] buf = bos.toByteArray();
		response.setContentLength(buf.length);
		sos.write(buf);
		bos.close();
		sos.close();
		log.info("图片验证码:" + rands);
		session.setAttribute("randomCode", new String(rands));
	}

	/**
	 * 生成图片
	 * 
	 * @param g
	 */
	private void drawBackground(Graphics g) {
		// g.setColor(new Color(0xDCDCDC));
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		for (int i = 0; i < 120; i++) {
			int x = (int) (Math.random() * WIDTH);
			int y = (int) (Math.random() * HEIGHT);
			int red = (int) (Math.random() * 255);
			int green = (int) (Math.random() * 255);
			int blue = (int) (Math.random() * 255);
			g.setColor(new Color(red, green, blue));
			g.drawOval(x, y, 1, 0);
		}

		// 干扰
		Random random = new Random();
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(WIDTH);
			int y = random.nextInt(WIDTH);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
	}

	/**
	 * 生成图片
	 * 
	 * @param g
	 * @param rands
	 */
	private void drawRands(Graphics g, char[] rands) {
		// g.setColor(Color.BLUE);
		Random random = new Random();
		int red = random.nextInt(255);
		int green = 0; // random.nextInt(50);
		int blue = 0; // random.nextInt(50);
		g.setColor(new Color(red, green, blue));
		g.setFont(new Font(null, Font.ITALIC | Font.BOLD, 19));
		g.drawString("" + rands[0], 1, 22);
		g.drawString("" + rands[1], 16, 20);
		g.drawString("" + rands[2], 31, 19);
		g.drawString("" + rands[3], 46, 22);
		g.drawString("" + rands[4], 61, 25);
	}

	/**
	 * 获取随机验证码
	 * 
	 * @return
	 */
	private char[] generateCheckCode() {
		String chars = "123456789ABCDEFGHIJKLMNPQRSTUVWXYZ";
		char[] rands = new char[5];
		for (int i = 0; i < 5; i++) {
			int rand = (int) (Math.random() * 33);
			rands[i] = chars.charAt(rand);
		}
		return rands;
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}