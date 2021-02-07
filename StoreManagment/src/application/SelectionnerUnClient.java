package application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import dao.ClientDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Client;

public class SelectionnerUnClient {
	VBox root = new VBox();
	double windowWidth = 900;
	double windowHeight = 500;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	private Consumer<Client> ClientSelectCallBack;
	
	public void setClientSelectCallBack(Consumer<Client> callback) {
		this.ClientSelectCallBack = callback;
	}
	
	Client selectedClient;
	
	List <Client> clients;
	
	ObservableList <Client> ClientsObservableList = FXCollections.observableArrayList();
	
	VBox Container = new VBox();
	
	Label TitleLabel = new Label("SELECTIONNER UN CLIENT");
	
	TextField SearchBar = new TextField();
	
	TableView <Client> ClientsTableView = new TableView<Client>();
	TableColumn <Client, Long> columnId = new TableColumn <Client, Long>("Id");
	TableColumn <Client, String> columnLastName = new TableColumn <Client, String>("Nom");
	TableColumn <Client, String> columnFirstName = new TableColumn <Client, String>("Prénom");
	TableColumn <Client, String> columnPhone = new TableColumn <Client, String>("Téléphone");
	TableColumn <Client, String> columnEmail = new TableColumn <Client, String>("Email");
	TableColumn <Client, String> columnAddress = new TableColumn <Client, String>("Adresse");
	
	private void addStylesToNodes() {
		scene.getStylesheets().add("assets/css/styles.css");
		
		Container.getStyleClass().add("container");
		Container.setSpacing(20);
		
		TitleLabel.getStyleClass().add("titleLabel");
		TitleLabel.setMinWidth(windowWidth);
		
		SearchBar.getStyleClass().add("searchBar");
		SearchBar.setMaxWidth(windowWidth / 2);
		
		ClientsTableView.getStyleClass().add("tableView");
		
	}

	private void getClients() {
		clients = new ClientDaoImpl().getAll();
		
		ClientsObservableList.addAll(clients);
	}
	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Selectionner un client");
		window.getIcons().add(new Image("file:store.jpg"));
		window.initModality(Modality.APPLICATION_MODAL);  
	}
	
	private void updateColumns() {
		columnId.setCellValueFactory(new PropertyValueFactory <Client, Long> ("id"));
		columnId.setPrefWidth(50);
		
		columnLastName.setCellValueFactory(new PropertyValueFactory <Client, String> ("lastName"));
		columnLastName.setPrefWidth(110);
		
		columnFirstName.setCellValueFactory(new PropertyValueFactory <Client, String> ("firstName"));
		columnFirstName.setPrefWidth(110);
		
		columnPhone.setCellValueFactory(new PropertyValueFactory <Client, String> ("phone"));
		columnPhone.setPrefWidth(100);
		
		columnEmail.setCellValueFactory(new PropertyValueFactory <Client, String> ("email"));
		columnEmail.setPrefWidth(200);
		
		columnAddress.setCellValueFactory(new PropertyValueFactory <Client, String> ("address"));
		columnAddress.setPrefWidth(286);
		
	}
	
	private void appendNodesToWindow() {
		root.getChildren().add(TitleLabel);
		
		SearchBar.setPromptText("Searcher par id (#3) ou par nom/prénom (Omar)");
		Container.getChildren().add(SearchBar);
		root.requestFocus();
		
		ClientsTableView.getColumns().addAll(columnId, columnLastName, columnFirstName, columnPhone, columnEmail, columnAddress);
		ClientsTableView.setItems(ClientsObservableList);
		
		Container.getChildren().addAll(ClientsTableView);
		
		root.getChildren().add(Container);
	}
	
	private void filterClients() {
		String SearchBarString = SearchBar.getText().trim().replaceAll("\\s+", " ");
		if (SearchBarString.equals("")) {
			ClientsObservableList.setAll(clients);
			return;
		}
		List <Client> filteredClients = new ArrayList <Client> (clients);
		if (SearchBarString.charAt(0) == '#') {
			if (SearchBarString.length() > 1) {
				Long id;
				try {
					id = Long.parseLong(SearchBarString.replace("#", ""));
					filteredClients.removeIf(client -> {
						return client.getId() != id;
					});
					ClientsObservableList.setAll(filteredClients);
				} catch (NumberFormatException e) {
					filteredClients.removeIf(client -> {
						return !client.getLastName().toLowerCase().contains(SearchBarString.toLowerCase())
							&& !client.getFirstName().toLowerCase().contains(SearchBarString.toLowerCase());
					});
					ClientsObservableList.setAll(filteredClients);
					System.out.println("ex");
					return;
				}
			}
		} else {
			filteredClients.removeIf(client -> {
				return !client.getLastName().toLowerCase().contains(SearchBarString.toLowerCase())
					&& !client.getFirstName().toLowerCase().contains(SearchBarString.toLowerCase());
			});
			ClientsObservableList.clear();
			ClientsObservableList.addAll(filteredClients);
		}
	}
	
	private void addEvents() {
		SearchBar.textProperty().addListener((observable) -> filterClients());
		
		ClientsTableView.setRowFactory(tv -> {
            TableRow<Client> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Client rowData = row.getItem();
                    ClientSelectCallBack.accept(rowData);
                    window.close();
                }
            });
            return row ;
        });
	}
	
	private void setDefaultValue() {		
		if (selectedClient != null) {
			TableViewSelectionModel<Client> selectionModel = ClientsTableView.getSelectionModel();
			for (int i = 0; i < ClientsObservableList.size(); i++) {
				if (ClientsObservableList.get(i).getId() == selectedClient.getId()) {
					selectionModel.select(i);
					break;
				}
			}
		}
	}
	
	public SelectionnerUnClient(Client selectedClient) {
		this.selectedClient = selectedClient;
		initWindow();
		addStylesToNodes();
		appendNodesToWindow();
		getClients();
		updateColumns();
		setDefaultValue();
		addEvents();
		window.show();
	}
}
