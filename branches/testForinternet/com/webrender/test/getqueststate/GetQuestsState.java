package com.webrender.test.getqueststate;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.webrender.protocol.enumn.EOPCODES;

public class GetQuestsState extends Thread {

	/**
	 *   
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while(true){
			try {
				String endpoint="http://localhost:8080/WebRender/services/QuestsState?wsdl";
				org.apache.axis.client.Service service=new org.apache.axis.client.Service();
				org.apache.axis.client.Call call=(org.apache.axis.client.Call)service.createCall();
				call.setTargetEndpointAddress(new java.net.URL(endpoint));
				call.setOperationName(new javax.xml.namespace.QName("http://www.animationsp.com","getQuestsStatus"));
				call.setTimeout(60000);
				Object result;
				long pre = System.currentTimeMillis();
				result = call.invoke(new Object[]{});
				long end = System.currentTimeMillis();
				long last = end - pre;
				if(last>20000){
					System.out.println(last);
				}
				
				sleep(1000);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
