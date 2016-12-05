package org.evergreen.web.exception;

public class RequestMappingException extends ActionException{
	
	private final static String NOT_FOUND = "No mapping found for HTTP request with URI";

	/**
	 * 
	 */
	private static final long serialVersionUID = 9033302480030050501L;

	public RequestMappingException() {
		super(NOT_FOUND);
	}

	public String getErrorMessage() {
		return NOT_FOUND;
	}
	
	

}
