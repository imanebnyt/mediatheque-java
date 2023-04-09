package dvds;

import client.Abonné;
import documents.Document;

public class Dvd implements Document{
	private int numDvd;
	private boolean adulte;
	private String titre;
	private int numAbo;
	private boolean reserve;
	private boolean emprunte;
	private Abonné emprunteur;
	private Abonné reserveur;


	    public Dvd(int numDvd, String titre, boolean adulte, int numAbo) {
	        this.numDvd = numDvd;
	        this.titre = titre;
	        this.adulte = adulte;
	        this.numAbo = numAbo;
	        this.emprunteur = null;
	        this.reserveur = null;
	        this.emprunte = false;
	        this.setReserve(false);
	   
	    }
	
	@Override
    public String toString() {
        return "Dvd{" +
                "numDvd=" + numDvd +
                ", adulte='" + adulte + '\'' +
                ", titre='" + titre + '\'' +
                  ", numAbo='" + numAbo + '\'' +
                '}';
    }
	
	  
	  public void setNumAbo(int numAbo) {
	       this.numAbo = numAbo;
	   }
	  

	    @Override
	    public int numero() {
	        return numDvd;
	    }

	    @Override
	    public Abonné emprunteur() {
	        return emprunteur;
	    }

	    public Abonné reserveur() {
	        return reserveur;
	    }

	    @Override
	    public void reservationPour(Abonné ab) {
	        if (emprunteur != null || reserveur != null) {
	            throw new IllegalStateException("Le DVD est d�j� emprunt� ou r�serv�.");
	        }
	        reserveur = ab;
	    }

	    @Override
	    public void empruntPar(Abonné ab) {
	        if (emprunteur != null && !emprunteur.equals(ab)) {
	            throw new IllegalStateException("Le DVD est d�j� emprunt� par un autre abonn�.");
	        }
	        if (reserveur != null && !reserveur.equals(ab)) {
	            throw new IllegalStateException("Le DVD est r�serv� pour un autre abonn�.");
	        }
	        emprunteur = ab;
	        reserveur = null;
	    }

	    @Override
	    public void retour() {
	        emprunteur = null;
	    }
		public int getNumDvd() {
			return numDvd;
		}

		public int getNumAbo() {
			return numAbo;
		}
		
		
		public String getTitre() {
			return titre;
		}

	    public boolean isAdulte() {
	        return adulte;
	    }
	    


		public boolean isReserve() {
			return reserve;
		}

		public void setReserve(boolean reserve) {
			this.reserve = reserve;
		}

	
	  }
	  

