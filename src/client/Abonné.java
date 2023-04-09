package client;


import java.util.List;

import documents.Document;
import ihm.BdConnection;
import mediatheque.Mediatheque;
import mediatheque.MediathequeException;

public class Abonné {
	private static final int MAX_EMPRUNTS = 5;
	private int numAbo;
	private String nomAbo;
	private String dateNaiss;
	
	private List<Document> emprunts;
	
	Mediatheque mediatheque = Mediatheque.getInstance();


	
	public Abonné(int numAbo, String nomAbo, String dateNaiss) {
		this.numAbo = numAbo;
		this.nomAbo = nomAbo;
	    this.dateNaiss = dateNaiss;
	    this.emprunts = BdConnection.récupérerDvds();
	}

	@Override
    public String toString() {
        return "Abonné{" +
                "numAbo=" + numAbo +
                ", nomAbo='" + nomAbo + '\'' +
                ", dateNaiss='" + dateNaiss + '\'' +
                '}';
    }
	
	 public int getNumAbo() {
	        return numAbo;
	    }

	    public String getNomAbo() {
	        return nomAbo;
	    }

	    public String getDateNaiss() {
	        return dateNaiss;
	    }


	  
	    public boolean emprunteActuellement() {
	        for (Document doc : mediatheque.getDocuments()) {
	            if (doc.emprunteur() != null && doc.emprunteur().equals(this)) {
	                return true;
	            }
	        }
	        return false;
	    }



	    public String emprunter(Document doc) throws MediathequeException {
	    	
	        // Vérifier que le document est disponible
			if (doc.emprunteur() != null) {
			
			    return  "Le document est déjà emprunté.";
			}
			
			// Vérifier que l'abonné n'a pas déjà emprunté le nombre maximal de documents
			if (emprunts.size() >= MAX_EMPRUNTS) {
			   return "Vous avez atteint le nombre maximal d'emprunts.";
			}
			
			// Ajouter le document à la liste des emprunts de l'abonné
			emprunts.add(doc);
			
			// Appliquer l'emprunt sur le document
			doc.empruntPar(this);
			
			return "Emprunt réussi." + doc;
			
			
	    }
	    
	public List<Document> getDvdsEmpruntes(){
		return emprunts;
	}
	
	public void retirerDvd(Document dvd) {
		emprunts.remove(dvd);
	}
	    
	   
	    
	    
	    
}
