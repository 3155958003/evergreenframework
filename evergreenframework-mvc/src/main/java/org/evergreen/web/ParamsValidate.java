package org.evergreen.web;

import java.io.IOException;
import java.util.Map;

import org.evergreen.web.ActionMapping;
import org.evergreen.web.exception.ActionException;

public interface ParamsValidate {

	public Map<String, String> validate(ActionMapping mapping) throws ActionException, IOException;

}
