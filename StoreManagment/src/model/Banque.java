package model;

public class Banque {
	long id;
	String nom;
	String abreviation;
	
	public Banque(Banque bq) {
		this.id = bq.getId();
		this.nom = bq.getNom();
		this.abreviation = bq.getAbreviation();
	}
	
	public Banque(String nom, String abreviation) {
		this.id = -1L;
		this.nom = nom;
		this.abreviation = abreviation;
	}
	
	public Banque(long id, String nom, String abreviation) {
		this.id = id;
		this.nom = nom;
		this.abreviation = abreviation;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAbreviation() {
		return abreviation;
	}

	public void setAbreviation(String abreviation) {
		this.abreviation = abreviation;
	}

	@Override
	public String toString() {
		return "Banque [id=" + id + ", nom=" + nom + ", abreviation=" + abreviation + "]";
	}
}
