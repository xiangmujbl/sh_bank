package com.mmec.util;
import java.util.HashMap;
import java.util.Map;



public class SendMsgUtil 
{
	// 乾璟通科技 短信通道
	public static int SendSMS(String uri, String account, String pwd, String mobile, String message)
	{
		try 
		{
    		// 群发的时候，mobile参数为英文状态下的逗号分割多个手机号码
			String res = HttpSender.batchSend(uri, account, pwd, mobile, message, true,  "");
			res = res.replaceAll("\n", ",");
			// 20110725160412,0,1234567890100
			// 响应时间为20110725160412，响应状态为0 表明那个成功提交到服务器；1234567890100为返回的msgid，这个工状态报告匹配时使用。
			String [] array = res.split(",");
			int len = array.length;
			if (len >= 3)
			{
				if ("0".equals(array[1]))
				{
					System.out.println("乾璟通科技 短信发送成功！");
				}
				else
				{				
					System.out.println("乾璟通科技 短信发送失败！");
				}
			}
			else
			{
				System.out.println("乾璟通科技 短信发送失败！");
				return -1;
			}		
			return Integer.parseInt(array[1]);
		} 
    	catch (Exception e) 
    	{
			//TODO 处理异常
			e.printStackTrace();
			return -1;
		} 
	}
	
	public static String getErrorInfoById(int id)
	{
		Map map = new HashMap();		
		map.put(0,   "提交成功");
		map.put(101, "无此用户");
		map.put(102, "密码错");
		map.put(103, "提交过快（提交速度超过流速限制）");
		map.put(104, "系统忙（因平台侧原因，暂时无法处理提交的短信）");
		map.put(105, "敏感短信（短信内容包含敏感词）");
		map.put(106, "消息长度错（>536或<=0）");
		map.put(107, "包含错误的手机号码");
		map.put(108, "手机号码个数错（群发>50000或<=0;单发>200或<=0）");
		map.put(109, "无发送额度（该用户可用短信数已使用完）");
		map.put(110, "不在发送时间内");
		map.put(111, "超出该账户当月发送额度限制");
		map.put(112, "无此产品，用户没有订购该产品");
		map.put(113, "extno格式错（非数字或者长度不对）");
		map.put(115, "自动审核驳回");
		map.put(161, "签名不合法，未带签名（用户必须带签名的前提下）");
		map.put(117, "IP地址认证错,请求调用的IP地址不是系统登记的IP地址");
		map.put(118, "用户没有相应的发送权限");
		map.put(119, "用户已过期");
		map.put(120, "内容不在白名单模板中");
		map.put(121, "相同内容短信超限");
		map.put(-1,  "未知错误");
		
		return map.get(id).toString();
	}
}
