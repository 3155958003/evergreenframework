package org.evergreen.web;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 目标Action调用过程的具体执行链
 * 整个执行链包括:
 * 1.Action实例的构建
 * 2.Action具体方法参数的映射
 * 3.方法参数的验证
 * 4.执行目标方法,并返回视图结果
 * Created by wangl on 16/4/7.
 */
public class ActionInvokeHandlerChain {

    private Iterator<ExecutionHandler> it;

    public ActionInvokeHandlerChain(){
        ServiceLoader<ExecutionHandler> loader = ServiceLoader.load(ExecutionHandler.class);
        it = loader.iterator();
    }

    public void doExecute(ActionMapping mapping) throws ServletException,
            IOException {
        if(it.hasNext()){
            ExecutionHandler handler = it.next();
            handler.execute(mapping, this);
        }
    }
}
