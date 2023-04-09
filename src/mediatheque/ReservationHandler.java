package mediatheque;



import java.io.*;
import java.net.Socket;

import client.Abonn�IntrouvableException;
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

            // Lire le message de r�servation envoy� par le client
            String message = input.readLine();

            // Traiter la demande de r�servation
            String response = Mediatheque.getInstance().reserver(message);

            // Envoyer la r�ponse au client
            output.write(response + "\n");
            output.flush();

            // Fermer les flux et la connexion
            input.close();
            output.close();
            socket.close();
        } catch (IOException | DocumentIntrouvableException | Abonn�IntrouvableException | DocumentDejaEmprunteException | DocumentDejaReserveException e) {
            System.out.println("Erreur lors du traitement de la requ�te de r�servation.");
            System.out.println(e.getMessage());
        }
    }
}
