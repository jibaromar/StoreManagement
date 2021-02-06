package model;

public class LigneCommande {
	long id;
	long quantity;
	Produit produit;
	BL BL;

	public LigneCommande(long id, long quantity, Produit produit, BL bLId) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.produit = produit;
		BL = bLId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public BL getBL() {
		return BL;
	}

	public void setBL(BL bLId) {
		BL = bLId;
	}
	
	public String toString() {
		return "{\n\tid:" + id + ",\n\tquantity:" + quantity + ",\n\tproduit:" + produit + ",\n\tBL:" + BL + "\n}";
	}
}
