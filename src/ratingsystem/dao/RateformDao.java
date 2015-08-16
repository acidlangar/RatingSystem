package ratingsystem.dao;

import java.sql.SQLException;

import ratingsystem.dominio.Partido;

public interface RateformDao {
    
    public void insertarRateform(Partido partido) throws SQLException;
}
