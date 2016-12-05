package org.evergreen.web.params.mapping;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.evergreen.web.ActionContext;
import org.evergreen.web.FrameworkServlet;

public abstract class ParamsMappingHandler {

	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) ActionContext
				.getContext().get(FrameworkServlet.REQUEST);
		
	}

	public abstract Object execute(ParamInfo paramInfo) throws IOException, ServletException;
}
