package ihm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import client.Abonn�;
import documents.Document;
import dvds.Dvd;

public class BdConnection {
	
	
	public static List<Abonn�> r�cup�rerAbonn�s() {
		
		  List<Abonn�> abonn�s = new ArrayList<>();
	    // Configuration de la connexion � la base de donn�es
        String url = "jdbc:mysql://localhost:3306/base_mediatheque";
        String user = "root";
        String password = "";
	  
	    Connection connection = null;
	    Statement statement = null;
	    ResultSet resultSet = null;

	    try {
	        // Connexion � la base de donn�es
	        connection = DriverManager.getConnection(url, user, password);

	        // Ex�cution de la requ�te pour r�cup�rer les abonn�s
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery("SELECT * FROM abonn�");

	        // Parcours du r�sultat de la requ�te et ajout des abonn�s � la liste
	        while (resultSet.next()) {
	        	 // R�cup�ration de la valeur de chaque colonne
	        	 int NumAbo = resultSet.getInt("NumAbo");
	             String NomAbo = resultSet.getString("NomAbo");
	             String dateNaiss = resultSet.getString("DateNaiss");
	             //cr�ation d'un nouvel abonn1
	       
	             Abonn� abonn� = new Abonn�(NumAbo,NomAbo,dateNaiss);
	             //ajout de l'abonn� � la liste des abonn�s
	            abonn�s.add(abonn�);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        // Fermeture des ressources
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	            }
	            if (statement != null) {
	                statement.close();
	            }
	            if (connection != null) {
	                connection.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return abonn�s;
	}
	
	
	public static List<Document> r�cup�rerDvds() {
		
		  List<Document> dvds = new ArrayList<>();
		  // Configuration de la connexion � la base de donn�es
        String url = "jdbc:mysql://localhost:3306/base_mediatheque";
        String user = "root";
        String password = "";
		  
	    Connection connection = null;
	    Statement statement = null;
	    ResultSet resultSet = null;

	    try {
	        // Connexion � la base de donn�es
	    	connection = DriverManager.getConnection(url, user, password);

	        // Ex�cution de la requ�te pour r�cup�rer les dvds
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery("SELECT * FROM dvd");

	        // Parcours du r�sultat de la requ�te et ajout des dvds � la liste
	        while (resultSet.next()) {
	        	 // R�cup�ration de la valeur de chaque colonne
                int numDvd = resultSet.getInt("numDvd");
                String titre = resultSet.getString("TitreDvd");
                boolean adulte = resultSet.getBoolean("adulte");
                int numAbo = resultSet.getInt("NumAbonn�");
                //cr�ation d'un nouveau dvd
                Document dvd = new Dvd(numDvd,titre,adulte,numAbo);
                //ajout du dvd � la liste des dvds
	            dvds.add(dvd);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        // Fermeture des ressources
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	            }
	            if (statement != null) {
	                statement.close();
	            }
	            if (connection != null) {
	                connection.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return dvds;
	}
}