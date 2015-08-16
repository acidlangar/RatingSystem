package ratingsystem.service;

import java.util.List;

import ratingsystem.dominio.Partido;

public interface RatingMethod {
    
    public void procesarPartidos(List<Partido> partidos);
}
