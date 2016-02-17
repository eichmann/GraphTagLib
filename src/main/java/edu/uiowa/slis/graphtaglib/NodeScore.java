package edu.uiowa.slis.graphtaglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NodeScore extends TagSupport {
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(NodeScore.class);

    public int doStartTag() throws JspException {
	Graph theGraph = (Graph) findAncestorWithClass(this, Graph.class);
	Node theNode = (Node) findAncestorWithClass(this, Node.class);
	log.trace("");
	try {
	    pageContext.getOut().print((int) Math.round(Math.max(3.0, ((theNode.getScore() / theGraph.maxScore) * 10.0))));
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
