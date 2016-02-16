package edu.uiowa.slis.graphtaglib;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class EdgeLookup extends TagSupport {
	private static final long serialVersionUID = 1L;

	String edgeLocator = null;
	String dataSource = null;
	Class cls = null;
	// It would be good to check if the Class implements EdgeGenerator...
	EdgePopulator edgeGen = null;

			
	public int doStartTag() throws JspException {
		try {
			this.cls = Class.forName(this.edgeLocator);
			Constructor<?> con = this.cls.getConstructor(String.class);
			this.edgeGen = (EdgePopulator) con.newInstance(this.dataSource);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Graph theGraph = (Graph)findAncestorWithClass(this, Graph.class);
		edgeGen.populateEdges(theGraph);
		return 1;
	}
	
	public void setLocator(String edgeLocator) {
		this.edgeLocator = edgeLocator;
	}
	
	public void setSource(String source) {
		this.dataSource = source;
	}
	
}
