package ratingsystem.dao;

import java.util.List;

import ratingsystem.dominio.Partido;

public interface PartidoDao {
    
    public List<Partido> consultarPartidos(String criteria);
}
