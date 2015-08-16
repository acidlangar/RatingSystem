package ratingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import ratingsystem.connection.SQLConnection;

import ratingsystem.dominio.Partido;

public class RateformDaoImpl implements RateformDao {
    public RateformDaoImpl() {
        super();
    }

    @Override
    public void insertarRateform(Partido partido) throws SQLException {
        
        Connection con = SQLConnection.connectToID_BT();
        PreparedStatement preparedStatement = null;
        String insertSQL = "INSERT INTO RATEFORM "
                                 + "(cd_liga, temporada, fecha_str, equipo_local, dif_rateform) "
                                 + "VALUES"
                                 + "(?,?,?,?,?)";
        
        try {
            preparedStatement = con.prepareStatement(insertSQL);

            DecimalFormat df = new DecimalFormat("###.##");
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            df.setDecimalFormatSymbols(dfs);

            preparedStatement.setInt(1, partido.getLiga());
            preparedStatement.setString(2, partido.getTemporada());
            preparedStatement.setString(3, partido.getFechaStr());
            preparedStatement.setString(4, partido.getEqL());
            preparedStatement.setInt(5, partido.getRankingLocal()-partido.getRankingVisitante());
            
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
