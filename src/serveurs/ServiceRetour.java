package serveurs;


import java.net.*;

import mediatheque.RetourHandler;

import java.io.*;

public class ServiceRetour implements Runnable {
    private ServerSocket serverSocket;
    private Socket client;
    private int port;

    public ServiceRetour(Socket socket) {
       client = socket;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Serveur de retour démarré sur le port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouvelle connexion entrante pour le service de retour.");

                // Traiter la requête de retour dans un thread séparé
                Thread t = new Thread(new RetourHandler(clientSocket));
                t.start();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la création du socket serveur de retour sur le port " + port);
            System.out.println(e.getMessage());
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'arrêt du service de retour.");
            System.out.println(e.getMessage());
        }
    }
}
