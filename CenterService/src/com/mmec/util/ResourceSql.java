package com.mmec.util;

/**
 * 
 * @author Administrator
 * 
 */
public class ResourceSql {
	/**
	 * 用过证书KEY查询用户账号状态
	 */
	public static String queryCustomByKey = "select c.status from maimaidun m,custom c where m.uid = c.id and "
											+ "m.serial_number=?";
	/**
	 * 检查证书KEY值是否存在
	 */
	public static String queryKeyIsExist = "";
	
	/**
	 *  查询证书有效期
	 */
	public static String queryCertValidety = "select date_begin,date_end from maimaidun where serial_number = ?";
}
