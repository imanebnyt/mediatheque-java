package mediatheque;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import client.Abonné;
import client.AbonnéIntrouvableException;
import mediatheque.MediathequeException;
import documents.Document;

public class RetourHandler extends Thread {
    
    private Socket socket = null;

    public RetourHandler(Socket socket) {
        super("RetourThread");
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            
            // Lire le numéro de l'abonné
            int numAbonne = Integer.parseInt(reader.readLine());
            
         // Chercher l'abonné dans la médiathèque
            Abonné abonne = Mediatheque.getInstance().chercherAbonne(numAbonne);

            // Lire le numéro du document retourné
            String numeroStr = reader.readLine();
            int numero = Integer.parseInt(numeroStr);

            // Chercher le document dans la médiathèque
            Document doc = Mediatheque.getInstance().chercherDocument(numero);
            

            // Retourner le document
           
				Mediatheque.getInstance().retournerDvd(abonne,numero);
			
            
            // Envoyer une réponse au client
            writer.println("Document retourné avec succès.");
            
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture des données du client : " + e.getMessage());
        } catch (AbonnéIntrouvableException e) {
            System.err.println("Abonné introuvable : " + e.getMessage());
        } 
    }
    
}
