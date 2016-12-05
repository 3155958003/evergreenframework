package org.evergreen.plugin.web;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import org.evergreen.plugin.PluginActionHandler;
import org.evergreen.plugin.BeanContainerException;
import org.evergreen.plugin.BeanNameUtil;
import org.evergreen.web.ActionDefinition;
import org.evergreen.web.exception.ActionException;
import org.evergreen.web.exception.ActionExceptionContext;
import org.evergreen.web.exception.RequestMappingException;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringActionHandler extends PluginActionHandler {

	private WebApplicationContext getApplicationContext() {
		return WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());
	}

	public Object crateTargetAction(ActionDefinition definition)
			throws IOException, ActionException {
		if (definition == null)
			throw new RequestMappingException();
		Method method = definition.getMethod();
		if (method != null)
			try {
				return getApplicationContext().getBean(getBeanName(method));
			} catch (BeansException e) {
				e.printStackTrace();
				new ActionExceptionContext(new RequestMappingException(),
						HttpServletResponse.SC_NOT_FOUND).throwException();
			} catch (BeanContainerException e) {
				e.printStackTrace();
				new ActionExceptionContext(new RequestMappingException(),
						HttpServletResponse.SC_NOT_FOUND).throwException();
			}
		return null;
	}

	/**
	 * 获取BeanName,也就是获取容器的key
	 */
	protected String getBeanName(Method method) throws BeanContainerException {
		if (method.getDeclaringClass().isAnnotationPresent(Controller.class)) {
			String value = method.getDeclaringClass()
					.getAnnotation(Controller.class).value();
			if (!"".equals(value))
				return value;
		} 
		if (method.getDeclaringClass().isAnnotationPresent(
				Component.class)) {
			String value = method.getDeclaringClass()
					.getAnnotation(Component.class).value();
			if (!"".equals(value))
				return value;
		} 
		//按照默认的名字查找
		return BeanNameUtil.toLowerBeanName((method.getDeclaringClass()
					.getSimpleName()));
	}

}
