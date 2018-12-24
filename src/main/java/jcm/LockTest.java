package jcm;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class LockTest {
	
	private static int tickets = 100 ;
	
	private static Lock lock = new ReentrantLock();
	
	public static void main(String[] args) {
		for(int i = 0 ;i<5 ;i++ ) {
			Thread t = new Thread(new Sell()) ;
			t.start(); 
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private static class Sell implements Runnable{

		public void run() {
			
			while(tickets>0) {
				lock.lock();
				if(tickets>0) {
					try {
						System.out.println(Thread.currentThread().getName()+"卖出"+(tickets--)+"票");
					}finally {
						lock.unlock(); 
					}
				}
				try {
					Thread.currentThread().sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
}
