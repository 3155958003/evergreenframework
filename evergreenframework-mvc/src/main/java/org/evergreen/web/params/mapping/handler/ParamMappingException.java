package org.evergreen.web.params.mapping.handler;

import org.evergreen.web.exception.ActionException;

public class ParamMappingException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5988051886996114898L;
	
	private String errorMessage;

	public ParamMappingException(String paramType, String paramName) {
		super(
				"Optional "+paramType+" parameter "
						+ paramName
						+ " is present but cannot be translated into a null value due to being declared as a primitive type. ");
		this.errorMessage = "Optional "+paramType+" parameter "
				+ paramName
				+ " is present but cannot be translated into a null value due to being declared as a primitive type. ";
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
