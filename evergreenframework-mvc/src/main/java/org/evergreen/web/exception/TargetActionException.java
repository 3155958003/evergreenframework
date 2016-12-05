package org.evergreen.web.exception;

public class TargetActionException extends ActionException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3291810734338162508L;
	
	private final static String NOT_FOUND_ACTION = "Can not find the target action.";

	public TargetActionException() {
		super(NOT_FOUND_ACTION);
	}
	
	public String getErrorMessage(){
		return NOT_FOUND_ACTION;
	}
}
