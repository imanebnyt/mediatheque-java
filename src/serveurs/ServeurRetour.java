package serveurs;

import java.io.*;
import java.net.*;

public class ServeurRetour implements Runnable {
	private ServerSocket listen_socket;

	// Cree un serveur TCP - objet de la classe ServerSocket
	public ServeurRetour(int port) throws IOException {
		listen_socket = new ServerSocket(port);
	}

	// Le serveur ecoute et accepte les connexions.
	// pour chaque connexion, il cree un ServiceInversion,
	// qui va la traiter, et le lance
	public void run() {
		try {
			System.err.println("Lancement du serveur au port " + this.listen_socket.getLocalPort());
			while (true)
				new Thread(new ServiceRetour(listen_socket.accept())).start();
		} catch (IOException e) {
			try {
				this.listen_socket.close();
			} catch (IOException e1) {
			}
			System.err.println("Arr�t du serveur au port " + this.listen_socket.getLocalPort());
		}
	}

	// restituer les ressources --> finalize
	protected void finalize() throws Throwable {
		try {
			this.listen_socket.close();
		} catch (IOException e1) {
		}
	}
}
