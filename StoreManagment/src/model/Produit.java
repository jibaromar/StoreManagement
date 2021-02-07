package model;

import java.time.LocalDate;

public class Produit {
	long id;
	String designation;
	Categorie categorie;
	double buyingPrice;
	double sellingPrice;
	int quantity;
	LocalDate date;
	
	public Produit(Produit produit) {
		if (produit != null) {
			this.id = produit.getId();
			this.designation = produit.getDesignation();
			this.categorie = produit.getCategorie();
			this.buyingPrice = produit.getBuyingPrice();
			this.sellingPrice = produit.getSellingPrice();
			this.quantity = produit.getQuantity();
			this.date = produit.getDate();			
		} else {
			this.id = -1L;
			this.designation = "";
			this.categorie = new Categorie(null);
			this.buyingPrice = 0.0;
			this.sellingPrice = 0.0;
			this.quantity = 0;
			this.date = LocalDate.now();	
		}
	}
	
	public Produit(String designation, Categorie categorie, double buyingPrice, double sellingPrice, int quantity, LocalDate date) {
		this.id = -1L;
		this.designation = designation;
		this.categorie = categorie;
		this.buyingPrice = buyingPrice;
		this.sellingPrice = sellingPrice;
		this.quantity = quantity;
		this.date = date;
	}
	
	public Produit(long id, String designation, Categorie categorie, double buyingPrice, double sellingPrice, int quantity, LocalDate date) {
		this.id = id;
		this.designation = designation;
		this.categorie = categorie;
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

	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
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
		return "{\n\tid:" + id + ",\n\tdesignation:" + designation + ",\n\tcategorie:" + categorie + ",\n\tbuyingPrice:" + buyingPrice + ",\n\tsellingPrice:" + sellingPrice + ",\n\tquantity:" + quantity + ",\n\tdate:" + date + "\n}";
	}
}
