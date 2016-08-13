package edu.uiowa.slis.graphtaglib;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class Graph extends BodyTagSupport {
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(Graph.class);
    Hashtable<String, GraphNode> nodeHash = null;
    Hashtable<String, Integer> edgeHash = null;
    public Vector<GraphNode> nodes = null;
    public Vector<GraphEdge> edges = null;
    double maxScore = 0.0;

    public Graph() {
	nodes = new Vector<GraphNode>();
	edges = new Vector<GraphEdge>();

	if (edgeHash == null) {
	    edgeHash = new Hashtable<String, Integer>();
	    nodeHash = new Hashtable<String, GraphNode>();

	}
    }

    public int doStartTag() throws JspException {
	nodes = new Vector<GraphNode>();
	edges = new Vector<GraphEdge>();

	log.info("in doStartTag");
	if (edgeHash == null) {
	    edgeHash = new Hashtable<String, Integer>();
	    nodeHash = new Hashtable<String, GraphNode>();

	}
	return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspTagException, JspException {
	log.info("in doEndTag");
	clearServiceState();
	return super.doEndTag();
    }

    private void clearServiceState() {
	nodeHash = null;
	edgeHash = null;
	nodes = null;
	edges = null;
	maxScore = 0.0;
    }

    public void addNode(GraphNode node) {
	GraphNode clone = nodeHash.get(node.getUri());
	
	if (clone != null) {
	    log.error("node already in graph with URI " + node.getUri());
	    clone.setScore(Math.max(clone.getScore(), node.getScore()));
	    maxScore = Math.max(clone.getScore(), maxScore);
	    return;
	}
	node.setID(nodes.size());
	nodes.add(node);
	maxScore = Math.max(node.getScore(), maxScore);
	nodeHash.put(node.getUri(), node);
    }
    
    public void removeNode(GraphNode node) {
	if (!nodeHash.containsKey(node.getUri())) {
	    log.error("node not in graph with URI " + node.getUri());
	    return;
	}
	
	// removing the node and its edges from the vectors and the hash
	log.debug("edge hash size: " + edgeHash.size() + " vector size: " + edges.size());
	nodes.remove(node);
	nodeHash.remove(node.getUri());
	
	// Remove all the edges of this node
	for (int x = edges.size() - 1; x >= 0; x--) {
	    GraphEdge edge = edges.elementAt(x);
	    if (edge.getSource() == node | edge.getTarget() == node) {
		log.trace("remove edge " + edge.getSource().getLabel() + " " + edge.getTarget().getUri());
		edges.remove(edge);
		edgeHash.remove(edge.getSource().getUri() + " " + edge.getTarget().getUri());
		edgeHash.remove(edge.getTarget().getUri() + " " + edge.getSource().getUri());
	    }
	}
	
	resetNodeIDs();
	return;
    }

    public GraphNode getNode(String uri) {
	return nodeHash.get(uri);
    }

    public void addEdge(GraphEdge edge) {
	edgeHash.put(edge.getSource().getUri() + " " + edge.getTarget().getUri(), 1);
	edges.add(edge);
    }
    
    public void removeEdge(GraphEdge edge) {
	edges.remove(edge);
	edgeHash.remove(edge);
    }
    
    public void addColoring(String label, HashMap<String, Integer> colors) {
	for (String uri : colors.keySet()) {
	    if (nodeHash.containsKey(uri)) {
		nodeHash.get(uri).addColor(label, colors.get(uri));
	    }
	}
    }

    void pruneOrphans() {
	Vector<GraphNode> newNodes = new Vector<GraphNode>();
	log.info("int pruneOrphans - " + nodes.size() + " nodes");

	for (int i = 0; i < nodes.size(); i++) {
	    String sourceURI = nodes.elementAt(i).getUri();
	    if (i <= 10) {
		newNodes.add(nodes.elementAt(i));
		continue;
	    }
	    for (int j = 0; j < nodes.size(); j++) {
		String targetURI = nodes.elementAt(j).getUri();
		if (edgeHash.containsKey(sourceURI + " " + targetURI) || edgeHash.containsKey(targetURI + " " + sourceURI)) {
		    newNodes.add(nodes.elementAt(i));
		    break;
		}
	    }
	}

	nodes = newNodes;
	resetNodeIDs();
    }

    void resetNodeIDs() {
	maxScore = 0;
	for (int i = 0; i < nodes.size(); i++) {
	    log.debug(nodes.elementAt(i).getUri() + " " + i);
	    nodes.elementAt(i).setID(i);
	    maxScore = Math.max(nodes.elementAt(i).getScore(), maxScore);
	}
    }
    
    public void dump() {
	log.info("current graph nodes:");
	for (GraphNode node : nodes) {
	    log.info("\tsite: " + node.getGroup("site") + " uri: " + node.getUri());
	}
    }
}
