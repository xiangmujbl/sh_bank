package com.mmec.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

public class MyProtocolSocketFactory implements ProtocolSocketFactory{
	private SSLContext sslcontext = null;
	
	private static SSLContext createEasySSLContext() {
		try {
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, new TrustManager[] { new X509TrustManager() {				
		    	@Override
		        public void checkClientTrusted(X509Certificate[] arg0,
		            String arg1) throws CertificateException {
		        }
		    	
		    	@Override
		        public void checkServerTrusted(X509Certificate[] arg0,
		            String arg1) throws CertificateException {
		        }
		    	
		    	public X509Certificate[] getAcceptedIssuers() {
		    		return null;
		        }
		    }
		}, null);
			
			return context;
		} catch (Exception e) {
	    	throw new HttpClientError(e.toString());
	    }
	}
	
	private SSLContext getSSLContext() {
		if (this.sslcontext == null) {
	      this.sslcontext = createEasySSLContext();
	    }
	    return this.sslcontext;
	}

	  @Override
	  public Socket createSocket(String host, int port) throws IOException,
	      UnknownHostException {
	    return getSSLContext().getSocketFactory().createSocket(host, port);
	  }
	  
	  @Override
	  public Socket createSocket(String host, int port, InetAddress clientHost,
	      int clientPort) throws IOException, UnknownHostException {
	    return getSSLContext().getSocketFactory().createSocket(host, port,
	        clientHost, clientPort);
	  }

	  @Override
	  public Socket createSocket(String host, int port, InetAddress localAddress,
	      int localPort, HttpConnectionParams params) throws IOException,
	      UnknownHostException, ConnectTimeoutException {
	    if (params == null) {
	      throw new IllegalArgumentException("Parameters may not be null");
	    }
	    int timeout = params.getConnectionTimeout();
	    SocketFactory socketfactory = getSSLContext().getSocketFactory();
	    if (timeout == 0) {
	      return socketfactory.createSocket(host, port, localAddress,localPort);
	    } else {
	      Socket socket = socketfactory.createSocket();
	      SocketAddress localaddr = new InetSocketAddress(localAddress,
	          localPort);
	      SocketAddress remoteaddr = new InetSocketAddress(host, port);
	      socket.bind(localaddr);
	      socket.connect(remoteaddr, timeout);
	      return socket;
	    }
	}
}
