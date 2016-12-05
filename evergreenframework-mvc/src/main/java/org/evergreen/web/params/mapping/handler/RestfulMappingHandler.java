package org.evergreen.web.params.mapping.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.evergreen.web.FrameworkServlet;
import org.evergreen.web.params.mapping.ParamInfo;
import org.evergreen.web.params.mapping.ParamsMappingHandler;
import org.evergreen.web.utils.beanutils.ConvertUtilsBean;

public class RestfulMappingHandler extends ParamsMappingHandler {

	public Object execute(ParamInfo paramInfo) throws IOException, ServletException{
		if (paramInfo.getParamType().isArray())
			return null;
		String[] subDest = (String[])getRequest().getAttribute(FrameworkServlet.DEST_PATH);
		String[] subOrig = (String[])getRequest().getAttribute(FrameworkServlet.ORIG_PATH);
		Map<String, String> params = initRestfulParams(subDest, subOrig);
		if(params.isEmpty())
			return null;
		ConvertUtilsBean convertUtil = new ConvertUtilsBean();
		String paramValue = params.get(paramInfo.getParamName());
		return paramValue != null ? convertUtil.convert(paramValue,
				paramInfo.getParamType()) : null;
	}
	
	/**
	 * 解析restful的url参数
	 * 
	 * @param subDest
	 * @param subOrig
	 * @return
	 */
	private Map<String, String> initRestfulParams(String[] subDest, String[] subOrig) {
		Map<String, String> restfulParamsMap = new HashMap<String, String>();
		for (int i = 0; i < subDest.length; i++) {
			if (subDest[i].startsWith("{") && subDest[i].endsWith("}")) {
				String paramName = subDest[i].substring(
						subDest[i].indexOf("{") + 1,
						subDest[i].lastIndexOf("}"));
				// 参数也许是一个正则表达式,根据约定进行截取
				paramName = paramName.split(":")[0];
				restfulParamsMap.put(paramName, subOrig[i]);
			}
		}
		return restfulParamsMap;
	}

}
