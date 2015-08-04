package ratingsystem.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {
    
    private static final String userName = "sa";
    private static final String password = "acmilanit";
    private static final String url = "jdbc:microsoft:sqlserver://EDGAR-PC;instanceName=SQLEXPRESS;databasename=ID_BT";
                    
    
    
    public SQLConnection() {
        super();
    }
    
    public static Connection connectToID_BT() {
        Connection con = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return con;
    }
    
}
