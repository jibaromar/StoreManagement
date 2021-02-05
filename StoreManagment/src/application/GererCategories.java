package application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
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
import model.Categorie;

public class GererCategories {
	VBox root = new VBox();
	double windowWidth = 900;
	double windowHeight = 500;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	private Consumer<List <Categorie>> CategoriesChangeCallback ;

    public void setCategoriesChangeCallback(Consumer<List <Categorie>> callback) {
        this.CategoriesChangeCallback = callback ;
    }
	
	List <Categorie> categories;
	
	ObservableList <Categorie> CategoriesObservableList = FXCollections.observableArrayList();
	
	VBox Container = new VBox();
	
	Label TitleLabel = new Label("LISTE DES CATEGORIES");
	
	TextField SearchBar = new TextField();
	
	TableView <Categorie> CategoriesTableView = new TableView<Categorie>();
	TableColumn <Categorie, Long> columnId = new TableColumn <Categorie, Long>("Id");
	TableColumn <Categorie, String> columnLabel = new TableColumn <Categorie, String>("Label");
	TableColumn <Categorie, String> columnDescription = new TableColumn <Categorie, String>("Description");
	
	HBox ButtonsContainer = new HBox();
	Button AddCategorieButton = new Button("Ajouter");
	
	private void addStylesToNodes() {
		scene.getStylesheets().add("assets/css/styles.css");
		
		Container.getStyleClass().add("container");
		Container.setSpacing(20);
		
		TitleLabel.getStyleClass().add("titleLabel");
		TitleLabel.setMinWidth(windowWidth);
		
		SearchBar.getStyleClass().add("searchBar");
		SearchBar.setMaxWidth(windowWidth / 2);
		
		CategoriesTableView.getStyleClass().add("tableView");
		
		ButtonsContainer.getStyleClass().add("buttonsContainer");
		ButtonsContainer.setSpacing(10);
		ButtonsContainer.setAlignment(Pos.CENTER_RIGHT);
		
	}

	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Liste des categories");
		window.getIcons().add(new Image("file:store.jpg"));
		window.initModality(Modality.APPLICATION_MODAL);  
	}
	
	private void updateColumns() {
		columnId.setCellValueFactory(new PropertyValueFactory <Categorie, Long> ("id"));
		columnId.setPrefWidth(50);
		
		columnLabel.setCellValueFactory(new PropertyValueFactory <Categorie, String> ("label"));
		columnLabel.setPrefWidth(306);
		
		columnDescription.setCellValueFactory(new PropertyValueFactory <Categorie, String> ("description"));
		columnDescription.setPrefWidth(500);
		
	}
	
	private void appendNodesToWindow() {
		root.getChildren().add(TitleLabel);
		
		SearchBar.setPromptText("Searcher par id (#3) ou par mot-clé (Santé)");
		Container.getChildren().add(SearchBar);
		root.requestFocus();
		
		CategoriesTableView.getColumns().addAll(columnId, columnLabel, columnDescription);
		CategoriesTableView.setItems(CategoriesObservableList);
		
		Container.getChildren().addAll(CategoriesTableView);
		
		ButtonsContainer.getChildren().add(AddCategorieButton);
		Container.getChildren().add(ButtonsContainer);
		
		root.getChildren().add(Container);
	}
	
	private void filterCategories() {
		String SearchBarString = SearchBar.getText().trim().replaceAll("\\s+", " ");
		if (SearchBarString.equals("")) {
			CategoriesObservableList.setAll(categories);
			return;
		}
		List <Categorie> filteredCategories = new ArrayList <Categorie> (categories);
		if (SearchBarString.charAt(0) == '#') {
			if (SearchBarString.length() > 1) {
				Long id;
				try {
					id = Long.parseLong(SearchBarString.replace("#", ""));
					filteredCategories.removeIf(categorie -> {
						return categorie.getId() != id;
					});
					CategoriesObservableList.setAll(filteredCategories);
				} catch (NumberFormatException e) {
					filteredCategories.removeIf(categorie -> {
						return !categorie.getLabel().toLowerCase().contains(SearchBarString.toLowerCase())
							&& !categorie.getDescription().toLowerCase().contains(SearchBarString.toLowerCase());
					});
					CategoriesObservableList.setAll(filteredCategories);
					System.out.println("ex");
					return;
				}
			}
		} else {
			filteredCategories.removeIf(categorie -> {
				return !categorie.getLabel().toLowerCase().contains(SearchBarString.toLowerCase())
						&& !categorie.getDescription().toLowerCase().contains(SearchBarString.toLowerCase());
			});
			CategoriesObservableList.setAll(filteredCategories);
		}
	}
	
	private void addEvents() {
		SearchBar.textProperty().addListener((observable) -> filterCategories());
		
		AddCategorieButton.setOnAction(event -> {
			NouvelleCategorie nouvelleCategorie = new NouvelleCategorie();
			nouvelleCategorie.setCategorieSelectCallback(categorie -> {
				categories.add(categorie);
				CategoriesObservableList.setAll(categories);
				CategoriesChangeCallback.accept(categories);
				window.show();
			});	
		});
		
		CategoriesTableView.setRowFactory(tv -> {
            TableRow<Categorie> row = new TableRow<Categorie>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Categorie rowData = row.getItem();
                    AfficherCategorie afficherCategorie = new AfficherCategorie(rowData);
                    afficherCategorie.setCategorieDeleteCallback(categorie -> {
                    	categories.removeIf(t -> t.getId() == categorie.getId());
                    	CategoriesObservableList.setAll(categories);
						CategoriesChangeCallback.accept(categories);
        			});
                    afficherCategorie.setCategorieEditCallback(categorie -> {
        				for (Categorie c: CategoriesObservableList) {
        					if (c.getId() == categorie.getId()) {
        						c.setLabel(categorie.getLabel());
        						c.setDescription(categorie.getDescription());
        						
        						// observable lists wont detect changes if values inside an element are changed
        						CategoriesObservableList.setAll(categories);
        						CategoriesChangeCallback.accept(categories);
        						break;
        					}
        				}
        			});
                }
            });
            return row ;
        });
	}
	
	public GererCategories(List <Categorie> categories) {
		this.categories = categories;
		CategoriesObservableList.addAll(categories);
		initWindow();
		addStylesToNodes();
		appendNodesToWindow();
		updateColumns();
		addEvents();
		window.show();
	}
}
