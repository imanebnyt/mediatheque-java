package ihm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.List;
import java.util.Scanner;

import mediatheque.Mediatheque;
import mediatheque.MediathequeException;
import client.Abonné;
import client.AbonnéIntrouvableException;
import documents.Document;
import documents.DocumentDejaEmprunteException;
import documents.DocumentDejaReserveException;
import documents.DocumentIntrouvableException;
import dvds.Dvd;
import serveurs.ServiceEmprunt;
import serveurs.ServiceReservation;
import serveurs.ServiceRetour;

public class Main {

    public static void main(String[] args) throws AbonnéIntrouvableException, MediathequeException {
        // Création d'une médiathèque
        Mediatheque mediatheque = Mediatheque.getInstance();
    	List<Abonné> abonnés_lis = BdConnection.récupérerAbonnés();
		List<Document> dvds_lis = BdConnection.récupérerDvds();
		
		//affichage des valeurs des abonnés
		for(Abonné abonné : abonnés_lis) {
			System.out.println("Nom Abonné : " + abonné.getNomAbo());
			System.out.println("Numero Abonné :" + abonné.getNumAbo());
			System.out.println("Date de Naissance" + abonné.getDateNaiss());
			System.out.println("---------------------");
		}
		
		// affichage des valeurs des dvds
		for(Document dvd : dvds_lis) {
			System.out.println("Numero Dvd :" + ((Dvd) dvd).getNumDvd());
			System.out.println("Titre :" + ((Dvd) dvd).getTitre());
			System.out.println("Adulte :" + ((Dvd) dvd).isAdulte());
			System.out.println("NumAbonné :" + ((Dvd) dvd).getNumAbo());
			System.out.println("---------------------");
		}

        // Création de services d'emprunt, de réservation et de retour
        ServiceEmprunt serviceEmprunt = null;
        ServiceReservation serviceReservation = null;
        ServiceRetour serviceRetour = null;
        
        

        try {
            serviceEmprunt = new ServiceEmprunt(new ServerSocket(4000).accept());
            serviceReservation = new ServiceReservation(new ServerSocket(3000).accept());
            serviceRetour = new ServiceRetour(new ServerSocket(5000).accept());
        } catch (IOException e) {
            System.out.println("Erreur lors de la création des services : " + e.getMessage());
        }

        

        // Ajout des services à la médiathèque
        mediatheque.addEmpruntService(serviceEmprunt);
        mediatheque.addReservationService(serviceReservation);
        mediatheque.addRetourService(serviceRetour);

        // Boucle pour interagir avec l'utilisateur
        while (true) {
            try {
                // Affichage du menu
                System.out.println("Que souhaitez-vous faire ? ");
                System.out.println("1. Réserver un DVD");
                System.out.println("2. Emprunter un DVD");
                System.out.println("3. Retourner un DVD");
                System.out.println("4. Quitter");

                // Lecture du choix de l'utilisateur
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String choix = reader.readLine();

                switch (choix) {
                    case "1":
                        // Réservation d'un DVD
                    	System.out.println("Veuillez entrer le numéro du DVD à réserver : ");
                    	int numero = Integer.parseInt(reader.readLine());
                    	System.out.println("Veuillez entrer votre identifiant d'abonné : ");
                    	int id = Integer.parseInt(reader.readLine());

                    	try {
                    	    String message = id + "" + numero;
                    	    String confirmation = mediatheque.reserver(message);
                    	    System.out.println(confirmation);
                    	} catch (AbonnéIntrouvableException e) {
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
                        System.out.println("Veuillez entrer le numéro du DVD à emprunter : ");
                        numero = scanner.nextInt();
                        System.out.println("Veuillez entrer votre identifiant d'abonné : ");
                        id = Integer.parseInt(reader.readLine());
                        Abonné abonne = mediatheque.chercherAbonne(id);
                        if (abonne != null) {
                            Dvd dvd = (Dvd) mediatheque.findDVDbyNumero(numero);
                            if (dvd != null) {
                                mediatheque.emprunter(abonne, dvd);
                                System.out.println("Le DVD numero" + numero + " à bien été emprunté par l'abonné "+ abonne.getNomAbo() + ".");
                            } else {
                                System.out.println("DVD non trouvé.");
                            }
                        } else {
                            System.out.println("Abonné non trouvé.");
                        }
                        break;
                    case "3":
                        // Retour d'un DVD
                    	Scanner scanner2 = new Scanner(System.in);
                        System.out.println("Veuillez entrer le numéro du DVD à retourner : ");
                        numero = scanner2.nextInt();                      
                        System.out.println("Veuillez entrer votre identifiant d'abonné : ");
                        id = Integer.parseInt(reader.readLine());
                        Abonné ab = mediatheque.chercherAbonne(id);                    
                        if (ab != null ) {
                        Dvd dvd = (Dvd) mediatheque.findDVDbyNumero(numero);
                           if (dvd != null) {
                               mediatheque.retournerDvd(ab, numero);
                             }
                          } else {
                            System.out.println("Le DVD avec le numéro " + " " + numero + " " + " n'a pas été trouvé.");
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
                System.out.println("Erreur lors de la lecture de l'entrée utilisateur.");
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