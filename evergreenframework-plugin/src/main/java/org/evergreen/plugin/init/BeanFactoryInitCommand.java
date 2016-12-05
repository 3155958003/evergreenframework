package org.evergreen.plugin.init;

import javax.servlet.ServletContext;

import org.evergreen.beans.factory.AnnotationContextFactory;
import org.evergreen.beans.factory.BeanFactory;
import org.evergreen.beans.utils.ScanUtil;
import org.evergreen.plugin.InitCommand;
import org.evergreen.plugin.web.EvergreenActionHandler;

/**
 * 初始化Bean工厂
 * @author lenovo
 *
 */
public class BeanFactoryInitCommand implements InitCommand{
	
	/**
	 * 扫描初始化
	 */
	public void execute(ServletContext servletContext) {
		if (servletContext.getAttribute(EvergreenActionHandler.BEAN_FACTORY) == null) {
			String path = servletContext.getInitParameter("scan");
			path = path == null ? "" : path;
			BeanFactory factory = new AnnotationContextFactory(path);
			servletContext.setAttribute(EvergreenActionHandler.BEAN_FACTORY, factory);
		}	
	}
	

}
