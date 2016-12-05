package org.evergreen.web.params.mapping.handler;

import java.io.IOException;

import javax.servlet.ServletException;

import org.evergreen.web.params.mapping.ParamInfo;
import org.evergreen.web.params.mapping.ParamsMappingHandler;
import org.evergreen.web.utils.beanutils.ConvertUtils;

public class BasicMappingHandler extends ParamsMappingHandler {

	public Object execute(ParamInfo paramInfo) throws IOException,
			ServletException {
		Object param = (paramInfo.getParamType().isArray()) ? getRequest()
				.getParameterValues(paramInfo.getParamName()) : getRequest()
				.getParameter(paramInfo.getParamName());
		if (param != null) {
			Object value = ConvertUtils
					.convert(param, paramInfo.getParamType());
			//当ConvertUtils转换的不是能识别的类型时.默认返回的是String
			//因此这里需要特殊处理一下
			return (value.getClass() == String.class && paramInfo
					.getParamType() != String.class) ? null : value;
		}
		return null;
	}
}
