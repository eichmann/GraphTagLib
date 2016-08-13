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

public class ImplicitEdgeLookup extends EdgePopulator {
    static Logger logger = Logger.getLogger(ImplicitEdgeLookup.class);
    Connection conn = null;
    Vector<GraphNode> nodes = null;
    DataSource theDataSource = null;
    
    public ImplicitEdgeLookup(String theDataSourceString) throws NamingException, SQLException {
	theDataSource = (DataSource) new InitialContext().lookup(theDataSourceString);
    }

    public void populateEdges(Graph theGraph) {
	logger.debug("in populatedEdges");
	nodes = theGraph.nodes;
	String uri = null;
	Hashtable<String, String> visitedHash = new Hashtable<String, String>();

	try {
	    conn = theDataSource.getConnection();
		
	    // Get co-authors for every node and add edge to graph
	    for (GraphNode source : nodes) {
		uri = source.getUri();
		logger.debug("implicit URI: " + uri);
		PreparedStatement theStmt = conn.prepareStatement("select author,coauthor,count,site,cosite from vivo_aggregated.coauthor where author=? or coauthor=?");
		theStmt.setString(1, uri);
		theStmt.setString(2, uri);
		ResultSet rs = theStmt.executeQuery();
		while (rs.next()) {
		    String author = rs.getString(1);
		    String coauthor = rs.getString(2);
		    int count = rs.getInt(3);
		    int site = rs.getInt(4);
		    int cosite = rs.getInt(5);
		    if (site == 0 || cosite == 0)
			continue;
		    if (visitedHash.containsKey(author + "|" + coauthor))
			continue;
		    logger.trace(author + "\t" + coauthor + "\t" + count);
		    GraphNode target = uri.equals(author) ? theGraph.getNode(coauthor) : theGraph.getNode(author);
		    if (target == null) // edge involving node outside the graph
					// frontier
			continue;
		    visitedHash.put(author + "|" + coauthor, author);
		    visitedHash.put(coauthor + "|" + author, coauthor);
		    logger.trace("\tsource: " + source + "\ttarget: " + target);
		    theGraph.addEdge(new GraphEdge(source, target, count));
		}
		rs.close();
		theStmt.close();
	    }
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	logger.debug("exiting populatedEdges");
    }
}
