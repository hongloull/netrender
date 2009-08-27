package com.webrender.test;

public class WSVisit {
	public static void main(String[] args) {
		try{
			String endpoint="http://localhost:8080/WebRender/services/UserLogin?wsdl";
			org.apache.axis.client.Service service=new org.apache.axis.client.Service();
			
			org.apache.axis.client.Call call=(org.apache.axis.client.Call)service.createCall();
			/*
			 * 
			 */
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new javax.xml.namespace.QName("http://www.edu-edu.com.cn/luopc/ws","getStatistics"));
			while(true){
				Object result=call.invoke(new Object[]{});
				System.out.println(result);
				Thread.sleep(1000);
			}
			
		}catch(Exception e){
			System.err.println(e.toString());
		} 
	}
}
