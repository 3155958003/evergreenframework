package org.evergreen.web.execution.chain;

import org.evergreen.web.ActionInvokeHandlerChain;
import org.evergreen.web.ActionMapping;
import org.evergreen.web.ExecutionHandler;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by wangl on 16/4/7.
 */
public class ActionInvokeExecutor implements ExecutionHandler{

    @Override
    public void execute(ActionMapping mapping, ActionInvokeHandlerChain chain) throws ServletException, IOException {
        try {
            Object returnVal = mapping.getDefinition().getMethod()
                    .invoke(mapping.getTarget(), mapping.getParams());
            mapping.setResult(returnVal);
            chain.doExecute(mapping);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
