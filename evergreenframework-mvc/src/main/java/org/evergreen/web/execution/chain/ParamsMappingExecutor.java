package org.evergreen.web.execution.chain;

import org.evergreen.web.ActionDefinition;
import org.evergreen.web.ActionInvokeHandlerChain;
import org.evergreen.web.ActionMapping;
import org.evergreen.web.ExecutionHandler;
import org.evergreen.web.exception.ActionException;
import org.evergreen.web.exception.ActionExceptionContext;
import org.evergreen.web.params.mapping.ParamInfo;
import org.evergreen.web.params.mapping.ParamsMappingHandler;
import org.evergreen.web.params.mapping.handler.ParamMappingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Created by wangl on 16/4/7.
 */
public class ParamsMappingExecutor implements ExecutionHandler{

    private final static List<Class<?>> basicType = new ArrayList<Class<?>>();

    static{
        basicType.add(Integer.TYPE);
        basicType.add(Short.TYPE);
        basicType.add(Byte.TYPE);
        basicType.add(Float.TYPE);
        basicType.add(Double.TYPE);
        basicType.add(Long.TYPE);
        basicType.add(Boolean.TYPE);
        basicType.add(Character.TYPE);
    }

    @Override
    public void execute(ActionMapping mapping, ActionInvokeHandlerChain chain) throws ServletException, IOException {
        // 映射参数
        Object[] params = paramsMapping(mapping);
        mapping.setParams(params);
        chain.doExecute(mapping);
    }

    private Object[] paramsMapping(ActionMapping mapping) throws ServletException,
            IOException {
        ActionDefinition definition = mapping.getDefinition();
        Object[] params = new Object[definition.getParamInfo().size()];
        for (int i = 0; i < params.length; i++) {
            ParamInfo paramInfo = definition.getParamInfo().get(i);
            params[i] = doExecute(paramInfo);
            // 如果映射参数不成功,且参数类型是基本类型,则抛出异常信息
            checkType(params[i], paramInfo);
        }
        return params;
    }

    // 执行批量参数映射
    private Object doExecute(ParamInfo paramInfo) throws IOException, ServletException {
        // 使用ServiceLoader来加载SPI实现类
        // 对应的实现类名称必须放在META-INF\services目录下
        ServiceLoader<ParamsMappingHandler> loader = ServiceLoader.load(ParamsMappingHandler.class);
        for (ParamsMappingHandler handler : loader) {
            Object param = handler.execute(paramInfo);
            // 如果映射成功,则返回该参数,如果为null,
            // 则继续循环使用下一个命令执行映射
            if (param != null)
                return param;
        }
        return null;
    }

    // 抛出基本类型的异常信息
    private void checkType(Object param, ParamInfo paramInfo)
            throws ActionException, IOException {
        if (param == null && basicType.contains(paramInfo.getParamType()))
            new ActionExceptionContext(new ParamMappingException(paramInfo
                    .getParamType().toString(), paramInfo.getParamName()),
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .throwException();
    }

}
