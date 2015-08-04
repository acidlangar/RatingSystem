package ratingsystem.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static String DB_URI = "jdbc:mysql://localhost/dbname";
    public static String DB_USER = "test";
    public static String DB_PASS = "password";

    // Singleton instance
    protected static DatabaseConnection _instance;

    protected String _uri;
    protected String _username;
    protected String _password;

    /**
     * Singleton, so no public constructor
     */
    protected DatabaseConnection(String uri, String username, String password) {
        _uri = uri;
        _username = username;
        _password = password;

        /*GenericObjectPool connectionPool = new GenericObjectPool(null);
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
            _uri, _username, _password);
        PoolableConnectionFactory poolableConnectionFactory =
            new PoolableConnectionFactory(connectionFactory, connectionPool,
                                            null, null, false, true);
        PoolingDriver driver = new PoolingDriver();
        driver.registerPool("test", connectionPool);*/
    }

    /**
     * Returns the singleton instance
     */
    public static DatabaseConnection getInstance() {
        if (_instance == null) {
            _instance = new DatabaseConnection(DB_URI, DB_USER, DB_PASS);
        }
        return _instance;
    }

    /**
     * Returns a connection to the database
     */
    public Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:apache:commons:dbcp:test");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }
}