package application;

import javafx.geometry.Insets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.CategorieDaoImpl;
import dao.ProduitDaoImpl;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Categorie;
import model.Produit;

public class ListeDesProduits {
	VBox root = new VBox();
	double windowWidth = 900;
	double windowHeight = 500;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	List <Categorie> categories;
	List <Produit> produits;
	
	ObservableList <Produit> ProductsObservableList = FXCollections.observableArrayList();
	
	VBox Container = new VBox();
	
	Label TitleLabel = new Label("LISTE DES PRODUITS");
	
	TextField SearchBar = new TextField();
	
	TableView <Produit> ProductsTableView = new TableView<Produit>();
	TableColumn <Produit, Long> columnId = new TableColumn <Produit, Long>("Id");
	TableColumn <Produit, String> columnDesignation = new TableColumn <Produit, String>("Designation");
	TableColumn <Produit, String> columnCategorieId = new TableColumn <Produit, String>("Categorie");
	TableColumn <Produit, Double> columnBuyingPrice = new TableColumn <Produit, Double>("P. Achat");
	TableColumn <Produit, Double> columnSellingPrice = new TableColumn <Produit, Double>("P. Vente");
	TableColumn <Produit, Integer> columnQuantity = new TableColumn <Produit, Integer>("Quantity");
	TableColumn <Produit, String> columnTotal = new TableColumn <Produit, String>("Total");
	TableColumn <Produit, LocalDate> columnDate = new TableColumn <Produit, LocalDate>("Date");
	
	double Total = 0;
	
	HBox TotalHboxContainer = new HBox();
	
	Label TotalLabel = new Label("TOTAL: ");
	Label TotalValueLabel = new Label("0.0");
	
	private void addStylesToNodes() {
		scene.getStylesheets().add("assets/css/styles.css");
		
		Container.getStyleClass().add("container");
		Container.setSpacing(20);
		
		TitleLabel.getStyleClass().add("titleLabel");
		TitleLabel.setMinWidth(windowWidth);
		
		SearchBar.getStyleClass().add("searchBar");
		SearchBar.setMaxWidth(windowWidth / 2);
//		SearchBar.setPadding(new Insets(10, 10, 10, 10));
		
		ProductsTableView.getStyleClass().add("tableView");
		
		TotalHboxContainer.getStyleClass().add("TotalHboxContainer");
		
	}

	private void getProducts() {
		produits = new ProduitDaoImpl().getAll();
		
		ProductsObservableList.addAll(produits);
	}
	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Liste des produits");
		window.getIcons().add(new Image("file:store.jpg"));
		window.initModality(Modality.APPLICATION_MODAL);  
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
		columnDate.setPrefWidth(100);
		
	}
	
	private void evaluateTotal() {
		double total = 0.0;
		for (Produit p: ProductsObservableList) {
			total += p.getBuyingPrice() * p.getQuantity();
		}
		TotalValueLabel.setText(String.format("%,.2f", total));
	}
	private void appendNodesToWindow() {
		root.getChildren().add(TitleLabel);
		
		SearchBar.setPromptText("Searcher par id (#3) ou par mot-clé (Galaxy S3)");
		Container.getChildren().add(SearchBar);
		root.requestFocus();
		
		ProductsTableView.getColumns().addAll(columnId, columnDesignation, columnCategorieId, columnBuyingPrice, columnSellingPrice, columnQuantity, columnTotal, columnDate);
		ProductsTableView.setItems(ProductsObservableList);
		
		TotalHboxContainer.getChildren().addAll(TotalLabel, TotalValueLabel);
		
		Container.getChildren().addAll(ProductsTableView, TotalHboxContainer);
		
		root.getChildren().add(Container);
	}
	
	private void filterProducts() {
		String SearchBarString = SearchBar.getText().trim().replaceAll("\\s+", " ");
		if (SearchBarString.equals("")) {
			ProductsObservableList.clear();
			ProductsObservableList.addAll(produits);
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
					ProductsObservableList.clear();
					ProductsObservableList.addAll(filteredProducts);
				} catch (NumberFormatException e) {
					filteredProducts.removeIf(product -> {
						return !product.getDesignation().toLowerCase().contains(SearchBarString.toLowerCase());
					});
					ProductsObservableList.clear();
					ProductsObservableList.addAll(filteredProducts);
					System.out.println("ex");
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
		SearchBar.textProperty().addListener((observable) -> filterProducts());
	}
	
	public ListeDesProduits() {
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
