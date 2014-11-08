package com.zlzkj.core.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * 预留模板的block区域
 * @author Simon
 *
 */
public class BlockTag extends TagSupport{
	
	private static final long serialVersionUID = 8135770626380776270L;
	
	private static String BLOCK = "__jsp_override__";
	
	private String name;
	
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return EVAL_BODY_INCLUDE or EVAL_BODY_BUFFERED or SKIP_BODY
	 */
	@Override
	public int doStartTag() throws JspException {
		return getOverriedContent() == null ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}

	/**
	 * @return EVAL_PAGE or SKIP_PAGE
	 */
	@Override
	public int doEndTag() throws JspException {
		String overriedContent = getOverriedContent();
		if(overriedContent == null) {
			return EVAL_PAGE;
		}
		
		try {
			pageContext.getOut().write(overriedContent);
		} catch (IOException e) {
			throw new JspException("write overridedContent occer IOException,block name:"+name,e);
		}
		return EVAL_PAGE;
	}
	
	private String getOverriedContent() {
		String varName = BLOCK + name;
		return (String)pageContext.getRequest().getAttribute(varName);
	}
	
}
