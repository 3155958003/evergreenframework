package org.evergreen.web;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.evergreen.web.params.mapping.ParamInfo;

/**
 * Action定义描述信息
 * 
 * @author wangl
 * 
 */
public class ActionDefinition {
	/**
	 * Action描述定义
	 */
	public final static String DEFINITION = "definition";
	/**
	 * controller uri
	 */
	private String controllerUrl = "";
	/**
	 * method uri
	 */
	private String actionUrl = "";
	/**
	 * requestMethods
	 */
	private List<String> requestMethods = new ArrayList<String>();

	/**
	 * Action Class
	 */
	private Class<?> actionClass;

	/**
	 * Action Method
	 */
	private Method method;

	/**
	 * 封装Action方法中所有参数的名字和类型
	 */
	private List<ParamInfo> paramInfo = new ArrayList<ParamInfo>();

	public String getUrlPattern() {
		return controllerUrl + actionUrl;
	}

	public void setControllerUrl(String controllerUrl) {
		this.controllerUrl = controllerUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public List<String> getRequestMethods() {
		return requestMethods;
	}

	public void setRequestMethods(List<String> requestMethods) {
		this.requestMethods = requestMethods;
	}

	public Class<?> getActionClass() {
		return actionClass;
	}

	public void setActionClass(Class<?> actionClass) {
		this.actionClass = actionClass;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public List<ParamInfo> getParamInfo() {
		return paramInfo;
	}

	public void setParamInfo(List<ParamInfo> paramInfo) {
		this.paramInfo = paramInfo;
	}
}
