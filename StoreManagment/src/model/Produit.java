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
	
	public Produit(Produit produit) {
		if (produit != null) {
			this.id = produit.getId();
			this.designation = produit.getDesignation();
			this.categorieId = produit.getCategorieId();
			this.buyingPrice = produit.getBuyingPrice();
			this.sellingPrice = produit.getSellingPrice();
			this.quantity = produit.getQuantity();
			this.date = produit.getDate();			
		} else {
			this.id = -1L;
			this.designation = "";
			this.categorieId = -1L;
			this.buyingPrice = 0.0;
			this.sellingPrice = 0.0;
			this.quantity = 0;
			this.date = LocalDate.now();	
		}
	}
	
	public Produit(String designation, long categorieId, double buyingPrice, double sellingPrice, int quantity, LocalDate date) {
		this.id = -1L;
		this.designation = designation;
		this.categorieId = categorieId;
		this.buyingPrice = buyingPrice;
		this.sellingPrice = sellingPrice;
		this.quantity = quantity;
		this.date = date;
	}
	
	public Produit(long id, String designation, long categorieId, double buyingPrice, double sellingPrice, int quantity, LocalDate date) {
		this.id = id;
		this.designation = designation;
		this.categorieId = categorieId;
		this.buyingPrice = buyingPrice;
		this.sellingPrice = sellingPrice;
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
	
	@Override
	public String toString() {
		return "{\n\tid:" + id + ",\n\tdesignation:" + designation + ",\n\tcategorieId:" + categorieId + ",\n\tbuyingPrice:" + buyingPrice + ",\n\tsellingPrice:" + sellingPrice + ",\n\tquantity:" + quantity + ",\n\tdate:" + date + "\n}";
	}
}
