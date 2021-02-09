package application;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import application.modals.Confirmation;
import application.modals.NOTIF_TYPE;
import application.modals.Notification;
import dao.CategorieDaoImpl;
import dao.ProduitDaoImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
	
    List <Categorie> categories;
	
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
	ComboBox<Categorie> CategoryComboBox = new ComboBox<Categorie>();
	Button ManageCategoriesButton = new Button();
	
	
	Label DateLabel = new Label("Date:");
	DatePicker DateInput =  new DatePicker();

	HBox RightButtonsContainer = new HBox();
	Button EditProductButton = new Button("Modifier");
	Button DeleteButton = new Button("Supprimer");
	
	HBox LeftButtonsContainer = new HBox();
	Button SubmitButton = new Button("Modifier");
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
		
		ManageCategoriesButton.setGraphic(new ImageView("assets/img/edit.png"));
		ManageCategoriesButton.setMaxHeight(10);
		
		RightButtonsContainer.getChildren().addAll(EditProductButton, DeleteButton);
		Container.getChildren().add(RightButtonsContainer);
		
		LeftButtonsContainer.getChildren().addAll(SubmitButton, CancelButton);		
		
		root.getChildren().add(Container);
	}
	
	private void addEvents() {
		DeleteButton.setOnAction(event -> {
			Confirmation confirmation = new Confirmation("Supprimer le produit", "Vous êtes sûr de vouloir supprimer ce produit?");
			confirmation.setResponseCallBack(response -> {
				if (response == true) {
					if (new ProduitDaoImpl().delete(produit.getId())) {
						ProductDeleteCallback.accept(produit);
					}
					window.close();
				}
			});
		});
		SubmitButton.setOnAction(event -> {
			if (isValidForm()) {
				Confirmation confirmation = new Confirmation("Modifier le produit", "Vous êtes sûr de vouloir modifier le produit?");
				confirmation.setResponseCallBack(response -> {
					if (response == true) {
						produit.setDesignation(DesignationTextField.getText());
						
						produit.setCategorie(CategoryComboBox.getValue());
						
						produit.setBuyingPrice(Double.parseDouble(BuyingPriceTextField.getText()));
						produit.setSellingPrice(Double.parseDouble(SellingPriceTextField.getText()));
						produit.setQuantity(Integer.parseInt(QuantityTextField.getText()));
						produit.setDate(DateInput.getValue());
						
						if (new ProduitDaoImpl().edit(produit)) {
							ProductEditCallback.accept(produit);
						}
						window.close();
					}
				});
			} else {
				new Notification(NOTIF_TYPE.ERROR, "Les données du formulaire ne sont pas valide.");
			}
		});
		CancelButton.setOnAction(event -> {
			Confirmation confirmation = new Confirmation("Annuler la modification", "Vous êtes sûr de vouloir annuler la modification du produit?");
			confirmation.setResponseCallBack(response -> {
				if (response == true) {
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
		DesignationTextField.textProperty().addListener((observable, oldValue, newValue) -> {
				isValidForm();
		});
		BuyingPriceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
				isValidForm();
		});
		SellingPriceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			isValidForm();
		});
		QuantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
				isValidForm();
		});
	}

	private void setDefaultValues(Produit produit) {
		TitleLabel.setText(produit.getDesignation());
		
		DesignationTextField.setText(produit.getDesignation());
		BuyingPriceTextField.setText(produit.getBuyingPrice() + "");
		SellingPriceTextField.setText(produit.getSellingPrice() + "");
		QuantityTextField.setText(produit.getQuantity() + "");
		
		// combo uses references if u put a referece of a categorie that doesnt exit inside it 
		// it will show the reference instead ot the cell factory format
		for (Categorie c: categories) {
			if (c.getId() == produit.getCategorie().getId()) {
				CategoryComboBox.setValue(c);
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
		addTextFieldsValidators();
		window.show();
	}
}
