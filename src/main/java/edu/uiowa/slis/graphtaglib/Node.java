package edu.uiowa.slis.graphtaglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Node extends TagSupport {
    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(Node.class);

    String uri = null;
    String label = null;
    int[] group = new int[2];
    double score = 0.0;
    int auxInt = 0;
    String auxString = null;
    double auxDouble = 0.0;

    public int doStartTag() throws JspException {
	try {
	    Graph theGraph = (Graph) findAncestorWithClass(this, Graph.class);
	    NodeIterator theIterator = (NodeIterator) findAncestorWithClass(this, NodeIterator.class);

	    if (theIterator == null) {
		log.debug("Adding node: " + uri + "\t" + label + "\tgroup: " + group[0] + "\tscore: " + score + "\tauxInt: " + auxInt + "\tauxString: "
			+ auxString);
		theGraph.addNode(new GraphNode(uri, label, group, score, auxInt, auxString, auxDouble));
		return SKIP_BODY;
	    } else {
		uri = theIterator.currentNode.getUri();
		label = theIterator.currentNode.getLabel();
		group = theIterator.currentNode.getGroup();
		score = theIterator.currentNode.getScore();
		auxInt = theIterator.currentNode.getAuxInt();
		auxString = theIterator.currentNode.getAuxString();
		auxDouble = theIterator.currentNode.getAuxDouble();
		return EVAL_BODY_INCLUDE;
	    }
	} catch (Exception e) {
	    log.error("Can't find enclosing Graph or NodeIterator tag ", e);
	    throw new JspTagException("Can't find enclosing Graph or NodeIterator tag ");
	}
    }

    public int doEndTag() throws JspTagException, JspException {
	clearServiceState();
	return super.doEndTag();
    }

    private void clearServiceState() {
	uri = null;
	label = null;
	group = null;
	score = 0.0;
    }

    public String getUri() {
	return uri;
    }

    public void setUri(String uri) {
	this.uri = uri;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public int[] getGroup() {
	return group;
    }

    public void setGroup(int group) {
	this.group[0] = group;
    }

    public double getScore() {
	return score;
    }

    public void setScore(double score) {
	this.score = score;
    }

    public int getAuxInt() {
	return auxInt;
    }

    public void setAuxInt(int auxInt) {
	this.auxInt = auxInt;
    }

    public String getAuxString() {
	return auxString;
    }

    public void setAuxString(String auxString) {
	this.auxString = auxString;
    }

    public double getAuxDouble() {
	return auxDouble;
    }

    public void setAuxDouble(double auxDouble) {
	this.auxDouble = auxDouble;
    }

}
