package com.service.test;

import java.util.concurrent.TimeUnit;

import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.config.Config;
import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
public class RedissionTest {
	private static final String LOCK_TITLE = "redisLock_";
	static Config config = new Config();
	static Redisson redisson;
	static {
		config.useSingleServer().setAddress("r-bp10fddbccdefca4.redis.rds.aliyuncs.com:6379")
				.setPassword("kYbFvuUzaDtzpNCGBOM9");
		redisson = (Redisson) Redisson.create(config);
	}

	public static void main(String[] args) {
		String lockName = "my_lock";
		acquire(lockName, 60);
		sleep(2000);
		release(lockName);
	}

	public static boolean acquire(String lockName, long leaseTime) {
		// 声明key对象
		String key = LOCK_TITLE + lockName;
		// 获取锁对象
		RLock mylock = redisson.getLock(key);
		// 加锁，并且设置锁过期时间，防止死锁的产生
		mylock.lock(leaseTime, TimeUnit.SECONDS);
		System.err.println("======lock======" + Thread.currentThread().getName());
		// 加锁成功
		return true;
	}

	// 锁的释放
	public static void release(String lockName) {
		// 必须是和加锁时的同一个key
		String key = LOCK_TITLE + lockName;
		// 获取所对象
		RLock mylock = redisson.getLock(key);
		// 释放锁（解锁）
		mylock.unlock();
		System.err.println("======unlock======" + Thread.currentThread().getName());
	}
	
	private static void sleep(long t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
