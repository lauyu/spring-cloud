package com.test.perf.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="sys")
public class SysConfig {

	boolean fakeMode = false;
	
}
