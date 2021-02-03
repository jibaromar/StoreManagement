package model;

public class Categorie {
	Long id;
	String label;
	String description;
	
	public Categorie(Long id, String label, String description) {
		super();
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
