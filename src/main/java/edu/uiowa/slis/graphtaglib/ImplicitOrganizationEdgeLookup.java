package edu.uiowa.slis.graphtaglib;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.NamingException;

public class ImplicitOrganizationEdgeLookup extends ImplicitEdgeLookup {

    public ImplicitOrganizationEdgeLookup(String dataSource) throws NamingException {
	super(dataSource);
    }

    public void populateEdges(Graph theGraph) {
	initDatabaseConnection();
	nodes = theGraph.nodes;
	String uri = null;
	Hashtable<String, String> visitedHash = new Hashtable<String, String>();

	// Get co-authors for every node and add edge to graph
	for (GraphNode source : nodes) {
	    try {
		uri = source.getUri();

		PreparedStatement theStmt = theConnection
			.prepareStatement("select organization,coorganization,count,site,cosite from vivo_aggregated.coorganization where organization=? or coorganization=?");
		theStmt.setString(1, uri);
		theStmt.setString(2, uri);
		ResultSet rs = theStmt.executeQuery();
		while (rs.next()) {
		    String organization = rs.getString(1);
		    String coorganization = rs.getString(2);
		    int count = rs.getInt(3);
		    int site = rs.getInt(4);
		    int cosite = rs.getInt(5);
		    if (site == 0 || cosite == 0)
			continue;
		    if (visitedHash.containsKey(organization + "|" + coorganization))
			continue;
		    logger.trace(organization + "\t" + coorganization + "\t" + count);
		    GraphNode target = uri.equals(organization) ? theGraph.getNode(coorganization) : theGraph.getNode(organization);
		    if (target == null) // edge involving node outside the graph
					// frontier
			continue;
		    visitedHash.put(organization + "|" + coorganization, organization);
		    visitedHash.put(coorganization + "|" + organization, coorganization);
		    logger.trace("\tsource: " + source + "\ttarget: " + target);
		    theGraph.addEdge(new GraphEdge(source, target, count));
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
    }
}
