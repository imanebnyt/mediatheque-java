package documents;

import client.Abonn�;

public interface Document {
	int numero();
	Abonn� emprunteur() ;
	Abonn� reserveur() ; 
	void reservationPour(Abonn� ab) ;
	void empruntPar(Abonn� ab);
	void retour();
	String getTitre();
}