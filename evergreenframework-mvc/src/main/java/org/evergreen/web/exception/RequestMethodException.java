package org.evergreen.web.exception;

public class RequestMethodException extends ActionException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2820182501405625005L;
	
	private String errorMessage;
	
	public RequestMethodException(String requestMethod) {
		super("Request method '"+requestMethod+"' not supported");
		this.errorMessage = "Request method '"+requestMethod+"' not supported";
	}

	public String getErrorMessage(){
		return errorMessage;
	}
	
	
}
