package org.evergreen.web.execution.chain;

import org.evergreen.web.*;
import org.evergreen.web.params.validate.HibernateBeanValidate;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

/**
 * Created by wangl on 16/4/7.
 */
public class ParamsValidExecutor implements ExecutionHandler{

    @Override
    public void execute(ActionMapping mapping, ActionInvokeHandlerChain chain) throws ServletException, IOException {
        ParamsValidate validateUtil = new HibernateBeanValidate();
        Map<String, String> errors = validateUtil.validate(mapping);
        if (!errors.isEmpty())
            ActionContext.getContext().put(ActionContext.ERRORS, errors);
        chain.doExecute(mapping);
    }
}
