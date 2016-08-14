package edu.uiowa.slis.graphtaglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class SiteID extends TagSupport {
    private static final long serialVersionUID = 1L;

    public int doStartTag() throws JspException {
	SiteIDIterator theIterator = (SiteIDIterator) findAncestorWithClass(this, SiteIDIterator.class);
	try {
	    pageContext.getOut().print(theIterator.currentID);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
	clearServiceState();
	return super.doEndTag();
    }

    private void clearServiceState() {
    }
}
