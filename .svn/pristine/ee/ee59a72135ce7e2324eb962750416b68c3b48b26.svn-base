package com.mmec.webservice.server;

import javax.xml.ws.Endpoint;
import com.mmec.webservice.service.impl.CommonBussinessImpl;

public class WSDLServer {
	
    protected WSDLServer() throws Exception {
    	
        CommonBussinessImpl implementor = new CommonBussinessImpl();
        String address = "http://localhost:8080/mmecserver3.0/webservice/Common";
        Endpoint.publish(address, implementor);
    }
    
    public static void main(String args[]) throws Exception {
    	
        System.out.println("Starting Server...");
        new WSDLServer();
        System.out.println("Server ready.");
    }
}

