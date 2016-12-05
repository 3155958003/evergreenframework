package org.evergreen.web.params.mapping.handler;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.servlet.ServletException;

import org.evergreen.web.annotation.RequestHeader;
import org.evergreen.web.params.mapping.ParamInfo;
import org.evergreen.web.utils.beanutils.ConvertUtilsBean;

public class HeaderMappingHandler extends AnnotationMappingHandler {

	public Object execute(ParamInfo paramInfo) throws IOException, ServletException{
		if (!hasAnnotation(paramInfo))
			return null;
		for (Annotation annotation : paramInfo.getAnnotations()) {
			if (annotation instanceof RequestHeader) {
				RequestHeader headerAnno = (RequestHeader) annotation;
				String headerVal = getRequest().getHeader(headerAnno.value());
				if (headerVal != null) {
					Object value = new ConvertUtilsBean().convert(headerVal,
							paramInfo.getParamType());
					if (value == null)
						throw new IllegalArgumentException(
								"Request header value could not cast "
										+ paramInfo.getParamType());
					else
						return value;
				} else {
					throw new RuntimeException(
							"Can not found the request header name "
									+ headerAnno.value());
				}
			}

		}
		return null;
	}
}
