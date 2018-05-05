package com.test.hystrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.netflix.hystrix.HystrixCommand;
import com.test.hystrix.HystrixTest.FakeTask;
import com.test.hystrix.HystrixTest.TestHystrixCommand;

public abstract class AbstractHystrixTest {

	protected void testSyncExuc(HystrixCommand<?> command) {
		List<Future<String>> futures = Collections.synchronizedList(new ArrayList<>()); 
		for(int i=0;i<8;i++) {
			TestHystrixCommand cmd1 = new TestHystrixCommand(i, 4000, new FakeTask(200));
			Future<String> result1 = cmd1.queue();
			System.out.println("add call req: i="+i);
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
	
}
