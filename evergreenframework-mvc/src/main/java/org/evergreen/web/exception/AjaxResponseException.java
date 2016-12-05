package org.evergreen.web.exception;

public class AjaxResponseException extends ActionException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4957715108891020289L;
	
	private final static String XMLREQUEST_ERROR = "Do you mean ajax ?";
	
	public AjaxResponseException(){
		super(XMLREQUEST_ERROR);
	}

	public String getErrorMessage() {
		return XMLREQUEST_ERROR;
	}
}
