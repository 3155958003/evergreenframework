package org.evergreen.web.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.evergreen.web.ActionContext;
import org.evergreen.web.FrameworkServlet;

public class ActionExceptionContext {

	private ActionException e;
	private int responseStatus;

	public ActionExceptionContext(ActionException e, int responseStatus) {
		this.e = e;
		this.responseStatus = responseStatus;
	}

	public void throwException() throws IOException, ActionException {
		HttpServletResponse response = (HttpServletResponse) ActionContext
				.getContext().get(FrameworkServlet.RESPONSE);
		response.sendError(responseStatus, e.getErrorMessage());
		throw e;
	}
}
