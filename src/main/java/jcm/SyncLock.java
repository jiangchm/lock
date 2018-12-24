package jcm;

public class SyncLock {
	
	private static int TICKETS = 100 ;
	
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
			while(TICKETS>0) {
				synchronized (SyncLock.class) {
					if(TICKETS>0) {
						System.out.println(Thread.currentThread().getName()+"卖出"+(TICKETS-- )+"票");
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
