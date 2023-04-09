package mediatheque;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import client.Abonn�;
import client.Abonn�IntrouvableException;
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
            
            // Lire le num�ro de l'abonn�
            int numAbonne = Integer.parseInt(reader.readLine());
            
         // Chercher l'abonn� dans la m�diath�que
            Abonn� abonne = Mediatheque.getInstance().chercherAbonne(numAbonne);

            // Lire le num�ro du document retourn�
            String numeroStr = reader.readLine();
            int numero = Integer.parseInt(numeroStr);

            // Chercher le document dans la m�diath�que
            Document doc = Mediatheque.getInstance().chercherDocument(numero);
            

            // Retourner le document
           
				Mediatheque.getInstance().retournerDvd(abonne,numero);
			
            
            // Envoyer une r�ponse au client
            writer.println("Document retourn� avec succ�s.");
            
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture des donn�es du client : " + e.getMessage());
        } catch (Abonn�IntrouvableException e) {
            System.err.println("Abonn� introuvable : " + e.getMessage());
        } 
    }
    
}
