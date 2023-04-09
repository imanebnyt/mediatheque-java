package serveurs;

import java.net.*;

import mediatheque.EmpruntHandler;

import java.io.*;

public class ServiceEmprunt implements Runnable {
    private ServerSocket serverSocket;
	private Socket client;
	private int port = 4000;

    public ServiceEmprunt(Socket socket) {
       client = socket;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Serveur d'emprunt démarré sur le port " + port);

            while (true) {
                 client = serverSocket.accept();
                System.out.println("Nouvelle connexion entrante pour le service d'emprunt.");

                // Traiter la requête d'emprunt dans un thread séparé
                Thread t = new Thread(new EmpruntHandler(client));
                t.start();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la création du socket serveur d'emprunt sur le port " + port);
            System.out.println(e.getMessage());
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'arrêt du service d'emprunt.");
            System.out.println(e.getMessage());
        }
    }
    
    public void lancer() {
		new Thread(this).start();	
	}
}
