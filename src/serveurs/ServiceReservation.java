package serveurs;



import java.net.*;

import mediatheque.ReservationHandler;

import java.io.*;

public class ServiceReservation implements Runnable {
    private ServerSocket serverSocket;
    private Socket client;
    private int port;

    public ServiceReservation(Socket socket) {
        client = socket;
     }
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Serveur de réservation démarré sur le port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouvelle connexion entrante pour le service de réservation.");

                Thread t = new Thread(new ReservationHandler(clientSocket));
                t.start();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la création du socket serveur de réservation sur le port " + port);
            System.out.println(e.getMessage());
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'arrêt du service de réservation.");
            System.out.println(e.getMessage());
        }
    }
}
