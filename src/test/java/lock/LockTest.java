package lock;

import static org.junit.Assert.assertNotNull;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类  
@ContextConfiguration(locations = {"classpath:bean.xml"})  
public class LockTest {
	
	private int tickets = 100 ;
	
	@Autowired
	@Qualifier("zklock")
	private Lock lock ;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		
		for(int i = 0 ;i<5 ;i++ ) {
			Thread t = new Thread(new Sell()) ;
			t.start();
		}
		assertNotNull(new Object());
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class Sell implements Runnable{

		public void run() {
			
			while(tickets>0) {
				lock.lock();
				try {
					if(tickets>0) {
						System.out.println(Thread.currentThread().getName()+"卖出"+(tickets--)+"票");
					}
				}finally {
					lock.unlock(); 
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
