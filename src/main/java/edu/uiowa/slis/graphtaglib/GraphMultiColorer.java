package edu.uiowa.slis.graphtaglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import edu.uiowa.slis.graphtaglib.CommunityDetection.LouvainMultilevelRefinementWrapper;
import edu.uiowa.slis.graphtaglib.CommunityDetection.LouvainWrapper;
import edu.uiowa.slis.graphtaglib.CommunityDetection.SmartLocalMovingWrapper;

public class GraphMultiColorer extends TagSupport {
    private static final long serialVersionUID = 1L;

    LouvainWrapper lColorer = new LouvainWrapper();
    LouvainMultilevelRefinementWrapper lmrColorer = new LouvainMultilevelRefinementWrapper();
    SmartLocalMovingWrapper slmColorer = new SmartLocalMovingWrapper();
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public GraphMultiColorer() {}
	
    @SuppressWarnings("unused")
    public int doStartTag() throws JspException {
	Graph theGraph = (Graph) findAncestorWithClass(this, Graph.class);

	return 1;
    }
	
}
