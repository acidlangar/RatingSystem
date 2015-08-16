package ratingsystem.dao;

import java.sql.SQLException;

import ratingsystem.dominio.Partido;

public interface PronosticoDao {
    
    public void insertarPronostico(Partido partido, int metodo, int codPronostico, String resultado, int stake) throws SQLException;
}
