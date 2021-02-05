package application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import dao.CategorieDaoImpl;
import dao.ProduitDaoImpl;
import javafx.geometry.Pos;
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

public class AfficherProduit {
	VBox root = new VBox();
	double windowWidth = 700;
	double windowHeight = 600;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	Produit produit = null;
	
	private Consumer<Produit> ProductDeleteCallback ;

    public void setProductDeleteCallback(Consumer<Produit> callback) {
        this.ProductDeleteCallback = callback ;
    }
    
    private Consumer<Produit> ProductEditCallback;

    public void setProductEditCallback(Consumer<Produit> callback) {
        this.ProductEditCallback = callback ;
    }
	
	List <Categorie> categories = new ArrayList <Categorie> ();
	
	Label TitleLabel = new Label("");
	
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
	Button ManageCategoriesButton = new Button("Gérer les catégories");
	
	
	Label DateLabel = new Label("Date:");
	DatePicker DateInput =  new DatePicker();

	HBox RightButtonsContainer = new HBox();
	
	Button EditProductButton = new Button("Modifier");
	Button DeleteButton = new Button("Supprimer");
	
	HBox LeftButtonsContainer = new HBox();
	Button SubmitButton = new Button("Modifier le produit");
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
		
		RightButtonsContainer.getStyleClass().add("buttonsContainer");
		RightButtonsContainer.setSpacing(10);
		RightButtonsContainer.setAlignment(Pos.CENTER_RIGHT);
		
		LeftButtonsContainer.getStyleClass().add("buttonsContainer");
		LeftButtonsContainer.setSpacing(10);
	}

	private void getCategories() {
		categories = new CategorieDaoImpl().getAll();
	}

	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Détail du produit");
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

		DesignationTextField.setDisable(true);
		Container.getChildren().addAll(DesignationLabel, DesignationTextField);
		
		Container.getChildren().add(CategoryLabel);
		
		CategoryComboBox.setDisable(true);
		CategorieContainer.getChildren().add(CategoryComboBox);
		
		Container.getChildren().add(CategorieContainer);
		
		BuyingPriceTextField.setDisable(true);
		Container.getChildren().addAll(BuyingPriceLabel, BuyingPriceTextField);
		
		SellingPriceTextField.setDisable(true);
		Container.getChildren().addAll(SellingPriceLabel, SellingPriceTextField);
		
		QuantityTextField.setDisable(true);
		Container.getChildren().addAll(QuantityLabel, QuantityTextField);
		
		DateInput.setDisable(true);
		Container.getChildren().addAll(DateLabel, DateInput);
		
		RightButtonsContainer.getChildren().addAll(EditProductButton, DeleteButton);
		Container.getChildren().add(RightButtonsContainer);
		
		LeftButtonsContainer.getChildren().addAll(SubmitButton, CancelButton);		
		
		root.getChildren().add(Container);
	}
	
	private void addEvents() {
		DeleteButton.setOnAction(event -> {
			if (new ProduitDaoImpl().delete(produit.getId())) {
				ProductDeleteCallback.accept(produit);
			}
			window.close();
		});
		SubmitButton.setOnAction(event -> {
			produit.setDesignation(DesignationTextField.getText());
			
			Long CategorieId = -1L;
			for (Categorie c: categories) {
				if (c.getLabel() == CategoryComboBox.getValue()) {
					CategorieId = c.getId();
					break;
				}
			}
			produit.setCategorieId(CategorieId);
			
			produit.setBuyingPrice(Double.parseDouble(BuyingPriceTextField.getText()));
			produit.setSellingPrice(Double.parseDouble(SellingPriceTextField.getText()));
			produit.setQuantity(Integer.parseInt(QuantityTextField.getText()));
			produit.setDate(DateInput.getValue());

			if (new ProduitDaoImpl().edit(produit)) {
				ProductEditCallback.accept(produit);
			}
			window.close();
		});
		CancelButton.setOnAction(event -> {
			// disable window close button
			window.setOnCloseRequest(e -> {
				window.close();
			});
					
			// change titles
			window.setTitle("Détail du produit");
			TitleLabel.setText(produit.getDesignation());
						
			// disable text fields
			DesignationTextField.setDisable(true);
			CategoryComboBox.setDisable(true);
			BuyingPriceTextField.setDisable(true);
			SellingPriceTextField.setDisable(true);
			QuantityTextField.setDisable(true);
			DateInput.setDisable(true);
			
			// remove manage categories button
			CategorieContainer.getChildren().remove(ManageCategoriesButton);
						
			// removing bottom buttons
			Container.getChildren().remove(LeftButtonsContainer);
			Container.getChildren().add(RightButtonsContainer);
		});
		ManageCategoriesButton.setOnAction(event -> {
			GererCategories gererCategories = new GererCategories(categories);
			gererCategories.setCategoriesChangeCallback(categories -> {
				this.categories = categories;
				addCategoriesToComboBox();
				CategoryComboBox.setValue(categories.get(categories.size()-1).getLabel());
				window.show();
			});
		});
		EditProductButton.setOnAction(event -> {
			// disable window close button
			window.setOnCloseRequest(e -> {
				e.consume();
			});
			
			// change titles
			window.setTitle("Modifié détail du produit");
			TitleLabel.setText("MODIFIER: " + produit.getDesignation());
			
			// enable text fields
			DesignationTextField.setDisable(false);
			CategoryComboBox.setDisable(false);
			BuyingPriceTextField.setDisable(false);
			SellingPriceTextField.setDisable(false);
			QuantityTextField.setDisable(false);
			DateInput.setDisable(false);
			
			// add manage categories button
			CategorieContainer.getChildren().add(ManageCategoriesButton);
			
			// removint top buttons
			Container.getChildren().remove(RightButtonsContainer);
			Container.getChildren().add(LeftButtonsContainer);
		});
	}
	
	private void setDefaultValues(Produit produit) {
		TitleLabel.setText(produit.getDesignation());
		
		DesignationTextField.setText(produit.getDesignation());
		BuyingPriceTextField.setText(produit.getBuyingPrice() + "");
		SellingPriceTextField.setText(produit.getSellingPrice() + "");
		QuantityTextField.setText(produit.getQuantity() + "");
		
		for (Categorie c: categories) {
			if (c.getId() == produit.getCategorieId()) {
				CategoryComboBox.setValue(c.getLabel());
			}
		}
		
		DateInput.setValue(produit.getDate());
	}
	
	public AfficherProduit(Produit produit) {
		this.produit = produit;
		addStylesToNodes();
		initWindow();
		getCategories();
		addCategoriesToComboBox();
		setDefaultValues(produit);
		appendNodesToWindow();
		addEvents();
		
		window.show();
	}
}
