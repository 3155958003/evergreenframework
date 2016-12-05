package org.evergreen.web.result;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.evergreen.web.exception.ActionExceptionContext;
import org.evergreen.web.exception.AjaxResponseException;

public class ForwardView extends JumpPage {
	
	public ForwardView() {		
	}

	public ForwardView(String url) {
		this.url = url;
	}

	protected void execute() throws IOException, ServletException {
		if (isXmlRequest())
			new ActionExceptionContext(new AjaxResponseException(),
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
					.throwException();
		if (url != null)
			getRequest().getRequestDispatcher("/"+url.trim()).forward(getRequest(),
					getResponse());
	}
}
