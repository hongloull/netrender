package com.webrender.test.getqueststate;

public class RunThreadForStates {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		System.out.println("start");
		int NUM = 150;
		for(int i=0;i<NUM;i++){
			GetQuestsState thread = new GetQuestsState();
			Thread.sleep(100);
			thread.start();			
		}
		System.out.println("FINISH");
	}

}
