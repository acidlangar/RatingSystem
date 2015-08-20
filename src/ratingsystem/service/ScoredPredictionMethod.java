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

import ratingsystem.dominio.Partido;

/**
 * @author Edgar.Rodriguez
 *
 */
public class ScoredPredictionMethod {
    
	public static final int PARTIDOS_FORMA = 5;	
	private static final int METODO = 4;
	
	public static void main(String[] args) {
		
		ScoredPredictionMethod rf = new ScoredPredictionMethod();
		rf.procesarPartidos(1, "2004");

	}

	private void procesarPartidos(int liga, String temporada) {

		try {
			
			File f = new File("C:\\Users\\edgar.rodriguez\\Documents\\2_PARTIDO.bcp");
			BufferedReader br = new BufferedReader(new FileReader(f));
		    String line;
		    int codigoPronostico = 0;
		    List<Partido> partidosTemporada = new ArrayList<Partido>();
		    
			File f2 = new File("C:\\Users\\edgar.rodriguez\\Documents\\pronostico.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(f2));

		    
		    while ((line = br.readLine()) != null) {
		    	
		    	StringTokenizer st = new StringTokenizer(line, "\t");
		    	
		    	Partido partido = new Partido();
		    	partido.setLiga(Integer.parseInt(st.nextToken()));
		    	partido.setTemporada(st.nextToken());
		    	partido.setFechaStr(st.nextToken());
		    	Calendar fecha = Calendar.getInstance();
		    	st.nextToken();
		    	partido.setFecha(fecha);
		    	partido.setEqL(st.nextToken());
		    	partido.setEqV(st.nextToken());
		    	partido.setGolesL(Integer.parseInt(st.nextToken()));
		    	partido.setGolesV(Integer.parseInt(st.nextToken()));
		    	partido.setResultado(st.nextToken());
		    	partido.setCuota1(Float.parseFloat(st.nextToken()));
		    	partido.setCuotaX(Float.parseFloat(st.nextToken()));
		    	partido.setCuota2(Float.parseFloat(st.nextToken()));
		    	partido.setDiffGL(Integer.parseInt(st.nextToken()));
		    	partido.setDiffGV(Integer.parseInt(st.nextToken()));
		    	partido.setRankingLocal(1000);
		    	partido.setRankingVisitante(1000);
		    	
		    	if (partido.getLiga() != liga) {
		    		break;		    		
		    	}
		    	
		    	if (!partido.getTemporada().equals(temporada)) {
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
                    String resultadoPartidoPrevio = partidoPrevio.getResultado().trim();
                    if (countLocal != PARTIDOS_FORMA && partidoPrevio.getEqL().equals(partido.getEqL())) {
                    	HGF += partido.getGolesL();
                    	HGA += partido.getGolesV();
                        countLocal++;
                    }
                    if (countVisitante != PARTIDOS_FORMA && partidoPrevio.getEqV().equals(partido.getEqV())) {
                    	AGF += partido.getGolesV();
                    	AGA += partido.getGolesL();
                        countVisitante++;
                    }
                    
                    if (countLocal == PARTIDOS_FORMA && countVisitante == PARTIDOS_FORMA){
                    	
                    	HGP = HGF + AGA;
                    	AGP = HGA + AGF;
                    	GPD = HGP - AGP;
                    	
                    	System.out.println(GPD);
                    	
                    	// definir resultado y stake
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
                    	bw.write(METODO + "\t" + 
                    			codigoPronostico + "\t" +
                    			liga + "\t" +
                    			partido.getTemporada() + "\t" +
                    			partido.getFechaStr() + "\t" +
                    			partido.getEqL() + "\t" +
                    			resultado + "\t" +
                    			stake  + "\n");
                    	
                        break;
                    }
                    
	            }
	            
	            partidosTemporada.add(partido);
	            //System.out.println(partido);
		    
		    }
		    br.close();
		    bw.close();
		    
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

}
