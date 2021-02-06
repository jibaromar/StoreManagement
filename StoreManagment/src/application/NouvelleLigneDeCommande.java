package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import dao.CategorieDaoImpl;
import dao.LigneCommandeDaoImpl;
import dao.ProduitDaoImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.BL;
import model.Categorie;
import model.LigneCommande;
import model.Produit;

public class NouvelleLigneDeCommande {
	VBox root = new VBox();
	double windowWidth = 900;
	double windowHeight = 500;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	private Consumer<LigneCommande> CommandLineSelectCallBack;
	
	public void setCommandLineSelectCallBack(Consumer <LigneCommande> callback) {
		this.CommandLineSelectCallBack = callback;
	}
	
	List <Categorie> categories;
	
	Produit selectedProduct;
	List <Produit> produits;
	
	ObservableList <Produit> ProductsObservableList = FXCollections.observableArrayList();
	
	VBox Container = new VBox();
	
	Label TitleLabel = new Label("NOUVELLE LIGNE DE COMMANDE");
	
	Label ProductsLabel = new Label("Produits:");
	
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
	
	HBox ButtonsContainer = new HBox();
	Button AddLigneCommandeButton = new Button("Ajouter la nouvelle ligne de commande");
	Button CancelButton = new Button("Annuler");
	
	private void addStylesToNodes() {
		scene.getStylesheets().add("assets/css/styles.css");
		
		Container.getStyleClass().add("container");
		Container.setSpacing(20);
		
		TitleLabel.getStyleClass().add("titleLabel");
		TitleLabel.setMinWidth(windowWidth);
		
		SearchBar.getStyleClass().add("searchBar");
		SearchBar.setMaxWidth(windowWidth / 2);
		
		CommandLignesTableView.getStyleClass().add("tableView");
		
		ButtonsContainer.getStyleClass().add("buttonsContainer");
		ButtonsContainer.setSpacing(10);
		
	}

	private void getProducts() {
		produits = new ProduitDaoImpl().getAll();
		
		ProductsObservableList.addAll(produits);
	}
	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Nouvelle ligne de commande");
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
			String CategorieLabel = "-";
			for (Categorie categorie: categories) {
				if (categorie.getId() == row.getValue().getCategorieId()) {
					CategorieLabel = categorie.getLabel();
				}
			}
			return new SimpleStringProperty(CategorieLabel);
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
	
	private void evaluateTotal() {
		double total = 0.0;
		for (Produit p: ProductsObservableList) {
			total += p.getBuyingPrice() * p.getQuantity();
		}
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
		
		ButtonsContainer.getChildren().addAll(AddLigneCommandeButton, CancelButton);
		Container.getChildren().addAll(ButtonsContainer);
		
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
			window.close();
		});
		
		SearchBar.textProperty().addListener((observable) -> filterProducts());
		
		CommandLignesTableView.setRowFactory(tv -> {
            TableRow<Produit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty()) {
                    Produit rowData = row.getItem();
                    selectedProduct = rowData;
                }
            });
            return row ;
        });
		
		AddLigneCommandeButton.setOnAction(event -> {
			CommandLineSelectCallBack.accept(new LigneCommande(-1L, Long.parseLong(QuantityTextField.getText()), selectedProduct, null));
			window.close();
		});
	}
	
	public NouvelleLigneDeCommande() {
		initWindow();
		addStylesToNodes();
		appendNodesToWindow();
		getCategories();
		getProducts();
		updateColumns();
		evaluateTotal();
		addEvents();
		window.show();
	}
}
