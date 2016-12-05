package org.evergreen.plugin.web;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import org.evergreen.beans.annotation.Component;
import org.evergreen.beans.factory.BeanFactory;
import org.evergreen.plugin.PluginActionHandler;
import org.evergreen.plugin.BeanContainerException;
import org.evergreen.plugin.BeanNameUtil;
import org.evergreen.web.ActionDefinition;
import org.evergreen.web.exception.ActionException;
import org.evergreen.web.exception.ActionExceptionContext;
import org.evergreen.web.exception.RequestMappingException;

public class EvergreenActionHandler extends PluginActionHandler {
	
	public static final String BEAN_FACTORY = "org.evergreen.container.factory";

	public Object crateTargetAction(ActionDefinition definition)
			throws IOException, ActionException {
		BeanFactory factory = (BeanFactory) getServletContext().getAttribute(
				BEAN_FACTORY);
		if (definition == null)
			throw new RequestMappingException();
		Method method = definition.getMethod();
		if (method != null)
			try {
				return factory.getBean(getBeanName(method));
			} catch (BeanContainerException e) {
				e.printStackTrace();
				new ActionExceptionContext(new RequestMappingException(),
						HttpServletResponse.SC_NOT_FOUND).throwException();
			}
		return null;
	}

	protected String getBeanName(Method method) throws BeanContainerException {
		if (method.getDeclaringClass().getAnnotation(Component.class) == null)
			throw new BeanContainerException();
		String beanName = method.getDeclaringClass()
				.getAnnotation(Component.class).value();
		beanName = ("".equals(beanName)) ? BeanNameUtil.toLowerBeanName((method
				.getDeclaringClass().getSimpleName())) : beanName;
		return beanName;
	}
}
