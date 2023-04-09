package mediatheque;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.Abonné;
import client.AbonnéIntrouvableException;
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
	private List<Abonné> abonnes;

    public Mediatheque() {
    	
        serviceRetour = new ArrayList<>();
        serviceEmprunt = new ArrayList<>();
        serviceReservation = new ArrayList<>();
        documents = BdConnection.récupérerDvds();
        abonnes = BdConnection.récupérerAbonnés();
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

    
    public Abonné chercherAbonne(int numeroAbonne) throws AbonnéIntrouvableException {
        for (Abonné ab : abonnes) {
            if (ab.getNumAbo() == numeroAbonne) {
                return ab;
            }
        }
        throw new AbonnéIntrouvableException("L'abonné avec le numéro " + numeroAbonne + " n'existe pas.");
    }

    
    public static int[] extractIds(String message) {
        String[] parts = message.split("");
        int[] ids = new int[2];
        ids[0] = Integer.parseInt(parts[0]); // numero d'abonné
        ids[1] = Integer.parseInt(parts[1]); // numero de document
        return ids;
    }
    
    // RESERVER UN DOCUMENT

    
    public synchronized String reserver(String message) throws DocumentIntrouvableException, AbonnéIntrouvableException, DocumentDejaEmprunteException, DocumentDejaReserveException {
        int[] ids = extractIds(message);
        int numeroAbonne = ids[0];
        int numeroDocument = ids[1];
        
        // Rechercher l'abonné correspondant au numéro d'abonné
        Abonné abonne = Mediatheque.getInstance().chercherAbonne(numeroAbonne);
        if (abonne == null) {
            throw new AbonnéIntrouvableException("Abonné non trouvé pour le numéro " + numeroAbonne);
        }
        
        // Rechercher le document correspondant au numéro de document
        Document document = Mediatheque.getInstance().chercherDocument(numeroDocument);
        if (document == null) {
            throw new DocumentIntrouvableException("Document non trouvé pour le numéro " + numeroDocument);
        }
        
        // Vérifier si le document est déjà emprunté ou réservé
        if (document.emprunteur() != null) {
            throw new DocumentDejaEmprunteException("Le document est déjà emprunté par un autre abonné.");
        }
        if (document.reserveur() != null) {
            throw new DocumentDejaReserveException("Le document est déjà réservé par un autre abonné.");
        }
        
        // Effectuer la réservation
        document.reservationPour(abonne);
        
        return "La réservation a été effectuée pour l'abonné " + abonne.getNomAbo() + " et le document " + document.getTitre();
    }
    
    // EMPRUNTER UN DOCUMENT
    
    public synchronized String emprunter(Abonné ab, Document doc) throws MediathequeException {
        if (ab.emprunteActuellement()) {
            throw new MediathequeException("L'abonné a déjà emprunté un document.");
        }
        if (doc.emprunteur() != null) {
            throw new MediathequeException("Le document est déjà emprunté.");
        }
        if (doc.reserveur() != null && !doc.reserveur().equals(ab)) {
            throw new MediathequeException("Le document est réservé pour un autre abonné.");
        }
        ab.emprunter(doc);
        doc.empruntPar(ab);
        return "L'abonné a emprunté le dvd ";
    }
    
    public void retournerDvd(Abonné abonne,int numeroDvd)  {
    	for(Document dvd : abonne.getDvdsEmpruntes()) {
    		if(dvd.numero() == numeroDvd) {
    			abonne.retirerDvd(dvd);
    			dvd.retour();
    			System.out.println("le Dvd" + " " + dvd.getTitre() +" " +"a été retourné par l'abonné" + " " + abonne.getNomAbo());
    		}
    		   		
    	}
    }
    
    
}

