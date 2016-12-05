package org.evergreen.web.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.evergreen.web.ActionDefinition;
import org.evergreen.web.TargetActionHandler;
import org.evergreen.web.exception.ActionException;
import org.evergreen.web.exception.ActionExceptionContext;
import org.evergreen.web.exception.RequestMappingException;

public class WebMapHandler implements TargetActionHandler {

	/**
	 * 从ServletContext中初始化action方法
	 * 
	 * @param definition
	 * @return
	 * @throws ActionException
	 * @throws IOException
	 */
	public Object crateTargetAction(ActionDefinition definition)
			throws IOException, ActionException {
		if (definition != null) {
			try {
				return definition.getActionClass().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			new ActionExceptionContext(new RequestMappingException(),
					HttpServletResponse.SC_NOT_FOUND).throwException();
		}
		return null;
	}

}
