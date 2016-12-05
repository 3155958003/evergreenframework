package org.evergreen.web;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by wangl on 16/4/7.
 */
public interface ExecutionHandler {

    public void execute(ActionMapping mapping, ActionInvokeHandlerChain chain) throws ServletException,
            IOException;


}
