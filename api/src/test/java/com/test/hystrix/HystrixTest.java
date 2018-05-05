package com.test.hystrix;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.test.hystrix.HystrixTest.FakeTask;
import com.test.hystrix.HystrixTest.TestHystrixCommand;

public class HystrixTest {

	public static void main(String[] args) throws Exception {
		HystrixCommandGroupKey commonCmdGroupKey = HystrixCommandGroupKey.Factory.asKey("CommonCommandGroup");
		
//		TestHystrixCommand cmd1 = new TestHystrixCommand(4000, new FakeTask(2000));
//		TestHystrixCommand cmd2 = new TestHystrixCommand(1000, new FakeTask(2000));
		
//		Future<String> result1 = cmd1.queue();
//		Future<String> result2 = cmd2.queue();
//		result1.get(3000, TimeUnit.MILLISECONDS);
//		result2.get(3000, TimeUnit.MILLISECONDS);
		new HystrixTest().testSyncExuc();
	}
	
	protected void testAsyncExuc() {
		List<Future<String>> futures = Collections.synchronizedList(new ArrayList<>()); 
		for(int i=0;i<8;i++) {
			System.out.println(Thread.currentThread().getName()+ " before queue cmd: num="+i);
			TestHystrixCommand cmd1 = new TestHystrixCommand(i, 4000, new FakeTask(200));
			Future<String> result1 = cmd1.queue();
			System.out.println(Thread.currentThread().getName()+ " after queue cmd: num="+i);
			futures.add(result1);
		}
		futures.forEach(ele -> {
			try {
				System.out.println(""+ele.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});
		System.out.println("    ---------------    ");
		futures.forEach(ele -> {
			try {
				System.out.println("future get=" + ele.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});
	}
	
	protected void testSyncExuc() {
		List<Future<String>> futures = Collections.synchronizedList(new ArrayList<>()); 
		for(int i=0;i<6;i++) {
			final int num = i;
			new Thread() {
				public void run() {
					long s = System.currentTimeMillis();
					System.out.println(Thread.currentThread().getName()+ " before exec cmd: num="+num);
					TestHystrixCommand cmd1 = new TestHystrixCommand(num, 400, new FakeTask(10000));
					String result = cmd1.execute();
					long e = System.currentTimeMillis();
					System.out.println(Thread.currentThread().getName()+ " after exec cmd: cost="+(e-s)+", num="+num+", result="+result);
				}
			}.start();
		}
	}
	
	static class TestHystrixCommand extends HystrixCommand<String> {
		Runnable task;
		int num;
		TestHystrixCommand(int num, int exeTimetout, Runnable task) {
//			super(group, threadPool, executionIsolationThreadTimeoutInMilliseconds);
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("TestHystrixCommand-Common-Group"))
			.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(2).withMaximumSize(6).withQueueSizeRejectionThreshold(4).withMaxQueueSize(2))
			.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(exeTimetout)
			.withCircuitBreakerEnabled(Boolean.TRUE).withFallbackEnabled(Boolean.TRUE)
			.withFallbackIsolationSemaphoreMaxConcurrentRequests(1)));
			this.num = num;
			this.task = task;
		}

		@Override  
	    protected String getFallback() {
			System.out.println("xxxxxx "+Thread.currentThread().getName()+" exeucute Falled, num="+num);
	        return Thread.currentThread().getName()+" exeucute Falled, num="+num;  
	    }  
		 
		@Override
		protected String run() throws Exception {
			System.out.println(Thread.currentThread().getName()+" ThreadPoolKey="+getThreadPoolKey() + " before task run, num="+num);
			if(task != null)  {
				task.run();
				System.out.println(Thread.currentThread().getName()+" ThreadPoolKey="+getThreadPoolKey() + " after task run, num="+num);
			}
			return Thread.currentThread().getName()+ " say hello, num="+num;
		}
		
	}
	
	static class FakeTask implements Runnable {
		int s;
		public FakeTask(int s) {
			this.s = s;
		}
		@Override
		public void run() {
			try {
				System.out.println(Thread.currentThread().getName()+ " call run");
				Thread.sleep(s);
			} catch (InterruptedException e) {
				System.out.println(Thread.currentThread().getName()+ " interrupted!");
			}
		}
		
	}

}
