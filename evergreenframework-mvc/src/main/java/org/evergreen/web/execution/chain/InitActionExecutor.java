package org.evergreen.web.execution.chain;

import org.evergreen.web.*;
import org.evergreen.web.exception.ActionExceptionContext;
import org.evergreen.web.exception.TargetActionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wangl on 16/4/7.
 */
public class InitActionExecutor implements ExecutionHandler{

    @Override
    public void execute(ActionMapping mapping, ActionInvokeHandlerChain chain) throws ServletException,
            IOException {
        HttpServletRequest request = (HttpServletRequest) ActionContext
                .getContext().get(FrameworkServlet.REQUEST);
        TargetActionHandler handler = (TargetActionHandler) request
                .getServletContext().getAttribute(FrameworkServlet.PLUGIN_HANDLER);
        Object targetAction = handler
                .crateTargetAction(mapping.getDefinition());
        if (targetAction == null) {
            new ActionExceptionContext(new TargetActionException(),
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .throwException();
        }
        mapping.setTarget(targetAction);
        chain.doExecute(mapping);
    }
}
