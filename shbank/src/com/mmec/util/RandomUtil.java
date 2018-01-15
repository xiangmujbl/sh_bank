package com.mmec.util;

import org.apache.log4j.Logger;

public class RandomUtil {
	private static Logger  log = Logger.getLogger(RandomUtil.class);
	/**
	 * 产生随机数 默认个数为5
	 * @return
	 */
	public static String getRandom() {
		StringBuffer sb = new StringBuffer();
		String[] c = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "q", "w", "e", "r",
				"t", "y", "u", "i", "p", "a", "s", "d", "f", "g", "h",
				"j", "k", "l", "z", "x", "c", "v", "b", "n", "m","0","o","12","0","o" };
		for (int i = 1; i < 6; i++) {
			int random = (int) (Math.random() * 35);
			sb.append(c[random]);
		}
		return sb.toString();
	}
	/**
	 * 产生随机数 默认个数为6 (只含数字)
	 * @return
	 */
	public static String getNumRandom() {
		StringBuffer sb = new StringBuffer();
		String[] c = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		for (int i = 1; i < 7; i++) {
			int random = (int) (Math.random() * 9);
			sb.append(c[random]);
		}
		return sb.toString();
	}

	/**
	 * 产生num位随机数
	 * @param max
	 * @return
	 */
	public static String getRandom(int num) {
		StringBuffer sb = new StringBuffer();
		String[] c = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "q", "w", "e", "r",
				"t", "y", "u", "i", "p", "a", "s", "d", "f", "g", "h",
				"j", "k", "l", "z", "x", "c", "v", "b", "n", "m" };
		for (int i = 0; i < num; i++) {
			int random = (int) (Math.random() * 35);
			sb.append(c[random]);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		for(int i=0;i<100;i++)
		log.info(getRandom());
	}
}
