package com.test.perf.common.util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;

/**
 * 
 * @ClassName: JsonUtil 
 * @Description: 对象和json之间的转换工具类，内部依赖fastjson实现
 * @author yu.lau
 * @date 2017年8月29日 下午7:12:05 
 *
 */
public class JsonUtil {

	/**
	 * 将Map转化为Json
	 * 
	 * @param map
	 * @return String
	 */
	public static <T> String mapToJson(Map<String, T> map) {
    	 return JSON.toJSONString(map);
	}
	
	/**
	 * 对象转换
	 * @param jsonStr
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJson(String jsonStr, Class<T> clazz) {
	    return JSON.parseObject(jsonStr, clazz);
	}
    
	public static <T> T fromJsonByType(String jsonStr, Type typeOfT) {
        return JSON.parseObject(jsonStr, typeOfT);
    }
    
    public static <T> T fromJson(JSONObject jsonStr, Class<T> clazz) {
        return JSON.parseObject(jsonStr.toJSONString(), clazz);
    }
    
	/**
     * 对象转json
     * 
     * @param obj
     * @return
     */
    public static <T> String toJson(T obj) {
        return toJson(obj, null);
    }
    
    /**
     * 对象转json
     * 
     * @param obj
     * @return
     */
    public static <T> String toJson(T obj, String... excludeFields) {
    	if(excludeFields != null) {
    		return JSON.toJSONString(obj, new ExcludeFieldFilter(excludeFields));
    	} else {
    		return JSON.toJSONString(obj);	
    	}
    }
    
    private static class ExcludeFieldFilter implements PropertyFilter {
    	private String[] excludeFields;
    	public ExcludeFieldFilter(String[] excludeFields) {
    		this.excludeFields = excludeFields;
    	}
    	
		@Override
		public boolean apply(Object object, String name, Object value) {
			if(excludeFields!=null && ArrayUtils.contains(excludeFields, name)) {
				return false;
			}
			return true;
		}
    }
    
    public static void main(String[] args) {
    	String jsonStr = "{\"key1\":123, \"key2\":\"val2\"}";
    	Map<String, String> entity = new HashMap<>();
    	entity.put("f1", "val1");
    	entity.put("f2", "val2");
    	entity.put("f3", "val3");
    	System.out.println(toJson(entity));
    	System.out.println(toJson(entity, "f2"));
    	System.out.println(fromJson(jsonStr, JSONObject.class).get("key1"));
    }
}
