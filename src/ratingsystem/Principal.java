package ratingsystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import java.util.TreeMap;
import java.util.TreeSet;

import ratingsystem.connection.SQLConnection;

import ratingsystem.dao.PartidoDao;

import ratingsystem.dao.PartidoDaoImpl;

import ratingsystem.dominio.GoalSupremacy;
import ratingsystem.dominio.Partido;

public class Principal {
    public Principal() {
        super();
    }

    public static void main(String[] args) {

        PartidoDao partidoDao = new PartidoDaoImpl();
        int cdLiga = 9;
        try {
            List<Partido> partidos = partidoDao.consultarPartidosByLiga(cdLiga);

            RatingMethod metodo = new GoalSupremacyMethod();
            metodo.procesarPartidos(partidos);        

        } catch (SQLException e) {
            e.printStackTrace();
        }
                
    }

    
}
