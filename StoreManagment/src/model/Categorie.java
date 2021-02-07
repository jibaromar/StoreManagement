package model;

public class Categorie {
	Long id;
	String label;
	String description;
	
	public Categorie(Categorie categorie) {
		if (categorie != null) {
			this.id = categorie.getId();
			this.label = categorie.getLabel();
			this.description = categorie.getDescription();			
		} else {
			this.id = -1L;
			this.label = "";
			this.description = "";
		}
	}
	
	public Categorie(String label, String description) {
		this.id = -1L;
		this.label = label;
		this.description = description;
	}
	
	public Categorie(Long id, String label, String description) {
		this.id = id;
		this.label = label;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	
}
