package org.evergreen.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.evergreen.web.exception.ActionException;
import org.evergreen.web.result.DefaultViewResult;

/**
 * 核心控制器,接受所有请求,并将请求分发给不同的业务控制器
 */
public class ActionServlet extends FrameworkServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 789342728181721564L;
	/**
	 * 映射处理器,匹配url到具体的Action
	 */
	private  ActionMappingHandler mappingHandler;

	/**
	 * 初始化操作,并初始化Action映射处理器
	 * @param config
	 * @throws ServletException
     */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		mappingHandler = new ActionMappingHandler();
	}

	/**
	 * 核心入口
	 */
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			// 初始化ActionContext对象
			initActionContext(request, response);
			// 请求映射,找到匹配的Action描述,返回ActionMapping对象
			ActionMapping mapping = requestMapping();
			// 如果mapping没有匹配的Action描述定义来处理请求,则当前请求交给默认servlet处理
			if (mapping.getDefinition() == null) {
				forwardDefaultServlet(request, response);
			} else {
				// 执行请求处理服务
				executeInvoke(mapping);
			}
		} catch (ActionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化contextMap对象,为session,servletContext作用域对象创建代理map
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ActionException
	 */
	private void initActionContext(HttpServletRequest request,
						   HttpServletResponse response) {
		Map<String, Object> contextMap = ActionContext.getContext()
				.getContextMap();
		// 将request对象放入map中
		contextMap.put(REQUEST, request);
		// 将response对象放入map中
		contextMap.put(RESPONSE, response);
		// 构建HttpSession的map代理,放入map中
		contextMap.put(SESSION,
				new ScopeMapContext(SESSION).createScopeProxyMap());
		// 构建ServletContext的map代理,放入map中
		contextMap.put(APPLICATION,
				new ScopeMapContext(APPLICATION).createScopeProxyMap());
	}

	/**
	 * 请求映射,找到对应的Action描述定义
	 * 匹配环节包括请求的uri,请求的方法匹配等
	 */
	private ActionMapping requestMapping() throws ActionException,
			IOException {
		return mappingHandler.handler();
	}

	/**
	 * Action执行处理器链,负责调用目标action的行为,以及参数映射等责任链操作
	 * 并将试图结果封装在ActionMapping中,最终通过
	 * resultResolve方法解析视图结果响应客户端
	 *
	 * @param mapping
	 * @throws IOException
	 * @throws ServletException
	 */
	private void executeInvoke(ActionMapping mapping) throws ServletException,
			IOException {
		new ActionInvokeHandlerChain().doExecute(mapping);
		resultResolve(mapping);
		cleanActionContext();
	}

	/**
	 * 处理视图结果
	 * 
	 * @param mapping 从mapping中取出视图结果对象
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private void resultResolve(ActionMapping mapping) throws IOException,
			ServletException {
		// 处理结果集
		Object returnVal = mapping.getResult();
		if (returnVal != null) {
			ViewResult viewResult = (returnVal instanceof ViewResult) ? (ViewResult) returnVal
					: new DefaultViewResult(returnVal);
			viewResult.execute();
		}
	}

	/**
	 * 清除ActionContext的本地线程副本
     */
	private void cleanActionContext(){
		ActionContext.localContext.remove();
	}

}
