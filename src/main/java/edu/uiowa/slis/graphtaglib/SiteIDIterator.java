package edu.uiowa.slis.graphtaglib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.sql.DataSource;

public class SiteIDIterator extends BodyTagSupport {
    private static final long serialVersionUID = 1L;

    private static final Log logger = LogFactory.getLog(SiteIDIterator.class);
    static Hashtable<Integer, String> siteHash = null;

    SortedSet<Integer> idSet = null;
    Iterator<Integer> idIterator = null;
    int currentID = 0;
    String currentSite = null;

    boolean pruneOrphans = false;
    
    synchronized void init() throws NamingException, SQLException {
	if (siteHash != null)
	    return;
	siteHash = new Hashtable<Integer, String>();
	DataSource theDataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/VIVOTagLib");
	Connection conn = theDataSource.getConnection();
	PreparedStatement stmt = conn.prepareStatement("select id,site from vivo_aggregated.site order by id");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    siteHash.put(rs.getInt(1), rs.getString(2));
	}
	stmt.close();
	conn.close();
    }

    public int doStartTag() throws JspException {
	logger.debug("doStartTag");
	try {
	    init();
	} catch (Exception e) {
	    logger.error("error initializing SiteIDIterator:", e);
	}
	idSet = new TreeSet<Integer>();
	Graph theGraph = (Graph) findAncestorWithClass(this, Graph.class);
	for (GraphNode node: theGraph.nodes) {
	    idSet.add(node.getGroup("site"));
	}
	
	idIterator = idSet.iterator();
	
	if (idIterator.hasNext()) {
	    currentID = idIterator.next();
	    currentSite = siteHash.get(currentID);
	    pageContext.setAttribute("isLastID", !idIterator.hasNext());
	    return EVAL_BODY_INCLUDE;
	}

	return SKIP_BODY;
    }

    public int doAfterBody() throws JspTagException {
	if (idIterator.hasNext()) {
	    currentID = idIterator.next();
	    currentSite = siteHash.get(currentID);
	    pageContext.setAttribute("isLastID", !idIterator.hasNext());
	    return EVAL_BODY_AGAIN;
	}

	return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
	logger.debug("doEndTag");
	clearServiceState();
	return super.doEndTag();
    }

    private void clearServiceState() {
	idSet = null;
	idIterator = null;
	currentID = 0;
	currentSite = null;

	pageContext.removeAttribute("isLastNode");
    }

}
