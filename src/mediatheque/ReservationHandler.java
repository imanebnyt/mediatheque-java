package mediatheque;



import java.io.*;
import java.net.Socket;

import client.AbonnéIntrouvableException;
import documents.DocumentDejaEmprunteException;
import documents.DocumentDejaReserveException;
import documents.DocumentIntrouvableException;

public class ReservationHandler extends Thread {
    private Socket socket = null;

    public ReservationHandler(Socket socket) {
        super("ReservationThread");
        this.socket = socket;
    }


    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Lire le message de réservation envoyé par le client
            String message = input.readLine();

            // Traiter la demande de réservation
            String response = Mediatheque.getInstance().reserver(message);

            // Envoyer la réponse au client
            output.write(response + "\n");
            output.flush();

            // Fermer les flux et la connexion
            input.close();
            output.close();
            socket.close();
        } catch (IOException | DocumentIntrouvableException | AbonnéIntrouvableException | DocumentDejaEmprunteException | DocumentDejaReserveException e) {
            System.out.println("Erreur lors du traitement de la requête de réservation.");
            System.out.println(e.getMessage());
        }
    }
}
