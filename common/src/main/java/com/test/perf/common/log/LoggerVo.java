package com.test.perf.common.log;

import java.util.Map;

public class LoggerVo {
	private String sid;
	private String ip;
	private String url;
	private Map<String, String> params;
	private long executeTime=-1;
	private Object result;
	public LoggerVo(String sid) {
		this.sid = sid;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public long getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(long executeTime) {
		this.executeTime = executeTime;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String toString(){
		return "{sid="+sid+",ip="+ip+",url="+url+",params="+params+",executeTime="+executeTime+",result="+result+"}";
	}
	
	
}
