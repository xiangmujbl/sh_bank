package com.mmec.util;

import java.util.HashSet;
import java.util.Set;

public class StringUtil {

	public static String nullToString(String str) {
		if (null == str || "null".equals(str))
			return "";
		else
			return str;
	}

	public static boolean isNull(String str) {
		if (nullToString(str).equals(""))
			return true;
		else
			return false;
	}

	/**
	 * JAVA判断字符串数组中是否包含某字符串元素
	 *
	 * @param substring
	 *            某字符串
	 * @param source
	 *            源字符串数组
	 * @return 包含则返回true，否则返回false
	 */
	public static boolean isContain(String substring, String[] source) {
		if (source == null || source.length == 0) {
			return false;
		}
		for (int i = 0; i < source.length; i++) {
			String aSource = source[i];
			if (aSource.equals(substring)) {
				return true;
			}
		}
		return false;
	}

	// 判断数组中是否有重复值
	public static boolean checkRepeat(String[] array) {
		Set<String> set = new HashSet<String>();
		for (String str : array) {
			set.add(str);
		}
		if (set.size() != array.length) {
			return true;// 有重复
		} else {
			return false;// 不重复
		}
	}
}
