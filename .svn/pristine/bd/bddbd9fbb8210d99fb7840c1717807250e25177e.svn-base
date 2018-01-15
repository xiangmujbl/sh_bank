package com.mmec.aps.dbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



/**
 * 持久化操作管理
 * @author Administrator
 *
 */
public class DBOperatorManager {
	/**
	 * 构建sql预编译存储对象
	 * @param sql
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static PreparedStatement createPreparedStatement(String sql) throws ClassNotFoundException, SQLException{
		Connection cnn = DBService.getInstance().getConnection();
		
		PreparedStatement pps = cnn.prepareStatement(sql);
		
		return pps;
	}
	
	/**
	 * 关闭sql预编译存储对象,数据库连接
	 * @param pps
	 * @throws SQLException
	 */
	public static void closeAll(PreparedStatement pps) throws SQLException{
		// 关闭数据库连接
		if(null != pps.getConnection()){
			pps.getConnection().close();
		}
		
		if(null != pps){
			pps.close();
		}
	}
}