package edu.uiowa.slis.graphtaglib;

public class GraphEdge {
	GraphNode source = null;
	GraphNode target = null;
	double weight = 0;
	
	public GraphEdge(GraphNode source, GraphNode target, double weight) {
		super();
		this.source = source;
		this.target = target;
		this.weight = weight;
	}

	public GraphNode getSource() {
		return source;
	}
	public void setSource(GraphNode source) {
		this.source = source;
	}
	public GraphNode getTarget() {
		return target;
	}
	public void setTarget(GraphNode target) {
		this.target = target;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
}
