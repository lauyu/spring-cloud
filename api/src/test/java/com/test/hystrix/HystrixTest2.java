package com.test.hystrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import com.test.hystrix.HystrixTest.FakeTask;
import com.test.hystrix.HystrixTest.TestHystrixCommand;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

public class HystrixTest2 {

	public static void main(String[] args) throws Exception {
		HystrixCommandGroupKey commonCmdGroupKey = HystrixCommandGroupKey.Factory.asKey("CommonCommandGroup");
		
		TestHystrixCommand cmd1 = new TestHystrixCommand(1, 100, 2, new FakeTask(1000));
//		TestHystrixCommand cmd2 = new TestHystrixCommand(1000, new FakeTask(2000));
		
		Future<String> result1 = cmd1.queue();
//		Future<String> result2 = cmd2.queue();
		System.out.println(Thread.currentThread().getName()+" future get="+result1.get());
//		result2.get(3000, TimeUnit.MILLISECONDS);
		
				
	}
	
	protected void testMaxConcurrent() {
		List<Future<String>> futures = Collections.synchronizedList(new ArrayList<>());
		for(int i=0;i<4;i++) {
			final int num = i;
			new Thread() {
				public void run() {
					try {
						long start = System.currentTimeMillis();
						System.out.println(Thread.currentThread().getName()+" before add HystrixCmd: i="+num);
						TestHystrixCommand cmd1 = new TestHystrixCommand(num, 200, 2, new FakeTask(2000));
						Future<String> result1 = cmd1.queue();
						if(result1.isCancelled()) {
							System.out.println(Thread.currentThread().getName()+" ----- fail add HystrixCmd: i="+num);
						} else {
							System.out.println(Thread.currentThread().getName()+" after add HystrixCmd: i="+num);	
						}
//						futures.add(result1);
						long end = System.currentTimeMillis();
						System.out.println(Thread.currentThread().getName()+" future get="+result1.get()+", cost="+(end-start));
					} catch (Exception e) {
					}
				}
			}.start();
		}
	}
	
	static class TestHystrixCommand extends HystrixCommand<String> {
		Runnable task;
		int num;
		TestHystrixCommand(int num, int exeTimetout, int maxConcurrent,Runnable task) {
//			super(group, threadPool, executionIsolationThreadTimeoutInMilliseconds);
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("TestHystrixCommand-Common-Group"))
//			.andCommandKey(HystrixCommandKey.Factory.asKey("TestHystrixCommand-num-"+num))
//			.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(2).withMaximumSize(4).withQueueSizeRejectionThreshold(2))
			.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(exeTimetout)
					.withCircuitBreakerEnabled(Boolean.TRUE).withFallbackEnabled(Boolean.TRUE)
					.withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE)
					.withFallbackIsolationSemaphoreMaxConcurrentRequests(maxConcurrent).withExecutionIsolationSemaphoreMaxConcurrentRequests(maxConcurrent)));
			this.task = task;
			this.num = num;
		}

		@Override  
	    protected String getFallback() {  
			System.out.println("xxxxxx "+Thread.currentThread().getName()+" exeucute Falled, num="+num);
	        return Thread.currentThread().getName()+" exeucute Falled, num="+num;  
	    }  
		 
		@Override
		protected String run() throws Exception {
			long start = System.currentTimeMillis();
			System.out.println(Thread.currentThread().getName()+" before task run, num="+num);
			if(task != null)  {
				task.run();
				long end = System.currentTimeMillis();
				System.out.println(Thread.currentThread().getName()+" CommandKey="+getCommandKey() + " after task run, cost="+(end-start)+", num="+num);
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
				e.printStackTrace();
			}
		}
		
	}

}
