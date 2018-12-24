package jcm;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class AbstractLock implements Lock {

	Random random = new Random() ;
	
	public void lock() {
		while(!tryLock()) {
			try {
				Thread.currentThread().sleep(random.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			waitForLock();
		}
	
	}

	protected void waitForLock() {
		
	}

	public void lockInterruptibly() throws InterruptedException {
		return;
	}

	public boolean tryLock() {
		return false;
	}

	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return false;
	}

	public void unlock() {
		
	}

	public Condition newCondition() {
		return null;
	}

}
