package edu.uiowa.slis.graphtaglib;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

//import edu.uiowa.slis.graphtaglib.CommunityDetection.LouvainMultilevelRefinementWrapper;
//import edu.uiowa.slis.graphtaglib.CommunityDetection.LouvainWrapper;
//import edu.uiowa.slis.graphtaglib.CommunityDetection.SmartLocalMovingWrapper;

public class GraphColorer extends TagSupport {
    private static final long serialVersionUID = 1L;

    double auxDouble = 0.0;
    String coloringAlg = null;
    Class<?> cls = null;
    Colorer theColorer = null;

    public GraphColorer() {

    }

    // For testing
    public GraphColorer(String alg) throws JspException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	setAlgorithm(alg);
    }

    public int doStartTag() throws JspException {
	Graph theGraph = (Graph) findAncestorWithClass(this, Graph.class);
	theColorer.addColoring(theGraph, coloringAlg + String.valueOf(this.auxDouble));

	
//	
//	SmartLocalMovingWrapper slmWrapper1 = new SmartLocalMovingWrapper(0.5);
//	slmWrapper1.addColoring(theGraph, "slmDetector-0.5");
//	SmartLocalMovingWrapper slmWrapper2 = new SmartLocalMovingWrapper(1.0);
//	slmWrapper2.addColoring(theGraph, "slmDetector-1.0");
//	SmartLocalMovingWrapper slmWrapper3 = new SmartLocalMovingWrapper(1.5);
//	slmWrapper3.addColoring(theGraph, "slmDetector-1.5");
//	SmartLocalMovingWrapper slmWrapper4 = new SmartLocalMovingWrapper(2.0);
//	slmWrapper4.addColoring(theGraph, "slmDetector-2.0");
//	SmartLocalMovingWrapper slmWrapper5 = new SmartLocalMovingWrapper(3.0);
//	slmWrapper5.addColoring(theGraph, "slmDetector-3.0");
//    LouvainWrapper lWrapper1 = new LouvainWrapper(0.5);
//	lWrapper1.addColoring(theGraph, "lDetector-0.5");
//	LouvainWrapper lWrapper2 = new LouvainWrapper(1.0);
//	lWrapper2.addColoring(theGraph, "lDetector-1.0");
//	LouvainWrapper lWrapper3 = new LouvainWrapper(1.5);
//	lWrapper3.addColoring(theGraph, "lDetector-1.5");
//	LouvainWrapper lWrapper4 = new LouvainWrapper(2.0);
//	lWrapper4.addColoring(theGraph, "lDetector-2.0");
//	LouvainWrapper lWrapper5 = new LouvainWrapper(3.0);
//	lWrapper5.addColoring(theGraph, "lDetector-3.0");
//	LouvainMultilevelRefinementWrapper lmrWrapper1 = new LouvainMultilevelRefinementWrapper(0.5);
//	lmrWrapper1.addColoring(theGraph, "lmrDetector-0.5");
//	LouvainMultilevelRefinementWrapper lmrWrapper2 = new LouvainMultilevelRefinementWrapper(1.0);
//	lmrWrapper2.addColoring(theGraph, "lmrDetector-1.0");
//	LouvainMultilevelRefinementWrapper lmrWrapper3 = new LouvainMultilevelRefinementWrapper(1.5);
//	lmrWrapper3.addColoring(theGraph, "lmrDetector-1.5");
//	LouvainMultilevelRefinementWrapper lmrWrapper4 = new LouvainMultilevelRefinementWrapper(2.0);
//	lmrWrapper4.addColoring(theGraph, "lmrDetector-2.0");
//	LouvainMultilevelRefinementWrapper lmrWrapper5 = new LouvainMultilevelRefinementWrapper(3.0);
//	lmrWrapper5.addColoring(theGraph, "lmrDetector-3.0");
	return 1;
    }

    public void setAuxdouble(double r) {
    this.auxDouble = r;
    }
    
    public void setAlgorithm(String alg) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	this.coloringAlg = alg;
	this.cls = Class.forName(alg);
	this.theColorer = (Colorer) cls.getDeclaredConstructor(double.class).newInstance(this.auxDouble);
    }
    


}
