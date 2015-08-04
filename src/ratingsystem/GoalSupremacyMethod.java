package ratingsystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import ratingsystem.dominio.Partido;

public class GoalSupremacyMethod implements RatingMethod {
    public static final int PARTIDOS_FORMA = 6;
    
    public GoalSupremacyMethod() {
        super();
    }

    @Override
    public void procesarPartidos() {
        try {
            String liga = "1";
            String temporada = "2004";
            File fileOrigen = new File("C:\\Users\\edgar\\Downloads\\datos\\partidos.txt");
            BufferedReader br = new BufferedReader(new FileReader(fileOrigen));
            String line;
            List<Partido> partidosTemporada = new ArrayList<Partido>();
            List<Partido> partidos = new ArrayList<Partido>();
            Integer totalPartidos = 0;
                              
            File fileDestinoDiferencia = new File("C:\\Users\\edgar\\Downloads\\datos\\partidosGoles.txt");
            BufferedWriter bwDiferencia = new BufferedWriter(new FileWriter(fileDestinoDiferencia));
            
             File fileDestinoGoalSupremacy = new File("C:\\Users\\edgar\\Downloads\\datos\\goalSupremacy.txt");
             BufferedWriter bwGoalSupremacy = new BufferedWriter(new FileWriter(fileDestinoGoalSupremacy));

             Map<Integer, ratingsystem.dominio.GoalSupremacy> goalSupremacyMapa = new TreeMap<Integer, ratingsystem.dominio.GoalSupremacy>();
             ratingsystem.dominio.GoalSupremacy goalSupremacy;
                 
            while ((line = br.readLine()) != null) {                
                
                StringTokenizer st = new StringTokenizer(line, "\t");
                         
                Partido partido = new Partido();
                partido.setLiga(st.nextToken());
                partido.setTemporada(st.nextToken());
                partido.setFecha(st.nextToken());
                partido.setEqL(st.nextToken());
                partido.setEqV(st.nextToken());
                partido.setGolesL(Integer.parseInt(st.nextToken()));
                partido.setGolesV(Integer.parseInt(st.nextToken()));
                partido.setResultado(st.nextToken());
                partido.setCuota1(st.nextToken());
                partido.setCuotaX(st.nextToken());
                partido.setCuota2(st.nextToken());
                partido.setDiffGL(Integer.parseInt(st.nextToken()));
                partido.setDiffGV(Integer.parseInt(st.nextToken()));
                         
                 if (!partido.getLiga().equals(liga) || !partido.getTemporada().equals(temporada)) {
                         liga = partido.getLiga();
                         temporada = partido.getTemporada();
                         for (Partido partidoL:partidosTemporada) {
                                 bwDiferencia.write(partidoL.toString()+ "\n");
                         }
                         partidos.addAll(partidosTemporada);
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
                        goalSupremacy = new ratingsystem.dominio.GoalSupremacy();
                    }
                    
                    switch (partido.getResultado()) {
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
            
            for (Partido partidoL:partidosTemporada) {
                bwDiferencia.write(partidoL.toString()+ "\n");
             }
            
             DecimalFormat df = new DecimalFormat("###.##");
             Iterator i = goalSupremacyMapa.keySet().iterator();
             while(i.hasNext()){
                 Integer key = (Integer) i.next();
                 ratingsystem.dominio.GoalSupremacy gs = goalSupremacyMapa.get(key);
                 bwGoalSupremacy.write(key + "\t" + gs.toString()+ 
                                       "\t" + df.format((double)(gs.getNumHomeWins()+gs.getNumDraws()+gs.getNumAwayWins())/totalPartidos*100) +
                                       "\t" + df.format((double)gs.getNumHomeWins()/(gs.getNumHomeWins()+gs.getNumDraws()+gs.getNumAwayWins())*100) +
                                       "\t" + df.format((double)gs.getNumDraws()/(gs.getNumHomeWins()+gs.getNumDraws()+gs.getNumAwayWins())*100) +
                                       "\t" + df.format((double)gs.getNumAwayWins()/(gs.getNumHomeWins()+gs.getNumDraws()+gs.getNumAwayWins())*100)
                                       + "\n");
              }
              System.out.println(totalPartidos);           
            br.close();
            bwDiferencia.close();
            bwGoalSupremacy.close();
                 
         }catch(IOException e) {
                 e.printStackTrace();
         }
    }
}
