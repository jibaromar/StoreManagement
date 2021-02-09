package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import application.modals.Confirmation;
import application.modals.NOTIF_TYPE;
import application.modals.Notification;
import dao.CategorieDaoImpl;
import dao.ProduitDaoImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Categorie;
import model.Produit;
import utils.Validators;

public class NouveauProduit {
	VBox root = new VBox();
	double windowWidth = 700;
	double windowHeight = 610;
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
	ComboBox<Categorie> CategoryComboBox = new ComboBox<Categorie>();
	Button ManageCategoriesButton = new Button();
	
	
	Label DateLabel = new Label("Date:");
	DatePicker DateInput =  new DatePicker();

	HBox ButtonsContainer = new HBox();
	
	Button AddProductButton = new Button("Ajouter");
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
		CategoryComboBox.setCellFactory(new Callback<ListView<Categorie>, ListCell<Categorie>>() {
 
            @Override
            public ListCell<Categorie> call(ListView<Categorie> param) {
                final ListCell<Categorie> cell = new ListCell<Categorie>() {
 
                    @Override
                    protected void updateItem(Categorie item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getLabel());
                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
		CategoryComboBox.setButtonCell(new ListCell<Categorie>() {
			 
            @Override
            protected void updateItem(Categorie item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getLabel());
                } else {
                    setText(null);
                }
            }
        });
		for (Categorie c: categories) {
			CategoryComboBox.getItems().add(c);			
		}
	}

	private void appendNodesToWindow() {
		root.getChildren().add(TitleLabel);
		
		Container.getChildren().addAll(DesignationLabel, DesignationTextField);
		
		Container.getChildren().add(CategoryLabel);
		
		ManageCategoriesButton.setGraphic(new ImageView("assets/img/add.png"));
		ManageCategoriesButton.setMaxHeight(10);
		CategorieContainer.getChildren().addAll(CategoryComboBox, ManageCategoriesButton);
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
	
	private boolean isValidForm() {
		boolean isValid = true;
		if (Validators.isEmpty(DesignationTextField.getText())){
			if (!DesignationTextField.getStyleClass().contains("invalidTextField")) {
				DesignationTextField.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			DesignationTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		if (!Validators.isPrice(BuyingPriceTextField.getText())){
			if (!BuyingPriceTextField.getStyleClass().contains("invalidTextField")) {
				BuyingPriceTextField.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			BuyingPriceTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		if (!Validators.isPrice(SellingPriceTextField.getText())){
			if (!SellingPriceTextField.getStyleClass().contains("invalidTextField")) {
				SellingPriceTextField.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			SellingPriceTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		if (!Validators.isQuantity(QuantityTextField.getText())){
			if (!QuantityTextField.getStyleClass().contains("invalidTextField")) {
				QuantityTextField.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			QuantityTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		return isValid;
	}
	
	private void addTextFieldsValidators() {
		DesignationTextField.textProperty().addListener((Observable, oldValue, newValue) -> {
			isValidForm();
		});
		BuyingPriceTextField.textProperty().addListener((Observable, oldValue, newValue) -> {
			isValidForm();
		});
		SellingPriceTextField.textProperty().addListener((Observable, oldValue, newValue) -> {
			isValidForm();
		});
		QuantityTextField.textProperty().addListener((Observable, oldValue, newValue) -> {
			isValidForm();
		});
	}
	
	private void addEvents() {
		CancelButton.setOnAction(event -> {
			Confirmation confirmation = new Confirmation("Quitter", "Vous êtes sûr de vouloir annuler l'ajout du produit?");
			confirmation.setResponseCallBack(response -> {
				if (response == true) {
					window.close();
				}
			});
		});
		ManageCategoriesButton.setOnAction(event -> {
			GererCategories gererCategories = new GererCategories(categories);
			gererCategories.setCategoriesChangeCallback(categories -> {
				this.categories = categories;
				addCategoriesToComboBox();
				CategoryComboBox.setValue(categories.get(categories.size()-1));
				window.show();
			});
		});
		AddProductButton.setOnAction(event -> {
			if (isValidForm()) {
				Confirmation confirmation = new Confirmation("Ajouter un nouveau produit", "Vous êtes sûr de vouloir ajouter ce nouveau produit?");
				confirmation.setResponseCallBack(response -> {
					if (response == true) {
						Produit produit = new Produit(0, DesignationTextField.getText(), CategoryComboBox.getValue(), Double.parseDouble(BuyingPriceTextField.getText()), Double.parseDouble(SellingPriceTextField.getText()), Integer.parseInt(QuantityTextField.getText()), DateInput.getValue());
						new ProduitDaoImpl().add(produit);
						new Notification(NOTIF_TYPE.SUCCESS, "Le produit est ajouté avec succés.");
						window.close();
					}
				});
			} else {
				new Notification(NOTIF_TYPE.ERROR, "Les données du formulaire sont invalide.");
			}
			
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
		CategoryComboBox.setValue(categories.get(0));
		appendNodesToWindow();
		addEvents();
		addTextFieldsValidators();
		window.show();
	}
}
