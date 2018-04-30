package com.test.perf.common.log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.perf.common.ctx.AppContext;
import com.test.perf.common.util.RandomUtil;

public class ContextFixFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(ContextFixFilter.class);
	public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";
	
	private FilterConfig filterConfig;
	
	private String beanName;
	
	private String defaultLanguage;
	
	private String sidParaName = "sid";
	private String stampParaName = "stamp";
	private String languageParaName = "language";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
		boolean hasAlreadyFilteredAttribute = request.getAttribute(alreadyFilteredAttributeName) != null;

		if (hasAlreadyFilteredAttribute || shouldNotFilter(httpRequest)) {

			// Proceed without invoking this filter...
			filterChain.doFilter(request, response);
		}
		else {
			// Do invoke this filter...
			request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
			try {
				doFilterInternal(httpRequest, httpResponse, filterChain);
			}
			finally {
				// Remove the "already filtered" request attribute for this request.
				request.removeAttribute(alreadyFilteredAttributeName);
			}
		}
	}

	protected String getAlreadyFilteredAttributeName() {
		String name = getFilterName();
		if (name == null) {
			name = getClass().getName();
		}
		return name + ALREADY_FILTERED_SUFFIX;
	}

	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return false;
	}
	
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException  {
		String sid = getSid(request);
		AppContext.setSid(sid);
//		if(LOGGER.isTraceEnabled()) {
//			LOGGER.trace("MsmartContext设置sid={}, stamp={}, language={}", AppContext.getSid(), 
//					AppContext.getStamp(), AppContext.getLanguage());
//		}
//		MDC.put("sid", sid);
		try {
			filterChain.doFilter(request, response); //这里不能捕获异常
		} finally {
			if(LOGGER.isTraceEnabled()) {
				LOGGER.trace("清除MsmartContext：{}", AppContext.keys());
			}
			AppContext.clear();
//			MDC.remove("sid");
		}
	}
	
	/**
	 * 
	 * 获取请求id，默认实现是返回{@link RandomUtil #getSimpleUUID}
	 * 
	 * @param request
	 * @return
	 */
	protected String getSid(HttpServletRequest request) {
		String sid = request.getParameter(sidParaName);
		if(StringUtils.isBlank(sid)) {
			return RandomUtil.getSimpleUUID();
		}
		return sid;
	}

	protected String getStamp(HttpServletRequest request) {
		if(StringUtils.isNotBlank(request.getParameter(stampParaName))) {
			return request.getParameter(stampParaName);	
		} else {
			return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		}
	}

	protected String getLanguage(HttpServletRequest request) {
		if(StringUtils.isNotBlank(request.getParameter(languageParaName))) {
			return request.getParameter(languageParaName);	
		} else {
			return defaultLanguage;
		}
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		if(StringUtils.isNotBlank(filterConfig.getInitParameter("defaultLanguage"))) {
			this.defaultLanguage = filterConfig.getInitParameter("defaultLanguage");
		}
		if(StringUtils.isNotBlank(filterConfig.getInitParameter("beanName"))) {
			this.beanName = filterConfig.getInitParameter("beanName");
		}
		if(StringUtils.isNotBlank(filterConfig.getInitParameter("sidParaName"))) {
			this.sidParaName = filterConfig.getInitParameter("sidParaName");
		}
		if(StringUtils.isNotBlank(filterConfig.getInitParameter("stampParaName"))) {
			this.stampParaName = filterConfig.getInitParameter("stampParaName");
		}
		if(StringUtils.isNotBlank(filterConfig.getInitParameter("languageParaName"))) {
			this.languageParaName = filterConfig.getInitParameter("languageParaName");
		}
		if(this.defaultLanguage == null) {
			this.defaultLanguage = "zh_CN";
		}
	}

	@Override
	public void destroy() {
		
	}
	
	protected final String getFilterName() {
		return (this.filterConfig != null ? this.filterConfig.getFilterName() : this.beanName);
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	public String getSidParaName() {
		return sidParaName;
	}

	public void setSidParaName(String sidParaName) {
		this.sidParaName = sidParaName;
	}

	public String getStampParaName() {
		return stampParaName;
	}

	public void setStampParaName(String stampParaName) {
		this.stampParaName = stampParaName;
	}

	public String getLanguageParaName() {
		return languageParaName;
	}

	public void setLanguageParaName(String languageParaName) {
		this.languageParaName = languageParaName;
	}
	
}
