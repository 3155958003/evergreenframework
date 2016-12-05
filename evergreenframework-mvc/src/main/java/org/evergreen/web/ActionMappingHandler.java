package org.evergreen.web;

import org.evergreen.web.exception.ActionException;
import org.evergreen.web.exception.ActionExceptionContext;
import org.evergreen.web.exception.RequestMethodException;
import org.evergreen.web.utils.MatcherUtil;
import org.evergreen.web.utils.UrlPatternUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 请求映射,找到对应的Action描述定义
 * 匹配环节包括请求的uri,请求的方法匹配等
 * Created by wangl on 16/4/5.
 */
public class ActionMappingHandler {

    public ActionMapping handler() throws ActionException,
            IOException {
        ActionMapping mapping = new ActionMapping();
        HttpServletRequest request = (HttpServletRequest) ActionContext
                .getContext().get(FrameworkServlet.REQUEST);
        String urlPattern = UrlPatternUtil.getUrlPattern(request);
        List<ActionDefinition> definitions = getActionDefinitions();
        boolean isRequestMethod = false;
        boolean isRequestUri = false;
        for (ActionDefinition actionDefinition : definitions) {
            if(MatcherUtil.pathMatch(actionDefinition.getUrlPattern(),urlPattern)){
                isRequestUri = true;
                if(MatcherUtil.requestMethodMatch(actionDefinition.getRequestMethods(),
                        request.getMethod())){
                    isRequestMethod = true;
                    // 将请求url和匹配的url放入当前请求作用域,用于后面做restful参数映射
                    saveRestPath(actionDefinition.getUrlPattern(), urlPattern);
                    //将definition对象封装到ActionMapping中并返回
                    mapping.setDefinition(actionDefinition);
                }
            }
        }
        if(isRequestUri && !isRequestMethod){
            new ActionExceptionContext(new RequestMethodException(
                    request.getMethod()), HttpServletResponse.SC_METHOD_NOT_ALLOWED)
                    .throwException();
        }
        return mapping;
    }


    /**
     * 获取所有的Action描述定义
     */
    private List<ActionDefinition> getActionDefinitions(){
        HttpServletRequest request = (HttpServletRequest) ActionContext
                .getContext().get(FrameworkServlet.REQUEST);
        @SuppressWarnings("unchecked")
        List<ActionDefinition> definitions = (List<ActionDefinition>) request
                .getServletContext().getAttribute(ActionDefinition.DEFINITION);
        return definitions;
    }

    /**
     * 将请求的url和Annotation上定义的url放入请求作用域, 在参数映射时解析restful参数
     */
    private void saveRestPath(String destPath, String origPath) {
        ActionContext.getContext().put(FrameworkServlet.DEST_PATH, destPath.split("/"));
        ActionContext.getContext().put(FrameworkServlet.ORIG_PATH, origPath.split("/"));
    }

}
