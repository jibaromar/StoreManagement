package model;

public class LigneCommande {
	long id;
	long quantity;
	Produit produit;
	BL BL;

	public LigneCommande(LigneCommande lc) {
		if (lc != null) {
			this.id = lc.getId();
			this.quantity = lc.getQuantity();
			this.produit = new Produit(lc.getProduit());
			this.BL = new BL(lc.getBL());
		} else {
			this.id = -1L;
			this.quantity = 0;
			this.produit = new Produit(null);
			this.BL = new BL(null);
		}
	}
	
	public LigneCommande(long quantity, Produit produit, BL bLId) {
		this.id = -1L;
		this.quantity = quantity;
		this.produit = produit;
		BL = bLId;
	}
	
	public LigneCommande(long id, long quantity, Produit produit, BL bLId) {
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
