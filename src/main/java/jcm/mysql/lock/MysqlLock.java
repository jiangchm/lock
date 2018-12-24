package jcm.mysql.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jcm.AbstractLock;
import jcm.mysql.mapper.LockMapper;

@Component
public class MysqlLock extends AbstractLock{
	
	private final static int ID = 1 ;
	
	@Autowired
	private LockMapper lockMapper ;


	public boolean tryLock() {
		try {
			if(1==lockMapper.insert(ID)) {
				return true ;
			}
		}catch(Exception e) {
			return  false;
		}
		return false ;
	}


	public void unlock() {
		lockMapper.delete(ID);
	}


}
