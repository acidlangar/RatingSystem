package ratingsystem.dao;

import java.sql.SQLException;

import java.util.List;

import ratingsystem.dominio.Partido;

public interface PartidoDao {
    
    public List<Partido> consultarPartidosByLiga(int cdLiga) throws SQLException;
}
