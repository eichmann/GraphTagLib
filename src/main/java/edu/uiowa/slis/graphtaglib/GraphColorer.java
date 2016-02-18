package edu.uiowa.slis.graphtaglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class GraphColorer extends TagSupport {
    private static final long serialVersionUID = 1L;

    String coloringAlg = null;
    Class cls = null;
    Colorer theColorer = null;

    public static void main(String args[]) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, JspException {
	String colorerAlg = "edu.uiowa.slis.graphtaglib.CommunityDetection.SmartLocalMovingWrapper";
	// String colorerAlg =
	// "edu.uiowa.slis.graphtaglib.CommunityDetection.LouvainWrapper";
	// String colorerAlg =
	// "edu.uiowa.slis.graphtaglib.CommunityDetection.LouvainMultilevelRefinementWrapper";

	GraphColorer theGraphColorer = new GraphColorer(colorerAlg);

	// Test graph
	Graph graph = new Graph();
	int[] groups0 = {1, 1};
	GraphNode node0 = new GraphNode("uri0", "name0", groups0, 1, 0, "", 0);
	graph.addNode(node0);
	int[] groups1 = {1, 1};
	GraphNode node1 = new GraphNode("uri1", "name1", groups1, 1, 0, "", 0);
	graph.addNode(node1);
	int[] groups2 = {1, 1};
	GraphNode node2 = new GraphNode("uri2", "name2", groups2, 1, 0, "", 0);
	graph.addNode(node2);
	int[] groups3 = {1, 1};
	GraphNode node3 = new GraphNode("uri3", "name3", groups3, 1, 0, "", 0);
	graph.addNode(node3);
	int[] groups4 = {1, 1};
	GraphNode node4 = new GraphNode("uri4", "name4", groups4, 1, 0, "", 0);
	graph.addNode(node4);
	int[] groups5 = {1, 1};
	GraphNode node5 = new GraphNode("uri5", "name5", groups5, 1, 0, "", 0);
	graph.addNode(node5);
	int[] groups6 = {1, 1};
	GraphNode node6 = new GraphNode("uri6", "name6", groups6, 1, 0, "", 0);
	graph.addNode(node6);
	int[] groups7 = {1, 1};
	GraphNode node7 = new GraphNode("uri7", "name7", groups7, 1, 0, "", 0);
	graph.addNode(node7);
	int[] groups8 = {1, 1};
	GraphNode node8 = new GraphNode("uri8", "name8", groups8, 1, 0, "", 0);
	graph.addNode(node8);
	GraphEdge edge0 = new GraphEdge(node0, node1, 1);
	GraphEdge edge1 = new GraphEdge(node1, node2, 1);
	GraphEdge edge2 = new GraphEdge(node2, node0, 1);
	GraphEdge edge3 = new GraphEdge(node3, node4, 1);
	GraphEdge edge4 = new GraphEdge(node4, node5, 1);
	GraphEdge edge5 = new GraphEdge(node5, node3, 1);
	GraphEdge edge6 = new GraphEdge(node6, node7, 1);
	GraphEdge edge7 = new GraphEdge(node7, node8, 1);
	GraphEdge edge8 = new GraphEdge(node8, node6, 1);
	GraphEdge edge9 = new GraphEdge(node2, node3, 1);
	GraphEdge edge10 = new GraphEdge(node5, node6, 1);
	GraphEdge edge11 = new GraphEdge(node8, node0, 1);
	// GraphEdge edge12 = new GraphEdge(node1, node5, 1);
	graph.addEdge(edge0);
	graph.addEdge(edge1);
	graph.addEdge(edge2);
	graph.addEdge(edge3);
	graph.addEdge(edge4);
	graph.addEdge(edge5);
	graph.addEdge(edge6);
	graph.addEdge(edge7);
	graph.addEdge(edge8);
	graph.addEdge(edge9);
	graph.addEdge(edge10);
	graph.addEdge(edge11);
	// graph.addEdge(edge12);

	// Color Nodes
	theGraphColorer.theColorer.colorGraph(graph);
	for (GraphNode n : graph.nodes) {
	    System.out.println(n.getID() + ": " + n.getGroup()[1]);
	    System.out.println("Site: " + n.getGroup()[0]);
	    // System.out.println(n.getUri() + ": " + n.getLabel());
	}

    }

    public GraphColorer() {

    }

    // For testing
    public GraphColorer(String alg) throws JspException, ClassNotFoundException, InstantiationException, IllegalAccessException {
	setAlgorithm(alg);
    }

    public int doStartTag() throws JspException {
	Graph theGraph = (Graph) findAncestorWithClass(this, Graph.class);
	theColorer.colorGraph(theGraph);
	return 1;
    }

    public void setAlgorithm(String alg) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
	this.coloringAlg = alg;
	this.cls = Class.forName(alg);
	this.theColorer = (Colorer) cls.newInstance();
    }

}
