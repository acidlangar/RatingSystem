package ratingsystem.dao;

import java.sql.SQLException;

import java.util.Map;

import ratingsystem.dominio.HistoricalResult;

public interface GoalSupremacyDao {
    
    public void insertarGoalSupremacy(int cdLiga, Map<Integer, HistoricalResult> mapa, int totalPartidos) throws SQLException;
}
