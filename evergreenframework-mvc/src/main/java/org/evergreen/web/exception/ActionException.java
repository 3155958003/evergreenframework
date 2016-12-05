package org.evergreen.web.exception;

import javax.servlet.ServletException;

public abstract class ActionException extends ServletException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6077688573906359576L;
	
	public ActionException(String exception) {
		super(exception);
	}

	public abstract String getErrorMessage();
}
