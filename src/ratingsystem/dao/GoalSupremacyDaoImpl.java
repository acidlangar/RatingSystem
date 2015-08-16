package ratingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.util.Iterator;
import java.util.Map;
import ratingsystem.connection.SQLConnection;
import ratingsystem.dominio.HistoricalResult;

public class GoalSupremacyDaoImpl implements GoalSupremacyDao {
    public GoalSupremacyDaoImpl() {
        super();
    }

    @Override
    public void insertarGoalSupremacy(int cdLiga, Map<Integer, HistoricalResult> mapa, int totalPartidos) throws SQLException {
        
        Connection con = SQLConnection.connectToID_BT();
        PreparedStatement preparedStatement = null;
        String insertSQL = "INSERT INTO GOAL_SUPREMACY "
                                 + "(cd_liga, match_rating, home_wins, draws, away_wins, "
                                 + "po_total_matches, po_home_wins, po_draws, po_away_wins) "
                                 + "VALUES"
                                 + "(?,?,?,?,?,?,?,?,?)";
        
        try {
            preparedStatement = con.prepareStatement(insertSQL);

            DecimalFormat df = new DecimalFormat("###.##");
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            df.setDecimalFormatSymbols(dfs);
            Iterator i = mapa.keySet().iterator();
            
            while(i.hasNext()){
                Integer key = (Integer) i.next();
                HistoricalResult gs = mapa.get(key);
                
                preparedStatement.setInt(1, cdLiga);
                preparedStatement.setInt(2, key);
                preparedStatement.setInt(3, gs.getNumHomeWins());
                preparedStatement.setInt(4, gs.getNumDraws());
                preparedStatement.setInt(5, gs.getNumAwayWins());
                
                String porcentajePartidos = df.format((double)(gs.getNumHomeWins()+gs.getNumDraws()+gs.getNumAwayWins())/totalPartidos*100);
                preparedStatement.setFloat(6, Float.parseFloat(porcentajePartidos));
                
                String porcentajeHome = df.format((double)gs.getNumHomeWins()/(gs.getNumHomeWins()+gs.getNumDraws()+gs.getNumAwayWins())*100);
                preparedStatement.setFloat(7, Float.parseFloat(porcentajeHome));
                
                String porcentajeDraw = df.format((double)gs.getNumDraws()/(gs.getNumHomeWins()+gs.getNumDraws()+gs.getNumAwayWins())*100);
                preparedStatement.setFloat(8, Float.parseFloat(porcentajeDraw));
                
                String porcentajeAway = df.format((double)gs.getNumAwayWins()/(gs.getNumHomeWins()+gs.getNumDraws()+gs.getNumAwayWins())*100);
                preparedStatement.setFloat(9, Float.parseFloat(porcentajeAway));
                
                preparedStatement.executeUpdate();
            }
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
