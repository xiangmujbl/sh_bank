package com.mmec.aps.dbo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mmec.util.ContextXmlUtil;
import com.mysql.jdbc.PreparedStatement;

/**
 * 
 * @author hlwei
 * 
 */
public class DBService {
	private static DBService dbs = null;

	// DB链接地址
	private String url;

	// 登陆账户
	private String user;

	// 登陆密码
	private String password;
	
	public static DBService getInstance() throws ClassNotFoundException{
		// 初始化对象
		if(null == dbs){
			dbs = new DBService();
		}
		
		return dbs;
	}

	public DBService() throws ClassNotFoundException {
		// 加载驱动
		Class.forName(ContextXmlUtil.getInstance().getValue("forname"));

		// 赋值
		url = ContextXmlUtil.getInstance().getValue("url");
		user = ContextXmlUtil.getInstance().getValue("username");
		password = ContextXmlUtil.getInstance().getValue("password");
	}

	/**
	 * 获取数据库链接
	 * 
	 * @return Connection
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		// 创建连接
		Connection conn = DriverManager.getConnection(url, user, password);

		return conn;
	}

	/**
	 * 关闭链接
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	public static void closeConnection(Connection conn) throws SQLException {
		conn.close();
	}
	
	public static void main(String args[]) throws SQLException, ClassNotFoundException {
		DBService.getInstance().getConnection();
	}
}
