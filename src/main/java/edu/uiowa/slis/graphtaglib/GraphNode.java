package edu.uiowa.slis.graphtaglib;

public class GraphNode {
	int ID = 0;
	String uri = null;
	String label = null;
	int group = 0;
	double score = 0.0;

	public GraphNode(String uri, String label, int group, double score) {
		super();
		this.uri = uri;
		this.label = label;
		this.group = group;
		this.score = score;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
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

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

}
