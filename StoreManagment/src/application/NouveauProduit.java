package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.CategorieDaoImpl;
import dao.ProduitDaoImpl;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Categorie;
import model.Produit;

public class NouveauProduit {
	VBox root = new VBox();
	double windowWidth = 700;
	double windowHeight = 600;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	List <Categorie> categories = new ArrayList <Categorie> ();
	
	Label TitleLabel = new Label("NOUVEAU PRODUIT");
	
	VBox Container = new VBox();
	
	Label DesignationLabel = new Label("Designation:");
	TextField DesignationTextField = new TextField();
	
	Label BuyingPriceLabel = new Label("Prix d'achat:");
	TextField BuyingPriceTextField = new TextField();
	
	Label SellingPriceLabel = new Label("Prix de vente:");
	TextField SellingPriceTextField = new TextField();
	
	Label QuantityLabel = new Label("Quantité:");
	TextField QuantityTextField = new TextField();
	
	Label CategoryLabel = new Label("Catégorie:");
	
	HBox CategorieContainer = new HBox();
	ComboBox<String> CategoryComboBox = new ComboBox<String>();
	Button AddCategorieButton = new Button("Ajouter");
	
	
	Label DateLabel = new Label("Date:");
	DatePicker DateInput =  new DatePicker();

	HBox ButtonsContainer = new HBox();
	
	Button AddProductButton = new Button("Ajouter le produit");
	Button CancelButton = new Button("Annuler");

	private void addStylesToNodes() {
		scene.getStylesheets().add("assets/css/styles.css");
		root.getStyleClass().add("root");
		
		TitleLabel.getStyleClass().add("titleLabel");
		TitleLabel.setMinWidth(windowWidth);
		
		Container.getStyleClass().add("container");
		Container.setSpacing(20);
		
		CategoryComboBox.getStyleClass().add("CategoryComboBox");
		
		CategorieContainer.getStyleClass().add("categorieContainer");
		CategorieContainer.setSpacing(10);
		
		ButtonsContainer.getStyleClass().add("buttonsContainer");
		ButtonsContainer.setSpacing(10);
	}

	private void getCategories() {
		categories = new CategorieDaoImpl().getAll();
	}

	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Ajouté un nouveau produit");
		window.getIcons().add(new Image("file:store.jpg"));
		window.initModality(Modality.APPLICATION_MODAL);  
	}
	
	private void addCategoriesToComboBox() {
		CategoryComboBox.getItems().clear();
		for (Categorie c: categories) {
			CategoryComboBox.getItems().add(c.getLabel());			
		}
	}

	private void appendNodesToWindow() {
		root.getChildren().add(TitleLabel);
		
		Container.getChildren().addAll(DesignationLabel, DesignationTextField);
		
		Container.getChildren().add(CategoryLabel);
		
		CategorieContainer.getChildren().addAll(CategoryComboBox, AddCategorieButton);
		Container.getChildren().add(CategorieContainer);
		
		Container.getChildren().addAll(BuyingPriceLabel, BuyingPriceTextField);
		Container.getChildren().addAll(SellingPriceLabel, SellingPriceTextField);
		Container.getChildren().addAll(QuantityLabel, QuantityTextField);
		
		DateInput.setValue(LocalDate.now());
		Container.getChildren().addAll(DateLabel, DateInput);
		
		ButtonsContainer.getChildren().addAll(AddProductButton, CancelButton);
		Container.getChildren().add(ButtonsContainer);
		
		root.getChildren().add(Container);
	}
	
	private void addEvents() {
		CancelButton.setOnAction(event -> {
			window.close();
		});
		AddCategorieButton.setOnAction(event -> {
			NouvelleCategorie nouvelleCategorie = new NouvelleCategorie();
			nouvelleCategorie.setCategorieSelectCallback(categorie -> {
				categories.add(categorie);
				addCategoriesToComboBox();
				CategoryComboBox.setValue(categories.get(categories.size()-1).getLabel());
				window.show();
			});
		});
		AddProductButton.setOnAction(event -> {
			// DAO
			Long CategorieId = -1L;
			for (Categorie c: categories) {
				if (c.getLabel() == CategoryComboBox.getValue()) {
					CategorieId = c.getId();
					break;
				}
			}
			Produit produit = new Produit(0, DesignationTextField.getText(), CategorieId, Double.parseDouble(BuyingPriceTextField.getText()), Double.parseDouble(SellingPriceTextField.getText()), Integer.parseInt(QuantityTextField.getText()), DateInput.getValue());
			new ProduitDaoImpl().add(produit);
			window.close();
		});
		window.setOnCloseRequest(event -> {
			event.consume();
		});
	}
	
	public NouveauProduit() {
		addStylesToNodes();
		initWindow();
		getCategories();
		addCategoriesToComboBox();
		CategoryComboBox.setValue(categories.get(0).getLabel());
		appendNodesToWindow();
		addEvents();
		
		window.show();
	}
}
