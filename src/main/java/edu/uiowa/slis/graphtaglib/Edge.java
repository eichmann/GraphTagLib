package edu.uiowa.slis.graphtaglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Edge extends TagSupport {
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(Edge.class);

    GraphNode sourceNode = null;
    GraphNode targetNode = null;
    GraphEdge currentEdge = null;
    String source = null;
    String target = null;
    double weight = 0.0;

    public int doStartTag() throws JspException {
	Graph theGraph = (Graph) findAncestorWithClass(this, Graph.class);
	EdgeIterator theIterator = (EdgeIterator) findAncestorWithClass(this, EdgeIterator.class);

	if (theIterator == null) {
	    log.debug("Adding edge source: " + source + "\ttarget: " + target + "\tweight: " + weight);
	    theGraph.addEdge(new GraphEdge(theGraph.getNode(source), theGraph.getNode(target), weight));
	    return SKIP_BODY;
	} else {
	    currentEdge = theIterator.currentEdge;
	    sourceNode = theIterator.currentEdge.getSource();
	    targetNode = theIterator.currentEdge.getTarget();
	    weight = theIterator.currentEdge.getWeight();
	    // return EVAL_BODY_INCLUDE;
	}

	// currentEdge = theIterator.currentEdge;
	log.trace("");
	return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspTagException, JspException {
	clearServiceState();
	return super.doEndTag();
    }

    private void clearServiceState() {
	sourceNode = null;
	targetNode = null;
	currentEdge = null;
	source = null;
	target = null;
	weight = 0;
    }

    public String getSource() {
	return source;
    }

    public void setSource(String source) {
	this.source = source;
    }

    public String getTarget() {
	return target;
    }

    public void setTarget(String target) {
	this.target = target;
    }

    public double getWeight() {
	return weight;
    }

    public void setWeight(double weight) {
	this.weight = weight;
    }
}
