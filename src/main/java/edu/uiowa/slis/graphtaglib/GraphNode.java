package edu.uiowa.slis.graphtaglib;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GraphNode {
    private static final Log log = LogFactory.getLog(GraphNode.class);

    int ID = 0;
    String uri = null;
    String label = null;
    HashMap<String, Integer> groups = new HashMap<String,Integer>();
    double score = 0.0;
    int auxInt = 0;
    String auxString = null;
    double auxDouble = 0.0;

    public GraphNode(String uri, String label, int site, double score, int auxInt, String auxString, double auxDouble) {
	super();
	this.uri = uri;
	this.label = label;
	this.addColor("site", site);
	this.score = score;
	this.auxInt = auxInt;
	this.auxString = auxString;
	this.auxDouble = auxDouble;
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
    
    public int getGroup(String group) {
	log.trace("getGroup: " + group + " " + groups);
	return groups.get(group);
    }

    public HashMap<String, Integer> getGroups() {
	return groups;
    }

    public void setGroups(HashMap<String, Integer> groups) {
	this.groups = groups;
    }
    
    public void addColor(String key, int color) {
    this.groups.put(key, color);
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
