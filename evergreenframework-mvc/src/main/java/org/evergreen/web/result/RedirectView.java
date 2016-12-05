package org.evergreen.web.result;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.evergreen.web.ActionContext;
import org.evergreen.web.exception.ActionExceptionContext;
import org.evergreen.web.exception.AjaxResponseException;

public class RedirectView extends JumpPage {
	
	public RedirectView(){
	}

	public RedirectView(String url) {
		this.url = url;
	}

	protected void execute() throws IOException, ServletException {
		if (isXmlRequest())
			new ActionExceptionContext(new AjaxResponseException(),
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
					.throwException();
		if (url != null)
			getResponse().sendRedirect(
					ActionContext.getContext().getContextPath() + "/"
							+ url.trim());

	}

}
