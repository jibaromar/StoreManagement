package model;

import java.time.LocalDate;

public class Reglement {
	long id;
	LocalDate date;
	double montant;
	String type;
	Long numero_cheque;
	LocalDate date_echance;
	Banque banque;
	String nom;
	
	public Reglement(Reglement rg) {
		super();
		this.id = rg.getId();
		this.date = rg.getDate();
		this.montant = rg.getMontant();
		this.type = rg.getType();
		this.numero_cheque = rg.getNumero_cheque();
		this.date_echance = rg.getDate_echance();
		this.banque = rg.getBanque();
		this.nom = rg.getNom();
	}
	
	public Reglement(LocalDate date, double montant) {
		super();
		this.id = -1L;
		this.date = date;
		this.montant = montant;
		this.type = "ESPECE";
		this.numero_cheque = null;
		this.date_echance = null;
		this.banque = null;
		this.nom = null;
	}
	
	public Reglement(LocalDate date, double montant, long numero_cheque, LocalDate date_echance,
			Banque banque, String nom) {
		super();
		this.id = -1L;
		this.date = date;
		this.montant = montant;
		this.type = "CHEQUE";
		this.numero_cheque = numero_cheque;
		this.date_echance = date_echance;
		this.banque = banque;
		this.nom = nom;
	}
	
	public Reglement(long id, LocalDate date, double montant) {
		super();
		this.id = id;
		this.date = date;
		this.montant = montant;
		this.type = "ESPECE";
		this.numero_cheque = null;
		this.date_echance = null;
		this.banque = null;
		this.nom = null;
	}
	
	public Reglement(long id, LocalDate date, double montant, long numero_cheque, LocalDate date_echance,
			Banque banque, String nom) {
		super();
		this.id = id;
		this.date = date;
		this.montant = montant;
		this.type = "CHEQUE";
		this.numero_cheque = numero_cheque;
		this.date_echance = date_echance;
		this.banque = banque;
		this.nom = nom;
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

	public long getNumero_cheque() {
		return numero_cheque;
	}

	public void setNumero_cheque(Long numero_cheque) {
		this.numero_cheque = numero_cheque;
	}

	public LocalDate getDate_echance() {
		return date_echance;
	}

	public void setDate_echance(LocalDate date_echance) {
		this.date_echance = date_echance;
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

	@Override
	public String toString() {
		return "Reglement [id=" + id + ", date=" + date + ", montant=" + montant + ", type=" + type + ", numero_cheque="
				+ numero_cheque + ", date_echance=" + date_echance + ", banque=" + banque + ", nom=" + nom + "]";
	}	
}
