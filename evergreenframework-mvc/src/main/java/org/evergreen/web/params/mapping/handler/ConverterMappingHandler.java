package org.evergreen.web.params.mapping.handler;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.servlet.ServletException;

import org.evergreen.web.annotation.Convert;
import org.evergreen.web.params.mapping.ParamInfo;
import org.evergreen.web.utils.beanutils.ConvertUtils;
import org.evergreen.web.utils.beanutils.Converter;

public class ConverterMappingHandler extends AnnotationMappingHandler{

	@Override
	public Object execute(ParamInfo paramInfo) throws IOException,
			ServletException {
		if (!hasAnnotation(paramInfo))
			return null;		
		for (Annotation annotation : paramInfo.getAnnotations()) {
			if(annotation instanceof Convert){
				String value = getRequest().getParameter(paramInfo.getParamName());
				try {
					Converter converter = (Converter) ((Convert)annotation).value().newInstance();
					ConvertUtils.register(converter, paramInfo.getParamType());
					return ConvertUtils.convert(value,paramInfo.getParamType());
				} catch (InstantiationException e) {
					e.printStackTrace();
					throw new ConvertException(e.getMessage());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					throw new ConvertException(e.getMessage());
				}
			}
		}
		return null;
	}

}
