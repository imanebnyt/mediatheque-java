package documents;

import client.Abonné;

public interface Document {
	int numero();
	Abonné emprunteur() ;
	Abonné reserveur() ; 
	void reservationPour(Abonné ab) ;
	void empruntPar(Abonné ab);
	void retour();
	String getTitre();
}