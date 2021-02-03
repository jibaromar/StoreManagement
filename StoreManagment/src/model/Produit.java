package model;

import java.time.LocalDate;

public class Produit {
	long id;
	String designation;
	long categorieId;
	double buyingPrice;
	double sellingPrice;
	int quantity;
	LocalDate date;
	
	public Produit(long id, String designation, long categorieId, double buyingPrice, double sellingPrice, int quantity, LocalDate date) {
		super();
		this.id = id;
		this.designation = designation;
		this.categorieId = categorieId;
		this.buyingPrice = buyingPrice;
		this.buyingPrice = sellingPrice;
		this.quantity = quantity;
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public long getCategorieId() {
		return categorieId;
	}

	public void setCategorieId(long categorieId) {
		this.categorieId = categorieId;
	}

	public double getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(double buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
