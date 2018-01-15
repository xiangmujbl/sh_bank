package com.mmec.aps.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.mmec.aps.dbo.DBOperatorManager;
import com.mmec.aps.service.QueryCertStatusService;
import com.mmec.thrift.service.CertKey;
import com.mmec.thrift.service.Result;
import com.mmec.util.DateFormatUtil;
import com.mmec.util.ResourceSql;

/**
 * 查询商户证书状态实现类
 * 
 * @author Administrator
 * 
 */
public class QueryCertStatusServiceImpl implements QueryCertStatusService {

	@Override
	public Result excute(CertKey certKey) {
		// 根据证书指纹查询账户信息
		Result result = verifyCustomStatus(certKey.getKey());

		// 账户信息认证失败，则立即响应错误结果
		if (1 == result.getStatus()) {
			return result;
		}

		// 检查证书是否在有效期内
		result = this.verifyCertValidety(certKey.getKey());

		return result;
	}

	/**
	 * 检查商户账户状态是否有效
	 */
	public Result verifyCustomStatus(String key) {
		// 初始化响应结果集
		Result result = new Result(0, "",null);

		try {
			// sql预编译存储对象
			PreparedStatement pps = DBOperatorManager
					.createPreparedStatement(ResourceSql.queryCustomByKey);

			// 设置查询条件
			pps.setString(1, key);

			// 执行数据库查询
			ResultSet rs = pps.executeQuery();

			// 为查询到账户信息，则响应错误结果
			if (false == rs.next()) {

				result.setStatus(1);
				result.setDesc("未找到该用户");

				// 关闭sql预编译存储对象,数据库连接
				DBOperatorManager.closeAll(pps);

				return result;
			}

			// 获取用户状态
			String status = rs.getString("status");

			// 判断用户状态，0新用户未激活
			if ("0".equals(status)) {
				result.setStatus(1);
				result.setDesc("新用户未激活");

				// 关闭sql预编译存储对象,数据库连接
				DBOperatorManager.closeAll(pps);

				return result;

			} else if ("2".equals(status)) {
				result.setStatus(1);
				result.setDesc("封禁用户");

				// 关闭sql预编译存储对象,数据库连接
				DBOperatorManager.closeAll(pps);

				return result;
			}

			// 关闭sql预编译存储对象,数据库连接
			DBOperatorManager.closeAll(pps);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 检查证书KEY值是否存在
	 */
	/*
	 * public Result verifyCertStatus(String key) {
	 * 
	 * return null; }
	 */

	/**
	 * 检查证书是否在有效期内
	 * 
	 * @throws SQLException
	 */
	public Result verifyCertValidety(String key) {
		// 初始化响应结果集
		Result result = new Result(0, "",null);

		// sql预编译存储对象
		PreparedStatement pps = null;
		try {
			// sql预编译存储对象
			pps = DBOperatorManager
					.createPreparedStatement(ResourceSql.queryCertValidety);

			// 设置查询条件
			pps.setString(1, key);

			// 执行数据库查询
			ResultSet rs = pps.executeQuery();

			// 买卖盾有效起始时间
			long beginDate = 0;

			// 买买盾有效截止时间
			long endDate = 0;

			// 获取查询结果
			while (rs.next()) {
				beginDate = DateFormatUtil.stringToDateForLong(rs.getString("date_begin"));
				
				endDate = DateFormatUtil.stringToDateForLong(rs.getString("date_end"));
				
				break;
			}

			// 系统当前时间
			long currentDate = System.currentTimeMillis();

			// 判断证书当前使用时间是否在有效期内
			if (beginDate > currentDate || currentDate > endDate) {
				// 不在有效期内
				result.setStatus(1);
				result.setDesc("证书过期");

				// 关闭sql预编译存储对象,数据库连接
				DBOperatorManager.closeAll(pps);

				return result;
			}

			// 关闭sql预编译存储对象,数据库连接
			DBOperatorManager.closeAll(pps);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return result;
	}
}
