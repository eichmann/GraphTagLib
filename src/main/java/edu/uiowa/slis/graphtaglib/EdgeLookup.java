package edu.uiowa.slis.graphtaglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class EdgeLookup extends TagSupport {
	private static final long serialVersionUID = 1L;

	String edgeLocator = null;
	Class cls = null;
	// It would be good to check if the Class implements EdgeGenerator...
	EdgePopulator edgeGen = null;
			
	public int doStartTag() throws JspException {
		Graph theGraph = (Graph)findAncestorWithClass(this, Graph.class);
		edgeGen.populateEdges(theGraph);
		return 1;
	}
	
	public void setLocator(String edgeLocator) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.edgeLocator = edgeLocator;
		this.cls = Class.forName(edgeLocator);
		this.edgeGen = (EdgePopulator) cls.newInstance();
	}
	
}
