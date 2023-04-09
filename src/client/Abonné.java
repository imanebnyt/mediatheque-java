package client;


import java.util.List;

import documents.Document;
import ihm.BdConnection;
import mediatheque.Mediatheque;
import mediatheque.MediathequeException;

public class Abonn� {
	private static final int MAX_EMPRUNTS = 5;
	private int numAbo;
	private String nomAbo;
	private String dateNaiss;
	
	private List<Document> emprunts;
	
	Mediatheque mediatheque = Mediatheque.getInstance();


	
	public Abonn�(int numAbo, String nomAbo, String dateNaiss) {
		this.numAbo = numAbo;
		this.nomAbo = nomAbo;
	    this.dateNaiss = dateNaiss;
	    this.emprunts = BdConnection.r�cup�rerDvds();
	}

	@Override
    public String toString() {
        return "Abonn�{" +
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
	    	
	        // V�rifier que le document est disponible
			if (doc.emprunteur() != null) {
			
			    return  "Le document est d�j� emprunt�.";
			}
			
			// V�rifier que l'abonn� n'a pas d�j� emprunt� le nombre maximal de documents
			if (emprunts.size() >= MAX_EMPRUNTS) {
			   return "Vous avez atteint le nombre maximal d'emprunts.";
			}
			
			// Ajouter le document � la liste des emprunts de l'abonn�
			emprunts.add(doc);
			
			// Appliquer l'emprunt sur le document
			doc.empruntPar(this);
			
			return "Emprunt r�ussi." + doc;
			
			
	    }
	    
	public List<Document> getDvdsEmpruntes(){
		return emprunts;
	}
	
	public void retirerDvd(Document dvd) {
		emprunts.remove(dvd);
	}
	    
	   
	    
	    
	    
}
