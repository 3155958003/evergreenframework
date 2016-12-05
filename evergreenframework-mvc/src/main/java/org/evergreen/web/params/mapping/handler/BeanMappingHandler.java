package org.evergreen.web.params.mapping.handler;

import java.io.IOException;

import javax.servlet.ServletException;

import org.evergreen.web.params.mapping.ParamInfo;
import org.evergreen.web.params.mapping.ParamsMappingHandler;
import org.evergreen.web.utils.beanutils.BeanUtilsBean;
import org.evergreen.web.utils.beanutils.ConvertUtilsBean;
import org.evergreen.web.utils.beanutils.PropertyUtilsBeanFirm;

public class BeanMappingHandler extends ParamsMappingHandler {

	public Object execute(ParamInfo paramInfo) throws IOException, ServletException{
		//如果是String类型则直接返回空,
		//BeanUtilsBean对字符串的处理如果为空值,会返回空串而不是null
		if(paramInfo.getParamType() == String.class)
			return null;
		try {
			//构建实例时如果发生异常,表示不是bean类型或者bean未提供默认构造函数s
			Object param = paramInfo.getParamType().newInstance();
			BeanUtilsBean util = new BeanUtilsBean(new ConvertUtilsBean(),
					new PropertyUtilsBeanFirm());
			// 映射参数
			util.populate(param, getRequest().getParameterMap());
			return param;
		} catch (Throwable e) {
			// e.printStackTrace();
			return null;
		}
	}
}
