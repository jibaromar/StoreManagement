package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.BLDaoImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.BL;

public class ListeDesBonsDeLivraison {
	VBox root = new VBox();
	double windowWidth = 900;
	double windowHeight = 500;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	List <BL> BLs;
	
	ObservableList <BL> BLsObservableList = FXCollections.observableArrayList();
	
	VBox Container = new VBox();
	
	Label TitleLabel = new Label("LISTE DES BON DE LIVRAISON");
	
	TextField SearchBar = new TextField();
	
	TableView <BL> BLsTableView = new TableView<BL>();
	TableColumn <BL, Long> columnId = new TableColumn <BL, Long>("Id");
	TableColumn <BL, String> columnLastName = new TableColumn <BL, String>("Nom client");
	TableColumn <BL, String> columnFirstName = new TableColumn <BL, String>("Prénom client");
	TableColumn <BL, String> columnEmail = new TableColumn <BL, String>("Email");
	TableColumn <BL, LocalDate> columnDate = new TableColumn <BL, LocalDate>("Date");
	
	private void addStylesToNodes() {
		scene.getStylesheets().add("assets/css/styles.css");
		
		Container.getStyleClass().add("container");
		Container.setSpacing(20);
		
		TitleLabel.getStyleClass().add("titleLabel");
		TitleLabel.setMinWidth(windowWidth);
		
		SearchBar.getStyleClass().add("searchBar");
		SearchBar.setMaxWidth(windowWidth / 2);
//		SearchBar.setPadding(new Insets(10, 10, 10, 10));
		
		BLsTableView.getStyleClass().add("tableView");
		
	}

	private void getBLs() {
		BLs = new BLDaoImpl().getAll();
		
		BLsObservableList.addAll(BLs);
	}
	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Liste des BLs");
		window.getIcons().add(new Image("file:store.jpg"));
		window.initModality(Modality.APPLICATION_MODAL);  
	}
	
	private void updateColumns() {
		columnId.setCellValueFactory(new PropertyValueFactory <BL, Long> ("id"));
		columnId.setPrefWidth(50);
		
		columnLastName.setCellValueFactory(row -> {
			return new SimpleStringProperty(row.getValue().getClient().getLastName());
		});
		columnLastName.setPrefWidth(170);
		
		columnFirstName.setCellValueFactory(row -> {
			return new SimpleStringProperty(row.getValue().getClient().getFirstName());
		});
		columnFirstName.setPrefWidth(170);
		
		columnEmail.setCellValueFactory(row -> {
			return new SimpleStringProperty(row.getValue().getClient().getEmail());
		});
		columnEmail.setPrefWidth(366);
		
		columnDate.setCellValueFactory(new PropertyValueFactory <BL, LocalDate> ("date"));
		columnDate.setPrefWidth(100);
		
	}
	
	private void appendNodesToWindow() {
		root.getChildren().add(TitleLabel);
		
		SearchBar.setPromptText("Searcher par id (#3) ou par nom/prénom (Omar)");
		Container.getChildren().add(SearchBar);
		root.requestFocus();
		
		BLsTableView.getColumns().addAll(columnId, columnLastName, columnFirstName, columnEmail, columnDate);
		BLsTableView.setItems(BLsObservableList);
		
		Container.getChildren().addAll(BLsTableView);
		
		root.getChildren().add(Container);
	}
	
	private void filterBLs() {
		String SearchBarString = SearchBar.getText().trim().replaceAll("\\s+", " ");
		System.out.println(SearchBarString);
		if (SearchBarString.equals("")) {
			BLsObservableList.setAll(BLs);
			return;
		}
		List <BL> filteredBLs = new ArrayList <BL> (BLs);
		if (SearchBarString.charAt(0) == '#') {
			if (SearchBarString.length() > 1) {
				Long id;
				try {
					id = Long.parseLong(SearchBarString.replace("#", ""));
					filteredBLs.removeIf(bl -> {
						return bl.getId() != id;
					});
					BLsObservableList.setAll(filteredBLs);
				} catch (NumberFormatException e) {
					filteredBLs.removeIf(bl -> {
						return !bl.getClient().getLastName().toLowerCase().contains(SearchBarString.toLowerCase())
							&& !bl.getClient().getFirstName().toLowerCase().contains(SearchBarString.toLowerCase());
					});
					BLsObservableList.setAll(filteredBLs);
					System.out.println("ex");
					return;
				}
			}
		} else {
			filteredBLs.removeIf(bl -> {
				System.out.println("searchbar=" + SearchBarString + "=");
				System.out.println("bl.getClient().getLastName().toLowerCase()=" + bl.getClient().getLastName().toLowerCase() + "=");
				return !bl.getClient().getLastName().toLowerCase().contains(SearchBarString.toLowerCase())
					&& !bl.getClient().getFirstName().toLowerCase().contains(SearchBarString.toLowerCase());
			});
			BLsObservableList.clear();
			BLsObservableList.addAll(filteredBLs);
		}
	}
	
	private void addEvents() {
		SearchBar.textProperty().addListener((observable) -> filterBLs());
		
		BLsTableView.setRowFactory(tv -> {
            TableRow<BL> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    BL rowData = row.getItem();
                    AfficherBL afficherBL = new AfficherBL(rowData);
                    afficherBL.setBLDeleteCallback(bl -> {
                    	BLsObservableList.removeIf(t -> t.getId() == bl.getId());
        			});
                    afficherBL.setBLEditCallback(bl -> {
        				for (BL c: BLsObservableList) {
        					if (c.getId() == bl.getId()) {
        						c.setClient(bl.getClient());
        						c.setDate(bl.getDate());
        						
        						// observable lists wont detect changes if values inside an element are changed
        						BLsObservableList.setAll(BLs);
        						break;
        					}
        				}
        			});
                }
            });
            return row ;
        });
	}
	
	public ListeDesBonsDeLivraison() {
		initWindow();
		addStylesToNodes();
		appendNodesToWindow();
		getBLs();
		updateColumns();
		addEvents();
		window.show();
	}
}
