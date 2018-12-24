package jcm.zookeeper.lock;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.springframework.stereotype.Component;

import jcm.AbstractLock;

@Component("zklock")
public class ZKImproveLock extends AbstractLock {
	
	private String LOCK_PATH = "/lock" ;
	
	private ZkClient zkclient ;
	
	@PostConstruct
	private void getZKClient() {
		zkclient = new ZkClient("localhost:2181") ;
		zkclient.setZkSerializer(new ZkSerializer() {
			
			public byte[] serialize(Object str) throws ZkMarshallingError {
				try {
					return str.toString().getBytes("utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null ;
			}
			
			public Object deserialize(byte[] bytes) throws ZkMarshallingError {
				try {
					return new String(bytes,"utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null ;
			}
		});
	}
	@Override
	public boolean tryLock() {
		try {
			zkclient.createPersistent(LOCK_PATH);
//			zkclient.createEphemeralSequential(path, data)
			return true ;
		}catch (ZkNodeExistsException e) {
		}
		return false ;
	}
	
	

	@Override
	protected void waitForLock() {
		final CountDownLatch countDown = new CountDownLatch(1) ;
		IZkDataListener  listener = new IZkDataListener() {
			
			public void handleDataDeleted(String arg0) throws Exception {
				countDown.countDown();
			}
			
			public void handleDataChange(String arg0, Object arg1) throws Exception {
			}
		};
		zkclient.subscribeDataChanges(LOCK_PATH, listener);
		try {
			countDown.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		zkclient.unsubscribeDataChanges(LOCK_PATH, listener);
	}



	@Override
	public void unlock() {
		zkclient.delete(LOCK_PATH) ;
	}
	
	

}
