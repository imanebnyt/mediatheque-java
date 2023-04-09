package ihm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.List;
import java.util.Scanner;

import mediatheque.Mediatheque;
import mediatheque.MediathequeException;
import client.Abonn�;
import client.Abonn�IntrouvableException;
import documents.Document;
import documents.DocumentDejaEmprunteException;
import documents.DocumentDejaReserveException;
import documents.DocumentIntrouvableException;
import dvds.Dvd;
import serveurs.ServiceEmprunt;
import serveurs.ServiceReservation;
import serveurs.ServiceRetour;

public class Main {

    public static void main(String[] args) throws Abonn�IntrouvableException, MediathequeException {
        // Cr�ation d'une m�diath�que
        Mediatheque mediatheque = Mediatheque.getInstance();
    	List<Abonn�> abonn�s_lis = BdConnection.r�cup�rerAbonn�s();
		List<Document> dvds_lis = BdConnection.r�cup�rerDvds();
		
		//affichage des valeurs des abonn�s
		for(Abonn� abonn� : abonn�s_lis) {
			System.out.println("Nom Abonn� : " + abonn�.getNomAbo());
			System.out.println("Numero Abonn� :" + abonn�.getNumAbo());
			System.out.println("Date de Naissance" + abonn�.getDateNaiss());
			System.out.println("---------------------");
		}
		
		// affichage des valeurs des dvds
		for(Document dvd : dvds_lis) {
			System.out.println("Numero Dvd :" + ((Dvd) dvd).getNumDvd());
			System.out.println("Titre :" + ((Dvd) dvd).getTitre());
			System.out.println("Adulte :" + ((Dvd) dvd).isAdulte());
			System.out.println("NumAbonn� :" + ((Dvd) dvd).getNumAbo());
			System.out.println("---------------------");
		}

        // Cr�ation de services d'emprunt, de r�servation et de retour
        ServiceEmprunt serviceEmprunt = null;
        ServiceReservation serviceReservation = null;
        ServiceRetour serviceRetour = null;
        
        

        try {
            serviceEmprunt = new ServiceEmprunt(new ServerSocket(4000).accept());
            serviceReservation = new ServiceReservation(new ServerSocket(3000).accept());
            serviceRetour = new ServiceRetour(new ServerSocket(5000).accept());
        } catch (IOException e) {
            System.out.println("Erreur lors de la cr�ation des services : " + e.getMessage());
        }

        

        // Ajout des services � la m�diath�que
        mediatheque.addEmpruntService(serviceEmprunt);
        mediatheque.addReservationService(serviceReservation);
        mediatheque.addRetourService(serviceRetour);

        // Boucle pour interagir avec l'utilisateur
        while (true) {
            try {
                // Affichage du menu
                System.out.println("Que souhaitez-vous faire ? ");
                System.out.println("1. R�server un DVD");
                System.out.println("2. Emprunter un DVD");
                System.out.println("3. Retourner un DVD");
                System.out.println("4. Quitter");

                // Lecture du choix de l'utilisateur
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String choix = reader.readLine();

                switch (choix) {
                    case "1":
                        // R�servation d'un DVD
                    	System.out.println("Veuillez entrer le num�ro du DVD � r�server : ");
                    	int numero = Integer.parseInt(reader.readLine());
                    	System.out.println("Veuillez entrer votre identifiant d'abonn� : ");
                    	int id = Integer.parseInt(reader.readLine());

                    	try {
                    	    String message = id + "" + numero;
                    	    String confirmation = mediatheque.reserver(message);
                    	    System.out.println(confirmation);
                    	} catch (Abonn�IntrouvableException e) {
                    	    System.out.println(e.getMessage());
                    	} catch (DocumentIntrouvableException e) {
                    	    System.out.println(e.getMessage());
                    	} catch (DocumentDejaEmprunteException e) {
                    	    System.out.println(e.getMessage());
                    	} catch (DocumentDejaReserveException e) {
                    	    System.out.println(e.getMessage());
                    	}
                    	break;

                    case "2":
                        // Emprunt d'un DVD
                    	Scanner scanner = new Scanner(System.in);
                        System.out.println("Veuillez entrer le num�ro du DVD � emprunter : ");
                        numero = scanner.nextInt();
                        System.out.println("Veuillez entrer votre identifiant d'abonn� : ");
                        id = Integer.parseInt(reader.readLine());
                        Abonn� abonne = mediatheque.chercherAbonne(id);
                        if (abonne != null) {
                            Dvd dvd = (Dvd) mediatheque.findDVDbyNumero(numero);
                            if (dvd != null) {
                                mediatheque.emprunter(abonne, dvd);
                                System.out.println("Le DVD numero" + numero + " � bien �t� emprunt� par l'abonn� "+ abonne.getNomAbo() + ".");
                            } else {
                                System.out.println("DVD non trouv�.");
                            }
                        } else {
                            System.out.println("Abonn� non trouv�.");
                        }
                        break;
                    case "3":
                        // Retour d'un DVD
                    	Scanner scanner2 = new Scanner(System.in);
                        System.out.println("Veuillez entrer le num�ro du DVD � retourner : ");
                        numero = scanner2.nextInt();                      
                        System.out.println("Veuillez entrer votre identifiant d'abonn� : ");
                        id = Integer.parseInt(reader.readLine());
                        Abonn� ab = mediatheque.chercherAbonne(id);                    
                        if (ab != null ) {
                        Dvd dvd = (Dvd) mediatheque.findDVDbyNumero(numero);
                           if (dvd != null) {
                               mediatheque.retournerDvd(ab, numero);
                             }
                          } else {
                            System.out.println("Le DVD avec le num�ro " + " " + numero + " " + " n'a pas �t� trouv�.");
                    }
                    break;
                    
                    case "4":
                    	//Quitter
                    	System.out.println("Etes vous vraiment sur de vouloir Quitter ? oui ou non");
                    	String message = reader.readLine();
                    		case "oui":
                    			System.out.println("Au revoir! A bientot!");                   		
                    		case "non" :                    			                            
                    break;
                        
                    default:
                        System.out.println("Choix non valide.");
                        break;
                
              
                	}

            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture de l'entr�e utilisateur.");
                e.printStackTrace();
            } finally {
            	if (serviceEmprunt != null) {
                    serviceEmprunt.stop();
                }
                if (serviceReservation != null) {
                    serviceReservation.stop();
                }
                if (serviceRetour != null) {
                    serviceRetour.stop();
                }
            }
        }
    }
    
}