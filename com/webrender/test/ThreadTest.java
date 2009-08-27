package com.webrender.test;

import com.webrender.server.ControlThreadServer;



public class ThreadTest {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		MitiSay thread1 = new MitiSay("Test");
		for(int i=0;i<2;i++)
        {
        	Thread.currentThread().sleep(1000);
        	System.out.println( "new "+thread1.getState() );
		}
		thread1.start();


		
//        thread1.stop();
        for(int i=0;i<2;i++)
        {
        	Thread.currentThread().sleep(1000);
        	System.out.println( "after start "+thread1.getState() );
		}
        
        thread1.suspend();
        
        for(int i=0;i<2;i++)
        {
        	Thread.currentThread().sleep(1000);
        	System.out.println( "after suspend "+thread1.getState() );
		}
// //       thread1.threadStop();
//        if (thread1.isAlive()) System.out.println("Control Thread is Active");
//        else System.out.println("Control Thread isnot Active");
//         
        thread1.resume();
        
        for(int i=0;i<10;i++)
        {
        	Thread.currentThread().sleep(1000);
        	System.out.println( "after resume "+ thread1.getState() );
        	if (thread1.getState().equals(Thread.State.TERMINATED))
        	{
        		System.out.println("！！stop ");
        	}
		}
        
//        
//        if (thread1.isInterrupted()) System.out.println("Control Thread is isInterrupted");
//        else System.out.println("Control Thread isnot isInterrupted");
        
	}
	
}
class MitiSay extends Thread {
    public MitiSay(String threadName) {
        super(threadName);
    }

    public void run() {
        System.out.println(getName() + " 线程运行开始!");
        for (int i = 0; i < 10; i++) {
            System.out.println(i + " " + getName());
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(getName() + " 线程运行结束!");
    }
}