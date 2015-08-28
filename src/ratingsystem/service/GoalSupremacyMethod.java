package ratingsystem.service;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ratingsystem.dao.GoalSupremacyDao;
import ratingsystem.dao.GoalSupremacyDaoImpl;

import ratingsystem.dominio.HistoricalResult;
import ratingsystem.dominio.Partido;


public class GoalSupremacyMethod implements RatingMethod {
    public static final int PARTIDOS_FORMA = 6;
    
    public GoalSupremacyMethod() {
        super();
    }

    @Override
    public void procesarPartidos(List<Partido> partidosSource) {
        try {
            int liga = partidosSource.get(0).getLiga();
            String temporada = partidosSource.get(0).getTemporada();
            List<Partido> partidosTemporada = new ArrayList<Partido>();
            Integer totalPartidos = 0;
                              
             Map<Integer, HistoricalResult> goalSupremacyMapa = new TreeMap<Integer, HistoricalResult>();
             HistoricalResult goalSupremacy;
                 
            for (Partido partido : partidosSource) {
                
                if (partido.getTemporada().equals("2014") || 
                    partido.getTemporada().equals("2014-2015") ||
                    partido.getTemporada().equals("2014/2015")) {
                    continue;
                }
                
                if (partido.getLiga() != liga) {
                    
                    GoalSupremacyDao goalSupremacyDao = new GoalSupremacyDaoImpl();
                    goalSupremacyDao.insertarGoalSupremacy(liga,goalSupremacyMapa,totalPartidos);
                    totalPartidos = 0;
                    goalSupremacyMapa = new TreeMap<Integer, HistoricalResult>();
                    liga = partido.getLiga();
                    
                }
                
                 if (!partido.getTemporada().equals(temporada)) {
                         temporada = partido.getTemporada();
                         partidosTemporada = new ArrayList<Partido>();
                 }
                         
                 int countLocal = 0, countVisitante = 0;
                 int golesLFavor = 0, golesLContra = 0;
                 int golesVFavor = 0, golesVContra = 0;
                 
                 for (int count=partidosTemporada.size(); count>0; count--) {
                         
                         Partido partidoPrevio = partidosTemporada.get(count-1);
                         if (countLocal != PARTIDOS_FORMA && partidoPrevio.getEqL().equals(partido.getEqL())) {
                             golesLFavor += partidoPrevio.getGolesL();
                             golesLContra += partidoPrevio.getGolesV();
                             countLocal++;
                         }
                         else if(countLocal != PARTIDOS_FORMA && partidoPrevio.getEqV().equals(partido.getEqL())) {
                             golesLFavor += partidoPrevio.getGolesV();
                             golesLContra += partidoPrevio.getGolesL();
                             countLocal++;
                         }
                         if (countVisitante != PARTIDOS_FORMA && partidoPrevio.getEqL().equals(partido.getEqV())) {
                             golesVFavor += partidoPrevio.getGolesL();
                             golesVContra += partidoPrevio.getGolesV();
                             countVisitante++;
                         }
                         else if (countVisitante != PARTIDOS_FORMA && partidoPrevio.getEqV().equals(partido.getEqV())) {
                             golesVFavor += partidoPrevio.getGolesV();
                             golesVContra += partidoPrevio.getGolesL();
                             countVisitante++;
                         }
                         
                         if (countLocal == PARTIDOS_FORMA && countVisitante == PARTIDOS_FORMA){
                             partido.setDiffGL(golesLFavor-golesLContra);
                             partido.setDiffGV(golesVFavor-golesVContra);
                             break;
                         }
                 }
                 
                 partidosTemporada.add(partido);
                 System.out.println(partido);
                
                 Integer diferencia = partido.getDiffGL()-partido.getDiffGV();
                
                if (countLocal == PARTIDOS_FORMA && countVisitante == PARTIDOS_FORMA) {
                    totalPartidos++;
                    goalSupremacy = goalSupremacyMapa.get(diferencia);
                    if( goalSupremacy == null ) {
                        goalSupremacy = new HistoricalResult();
                    }
                    
                    switch (partido.getResultado().trim()) {
                        case "1": 
                                goalSupremacy.setNumHomeWins(goalSupremacy.getNumHomeWins()+1);   
                                break;
                        case "X": 
                                goalSupremacy.setNumDraws(goalSupremacy.getNumDraws()+1);   
                                break;
                        case "2": 
                                goalSupremacy.setNumAwayWins(goalSupremacy.getNumAwayWins()+1);    
                                break;
                    }
                    
                    goalSupremacyMapa.put(diferencia, goalSupremacy);
                }                
 
            }
            
            GoalSupremacyDao goalSupremacyDao = new GoalSupremacyDaoImpl();
            goalSupremacyDao.insertarGoalSupremacy(liga,goalSupremacyMapa,totalPartidos);
            
            System.out.println(totalPartidos);
                 
         } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
