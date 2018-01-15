package com.mmec.util;

import java.util.Iterator;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.protocol.Protocol;

public class CallWebServiceUtil {
	public static String CallHttpsService(String endpoint,String nameSpace,String methodName,String[] paramName,String[] paramValue){
        String result = "";		
		
		try{
			//使用ServiceClient调用https的WebService
			ServiceClient serviceClient = new ServiceClient();
	
			MyProtocolSocketFactory socketfactory = new MyProtocolSocketFactory();
	
			Protocol protocol = new Protocol("https", socketfactory, 443);
			
			Options options = serviceClient.getOptions();
			
			options.setProperty(HTTPConstants.CUSTOM_PROTOCOL_HANDLER, protocol);
			
			//设置调用地址
			OMElement ret = null;
	        EndpointReference targetEPR = new EndpointReference(endpoint);
	        options.setTo(targetEPR);
	        serviceClient.setOptions(options);
	        OMFactory fac = OMAbstractFactory.getOMFactory();
	        //设置命名空间
	        OMNamespace omNs = fac.createOMNamespace(nameSpace,methodName);
	        OMElement eleData = fac.createOMElement(methodName, omNs);
	        //对应参数的节点
	        String[] strs = paramName;
	        //参数值
	        String[] val = paramValue;
	
	        //添加参数值
	        OMNamespace emOmNs = fac.createOMNamespace("","");        
	        for (int i = 0; i < strs.length; i++) {
	            OMElement inner = fac.createOMElement(strs[i], emOmNs);
	            inner.setText(val[i]);
	            eleData.addChild(inner);
	        }
	        //发送soap请求
	        ret = serviceClient.sendReceive(eleData);
	        
	        //解析返回值
	        Iterator<?> iterator=ret.getChildElements(); 
	
	        while(iterator.hasNext()){
	        	OMNode omNode = (OMNode) iterator.next();
	        	if(omNode.getType()==OMNode. ELEMENT_NODE){
		        	OMElement omElement = (OMElement) omNode;
		        	if (omElement.getLocalName().toLowerCase().equals("return" )) {
		        		result = omElement.getText();
		        	}
		        }
	        }
		}
		catch(Exception e){
			e.printStackTrace();
		}
        
        return result;
	}
	
	public static String CallHttpService(String endpoint,String nameSpace,String methodName,String[] paramName,String[] paramValue){
        String result = "";		
		
		try{
			//使用ServiceClient调用https的WebService
			ServiceClient serviceClient = new ServiceClient();	
	
			Options options = serviceClient.getOptions();			
			
			//设置调用地址
			OMElement ret = null;
	        EndpointReference targetEPR = new EndpointReference(endpoint);
	        options.setTo(targetEPR);
	        serviceClient.setOptions(options);
	        OMFactory fac = OMAbstractFactory.getOMFactory();
	        //设置命名空间
	        OMNamespace omNs = fac.createOMNamespace(nameSpace,methodName);
	        OMElement eleData = fac.createOMElement(methodName, omNs);
	        //对应参数的节点
	        String[] strs = paramName;
	        //参数值
	        String[] val = paramValue;
	
	        //添加参数值
	        OMNamespace emOmNs = fac.createOMNamespace("","");        
	        for (int i = 0; i < strs.length; i++) {
	            OMElement inner = fac.createOMElement(strs[i], emOmNs);
	            inner.setText(val[i]);
	            eleData.addChild(inner);
	        }
	        //发送soap请求
	        ret = serviceClient.sendReceive(eleData);
	        
	        //解析返回值
	        Iterator<?> iterator=ret.getChildElements(); 
	
	        while(iterator.hasNext()){
	        	OMNode omNode = (OMNode) iterator.next();
	        	if(omNode.getType()==OMNode. ELEMENT_NODE){
		        	OMElement omElement = (OMElement) omNode;
		        	if (omElement.getLocalName().toLowerCase().equals("return" )) {
		        		result = omElement.getText();
		        	}
		        }
	        }
		}
		catch(Exception e){
			e.printStackTrace();
		}
        
        return result;
	}
}
