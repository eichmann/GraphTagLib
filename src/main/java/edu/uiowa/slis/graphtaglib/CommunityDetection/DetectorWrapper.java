package edu.uiowa.slis.graphtaglib.CommunityDetection;

import java.io.IOException;
import java.util.Vector;


import edu.uiowa.slis.graphtaglib.Colorer;
import edu.uiowa.slis.graphtaglib.Graph;
import edu.uiowa.slis.graphtaglib.GraphEdge;
import edu.uiowa.slis.graphtaglib.GraphNode;

public abstract class DetectorWrapper implements Colorer{

	Detector theDetector = null;
	Integer modularityFunction = 1;
//	Vector<String> URIs = null;
	
    public static void main(String args[]) throws IOException {
    	SmartLocalMovingWrapper theWrapper = new SmartLocalMovingWrapper();
    	//LouvainWrapper theWrapper = new LouvainWrapper();
    	//LouvainMultilevelRefinementWrapper theWrapper = new LouvainMultilevelRefinementWrapper();
    	
    	// Test graph
    	Graph graph = new Graph();
    	GraphNode node0 = new GraphNode("uri0", "name0", 1, 1, 0, "", 0);
    	graph.addNode(node0);
    	GraphNode node1 = new GraphNode("uri1", "name1", 1, 1, 0, "", 0);
    	graph.addNode(node1);
    	GraphNode node2 = new GraphNode("uri2", "name2", 1, 1, 0, "", 0);
    	graph.addNode(node2);
    	GraphNode node3 = new GraphNode("uri3", "name3", 1, 1, 0, "", 0);
    	graph.addNode(node3);
    	GraphNode node4 = new GraphNode("uri4", "name4", 1, 1, 0, "", 0);
    	graph.addNode(node4);
    	GraphNode node5 = new GraphNode("uri5", "name5", 1, 1, 0, "", 0);
    	graph.addNode(node5);
    	GraphNode node6 = new GraphNode("uri6", "name6", 1, 1, 0, "", 0);
    	graph.addNode(node6);
    	GraphNode node7 = new GraphNode("uri7", "name7", 1, 1, 0, "", 0);
    	graph.addNode(node7);
    	GraphNode node8 = new GraphNode("uri8", "name8", 1, 1, 0, "", 0);
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
		//GraphEdge edge12 = new GraphEdge(node1, node5, 1);
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
    	//graph.addEdge(edge12);


    	//Detect Communities
    	theWrapper.colorGraph(graph);
    	for (GraphNode n : graph.nodes) {
    		System.out.println(n.getID() + ": " + n.getGroup());
    		//System.out.println(n.getUri() + ": " + n.getLabel());
    	}
    	
	}
    
	public void colorGraph(Graph theGraph) {
		Vector<String> URIs = new Vector<String>(theGraph.nodes.size());
    	for (GraphNode n : theGraph.nodes) {
    		URIs.add(n.getID(), n.getUri());
    	}
		int[][] clusters = detect(theGraph);
		setGroups(URIs, clusters, theGraph);
	}
        
    int[][] detect (Graph g) {
    	Network network = GenerateNetwork(g);
    	theDetector.detect(network);
    	return network.getNodesPerCluster();
    }
    
    Network GenerateNetwork(Graph g) {
    	Vector<Integer> node1Vector = new Vector<Integer>();
    	Vector<Integer> node2Vector = new Vector<Integer>();
    	Vector<Double> edgeWeightVector = new Vector<Double>();

    	for (GraphEdge e : g.edges) {
    	    node1Vector.add(e.getSource().getID());
    	    node2Vector.add(e.getTarget().getID());
    	    edgeWeightVector.add(e.getWeight());
    	}
    	return ModularityOptimizer.convertVectors(this.modularityFunction, node1Vector, node2Vector, edgeWeightVector);
    }
    
    void setGroups(Vector<String> URIs, int[][] clusters, Graph g) {
    	for (int i = 0; i < clusters.length; i++) {
    		for (int n : clusters[i]) {
    			String uri = URIs.get(n); 
    			g.getNode(uri).setGroup(i);
    		}
    	}
    }

	//Call algorithm with file
	//Iterate through nodes and set group based on algorithm output
}
