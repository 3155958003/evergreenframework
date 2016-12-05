package org.evergreen.web.params.mapping.handler;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.evergreen.web.annotation.CookieValue;
import org.evergreen.web.params.mapping.ParamInfo;
import org.evergreen.web.utils.beanutils.ConvertUtilsBean;

public class CookieMappingHandler extends AnnotationMappingHandler {

	public Object execute(ParamInfo paramInfo) throws IOException, ServletException{
		if (!hasAnnotation(paramInfo))
			return null;
		for (Annotation annotation : paramInfo.getAnnotations()) {
			if(annotation instanceof CookieValue){
				CookieValue cookieAnno = (CookieValue)annotation;
				for (Cookie cookie : getRequest().getCookies()) {
					if(cookie.getName().equals(cookieAnno.value())){
						Object value =  new ConvertUtilsBean().convert(cookie.getValue(),
								paramInfo.getParamType());
						if(value ==null)
							throw new IllegalArgumentException("Cookie value could not cast "+paramInfo.getParamType());
						else 
							return value;
					}
				}
				throw new RuntimeException("Can not found the cookie name "+cookieAnno.value());
			}
		}
		return null;
	}
}
