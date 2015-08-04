package ratingsystem.dominio;

public class Partido {
    
    private String liga;
     private String temporada;
     private String fecha;
     private String eqL;
     private String eqV;
     private int golesL;
     private int golesV;
     private String resultado;
     private String cuota1;
     private String cuotaX;
     private String cuota2;
     private int diffGL;
     private int diffGV;
     /**
      * @return the liga
      */
     public String getLiga() {
             return liga;
     }
     /**
      * @param liga the liga to set
      */
     public void setLiga(String liga) {
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
     public String getFecha() {
             return fecha;
     }
     /**
      * @param fecha the fecha to set
      */
     public void setFecha(String fecha) {
             this.fecha = fecha;
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
     public String getCuota1() {
             return cuota1;
     }
     /**
      * @param cuota1 the cuota1 to set
      */
     public void setCuota1(String cuota1) {
             this.cuota1 = cuota1;
     }
     /**
      * @return the cuotaX
      */
     public String getCuotaX() {
             return cuotaX;
     }
     /**
      * @param cuotaX the cuotaX to set
      */
     public void setCuotaX(String cuotaX) {
             this.cuotaX = cuotaX;
     }
     /**
      * @return the cuota2
      */
     public String getCuota2() {
             return cuota2;
     }
     /**
      * @param cuota2 the cuota2 to set
      */
     public void setCuota2(String cuota2) {
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
     
     /* (non-Javadoc)
      * @see java.lang.Object#toString()
      */
     @Override
     public String toString() {
             return "" + liga + "\t" + temporada
                             + "\t" + fecha + "\t" + eqL + "\t" + eqV
                             + "\t" + golesL + "\t" + golesV
                             + "\t" + resultado + "\t" + cuota1
                             + "\t" + cuotaX + "\t" + cuota2 + "\t"
                             + diffGL + "\t" + diffGV
                             + "\t" + (diffGL-diffGV);
     }
}
