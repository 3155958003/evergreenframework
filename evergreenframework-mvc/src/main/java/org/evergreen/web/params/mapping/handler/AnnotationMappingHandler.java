package org.evergreen.web.params.mapping.handler;

import org.evergreen.web.params.mapping.ParamInfo;
import org.evergreen.web.params.mapping.ParamsMappingHandler;

public abstract class AnnotationMappingHandler extends ParamsMappingHandler{

	protected boolean hasAnnotation(ParamInfo paramInfo) {
		return (paramInfo.getAnnotations().length != 0) ? true : false;
	}
}
