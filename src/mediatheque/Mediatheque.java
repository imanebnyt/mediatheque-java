package mediatheque;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.Abonn�;
import client.Abonn�IntrouvableException;
import documents.Document;
import documents.DocumentDejaEmprunteException;
import documents.DocumentDejaReserveException;
import documents.DocumentIntrouvableException;
import dvds.Dvd;
import ihm.BdConnection;
import serveurs.ServiceEmprunt;
import serveurs.ServiceReservation;
import serveurs.ServiceRetour;

public class Mediatheque {
    private static Mediatheque instance;
	private Map<Integer, List<Document>>catalogue;
    private List<ServiceRetour> serviceRetour;
    private List<ServiceEmprunt> serviceEmprunt;
    private List<ServiceReservation> serviceReservation;
	private List<Document> documents;
	private List<Abonn�> abonnes;

    public Mediatheque() {
    	
        serviceRetour = new ArrayList<>();
        serviceEmprunt = new ArrayList<>();
        serviceReservation = new ArrayList<>();
        documents = BdConnection.r�cup�rerDvds();
        abonnes = BdConnection.r�cup�rerAbonn�s();
        catalogue = new HashMap<>();
    }
    
    public static Mediatheque getInstance() {
        if (instance == null) {
            instance = new Mediatheque();
        }
        return instance;
    }


    public Document findDVDbyNumero(int numero) {
       for (Document doc: documents) {
    	   if(doc.numero() == numero) {
    		   return doc;
    	   }
       }
       return null;
        		
        }
        

    public List<Dvd> findDVDbyTitre(String titre) {
        List<Dvd> result = new ArrayList<>();
        for (List<Document> dvd : catalogue.values()) {
            if (((Document) dvd).getTitre().equals(titre)) {
                result.addAll((Collection<? extends Dvd>) dvd);
            }
        }
        return result;
    }

    public void addRetourService(ServiceRetour service) {
        serviceRetour.add(service);
    }

    public void addEmpruntService(ServiceEmprunt service) {
        serviceEmprunt.add(service);
    }

    public void addReservationService(ServiceReservation service) {
        serviceReservation.add(service);
    }

    public void removeRetourService(ServiceRetour service) {
        serviceRetour.remove(service);
    }

    public void removeEmpruntService(ServiceEmprunt service) {
        serviceEmprunt.remove(service);
    }

    public void removeReservationService(ServiceReservation service) {
    	serviceReservation.remove(service);
    }

    public List<ServiceRetour> getRetourServices() {
        return serviceRetour;
    }

    public List<ServiceEmprunt> getEmpruntServices() {
        return serviceEmprunt;
    }

    public List<ServiceReservation> getReservationServices() {
        return serviceReservation;
    }
    
    public Document chercherDocument(int numero) {
        for (Document doc : documents) {
            if (doc.numero() == numero) {
                return doc;
            }
        }
        return null;
    }
    
    public List<Document> getDocuments() {
        return this.documents;
    }

    
    public Abonn� chercherAbonne(int numeroAbonne) throws Abonn�IntrouvableException {
        for (Abonn� ab : abonnes) {
            if (ab.getNumAbo() == numeroAbonne) {
                return ab;
            }
        }
        throw new Abonn�IntrouvableException("L'abonn� avec le num�ro " + numeroAbonne + " n'existe pas.");
    }

    
    public static int[] extractIds(String message) {
        String[] parts = message.split("");
        int[] ids = new int[2];
        ids[0] = Integer.parseInt(parts[0]); // numero d'abonn�
        ids[1] = Integer.parseInt(parts[1]); // numero de document
        return ids;
    }
    
    // RESERVER UN DOCUMENT

    
    public synchronized String reserver(String message) throws DocumentIntrouvableException, Abonn�IntrouvableException, DocumentDejaEmprunteException, DocumentDejaReserveException {
        int[] ids = extractIds(message);
        int numeroAbonne = ids[0];
        int numeroDocument = ids[1];
        
        // Rechercher l'abonn� correspondant au num�ro d'abonn�
        Abonn� abonne = Mediatheque.getInstance().chercherAbonne(numeroAbonne);
        if (abonne == null) {
            throw new Abonn�IntrouvableException("Abonn� non trouv� pour le num�ro " + numeroAbonne);
        }
        
        // Rechercher le document correspondant au num�ro de document
        Document document = Mediatheque.getInstance().chercherDocument(numeroDocument);
        if (document == null) {
            throw new DocumentIntrouvableException("Document non trouv� pour le num�ro " + numeroDocument);
        }
        
        // V�rifier si le document est d�j� emprunt� ou r�serv�
        if (document.emprunteur() != null) {
            throw new DocumentDejaEmprunteException("Le document est d�j� emprunt� par un autre abonn�.");
        }
        if (document.reserveur() != null) {
            throw new DocumentDejaReserveException("Le document est d�j� r�serv� par un autre abonn�.");
        }
        
        // Effectuer la r�servation
        document.reservationPour(abonne);
        
        return "La r�servation a �t� effectu�e pour l'abonn� " + abonne.getNomAbo() + " et le document " + document.getTitre();
    }
    
    // EMPRUNTER UN DOCUMENT
    
    public synchronized String emprunter(Abonn� ab, Document doc) throws MediathequeException {
        if (ab.emprunteActuellement()) {
            throw new MediathequeException("L'abonn� a d�j� emprunt� un document.");
        }
        if (doc.emprunteur() != null) {
            throw new MediathequeException("Le document est d�j� emprunt�.");
        }
        if (doc.reserveur() != null && !doc.reserveur().equals(ab)) {
            throw new MediathequeException("Le document est r�serv� pour un autre abonn�.");
        }
        ab.emprunter(doc);
        doc.empruntPar(ab);
        return "L'abonn� a emprunt� le dvd ";
    }
    
    public void retournerDvd(Abonn� abonne,int numeroDvd)  {
    	for(Document dvd : abonne.getDvdsEmpruntes()) {
    		if(dvd.numero() == numeroDvd) {
    			abonne.retirerDvd(dvd);
    			dvd.retour();
    			System.out.println("le Dvd" + " " + dvd.getTitre() +" " +"a �t� retourn� par l'abonn�" + " " + abonne.getNomAbo());
    		}
    		   		
    	}
    }
    
    
}

