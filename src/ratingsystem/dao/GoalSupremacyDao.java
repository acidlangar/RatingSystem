package ratingsystem.dao;

import java.sql.SQLException;

import java.util.Map;

import ratingsystem.dominio.GoalSupremacy;

public interface GoalSupremacyDao {
    
    public void insertarGoalSupremacy(int cdLiga, Map<Integer, GoalSupremacy> mapa, int totalPartidos) throws SQLException;
}
