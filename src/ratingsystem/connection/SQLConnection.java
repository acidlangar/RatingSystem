package ratingsystem.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnection {
    
    private static final String userName = "sa";
    private static final String password = "acmilanit";
    private static final String url = "jdbc:sqlserver://EDGAR-PC;instanceName=SQLEXPRESS;databasename=ID_BT";
                    
    
    
    public SQLConnection() {
        super();
    }
    
    public static Statement connectToID_BT() {
        Statement stm = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(url, userName, password);
            stm = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return stm;
    }
    
}
