package ratingsystem.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ratingsystem.connection.SQLConnection;

import ratingsystem.dominio.Partido;

public class PartidoDaoImpl implements PartidoDao {
    public PartidoDaoImpl() {
        super();
    }

    @Override
    public List<Partido> consultarPartidos(String criteria) {
        
        List<Partido> partidos = new ArrayList<Partido>();
        Statement statement = SQLConnection.connectToID_BT();
        
        if (statement != null) {            
            
            try {
                ResultSet rs = statement.executeQuery("SELECT cd_liga," +
                                                        "temporada," +
                                                        "fecha," +
                                                        "equipo_local," +
                                                        "equipo_visitante," +
                                                        "goles_local," +
                                                        "goles_visitante," +
                                                        "resultado," +
                                                        "cuota1," +
                                                        "cuotaX," +
                                                        "cuota2," +
                                                        "dif_goles_local," +
                                                        "dif_goles_visitante " +
                                                        "FROM PARTIDO " +
                                                        "WHERE "+criteria);
                if(rs!=null){
                    Partido partido;
                    while (rs.next()){
                        
                        partido = new Partido();
                        partido.setLiga(rs.getInt("cd_liga"));
                        partido.setTemporada(rs.getString("temporada"));
                        Calendar fecha = Calendar.getInstance();
                        fecha.setTime(rs.getTimestamp("fecha"));
                        partido.setFecha(fecha);
                        partido.setEqL(rs.getString("equipo_local"));
                        partido.setEqV(rs.getString("equipo_visitante"));
                        partido.setGolesL(rs.getInt("goles_local"));
                        partido.setGolesV(rs.getInt("goles_visitante"));
                        partido.setResultado(rs.getString("resultado"));
                        partido.setCuota1(rs.getFloat("cuota1"));
                        partido.setCuotaX(rs.getFloat("cuotaX"));
                        partido.setCuota2(rs.getFloat("cuota2"));
                        partido.setDiffGL(rs.getInt("dif_goles_local"));
                        partido.setDiffGV(rs.getInt("dif_goles_visitante"));
                        
                        partidos.add(partido);
                        
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return partidos;
    }
}
