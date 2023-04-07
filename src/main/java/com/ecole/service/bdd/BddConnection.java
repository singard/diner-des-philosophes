package com.ecole.service.bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class BddConnection {

    private static final int TAILLE_POOL = 3;
    private static BddConnection bddConnection;
    private Queue<Connection> connectionsDisponibles = new LinkedList<>();
    private List<Connection> connectionsActives = new LinkedList<>();
    private Semaphore connectionSemaphore;
//    // postgres config
//    private final static String JDBC_URL = "jdbc:postgresql://localhost/traces";
//    private final static String USER = "postgres";
//    private final static String PASS = "postgres";

    // h2 config
    // private final static String JDBC_URL = "jdbc:h2:./tracesDB";
    // private final static String JDBC_URL =
    // "jdbc:h2:~/WS-eclipse2303/trace-core/tracesDB";
    // private final static String JDBC_URL = "jdbc:h2:tcp://localhost/./tracesDB";
    // private final static String JDBC_URL = "jdbc:h2:tcp://localhost//tracesDB";
    // private final static String JDBC_URL = "jdbc:h2:~/tracesDB";
    private final static String JDBC_URL = "jdbc:h2:tcp://localhost/~/tracesDB";

    private final static String USER = "root";
    private final static String PASS = "root";

    private BddConnection() {

	for (int i = 0; i < TAILLE_POOL; i++) {
	    try {
		Class.forName("org.h2.Driver");
		connectionsDisponibles.add(DriverManager.getConnection(JDBC_URL, USER, PASS));
	    } catch (SQLException | ClassNotFoundException e) {
		e.printStackTrace();
	    }
	}
	connectionSemaphore = new Semaphore(connectionsDisponibles.size());
    }

    public Connection acquireConnectionDisponible() throws InterruptedException {
	connectionSemaphore.acquire();
	System.out.println("connection acquise, reste " + connectionSemaphore.availablePermits() + " connexions, "
		+ connectionSemaphore.getQueueLength() + " threads bloqué(s).");
	Connection connection = connectionsDisponibles.poll();
	connectionsActives.add(connection);
	return connection;
    }

    public void releaseConnection(Connection connection) {
	connectionsActives.remove(connection);
	connectionsDisponibles.add(connection);
	connectionSemaphore.release();
	System.out.println("connection relâchée, reste " + connectionSemaphore.availablePermits());
    }

    public static BddConnection instance;

    public synchronized static BddConnection getInstance() {
	if (instance == null) {
	    instance = new BddConnection();
	}
	// System.out.println("getInstance est appelé");
	return instance;
    }

}

