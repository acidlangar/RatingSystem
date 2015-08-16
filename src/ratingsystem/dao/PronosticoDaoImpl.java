package ratingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ratingsystem.connection.SQLConnection;

import ratingsystem.dominio.Partido;

public class PronosticoDaoImpl implements PronosticoDao {
    public PronosticoDaoImpl() {
        super();
    }

    @Override
    public void insertarPronostico(Partido partido, int metodo, int codPronostico, String resultado, int stake) throws SQLException {
        
        Connection con = SQLConnection.connectToID_BT();
        PreparedStatement preparedStatement = null;
        String insertSQL = "INSERT INTO PRONOSTICO "
                                 + "(cd_Metodo, codigo, cd_liga, temporada, fecha_str, equipo_local, resultado, stake) "
                                 + "VALUES"
                                 + "(?,?,?,?,?,?,?,?)";
        
        try {
            preparedStatement = con.prepareStatement(insertSQL);

            preparedStatement.setInt(1, metodo);
            preparedStatement.setInt(2, codPronostico);
            preparedStatement.setInt(3, partido.getLiga());
            preparedStatement.setString(4, partido.getTemporada());
            preparedStatement.setString(5, partido.getFechaStr());
            preparedStatement.setString(6, partido.getEqL());
            preparedStatement.setString(7, resultado);
            preparedStatement.setInt(8, stake);
            
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }
}
