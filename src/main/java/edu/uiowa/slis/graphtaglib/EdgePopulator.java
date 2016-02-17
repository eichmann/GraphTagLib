package edu.uiowa.slis.graphtaglib;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public abstract class EdgePopulator {

    DataSource theDataSource = null;

    public EdgePopulator() {
    }

    public EdgePopulator(String source) throws NamingException {
	theDataSource = (DataSource) new InitialContext().lookup(source);
    }

    public EdgePopulator(DataSource source) {
	theDataSource = source;
    }

    public abstract void populateEdges(Graph theGraph);
}
