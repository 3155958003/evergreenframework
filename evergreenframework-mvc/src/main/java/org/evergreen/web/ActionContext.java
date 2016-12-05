package org.evergreen.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import javax.servlet.AsyncContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.evergreen.web.utils.ResourceUtil;

public class ActionContext {

	/**
	 * 错误消息
	 */
	public final static String ERRORS = "org.evergreen.web.ActionContext.errors";

	/**
	 * 令牌
	 */
	public final static String TOKEN = "org.evergreen.web.ActionContext.token";

	/**
	 * 区域语言
	 */
	public final static String LOCALE = "org.evergreen.web.ActionContext.locale";

	/**
	 *  国际化资源
	 */
	final static String I18N = "i18n";

	/**
	 * 使用ThreadLocal存放当前客户端的ActionContext
	 */
	final static ThreadLocal<ActionContext> localContext = new ThreadLocal<ActionContext>();

	/**
	 * 上下文的Map
	 */
	private Map<String, Object> contextMap = new HashMap<String, Object>();


	/**
	 * 私有构造函数
	 */
	private ActionContext() {
	}

	Map<String, Object> getContextMap() {
		return contextMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getSession() {
		return (Map<String, Object>) contextMap.get(FrameworkServlet.SESSION);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getApplication() {
		return (Map<String, Object>) contextMap
				.get(FrameworkServlet.APPLICATION);
	}

	/*
	 * 放入请求作用域
	 */
	public void put(String key, Object value) {
		((HttpServletRequest) contextMap.get(FrameworkServlet.REQUEST))
				.setAttribute(key, value);
	}

	/*
	 * 从请求作用域取值
	 */
	public Object get(String key) {
		return (FrameworkServlet.REQUEST.equals(key) || FrameworkServlet.RESPONSE
				.equals(key)) ? contextMap.get(key)
				: ((HttpServletRequest) contextMap
						.get(FrameworkServlet.REQUEST)).getAttribute(key);

	}

	/**
	 * 从请求作用域中移除对应的值
	 */
	public Object remove(String key) {
		HttpServletRequest request = (HttpServletRequest) contextMap
				.get(FrameworkServlet.REQUEST);
		Object obj = request.getAttribute(key);
		request.removeAttribute(key);
		return obj;
	}

	/**
	 * 获取当前线程绑定的ActionContext
	 * 
	 * @return
	 */
	public static ActionContext getContext() {
		if (localContext.get() == null)
			// 如果当前线程上没有绑定ActionContext,则创建一个并绑定当前线程
			localContext.set(new ActionContext());
		// 返回当前线程的ActionContext对象
		return localContext.get();
	}
	
	/**
	 * 获取ServletContext初始化参数
	 */
	public String getContextParam(String name){
		HttpServletRequest request = (HttpServletRequest) get(FrameworkServlet.REQUEST);
		return request.getServletContext().getInitParameter(name);
	}

	/**
	 * 添加验证错误信息
	 */
	public void addError(String key, String message) {
		if (message.startsWith("{") && message.endsWith("}")) {
			String rkey = message.substring(1, message.length() - 1);
			Locale locale = getLocale();
			String baseName = getContextParam(I18N);
			Map<String, String> resourceMap = ResourceUtil.getResource(
					baseName, locale);
			getErrors().put(key, resourceMap.get(rkey));
		} else {
			getErrors().put(key, message);
		}
	}

	/**
	 * 当前验证结果是否有错误信息
	 * 
	 * @return
	 */
	public boolean hasErrors() {
		return (get(ERRORS) != null) ? true : false;
	}

	/**
	 * 获取验证错误信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getErrors() {
		if (get(ERRORS) == null)
			put(ERRORS, new HashMap<String, String>());
		return (Map<String, String>) get(ERRORS);
	}

	/**
	 * 验证令牌
	 * 
	 * @return
	 */
	public boolean invalidToken() {
		HttpServletRequest request = (HttpServletRequest) get(FrameworkServlet.REQUEST);
		String reqToken = request.getParameter(TOKEN);
		Object sessionToken = request.getSession().getAttribute(TOKEN);
		if (sessionToken.toString().equals(reqToken)){
			getSession().put(TOKEN, UUID.randomUUID().toString());
			return false;
		}	
		return true;
	}

	/**
	 * 获取资源绝对路径
	 * 
	 * @param arg0
	 * @return
	 */
	public String getRealPath(String arg0) {
		HttpServletRequest request = (HttpServletRequest) get(FrameworkServlet.REQUEST);
		return request.getServletContext().getRealPath(arg0);
	}

	/**
	 * 获取上下文路径
	 * 
	 * @return
	 */
	public String getContextPath() {
		HttpServletRequest request = (HttpServletRequest) get(FrameworkServlet.REQUEST);
		return request.getServletContext().getContextPath();
	}

	/**
	 * 获取所有请求参数
	 */
	public Map<String, String[]> getParameters() {
		HttpServletRequest request = (HttpServletRequest) get(FrameworkServlet.REQUEST);
		return request.getParameterMap();
	}

	/**
	 * 获取sessionID
	 */
	public String getSessionId() {
		HttpServletRequest request = (HttpServletRequest) get(FrameworkServlet.REQUEST);
		return request.getSession().getId();
	}

	/**
	 * 添加cookie
	 */
	public void addCookie(Cookie cookie) {
		HttpServletResponse response = (HttpServletResponse) get(FrameworkServlet.RESPONSE);
		response.addCookie(cookie);
	}

	/**
	 * 设置locale
	 */
	public void setLocale(Locale locale) {
		getSession().put(LOCALE, locale);
	}
	
	/**
	 * 获取Locale
	 */
	public Locale getLocale(){
		HttpServletRequest request = (HttpServletRequest) get(FrameworkServlet.REQUEST);
		Locale locale = (Locale) getSession().get(ActionContext.LOCALE);
		return (locale == null) ? request.getLocale() : locale;
	}

	/**
	 * Url编码
	 */
	public String encodeURL(String url) {
		HttpServletResponse response = (HttpServletResponse) get(FrameworkServlet.RESPONSE);
		return response.encodeURL(url);
	}

	public String encodeRedirectURL(String url) {
		HttpServletResponse response = (HttpServletResponse) get(FrameworkServlet.RESPONSE);
		return response.encodeRedirectURL(url);
	}

	/**
	 * 开启异步
	 * 
	 * @return
	 */
	private AsyncContext startAsync() {
		HttpServletRequest request = (HttpServletRequest) get(FrameworkServlet.REQUEST);
		HttpServletResponse response = (HttpServletResponse) get(FrameworkServlet.RESPONSE);
		return request.isAsyncStarted() ? request.getAsyncContext() : request
				.startAsync(request, response);
	}

	/**
	 * 获取异步执行器
	 * 
	 * @return
	 */
	public AsyncExecutor getAsyncExecutor() {
		// 匿名内部类构建一个异步执行器
		return new AsyncExecutor() {
			public void execute(RunnableSupport runnable) {
				runnable.setAsyncContext(startAsync());
				ExecutorService pool = (ExecutorService) getApplication().get(
						FrameworkServlet.THREAD_POOL);
				pool.execute(runnable);
			}
		};
	}

}
