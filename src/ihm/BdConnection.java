package ihm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import client.Abonné;
import documents.Document;
import dvds.Dvd;

public class BdConnection {
	
	
	public static List<Abonné> récupérerAbonnés() {
		
		  List<Abonné> abonnés = new ArrayList<>();
	    // Configuration de la connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/base_mediatheque";
        String user = "root";
        String password = "";
	  
	    Connection connection = null;
	    Statement statement = null;
	    ResultSet resultSet = null;

	    try {
	        // Connexion à la base de données
	        connection = DriverManager.getConnection(url, user, password);

	        // Exécution de la requête pour récupérer les abonnés
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery("SELECT * FROM abonné");

	        // Parcours du résultat de la requête et ajout des abonnés à la liste
	        while (resultSet.next()) {
	        	 // Récupération de la valeur de chaque colonne
	        	 int NumAbo = resultSet.getInt("NumAbo");
	             String NomAbo = resultSet.getString("NomAbo");
	             String dateNaiss = resultSet.getString("DateNaiss");
	             //création d'un nouvel abonn1
	       
	             Abonné abonné = new Abonné(NumAbo,NomAbo,dateNaiss);
	             //ajout de l'abonné à la liste des abonnés
	            abonnés.add(abonné);
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
	    return abonnés;
	}
	
	
	public static List<Document> récupérerDvds() {
		
		  List<Document> dvds = new ArrayList<>();
		  // Configuration de la connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/base_mediatheque";
        String user = "root";
        String password = "";
		  
	    Connection connection = null;
	    Statement statement = null;
	    ResultSet resultSet = null;

	    try {
	        // Connexion à la base de données
	    	connection = DriverManager.getConnection(url, user, password);

	        // Exécution de la requête pour récupérer les dvds
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery("SELECT * FROM dvd");

	        // Parcours du résultat de la requête et ajout des dvds à la liste
	        while (resultSet.next()) {
	        	 // Récupération de la valeur de chaque colonne
                int numDvd = resultSet.getInt("numDvd");
                String titre = resultSet.getString("TitreDvd");
                boolean adulte = resultSet.getBoolean("adulte");
                int numAbo = resultSet.getInt("NumAbonné");
                //création d'un nouveau dvd
                Document dvd = new Dvd(numDvd,titre,adulte,numAbo);
                //ajout du dvd à la liste des dvds
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