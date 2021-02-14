package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import application.modals.Confirmation;
import application.modals.NOTIF_TYPE;
import application.modals.Notification;
import dao.CategorieDaoImpl;
import dao.ProduitDaoImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Categorie;
import model.LigneCommande;
import model.Produit;
import utils.Validators;

public class ModifierLigneDeCommande {
	VBox root = new VBox();
	double windowWidth = 900;
	double windowHeight = 500;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	private Consumer<LigneCommande> CommandLineModifyCallBack;
	
	public void setCommandLineModifyCallBack(Consumer <LigneCommande> callback) {
		this.CommandLineModifyCallBack = callback;
	}
	
	private Consumer<LigneCommande> CommandLigneDeleteCallBack;
	
	public void setCommandLigneDeleteCallBack(Consumer <LigneCommande> callback) {
		this.CommandLigneDeleteCallBack = callback;
	}
	
	LigneCommande tmpLC;
	
	List <Categorie> categories;
	List <Produit> produits;
	
	ObservableList <Produit> ProductsObservableList = FXCollections.observableArrayList();
	
	VBox Container = new VBox();
	
	Label TitleLabel = new Label("MODIFIER LIGNE DE COMMANDE");
	
	Label ProductsLabel = new Label("Produit:");
	
	TextField SearchBar = new TextField();
	
	TableView <Produit> CommandLignesTableView = new TableView<Produit>();
	TableColumn <Produit, Long> columnId = new TableColumn <Produit, Long>("Id");
	TableColumn <Produit, String> columnDesignation = new TableColumn <Produit, String>("Designation");
	TableColumn <Produit, String> columnCategorieId = new TableColumn <Produit, String>("Categorie");
	TableColumn <Produit, Double> columnBuyingPrice = new TableColumn <Produit, Double>("P. Achat");
	TableColumn <Produit, Double> columnSellingPrice = new TableColumn <Produit, Double>("P. Vente");
	TableColumn <Produit, Integer> columnQuantity = new TableColumn <Produit, Integer>("Quantity");
	TableColumn <Produit, String> columnTotal = new TableColumn <Produit, String>("Total");
	TableColumn <Produit, LocalDate> columnDate = new TableColumn <Produit, LocalDate>("Date");
	
	Label QuantityLable = new Label("Quantié:");
	TextField QuantityTextField = new TextField();
		
	Button ModifyLigneCommandeButton = new Button("Modifier");
	Button CancelButton = new Button("Annuler");
	HBox BottomLeftButtonsHboxContainer = new HBox(ModifyLigneCommandeButton, CancelButton);
	
	Button DeleteButton = new Button("Supprimer");
	HBox BottomRightButtonsHboxContainer = new HBox(DeleteButton);

	BorderPane BottomButtonsBorderPaneContainer = new BorderPane(null, null, BottomRightButtonsHboxContainer, null, BottomLeftButtonsHboxContainer);
	
	private void addStylesToNodes() {
		scene.getStylesheets().add("assets/css/styles.css");
		
		Container.getStyleClass().add("container");
		Container.setSpacing(20);
		
		TitleLabel.getStyleClass().add("titleLabel");
		TitleLabel.setMinWidth(windowWidth);
		
		SearchBar.getStyleClass().add("searchBar");
		SearchBar.setMaxWidth(windowWidth / 2);
		
		CommandLignesTableView.getStyleClass().add("tableView");
		
		BottomLeftButtonsHboxContainer.getStyleClass().add("buttonsContainer");
		BottomLeftButtonsHboxContainer.setSpacing(10);
		
		BottomRightButtonsHboxContainer.getStyleClass().add("buttonsContainer");
		BottomRightButtonsHboxContainer.setSpacing(10);
	}

	private void getProducts() {
		produits = new ProduitDaoImpl().getAll();
		
		ProductsObservableList.addAll(produits);
	}
	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Modifier ligne de commande");
		window.getIcons().add(new Image("file:store.jpg"));
		window.initModality(Modality.APPLICATION_MODAL);
		window.setX(100);
		window.setY(200);
	}
	
	private void getCategories() {
		categories = new CategorieDaoImpl().getAll();
	}
	
	private void updateColumns() {
		columnId.setCellValueFactory(new PropertyValueFactory <Produit, Long> ("id"));
		columnId.setPrefWidth(50);
		
		columnDesignation.setCellValueFactory(new PropertyValueFactory <Produit, String> ("designation"));
		columnDesignation.setPrefWidth(160);
		
		columnCategorieId.setCellValueFactory(row -> {
			return new SimpleStringProperty(row.getValue().getCategorie().getLabel());
		});
		columnCategorieId.setPrefWidth(150);
		
		columnBuyingPrice.setCellValueFactory(new PropertyValueFactory <Produit, Double> ("buyingPrice"));
		columnBuyingPrice.setPrefWidth(100);
		columnSellingPrice.setCellValueFactory(new PropertyValueFactory <Produit, Double> ("sellingPrice"));
		columnSellingPrice.setPrefWidth(100);
		
		columnQuantity.setCellValueFactory(new PropertyValueFactory <Produit, Integer> ("quantity"));
		columnQuantity.setPrefWidth(100);
		
		columnTotal.setCellValueFactory(row -> {
			Double d = row.getValue().getBuyingPrice() * row.getValue().getQuantity();
			
			return new SimpleStringProperty(d.toString());
		});
		columnTotal.setPrefWidth(100);
		
		columnDate.setCellValueFactory(new PropertyValueFactory <Produit, LocalDate> ("date"));
		columnDate.setPrefWidth(96);
		
	}

	private void appendNodesToWindow() {
		root.getChildren().addAll(TitleLabel);
		
		Container.getChildren().add(ProductsLabel);
		
		SearchBar.setPromptText("Searcher par id (#3) ou par mot-clé (Galaxy S3)");
		Container.getChildren().add(SearchBar);
		root.requestFocus();
		
		CommandLignesTableView.getColumns().addAll(columnId, columnDesignation, columnCategorieId, columnBuyingPrice, columnSellingPrice, columnQuantity, columnTotal, columnDate);
		CommandLignesTableView.setItems(ProductsObservableList);
		
		Container.getChildren().addAll(CommandLignesTableView);
		
		Container.getChildren().addAll(QuantityLable, QuantityTextField);
		
		DeleteButton.setAlignment(Pos.BASELINE_RIGHT);
		Container.getChildren().add(BottomButtonsBorderPaneContainer);
		
		root.getChildren().add(Container);
	}
	
	private void filterProducts() {
		String SearchBarString = SearchBar.getText().trim().replaceAll("\\s+", " ");
		if (SearchBarString.equals("")) {
			ProductsObservableList.setAll(produits);
			return;
		}
		List <Produit> filteredProducts = new ArrayList <Produit> (produits);
		if (SearchBarString.charAt(0) == '#') {
			if (SearchBarString.length() > 1) {
				Long id;
				try {
					id = Long.parseLong(SearchBarString.replace("#", ""));
					filteredProducts.removeIf(product -> {
						return product.getId() != id;
					});
					ProductsObservableList.setAll(filteredProducts);
				} catch (NumberFormatException e) {
					filteredProducts.removeIf(product -> {
						return !product.getDesignation().toLowerCase().contains(SearchBarString.toLowerCase());
					});
					ProductsObservableList.setAll(filteredProducts);
					return;
				}
			}
		} else {
			filteredProducts.removeIf(product -> {
				return !product.getDesignation().toLowerCase().contains(SearchBarString.toLowerCase());
			});
			ProductsObservableList.clear();
			ProductsObservableList.addAll(filteredProducts);
		}
	}
	
	private void addEvents() {
		CancelButton.setOnAction(event -> {
			Confirmation confirmation = new Confirmation("Annuler les modifications", "Vous êtes sûr de vouloir annuler les modifications sur cette ligne de commande?");
			confirmation.setResponseCallBack(response -> {
				if (response == true) {
					window.close();
				}
			});
		});
		
		DeleteButton.setOnAction(event -> {
			Confirmation confirmation = new Confirmation("Supprimer la ligne de commande", "Vous êtes sûr de vouloir supprimer cette ligne de commande?");
			confirmation.setResponseCallBack(response -> {
				if (response == true) {
					CommandLigneDeleteCallBack.accept(tmpLC);
					window.close();
				}
			});
		});
		
		SearchBar.textProperty().addListener((observable) -> filterProducts());
		
		CommandLignesTableView.setRowFactory(tv -> {
            TableRow<Produit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty()) {
                    Produit rowData = row.getItem();
                    tmpLC.setProduit(rowData);
                }
            });
            return row ;
        });
		
		ModifyLigneCommandeButton.setOnAction(event -> {
			if (isValidForm()) {
				Confirmation confirmation = new Confirmation("Modifier la ligne de commande", "Vous êtes sûr de vouloir modifier cette ligne de commande?");
				confirmation.setResponseCallBack(response -> {
					if (response == true) {
						tmpLC.setQuantity(Long.parseLong(QuantityTextField.getText()));
						CommandLineModifyCallBack.accept(tmpLC);
						window.close();
					}
				});
			} else {
				new Notification(NOTIF_TYPE.ERROR, "Les données du formulaire ne sont pas valide.");
			}
		});
	}
	
	private boolean isValidForm() {
		boolean isValid = true;
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
		QuantityTextField.textProperty().addListener((Observable, oldValue, newValue) -> {
			isValidForm();
		});
	}
	private void setDefaultValues() {
		QuantityTextField.setText(tmpLC.getQuantity() + "");
		TableViewSelectionModel<Produit> selectionModel = CommandLignesTableView.getSelectionModel();
		for (int i = 0; i < ProductsObservableList.size(); i++) {
			if (ProductsObservableList.get(i).getId() == tmpLC.getProduit().getId()) {
				selectionModel.select(i);
				break;
			}
		}
	}
	public ModifierLigneDeCommande(LigneCommande lc) {
		tmpLC = new LigneCommande(lc);
		initWindow();
		addStylesToNodes();
		appendNodesToWindow();
		getCategories();
		getProducts();
		updateColumns();
		setDefaultValues();
		addEvents();
		addTextFieldsValidators();
		window.show();
	}
}
