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
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import ratingsystem.dominio.Partido;

/**
 * @author Edgar.Rodriguez
 *
 */
public class FootyforecastMethod {
    
	public static final int PARTIDOS_FORMA = 6;	
	private static final String VICTORIA_LOCAL = "1";
	private static final String VICTORIA_VISITANTE = "2";
	private static final String EMPATE = "X";
	private static final int METODO = 3;
	
	public static void main(String[] args) {
		
		FootyforecastMethod rf = new FootyforecastMethod();
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
