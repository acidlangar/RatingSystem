/**
 * 
 */
package ratingsystem.service;

import java.util.ArrayList;
import java.util.List;

import ratingsystem.dao.PronosticoDao;
import ratingsystem.dao.PronosticoDaoImpl;

import ratingsystem.dominio.Partido;

/**
 * @author Edgar.Rodriguez
 *
 */
public class FootyforecastMethod implements RatingMethod {
    
    public static final int PARTIDOS_FORMA = 6;	
    private static final String VICTORIA_LOCAL = "1";
    private static final String VICTORIA_VISITANTE = "2";
    private static final String EMPATE = "X";
    private static final int METODO = 3;
	

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
                int puntosLocal = 0, puntosVisitante = 0;
                float FFPHome = 0, FFPAway = 0, FFPForcast = 0;
                String resultado = "";
                int stake = 0;
                
                for (int count=partidosTemporada.size(); count>0; count--) {
	                    
                    Partido partidoPrevio = partidosTemporada.get(count-1);
                    String resultadoPartidoPrevio = partidoPrevio.getResultado().trim();
                    if (countLocal != PARTIDOS_FORMA && partidoPrevio.getEqL().equals(partido.getEqL())) {
                    	if (VICTORIA_LOCAL.equals(resultadoPartidoPrevio)) {
                    		puntosLocal += 3;
                    	}
                    	else if (EMPATE.equals(resultadoPartidoPrevio)) {
                    		puntosLocal += 1;
                    	}
                        countLocal++;
                    }
                    else if(countLocal != PARTIDOS_FORMA && partidoPrevio.getEqV().equals(partido.getEqL())) {
                    	if (VICTORIA_VISITANTE.equals(resultadoPartidoPrevio)) {
                    		puntosLocal += 3;
                    	}
                    	else if (EMPATE.equals(resultadoPartidoPrevio)) {
                    		puntosLocal += 1;
                    	}
                        countLocal++;
                    }
                    if (countVisitante != PARTIDOS_FORMA && partidoPrevio.getEqL().equals(partido.getEqV())) {
                    	if (VICTORIA_LOCAL.equals(resultadoPartidoPrevio)) {
                    		puntosVisitante += 3;
                    	}
                    	else if (EMPATE.equals(resultadoPartidoPrevio)) {
                    		puntosVisitante += 1;
                    	}
                        countVisitante++;
                    }
                    else if (countVisitante != PARTIDOS_FORMA && partidoPrevio.getEqV().equals(partido.getEqV())) {
                    	if (VICTORIA_VISITANTE.equals(resultadoPartidoPrevio)) {
                    		puntosVisitante += 3;
                    	}
                    	else if (EMPATE.equals(resultadoPartidoPrevio)) {
                    		puntosVisitante += 1;
                    	}
                        countVisitante++;
                    }
                    
                    if (countLocal == PARTIDOS_FORMA && countVisitante == PARTIDOS_FORMA){
                    	FFPHome = ((float) puntosLocal / (PARTIDOS_FORMA * 3)) * 100;
                    	FFPAway = ((float) puntosVisitante / (PARTIDOS_FORMA * 3)) * 100;
                    	FFPForcast = (FFPHome + (100 - FFPAway)) / 2;
                    	                 	
                    	System.out.println("puntosLocal "+puntosLocal+" puntosVisitante "+puntosVisitante+" FFPForcast "+FFPForcast);
                    	
                    	// definir resultado y stake
                    	if (FFPForcast > 51) {
                    		resultado = "1";
                    		stake = (int) (FFPForcast / 10);
                    	}
                    	else if (FFPForcast <=51 && FFPForcast >= 49) {
                    		resultado = "X";
                    		stake = 3;
                    	}
                    	else if (FFPForcast < 49) {
                    		resultado = "2";
                    		stake = ( FFPForcast < 10 ? 10 : (int) ((1/FFPForcast) * 100));
                    	}
                    	
                    	// insertarPronostico
                    	codigoPronostico++;
                        PronosticoDao pronosticoDao = new PronosticoDaoImpl();
                        pronosticoDao.insertarPronostico(partido, 4, codigoPronostico, resultado, stake);
                                            	
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
