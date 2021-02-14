package model;

import java.time.LocalDate;

public class Reglement {
	long id;
	LocalDate date;
	double montant;
	String type;
	Long numero_cheque;
	LocalDate date_echeance;
	Banque banque;
	String nom;
	BL BL;
	
	public Reglement(Reglement rg) {
		super();
		this.id = rg.getId();
		this.date = rg.getDate();
		this.montant = rg.getMontant();
		this.type = rg.getType();
		this.numero_cheque = rg.getNumero_cheque();
		this.date_echeance = rg.getDate_echeance();
		this.banque = rg.getBanque();
		this.nom = rg.getNom();
		this.BL = rg.getBL();
	}
	
	public Reglement(LocalDate date, double montant) {
		super();
		this.id = -1L;
		this.date = date;
		this.montant = montant;
		this.type = "ESPECE";
		this.numero_cheque = null;
		this.date_echeance = null;
		this.banque = null;
		this.nom = null;
		this.BL = null;
	}
	
	public Reglement(LocalDate date, double montant, long numero_cheque, LocalDate date_echeance,
			Banque banque, String nom) {
		super();
		this.id = -1L;
		this.date = date;
		this.montant = montant;
		this.type = "CHEQUE";
		this.numero_cheque = numero_cheque;
		this.date_echeance = date_echeance;
		this.banque = banque;
		this.nom = nom;
		this.BL = null;
	}
	
	public Reglement(long id, LocalDate date, double montant, BL BL) {
		super();
		this.id = id;
		this.date = date;
		this.montant = montant;
		this.type = "ESPECE";
		this.numero_cheque = null;
		this.date_echeance = null;
		this.banque = null;
		this.nom = null;
		this.BL = BL;
	}
	
	public Reglement(long id, LocalDate date, double montant, long numero_cheque, LocalDate date_echeance,
			Banque banque, String nom, BL BL) {
		super();
		this.id = id;
		this.date = date;
		this.montant = montant;
		this.type = "CHEQUE";
		this.numero_cheque = numero_cheque;
		this.date_echeance = date_echeance;
		this.banque = banque;
		this.nom = nom;
		this.BL = BL;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getMontant() {
		return montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getNumero_cheque() {
		return numero_cheque;
	}

	public void setNumero_cheque(Long numero_cheque) {
		this.numero_cheque = numero_cheque;
	}

	public LocalDate getDate_echeance() {
		return date_echeance;
	}

	public void setDate_echeance(LocalDate date_echeance) {
		this.date_echeance = date_echeance;
	}

	public Banque getBanque() {
		return banque;
	}

	public void setBanque(Banque banque) {
		this.banque = banque;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	
	public BL getBL() {
		return BL;
	}

	public void setBL(BL bL) {
		BL = bL;
	}

	@Override
	public String toString() {
		return "Reglement [id=" + id + ", date=" + date + ", montant=" + montant + ", type=" + type + ", numero_cheque="
				+ numero_cheque + ", date_echeance=" + date_echeance + ", banque=" + banque + ", nom=" + nom + "]";
	}	
}
