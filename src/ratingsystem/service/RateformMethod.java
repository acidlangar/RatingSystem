package ratingsystem.service;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.TreeMap;

import ratingsystem.dao.GoalSupremacyDao;
import ratingsystem.dao.GoalSupremacyDaoImpl;

import ratingsystem.dao.PronosticoDao;
import ratingsystem.dao.PronosticoDaoImpl;
import ratingsystem.dao.RateformDao;

import ratingsystem.dao.RateformDaoImpl;

import ratingsystem.dominio.HistoricalResult;
import ratingsystem.dominio.Partido;

public class RateformMethod implements RatingMethod {
    
    private static final String VICTORIA_LOCAL = "1";
    private static final String VICTORIA_VISITANTE = "2";
    private static final String EMPATE = "X";

    public RateformMethod() {
        super();
    }

    @Override
    public void procesarPartidos(List<Partido> partidosSource) {
        
        try {
            int liga = partidosSource.get(0).getLiga();
            String temporada = partidosSource.get(0).getTemporada();
            List<Partido> partidosTemporada = new ArrayList<Partido>();
            Integer totalPartidos = 0, codPronostico = 1;
                 
            for (Partido partido : partidosSource) {
                
                partido.setRankingLocal(1000);
                partido.setRankingVisitante(1000);

                 if (partido.getLiga() != liga || !partido.getTemporada().equals(temporada)) {
                         liga = partido.getLiga();
                         temporada = partido.getTemporada();
                         partidosTemporada = new ArrayList<Partido>();
                 }
                
                boolean localFound = false, visitFound = false;
                                        
                for (int count=partidosTemporada.size(); count>0; count--) {
                        
                        Partido partidoPrevio = partidosTemporada.get(count-1); 
                        if (!localFound && partidoPrevio.getEqL().equals(partido.getEqL())) {
                                localFound = true;
                                int rankingLocal = 0;
                                
                                if (VICTORIA_LOCAL.equalsIgnoreCase(partidoPrevio.getResultado().trim())) {
                                        rankingLocal = partidoPrevio.getRankingLocal() + (partidoPrevio.getRankingVisitante()*5)/100; 
                                }
                                else if (VICTORIA_VISITANTE.equalsIgnoreCase(partidoPrevio.getResultado().trim())) {
                                        rankingLocal = partidoPrevio.getRankingLocal() - (partidoPrevio.getRankingLocal()*7)/100;
                                }
                                else if (EMPATE.equalsIgnoreCase(partidoPrevio.getResultado().trim())) {
                                        rankingLocal = partidoPrevio.getRankingLocal() + (
                                                        ((partidoPrevio.getRankingLocal()*7)/100 + (partidoPrevio.getRankingVisitante()*5)/100)/2 - (partidoPrevio.getRankingLocal()*7)/100 );
                                }
                                partido.setRankingLocal(rankingLocal);

                        }
                        else if(!localFound && partidoPrevio.getEqV().equals(partido.getEqL())) {
                                localFound = true;
                                int rankingLocal = 0;
                                
                                if (VICTORIA_LOCAL.equalsIgnoreCase(partidoPrevio.getResultado().trim())) {
                                        rankingLocal = partidoPrevio.getRankingVisitante() - (partidoPrevio.getRankingVisitante()*5)/100; 
                                }
                                else if (VICTORIA_VISITANTE.equalsIgnoreCase(partidoPrevio.getResultado().trim())) {
                                        rankingLocal = partidoPrevio.getRankingVisitante() + (partidoPrevio.getRankingLocal()*7)/100;
                                }
                                else if (EMPATE.equalsIgnoreCase(partidoPrevio.getResultado().trim())) {
                                        rankingLocal = partidoPrevio.getRankingVisitante() + (
                                                        ((partidoPrevio.getRankingLocal()*7)/100 + (partidoPrevio.getRankingVisitante()*5)/100)/2 - (partidoPrevio.getRankingVisitante()*5)/100 );
                                }
                                partido.setRankingLocal(rankingLocal);
                        }
                        
                        if (!visitFound && partidoPrevio.getEqL().equals(partido.getEqV())) {
                                visitFound = true;
                                int rankingVisitante = 0;
                                
                                if (VICTORIA_LOCAL.equalsIgnoreCase(partidoPrevio.getResultado().trim())) {
                                        rankingVisitante = partidoPrevio.getRankingLocal() + (partidoPrevio.getRankingVisitante()*5)/100; 
                                }
                                else if (VICTORIA_VISITANTE.equalsIgnoreCase(partidoPrevio.getResultado().trim())) {
                                        rankingVisitante = partidoPrevio.getRankingLocal() - (partidoPrevio.getRankingLocal()*7)/100;
                                }
                                else if (EMPATE.equalsIgnoreCase(partidoPrevio.getResultado().trim())) {
                                        rankingVisitante = partidoPrevio.getRankingLocal() + (
                                                        ((partidoPrevio.getRankingLocal()*7)/100 + (partidoPrevio.getRankingVisitante()*5)/100)/2 - (partidoPrevio.getRankingLocal()*7)/100 );
                                }
                                partido.setRankingVisitante(rankingVisitante);
                        }
                        else if (!visitFound && partidoPrevio.getEqV().equals(partido.getEqV())) {
                                visitFound = true;

                                int rankingVisitante = 0;
                                
                                if (VICTORIA_LOCAL.equalsIgnoreCase(partidoPrevio.getResultado().trim())) {
                                        rankingVisitante = partidoPrevio.getRankingVisitante() - (partidoPrevio.getRankingVisitante()*5)/100; 
                                }
                                else if (VICTORIA_VISITANTE.equalsIgnoreCase(partidoPrevio.getResultado().trim())) {
                                        rankingVisitante = partidoPrevio.getRankingVisitante() + (partidoPrevio.getRankingLocal()*7)/100;
                                }
                                else if (EMPATE.equalsIgnoreCase(partidoPrevio.getResultado().trim())) {
                                        rankingVisitante = partidoPrevio.getRankingVisitante() + (
                                                        ((partidoPrevio.getRankingLocal()*7)/100 + (partidoPrevio.getRankingVisitante()*5)/100)/2 - (partidoPrevio.getRankingVisitante()*5)/100 );
                                }
                                partido.setRankingVisitante(rankingVisitante);
                        } 
                        
                        if (localFound && visitFound){
                            this.generarPronostico(partido, codPronostico);
                            codPronostico++;
                            break;
                        }
                }
                
                partidosTemporada.add(partido);
                System.out.println(partido);
            
            }
            
            System.out.println(totalPartidos);        
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void generarPronostico(Partido partido, int codPronostico) {
        try {
            int diferencia = partido.getRankingLocal() - partido.getRankingVisitante();
            String resultado = "";
            int stake = 0;
            float cuota = 0;
            
            if (diferencia > -120) {
                resultado = VICTORIA_LOCAL;
                cuota = partido.getCuota1();
            }
            else if (diferencia <= -120 && diferencia >=-240) {
                resultado = EMPATE;
                cuota = partido.getCuotaX();
            }
            else {
                resultado = VICTORIA_VISITANTE;
                cuota = partido.getCuota2();
            }
            
            if (EMPATE.equals(resultado)) {
                stake = 3;
            }
            else {
                 /* if(cuota<1.5) {
                     stake = 10;
                 }
                 else if(cuota>=1.5 && cuota<1.7) {
                     stake = 9;
                 }
                 else if(cuota>=1.7 && cuota<1.9) {
                     stake = 8;
                 }
                 else if(cuota>=1.9 && cuota<2.1) {
                     stake = 7;
                 }
                 else if (cuota>=2.1 && cuota<2.3) {
                     stake = 6;
                 }
                 else if(cuota>=2.3 && cuota<2.5) {
                     stake = 5;
                 }
                 else if(cuota>=2.5 && cuota<2.7) {
                     stake = 4;
                 }
                 else if (cuota>=2.7 && cuota<2.9) {
                     stake = 3;
                 }
                 else if(cuota>=2.9 && cuota<3.1) {
                     stake = 2;
                 }
                 else if(cuota>=3.1) {
                     stake = 1;
                 } */
                stake = 4;
            }
            
            PronosticoDao pronosticoDao = new PronosticoDaoImpl();
            pronosticoDao.insertarPronostico(partido, 2, codPronostico, resultado, stake);
                 
         } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
