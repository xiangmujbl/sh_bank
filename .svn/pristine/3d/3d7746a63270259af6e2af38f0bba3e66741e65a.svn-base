package com.mmec.sync;

import java.util.TimerTask;

import com.mmec.business.service.BaseService;

public class AbstractSyncTask extends TimerTask {

	private BaseService baseService;

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public void run() {

		baseService.syncData();

		/*
		 * try { //获取同步数据 List list = getSyncList();
		 * 
		 * //将数据转换为json传入服务端 Gson gson = new Gson(); String jsonStr =
		 * gson.toJson(list);
		 * 
		 * //获取同步http地址 HttpClient client = new HttpClient(); PostMethod method
		 * = new PostMethod(getHttpURL()); ((PostMethod)
		 * method).addParameter("data", jsonStr); HttpMethodParams param =
		 * method.getParams(); param.setContentCharset("UTF-8");
		 * client.executeMethod(method);
		 * System.out.println(method.getStatusLine());
		 * 
		 * //获取服务端返回，返回值为 133122,133133,133121 InputStream stream =
		 * method.getResponseBodyAsStream(); BufferedReader br = new
		 * BufferedReader(new InputStreamReader(stream, "UTF-8")); StringBuffer
		 * buf = new StringBuffer(); String line; while (null != (line =
		 * br.readLine())) { buf.append(line).append("\n"); }
		 * System.out.println(buf.toString()); //释放连接
		 * method.releaseConnection(); String lineStr = buf.toString(); String[]
		 * ids = lineStr.split(",");
		 * 
		 * //将同步成功的数据修改为成功状态 updateSyncStatus(ids);
		 * 
		 * //将超过24小时的数据修改为停用状态 updateSyncStop(); } catch(Exception e) {
		 * e.printStackTrace(); }
		 */
	}

}
