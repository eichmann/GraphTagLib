package edu.uiowa.slis.graphtaglib.filters;

import java.util.Vector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import edu.uiowa.slis.graphtaglib.Graph;
import edu.uiowa.slis.graphtaglib.GraphEdge;
import edu.uiowa.slis.graphtaglib.GraphNode;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.sql.Connection;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class NodeDistance extends TagSupport {
    private static final long serialVersionUID = 1L;

    static Logger logger = Logger.getLogger(NodeDistance.class);
    Connection conn = null;
    Graph theGraph = null;
    Vector<GraphNode> nodes = null;
    Vector<GraphEdge> edges = null;
    DataSource theDataSource = null;
    int radius = 0;
    String selectedNode = null;
    String dataSource = null;

    public NodeDistance() {
    }

    public NodeDistance(String dataSource) throws NamingException, SQLException {
	theDataSource = (DataSource) new InitialContext().lookup(dataSource);
    }

    public void filterNodes(Graph theGraph, String selectedNode, int radius) {
	logger.debug("in filterNodes");
	nodes = theGraph.nodes;
	edges = theGraph.edges;
	String uri = null;
	Hashtable<String, String> visitedHash = new Hashtable<String, String>();
	String sites = "";

	try {
	    theDataSource = (DataSource) new InitialContext().lookup(dataSource);
	    logger.debug("nodeDistanceFilter with node = " + selectedNode + " radius = " + String.valueOf(radius));

	    conn = theDataSource.getConnection();
	    // Get co-authors for node and add edge to graph only if that
	    // neighbor is within our radius

	    // we have uri -> coauthors.uri -> site
	    // PreparedStatement theStmt =
	    // conn.prepareStatement("select candidate.*,person.uri from vivo_aggregated.person, vivo_aggregated.site_loc as candidate, vivo_aggregated.site_loc as center where center.id=person.id and center.id=? and circle(center.position,(?/111.0)) @> candidate.position");
	    PreparedStatement theStmt = conn
		    .prepareStatement("select candidate.* from vivo_aggregated.site_loc as candidate, vivo_aggregated.site_loc as center where center.id=? and circle(center.position,(?/111.0)) @> candidate.position");

	    theStmt.setInt(1, Integer.parseInt(selectedNode));
	    theStmt.setInt(2, radius);
	    logger.debug("Query:: " + theStmt.toString());
	    ResultSet rs = theStmt.executeQuery();
	    while (rs.next()) {
		logger.trace(rs.getString(1) + ' ' + rs.getString(2) + ' ' + rs.getString(3));
		sites += rs.getString(1) + ",";
	    }

	    logger.debug("Sites within our search radius are " + sites);
	    rs.close();
	    theStmt.close();
	    logger.debug("Before Filter: Number of nodes=" + nodes.size() + " Number of edges=" + edges.size());
	    // keep nodes belonging to site above and remove others
	    theStmt = conn.prepareStatement("select person.uri from vivo_aggregated.person where person.id not in (" + sites.substring(0, sites.length() - 1) + ")");
	    logger.debug("Query:: " + theStmt.toString());
	    rs = theStmt.executeQuery();
	    while (rs.next()) {
		uri = rs.getString(1);
		GraphNode node = theGraph.getNode(uri);
		if (node != null) {
		    theGraph.removeNode(node);
		    try {
			if (theGraph.getNode(uri) != null) {
			    logger.debug("Something is wrong");
			}
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
	    }
	    logger.debug("After Filter: Number of nodes=" + nodes.size() + " Number of edges=" + edges.size());

	    rs.close();
	    theStmt.close();

	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	    logger.debug(selectedNode + ' ' + radius);
	}
	logger.debug("exiting filterNodes");
	if (logger.isDebugEnabled())
	    theGraph.dump();
    }

    public int doStartTag() throws JspException {
	theGraph = (Graph) findAncestorWithClass(this, Graph.class);
	filterNodes(theGraph, selectedNode, radius);
	return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspTagException, JspException {
	clearServiceState();
	return super.doEndTag();
    }

    private void clearServiceState() {
	selectedNode = null;
	radius = 0;
    }

    public void setRadius(int radius) {
	if (radius == 0) {
	    radius = 7000;
	}
	this.radius = radius;
    }

    public String getSelectedNode() {
	return selectedNode;
    }

    public void setSelectedNode(String selectedNode) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
	    InvocationTargetException, NoSuchMethodException, SecurityException {
	if (selectedNode == "") {
	    selectedNode = "1";
	}

	this.selectedNode = selectedNode;
    }

    public void setSource(String source) {
	this.dataSource = source;
    }
}
