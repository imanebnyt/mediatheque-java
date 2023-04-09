package mediatheque;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import client.Abonné;
import client.AbonnéIntrouvableException;
import documents.Document;

public class EmpruntHandler extends Thread {
	  private Socket socket = null;

	    public EmpruntHandler(Socket socket) {
	        super("EmpruntThread");
	        this.socket = socket;
	    }

    @Override
    public void run() {
        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            String message = in.readLine();
            String[] parts = message.split(",");
            int numAbonne = Integer.parseInt(parts[0]);
            int numDocument = Integer.parseInt(parts[1]);

            Abonné abonne = Mediatheque.getInstance().chercherAbonne(numAbonne);
            Document document = Mediatheque.getInstance().chercherDocument(numDocument);

            String response = Mediatheque.getInstance().emprunter(abonne,document);
            out.println(response);
           
        } catch (IOException e) {
            System.err.println("Erreur lors du traitement de la requête : " + e.getMessage());
        } catch (MediathequeException e) {
            System.err.println("Erreur lors du traitement de la requête : " + e.getMessage());
        } catch (AbonnéIntrouvableException e) {
            System.out.println("Abonné introuvable. Veuillez vérifier le numéro d'abonné.");
			e.printStackTrace();
		} finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture de la socket : " + e.getMessage());
            }
        }
    }
}
