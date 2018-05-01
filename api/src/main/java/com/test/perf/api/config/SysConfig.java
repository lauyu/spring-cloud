package com.test.perf.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="sys")
public class SysConfig {

	boolean fakeMode = false;
	boolean fakeSid = false;
	public boolean isFakeMode() {
		return fakeMode;
	}
	public void setFakeMode(boolean fakeMode) {
		this.fakeMode = fakeMode;
	}
	public boolean isFakeSid() {
		return fakeSid;
	}
	public void setFakeSid(boolean fakeSid) {
		this.fakeSid = fakeSid;
	}
	
	
}
