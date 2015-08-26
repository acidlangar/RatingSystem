package ratingsystem;

import java.sql.SQLException;

import java.util.List;

import ratingsystem.dao.PartidoDao;
import ratingsystem.dao.PartidoDaoImpl;

import ratingsystem.dominio.Partido;

import ratingsystem.service.GoalSupremacyMethod;
import ratingsystem.service.RateformMethod;
import ratingsystem.service.RatingMethod;

public class Principal {
    public Principal() {
        super();
    }

    public static void main(String[] args) {

        PartidoDao partidoDao = new PartidoDaoImpl();
        int cdLiga = 0;
        try {
            for (cdLiga = 1; cdLiga<=1; cdLiga++) {
                List<Partido> partidos = partidoDao.consultarPartidosByTemporada("2014");
    
                /* RatingMethod metodo = new GoalSupremacyMethod();
                metodo.procesarPartidos(partidos); */
                
                RatingMethod metodo = new RateformMethod();
                metodo.procesarPartidos(partidos); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
                
    }

    
}
