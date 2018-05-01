package com.test.perf.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="sys")
public class SysConfig {

	Delay delay;
	
	public Delay getDelay() {
		return delay;
	}

	public void setDelay(Delay delay) {
		this.delay = delay;
	}

	public static class Delay {
		Integer min=1;
		Integer max=10;
		public Integer getMin() {
			return min;
		}
		public Integer getMax() {
			return max;
		}
		public void setMin(Integer min) {
			this.min = min;
		}
		public void setMax(Integer max) {
			this.max = max;
		}
		
	}
	
}
