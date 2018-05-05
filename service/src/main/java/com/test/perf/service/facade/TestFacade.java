package com.test.perf.service.facade;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.perf.common.constant.Constants;
import com.test.perf.common.vo.Response;
import com.test.perf.entity.City;
import com.test.perf.service.config.SysConfig;

@RestController
public class TestFacade extends BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestFacade.class);
	private static final String PREFIX = "city_";
	@Autowired
	SysConfig config;
	
//	@Autowired
	RedisTemplate<String, City> redisTemplate;
	
	@RequestMapping(Constants.HELLOWORLD)
	public Response<String> helloworld(Long random) {
		return new Response<>("Hello world: "+random);
	}
	
	@RequestMapping(Constants.DELAY)
	public Response<String> delay(Integer delay) {
		int t = delay==null? RandomUtils.nextInt(config.getDelay().getMin(), config.getDelay().getMax()):delay;
		sleep(t);
		return new Response<>("delay: "+t);
	}
	
	@RequestMapping(value = Constants.REDIS_READ, method = RequestMethod.POST)
	public Response<City> redisRead(@RequestParam("sid") String sid, Integer id) {
		return new Response<City>(redisTemplate.opsForValue().get(getKey(id)));
	}

	@RequestMapping(value = Constants.REDIS_WRITE, method = RequestMethod.POST)
	public Response<?> redisWrite(@RequestParam("sid") String sid, @RequestBody City city) {
		if(!isNull(city) && isNull(city.getId())) {
			city.setId(RandomUtils.nextInt());
		}
		redisTemplate.opsForValue().set(getKey(city.getId()), city);
		return new Response<>();
	}
	
	private String getKey(Integer id) {
		return PREFIX + id;
	}
	
	private void sleep(int t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			LOGGER.error("sleep error", e);
		}
	}
}
