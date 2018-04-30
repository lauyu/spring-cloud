package com.test.perf.common.ctx;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class AppContext {

	private static ThreadLocal<Map<String, Object>> delegate = new ThreadLocal<>();
	private static final String SESSION_KEY = "req_session_id";
	
	private static Map<String, Object> getContext() {
		Map<String, Object> context = delegate.get();
		if(context == null) {
			initialValue();
			context = delegate.get();
		}
		return context;
	}
	
	public static String getSid() {
		return get(SESSION_KEY, String.class);
	}
	
	public static void setSid(String sessionId) {
		set(SESSION_KEY, sessionId);
	}
	
	
	public static <T> T get(String key) {
		return get(key, null);
	}
	
	public static <T> T get(String key, Class<T> clz) {
		if(clz == null) {
			return (T) getContext().get(key);
		} else {
			return clz.cast(getContext().get(key));
		}
	}
	
	public static void set(String key, Object val) {
		getContext().put(key, val);
	}
	
	private static void initialValue() {
		delegate.set(new ConcurrentHashMap<>());
	}

	/**
	 * 清除所有的MsmartContext
	 */
	public static void clear() {
		delegate.remove();
	}
	
	/**
	 * remove指定的key
	 */
	public static void remove(String key) {
		getContext().remove(key);
	}
	
	/**
	 * 返回当前context的所有keys，一般只有在log或者调试代码中使用
	 * @return
	 */
	public static Set<String> keys() {
		return getContext().keySet();
	}
}
