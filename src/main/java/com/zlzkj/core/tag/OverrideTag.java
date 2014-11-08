package com.zlzkj.core.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * 覆盖模板的block预留区域
 * @author Simon
 *
 */
public class OverrideTag extends BodyTagSupport{
	
	private static final long serialVersionUID = -451901456261139850L;
	
	private static String BLOCK = "__jsp_override__";
	
	private String name;
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int doStartTag() throws JspException {
		return isOverrided() ? SKIP_BODY : EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag() throws JspException {
		if(isOverrided()) {
			return EVAL_PAGE;
		}
		BodyContent b = getBodyContent();
//		System.out.println("Override.content:"+b.getString());
		String varName = BLOCK + name;

		pageContext.getRequest().setAttribute(varName, b.getString());
		return EVAL_PAGE;
	}

	private boolean isOverrided() {
		String varName = BLOCK + name;
		return pageContext.getRequest().getAttribute(varName) != null;
	}
}
