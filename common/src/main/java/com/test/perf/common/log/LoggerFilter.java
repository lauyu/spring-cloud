package com.test.perf.common.log;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.perf.common.ctx.AppContext;

public class LoggerFilter implements Filter {
	private String STARTTIMEKEY="intellease.log.startTime";
	private Logger LOGGER = LoggerFactory.getLogger(LoggerFilter.class);
	private static final boolean debug = false;
	FilterConfig filterConfig = null;
	private Set<String> intimateParams = new HashSet<>();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		if (filterConfig != null) {
			if (debug) {
				log("LogFilter:Initializing filter");
			}
		}
		if(StringUtils.isNotBlank(filterConfig.getInitParameter("intimateParams"))) {
			for(String p: filterConfig.getInitParameter("intimateParams").split(",")) {
				intimateParams.add(p);
			}
		} else {
			intimateParams.add("password");
			intimateParams.add("iamPwd");
		}
		ServletContext servletContext = filterConfig.getServletContext();
//		wac = (WebApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		WrapperResponse wrapperResponse = null;
		if(response instanceof HttpServletResponse){
			wrapperResponse=new WrapperResponse((HttpServletResponse)response);
		}
		LoggerVo loggerVo = new LoggerVo(AppContext.getSid());
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			loggerVo.setUrl(req.getRequestURL().toString());
			Map<String, String> params = new HashMap<String, String>();
			@SuppressWarnings("rawtypes")
			Enumeration enu = req.getParameterNames();
			while (enu.hasMoreElements()) {
				String paramName = (String) enu.nextElement();
				String paramValue = request.getParameter(paramName);
				params.put(paramName, paramValue);
				//屏蔽隐私参数，如密码等
				if(intimateParams.contains(paramName)) {
					params.put(paramName, "*****");
				}
			}
			loggerVo.setParams(params);
			LOGGER.info("reqParam:"+loggerVo.toString());
			
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		//0.记录起始时间
		request.setAttribute(STARTTIMEKEY, System.currentTimeMillis());
		
		chain.doFilter(request, wrapperResponse);
		PrintWriter out=null;
		ServletOutputStream sout=null;
		try {
		if (request instanceof HttpServletRequest) {
			//1.获取请求结果			 
			byte[] data = wrapperResponse.getResponseData();
			String result = new String(data);
			if(wrapperResponse.isPrintWriter()){
				out=response.getWriter();
				out.write(result);
			} else {
				sout=response.getOutputStream();
				sout.write(data);
			}
			wrapperResponse.flushBuffer();
			loggerVo.setResult(result);
			//2.获取请求参数
			HttpServletRequest req = (HttpServletRequest) request;
			
			try {
				//3.耗时时间记录
				long endTime = System.currentTimeMillis();
				long startTime = (long) request.getAttribute(STARTTIMEKEY);
				loggerVo.setExecuteTime(endTime - startTime);
				
				//4.获取真实请求ip
				//nginx反向代理，尝试获取真实ip地址 //需要nginx中配置
				
				String remoteAddr = req.getRemoteAddr();
				String forwarded = req.getHeader("X-Forwarded-For"); 
				String realIp = req.getHeader("X-Real-IP");
				String ip="";
				if (realIp == null) {
					 if (forwarded == null) {
						 ip = remoteAddr;
					 } else {
						int index = forwarded.indexOf(",");
						if(index != -1){
							ip = forwarded.substring(0,index);
						}
					 }
				 } else {
					 ip = realIp;
				}
				loggerVo.setIp(ip);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//6.记录或者发送相关日志
		LOGGER.info("resResult:"+loggerVo.toString());
		} catch(Exception e) {
			LOGGER.error("Filter log error="+e.getMessage(),e);
		} finally {
			if(out!=null){
				try {
					out.close();
				} catch(Exception e){}
			}
			if(sout!=null){
				try {
					sout.close();
				}catch(Exception e){}
			}
		}
	}
	
	private void log(String string) {
		filterConfig.getServletContext().log(string);
	}
	
}
