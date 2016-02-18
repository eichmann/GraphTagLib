package edu.uiowa.slis.graphtaglib;

import java.util.Vector;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

//import edu.uiowa.icts.RDFUtil.graph.ConceptRecognizer;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.sql.Connection;

public class ImplicitEdgeLookup extends EdgePopulator {
    /**
	 * 
	 */

    private static final long serialVersionUID = 1L;
    static Logger logger = Logger.getLogger(ImplicitEdgeLookup.class);
    private Vector<GraphNode> nodes = null;
    // private DataSource theDataSource = null;
    private Connection theConnection = null;
    static boolean use_ssl = false;

    public ImplicitEdgeLookup(String dataSource) throws NamingException {
	super(dataSource);
    }

    public void initDatabaseConnection() {
	// Database connection parameters
	Properties props = new Properties();
	props.setProperty("user", "eichmann");
	props.setProperty("password", "translational");

	if (use_ssl) {
	    props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
	    props.setProperty("ssl", "true");
	}
	// Connect to database
	try {
	    // theDataSource = (DataSource) new
	    // InitialContext().lookup("java:/comp/env/jdbc/VIVOTagLib");
	    theConnection = theDataSource.getConnection();
	} catch (Exception e) {
	    logger.error("Exception raised in ImplicitEdgeLookup: ", e);
	    logger.error("Trying new data source for ImplicitEdgeLookup");
	    try {
		Class.forName("org.postgresql.Driver");
		theConnection = DriverManager.getConnection("jdbc:postgresql://marengo.info-science.uiowa.edu/loki", props);
	    } catch (Exception e1) {
		logger.error("Exception raised in ImplicitEdgeLookup: ", e1);
	    }
	}
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
			.prepareStatement("select author,coauthor,count,site,cosite from vivo_aggregated.coauthor where author=? or coauthor=?");
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
		    logger.info(author + "\t" + coauthor + "\t" + count);
		    GraphNode target = uri.equals(author) ? theGraph.getNode(coauthor) : theGraph.getNode(author);
		    if (target == null) // edge involving node outside the graph
					// frontier
			continue;
		    visitedHash.put(author + "|" + coauthor, author);
		    visitedHash.put(coauthor + "|" + author, coauthor);
		    logger.info("\tsource: " + source + "\ttarget: " + target);
		    theGraph.addEdge(new GraphEdge(source, target, count));
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
    }
}
