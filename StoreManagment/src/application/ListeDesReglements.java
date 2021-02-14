package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.ReglementDaoImpl;
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
import model.Banque;
import model.Reglement;

public class ListeDesReglements {
	VBox root = new VBox();
	double windowWidth = 1150;
	double windowHeight = 500;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	List <Reglement> reglements;
	
	ObservableList <Reglement> ReglementsObservableList = FXCollections.observableArrayList();
	
	VBox Container = new VBox();
	
	Label TitleLabel = new Label("LISTE DES REGLEMENTS");
	
	TextField SearchBar = new TextField();
	
	TableView <Reglement> ReglementsTableView = new TableView<Reglement>();
	TableColumn <Reglement, Long> columnReglementId = new TableColumn <Reglement, Long>("Id règlement");
	TableColumn <Reglement, String> columnBLId = new TableColumn <Reglement, String>("Id règlement");
	TableColumn <Reglement, String> columnReglementLastName = new TableColumn <Reglement, String>("Nom");
	TableColumn <Reglement, String> columnReglementFirstName = new TableColumn <Reglement, String>("Prènom");
	TableColumn <Reglement, LocalDate> columnReglementDate = new TableColumn <Reglement, LocalDate>("Date");
	TableColumn <Reglement, Double> columnReglementMontant = new TableColumn <Reglement, Double>("Mantant");
	TableColumn <Reglement, String> columnReglementType = new TableColumn <Reglement, String>("Type");
	TableColumn <Reglement, Long> columnReglementNumeroCheque = new TableColumn <Reglement, Long>("Numero du chèque");
	TableColumn <Reglement, LocalDate> columnReglementDateEcheance = new TableColumn <Reglement, LocalDate>("Date");
	TableColumn <Reglement, String> columnReglementBanque = new TableColumn <Reglement, String>("Banque");
	TableColumn <Reglement, String> columnReglementNom = new TableColumn <Reglement, String>("Nom");
	
	
	private void addStylesToNodes() {
		scene.getStylesheets().add("assets/css/styles.css");
		
		Container.getStyleClass().add("container");
		Container.setSpacing(20);
		
		TitleLabel.getStyleClass().add("titleLabel");
		TitleLabel.setMinWidth(windowWidth);
		
		SearchBar.getStyleClass().add("searchBar");
		SearchBar.setMaxWidth(windowWidth / 2);
		
		ReglementsTableView.getStyleClass().add("tableView");
		
	}

	private void getReglements() {
		reglements = new ReglementDaoImpl().getAll();
		
		ReglementsObservableList.addAll(reglements);
	}
	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Liste des règlements");
		window.getIcons().add(new Image("file:store.jpg"));
		window.initModality(Modality.APPLICATION_MODAL);  
	}
	
	private void updateColumns() {
		columnReglementId.setCellValueFactory(new PropertyValueFactory <Reglement, Long> ("id"));
		columnReglementId.setPrefWidth(50);
		
		columnBLId.setCellValueFactory(row -> {
			return new SimpleStringProperty(row.getValue().getBL().getId() + "");
		});
		columnBLId.setPrefWidth(50);
		
		columnReglementLastName.setCellValueFactory(row -> {
			return new SimpleStringProperty(row.getValue().getBL().getClient().getLastName() + "");
		});
		columnReglementLastName.setPrefWidth(100);
		
		columnReglementFirstName.setCellValueFactory(row -> {
			return new SimpleStringProperty(row.getValue().getBL().getClient().getFirstName() + "");
		});
		columnReglementFirstName.setPrefWidth(100);
		
		columnReglementDate.setCellValueFactory(new PropertyValueFactory <Reglement, LocalDate> ("date"));
		columnReglementDate.setPrefWidth(100);
		
		columnReglementMontant.setCellValueFactory(new PropertyValueFactory <Reglement, Double> ("montant"));
		columnReglementMontant.setPrefWidth(100);
		
		columnReglementType.setCellValueFactory(new PropertyValueFactory <Reglement, String> ("type"));
		columnReglementType.setPrefWidth(100);
		
		columnReglementNumeroCheque.setCellValueFactory(new PropertyValueFactory <Reglement, Long> ("numero_cheque"));
		columnReglementNumeroCheque.setPrefWidth(181);
		
		columnReglementDateEcheance.setCellValueFactory(new PropertyValueFactory <Reglement, LocalDate> ("date_echeance"));
		columnReglementDateEcheance.setPrefWidth(100);
		
		columnReglementBanque.setCellValueFactory(row -> {
			Banque banque = row.getValue().getBanque();
			if (banque != null) {
				return new SimpleStringProperty(banque.getAbreviation() + "");
			}
			return new SimpleStringProperty("");
		});
		columnReglementBanque.setPrefWidth(75);
		
		columnReglementNom.setCellValueFactory(new PropertyValueFactory <Reglement, String> ("nom"));
		columnReglementNom.setPrefWidth(150);
		
	}
	
	private void appendNodesToWindow() {
		root.getChildren().add(TitleLabel);
		
		SearchBar.setPromptText("Searcher par id (#3) ou par nom/prénom (Omar)");
		Container.getChildren().add(SearchBar);
		root.requestFocus();
		
		ReglementsTableView.getColumns().addAll(columnReglementId, columnBLId, columnReglementLastName, columnReglementFirstName, columnReglementDate, columnReglementMontant,  columnReglementType, columnReglementNumeroCheque,  columnReglementDateEcheance, columnReglementBanque, columnReglementNom);
		ReglementsTableView.setItems(ReglementsObservableList);
		
		Container.getChildren().addAll(ReglementsTableView);
		
		root.getChildren().add(Container);
	}
	
	private void filterReglements() {
		String SearchBarString = SearchBar.getText().trim().replaceAll("\\s+", " ");
		if (SearchBarString.equals("")) {
			ReglementsObservableList.setAll(reglements);
			return;
		}
		List <Reglement> filteredReglements = new ArrayList <Reglement> (reglements);
		if (SearchBarString.charAt(0) == '#') {
			if (SearchBarString.length() > 1) {
				Long id;
				try {
					id = Long.parseLong(SearchBarString.replace("#", ""));
					filteredReglements.removeIf(reglement -> {
						return reglement.getId() != id;
					});
					ReglementsObservableList.setAll(filteredReglements);
				} catch (NumberFormatException e) {
					filteredReglements.removeIf(reglement -> {
						return !reglement.getBL().getClient().getLastName().toLowerCase().contains(SearchBarString.toLowerCase())
							&& !reglement.getBL().getClient().getFirstName().toLowerCase().contains(SearchBarString.toLowerCase());
					});
					ReglementsObservableList.setAll(filteredReglements);
					return;
				}
			}
		} else {
			filteredReglements.removeIf(reglement -> {
				return !reglement.getBL().getClient().getLastName().toLowerCase().contains(SearchBarString.toLowerCase())
					&& !reglement.getBL().getClient().getFirstName().toLowerCase().contains(SearchBarString.toLowerCase());
			});
			ReglementsObservableList.clear();
			ReglementsObservableList.addAll(filteredReglements);
		}
	}
	
	private void addEvents() {
		SearchBar.textProperty().addListener((observable) -> filterReglements());
		
		ReglementsTableView.setRowFactory(tv -> {
            TableRow<Reglement> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Reglement rowData = row.getItem();
                    AfficherReglement afficherReglement = new AfficherReglement(rowData.getBL());
                    afficherReglement.setReglementDeleteCallBack(reglement -> {
                    	ReglementsObservableList.removeIf(t -> t.getId() == reglement.getId());
        			});
                    afficherReglement.setReglementAddCallBack(reglement -> {
                    	ReglementsObservableList.add(reglement);
        			});
                    afficherReglement.setReglementEditCallBack(reglement -> {
        				for (Reglement c: ReglementsObservableList) {
        					if (c.getId() == reglement.getId()) {
        						c.setDate(reglement.getDate());
        						c.setMontant(reglement.getMontant());
        						c.setType(reglement.getType());
        						c.setNumero_cheque(reglement.getNumero_cheque());
        						c.setDate_echeance(reglement.getDate_echeance());
        						c.setBanque(reglement.getBanque());
        						c.setNom(reglement.getNom());
        						
        						// observable lists wont detect changes if values inside an element are changed
        						ReglementsObservableList.setAll(reglements);
        						break;
        					}
        				}
        			});
                }
            });
            return row ;
        });
	}
	
	public ListeDesReglements() {
		initWindow();
		addStylesToNodes();
		appendNodesToWindow();
		getReglements();
		updateColumns();
		addEvents();
		window.show();
	}
}
