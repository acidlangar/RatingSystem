package ratingsystem.dominio;

import java.util.Calendar;

public class Partido {
    
    private int liga;
     private String temporada;
     private Calendar fecha;
     private String fechaStr;
     private String eqL;
     private String eqV;
     private int golesL;
     private int golesV;
     private String resultado;
     private float cuota1;
     private float cuotaX;
     private float cuota2;
     private int diffGL;
     private int diffGV;
     private int rankingLocal;
     private int rankingVisitante;
     
     /**
      * @return the liga
      */
     public int getLiga() {
             return liga;
     }
     /**
      * @param liga the liga to set
      */
     public void setLiga(int liga) {
             this.liga = liga;
     }
     /**
      * @return the temporada
      */
     public String getTemporada() {
             return temporada;
     }
     /**
      * @param temporada the temporada to set
      */
     public void setTemporada(String temporada) {
             this.temporada = temporada;
     }
     /**
      * @return the fecha
      */
     public Calendar getFecha() {
             return fecha;
     }
     /**
      * @param fecha the fecha to set
      */
     public void setFecha(Calendar fecha) {
             this.fecha = fecha;
     }

    public void setFechaStr(String fechaStr) {
        this.fechaStr = fechaStr;
    }

    public String getFechaStr() {
        return fechaStr;
    }

    /**
     * @return the eqL
     */
     public String getEqL() {
             return eqL;
     }
     /**
      * @param eqL the eqL to set
      */
     public void setEqL(String eqL) {
             this.eqL = eqL;
     }
     /**
      * @return the eqV
      */
     public String getEqV() {
             return eqV;
     }
     /**
      * @param eqV the eqV to set
      */
     public void setEqV(String eqV) {
             this.eqV = eqV;
     }
     /**
      * @return the golesL
      */
     public int getGolesL() {
             return golesL;
     }
     /**
      * @param golesL the golesL to set
      */
     public void setGolesL(int golesL) {
             this.golesL = golesL;
     }
     /**
      * @return the golesV
      */
     public int getGolesV() {
             return golesV;
     }
     /**
      * @param golesV the golesV to set
      */
     public void setGolesV(int golesV) {
             this.golesV = golesV;
     }
     /**
      * @return the resultado
      */
     public String getResultado() {
             return resultado;
     }
     /**
      * @param resultado the resultado to set
      */
     public void setResultado(String resultado) {
             this.resultado = resultado;
     }
     /**
      * @return the cuota1
      */
     public float getCuota1() {
             return cuota1;
     }
     /**
      * @param cuota1 the cuota1 to set
      */
     public void setCuota1(float cuota1) {
             this.cuota1 = cuota1;
     }
     /**
      * @return the cuotaX
      */
     public float getCuotaX() {
             return cuotaX;
     }
     /**
      * @param cuotaX the cuotaX to set
      */
     public void setCuotaX(float cuotaX) {
             this.cuotaX = cuotaX;
     }
     /**
      * @return the cuota2
      */
     public float getCuota2() {
             return cuota2;
     }
     /**
      * @param cuota2 the cuota2 to set
      */
     public void setCuota2(float cuota2) {
             this.cuota2 = cuota2;
     }
     /**
      * @return the diffGL
      */
     public int getDiffGL() {
             return diffGL;
     }
     /**
      * @param diffGL the diffGL to set
      */
     public void setDiffGL(int diffGL) {
             this.diffGL = diffGL;
     }
     /**
      * @return the diffGV
      */
     public int getDiffGV() {
             return diffGV;
     }
     /**
      * @param diffGV the diffGV to set
      */
     public void setDiffGV(int diffGV) {
             this.diffGV = diffGV;
     }


    public void setRankingLocal(int rankingLocal) {
        this.rankingLocal = rankingLocal;
    }

    public int getRankingLocal() {
        return rankingLocal;
    }

    public void setRankingVisitante(int rankingVisitante) {
        this.rankingVisitante = rankingVisitante;
    }

    public int getRankingVisitante() {
        return rankingVisitante;
    }
    /* (non-Javadoc)
      * @see java.lang.Object#toString()
      */
     @Override
     public String toString() {
             return "" + liga + "\t" + temporada
                             + "\t" + fecha.getTime() + "\t" + eqL + "\t" + eqV
                             + "\t" + golesL + "\t" + golesV
                             + "\t" + resultado + "\t" + cuota1
                             + "\t" + cuotaX + "\t" + cuota2 + "\t"
                             + diffGL + "\t" + diffGV
                             + "\t" + (diffGL-diffGV);
     }
}
