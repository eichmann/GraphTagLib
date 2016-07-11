package edu.uiowa.slis.graphtaglib;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class GraphColorer extends TagSupport {
    private static final long serialVersionUID = 1L;

    double auxDouble = 0.0;
    String coloringAlg = null;
    Class<?> cls = null;
    Colorer theColorer = null;

    public GraphColorer() {

    }

    // For testing
    public GraphColorer(String alg) throws JspException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
	    InvocationTargetException, NoSuchMethodException, SecurityException {
	setAlgorithm(alg);
    }

    public int doStartTag() throws JspException {
	Graph theGraph = (Graph) findAncestorWithClass(this, Graph.class);
	theColorer.addColoring(theGraph, coloringAlg + String.valueOf(this.auxDouble));
	return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspTagException, JspException {
	clearServiceState();
	return super.doEndTag();
    }

    private void clearServiceState() {
	coloringAlg = null;
	cls = null;
	theColorer = null;
	auxDouble = 0.0;
    }

    public void setAuxdouble(double r) {
	this.auxDouble = r;
    }

    public void setAlgorithm(String alg) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
	    InvocationTargetException, NoSuchMethodException, SecurityException {
	this.coloringAlg = alg;
	this.cls = Class.forName(alg);
	this.theColorer = (Colorer) cls.getDeclaredConstructor(double.class).newInstance(this.auxDouble);
    }

}
