package edu.uiowa.slis.graphtaglib.filters;

import java.lang.reflect.Constructor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.graphtaglib.Graph;

public class EdgeLookup extends TagSupport {
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(EdgeLookup.class);

    String edgeLocator = null;
    String dataSource = null;
    @SuppressWarnings("rawtypes")
    Class cls = null;
    // It would be good to check if the Class implements EdgeGenerator...
    EdgePopulator edgeGen = null;

    @SuppressWarnings("unchecked")
    public int doStartTag() throws JspException {
	log.debug("in doStartTag");
	try {
	    this.cls = Class.forName(this.edgeLocator);
	    Constructor<?> con = this.cls.getConstructor(String.class);
	    this.edgeGen = (EdgePopulator) con.newInstance(dataSource);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	Graph theGraph = (Graph) findAncestorWithClass(this, Graph.class);
	edgeGen.populateEdges(theGraph);
	
	return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
	log.debug("in doEndTag");
	clearServiceState();
	return super.doEndTag();
    }

    private void clearServiceState() {
	edgeLocator = null;
	edgeGen = null;
    }

    public void setMethod(String edgeLocator) {
	this.edgeLocator = edgeLocator;
    }

    public void setSource(String source) {
	this.dataSource = source;
    }
}
