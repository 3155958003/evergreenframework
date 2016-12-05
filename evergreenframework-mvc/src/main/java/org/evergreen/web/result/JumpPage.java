package org.evergreen.web.result;


import org.evergreen.web.ViewResult;

public abstract class JumpPage extends ViewResult {

	final static String HEADER = "X-Requested-With";
	final static String XML_REQUEST = "XMLHttpRequest";
	
	protected String url;

	protected boolean isXmlRequest() {
		return (XML_REQUEST.equals(getRequest().getHeader(HEADER))) ? true
				: false;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	

}
