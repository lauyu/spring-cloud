package com.test.perf.common.util;

import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtil {

	private static Random rand = new Random();

	public static String randomAlphanumeric(int length) {
		return RandomStringUtils.randomAlphanumeric(length);
	}
	
	public static String randomNumeric(int length) {
		return RandomStringUtils.randomNumeric(length);
	}
	
	public static synchronized long getId() {
		return Math.abs(rand.nextLong());
	}

	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
	public static String getSimpleUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
	public static String getSubSimpleUUID(int num) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, num);
    }
	
}
