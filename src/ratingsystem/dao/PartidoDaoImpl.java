package ratingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public List<Partido> consultarPartidosByLiga(int cdLiga) throws SQLException {
        
        List<Partido> partidos = new ArrayList<Partido>();
        Connection con = SQLConnection.connectToID_BT();
        PreparedStatement preparedStmt = null;
        
        if (con != null) {            
            
            try {
                preparedStmt = con.prepareStatement("SELECT cd_liga," +
                                                        "temporada," +
                                                        "fecha_str," +
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
                                                        "WHERE cd_liga = ?");
                preparedStmt.setInt(1, cdLiga);
                
                ResultSet rs = preparedStmt.executeQuery();
                if(rs!=null){
                    Partido partido;
                    while (rs.next()){
                        
                        partido = new Partido();
                        partido.setLiga(rs.getInt("cd_liga"));
                        partido.setTemporada(rs.getString("temporada"));
                        partido.setFechaStr(rs.getString("fecha_str"));
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
            finally {
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
                if (con != null) {
                    con.close();
                }
            }
        }
        return partidos;
    }
    
    @Override
    public List<Partido> consultarPartidos() throws SQLException {
        
        List<Partido> partidos = new ArrayList<Partido>();
        Connection con = SQLConnection.connectToID_BT();
        PreparedStatement preparedStmt = null;
        
        if (con != null) {            
            
            try {
                preparedStmt = con.prepareStatement("SELECT cd_liga," +
                                                        "temporada," +
                                                        "fecha_str," +
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
                                                        "FROM PARTIDO");
                
                ResultSet rs = preparedStmt.executeQuery();
                if(rs!=null){
                    Partido partido;
                    while (rs.next()){
                        
                        partido = new Partido();
                        partido.setLiga(rs.getInt("cd_liga"));
                        partido.setTemporada(rs.getString("temporada"));
                        partido.setFechaStr(rs.getString("fecha_str"));
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
            finally {
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
                if (con != null) {
                    con.close();
                }
            }
        }
        return partidos;
    }
    
    @Override
    public List<Partido> consultarPartidosByTemporada(String temporada) throws SQLException {
        
        List<Partido> partidos = new ArrayList<Partido>();
        Connection con = SQLConnection.connectToID_BT();
        PreparedStatement preparedStmt = null;
        
        if (con != null) {            
            
            try {
                preparedStmt = con.prepareStatement("SELECT cd_liga," +
                                                        "temporada," +
                                                        "fecha_str," +
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
                                                        "WHERE temporada like ?");
                preparedStmt.setString(1, temporada+"%");
                
                ResultSet rs = preparedStmt.executeQuery();
                if(rs!=null){
                    Partido partido;
                    while (rs.next()){
                        
                        partido = new Partido();
                        partido.setLiga(rs.getInt("cd_liga"));
                        partido.setTemporada(rs.getString("temporada"));
                        partido.setFechaStr(rs.getString("fecha_str"));
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
            finally {
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
                if (con != null) {
                    con.close();
                }
            }
        }
        return partidos;
    }
}
