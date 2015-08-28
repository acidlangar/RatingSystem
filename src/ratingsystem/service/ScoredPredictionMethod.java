/**
 * 
 */
package ratingsystem.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import ratingsystem.dao.PronosticoDao;
import ratingsystem.dao.PronosticoDaoImpl;

import ratingsystem.dominio.Partido;

/**
 * @author Edgar.Rodriguez
 *
 */
public class ScoredPredictionMethod implements RatingMethod {
    
    public static final int PARTIDOS_FORMA = 5;

    @Override
    public void procesarPartidos(List<Partido> partidosSource) {

        try {
                
            int liga = partidosSource.get(0).getLiga();
            String temporada = partidosSource.get(0).getTemporada();
            int codigoPronostico = 0;
            List<Partido> partidosTemporada = new ArrayList<Partido>();
            
            for (Partido partido : partidosSource) {
		    	
                if (partido.getLiga() != liga || !partido.getTemporada().equals(temporada)) {
                    liga = partido.getLiga();
                    temporada = partido.getTemporada();
                    partidosTemporada = new ArrayList<Partido>();
                }
                        
                int countLocal = 0, countVisitante = 0;
                int HGF = 0, HGA = 0, AGF = 0, AGA = 0;
                int HGP = 0, AGP = 0, GPD = 0;
                String resultado = "";
                int stake = 0;
                
                for (int count=partidosTemporada.size(); count>0; count--) {
	                    
                    Partido partidoPrevio = partidosTemporada.get(count-1);
                    if (countLocal != PARTIDOS_FORMA && partidoPrevio.getEqL().equals(partido.getEqL())) {
                    	HGF += partidoPrevio.getGolesL();
                    	HGA += partidoPrevio.getGolesV();
                        countLocal++;
                    }
                    if (countVisitante != PARTIDOS_FORMA && partidoPrevio.getEqV().equals(partido.getEqV())) {
                    	AGF += partidoPrevio.getGolesV();
                    	AGA += partidoPrevio.getGolesL();
                        countVisitante++;
                    }
                    
                    if (countLocal == PARTIDOS_FORMA && countVisitante == PARTIDOS_FORMA){
                    	
                    	HGP = HGF + AGA;
                    	AGP = HGA + AGF;
                    	GPD = HGP - AGP;
                    	
                    	System.out.println(GPD);
                        
                    	// definir resultado y stake
                        stake = 4;
                        if (GPD >= 2) {
                    		resultado = "1";
                    	}
                    	else if (GPD > -2 && GPD < 2) {
                    		resultado = "X";
                    		stake = 3;
                    	}
                    	else if (GPD <= -2) {
                    		resultado = "2";
                    	}
                    	
                    	// insertarPronostico
                        codigoPronostico++;
                        PronosticoDao pronosticoDao = new PronosticoDaoImpl();
                        pronosticoDao.insertarPronostico(partido, 5, codigoPronostico, resultado, stake);
                    	
                        break;
                    }
                    
                }
                
                partidosTemporada.add(partido);
                System.out.println(partido);
                
            }
		    
        }catch(Exception e) {
            e.printStackTrace();
        }
		
    }

}
