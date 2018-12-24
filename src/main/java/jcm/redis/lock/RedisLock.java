package jcm.redis.lock;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jcm.AbstractLock;
import redis.clients.jedis.Jedis;

@Component
public class RedisLock extends AbstractLock{
	
	@Autowired
	private RedisTemplate redisTemplate ;
	
	private final String KEY = "TICKETS" ;
	
	private String NX = "NX" ;
	private String PX = "PX" ;
	private String OK = "OK" ;
	private ThreadLocal<String> tl = new ThreadLocal<String>();
	private Jedis getJedis() {
		Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection() ;
		return jedis ;
	}

	public boolean tryLock() {
		Jedis jedis = getJedis();
		String randomValue = UUID.randomUUID().toString() ;
		String status = jedis.set(KEY, randomValue, NX, PX, 100000l) ;
		if(OK.equals(status)) {
			tl.set(randomValue);
			return true ;
		}
		return false;
	}

	public void unlock() {
		String script;
		try {
			script = FileUtils.readFileToString(new File("D:\\sts-workspace\\lock\\src\\main\\resources\\redis-unlock.txt"));
//			script = FileUtils.readFileToString(new File(this.getClass().getResource("redis-unlock.txt").toURI()));
			Jedis jedis = getJedis();
			Object obj = jedis.eval(script,Arrays.asList(KEY),Arrays.asList(tl.get())) ;
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
