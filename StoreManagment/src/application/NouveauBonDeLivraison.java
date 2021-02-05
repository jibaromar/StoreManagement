package application;

import java.time.LocalDate;
import java.util.List;

import dao.ClientDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.BL;
import model.Client;

public class NouveauBonDeLivraison {
	VBox root = new VBox();
	double windowWidth = 900;
	double windowHeight = 600;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	List <BL> bls;
	
	ObservableList <BL> BLObservableList = FXCollections.observableArrayList();
	
	Label TitleLabel = new Label("NOUVEAU BON DE LIVRAISON");
	
	VBox Container = new VBox();
	
	BorderPane Top = new BorderPane();
	
	VBox BLDetails = new VBox();
	
	Label BLDetailsLabel = new Label("DETAILS DE BON DE LIVRAISON");
	
	Label ClientLabel = new Label("Client:");
	TextField ClientTextField = new TextField();
	
	Label DateLabel = new Label("Date:");
	DatePicker DateInput = new DatePicker();
	
	VBox Payment = new VBox();
	
	Label PaymentLabel = new Label("REGLEMENT DE BON DE LIVRAISON");
	
	Label TotalHTLabel = new Label("Total HT:");
	Label TVA1Label = new Label("TVA 7%:");
	Label TVA2Label = new Label("TVA 20%:");
	Label TotalLabel = new Label("Total:");
	
	TableView <BL> BLTableView = new TableView<BL>();
	TableColumn <BL, Long> columnId = new TableColumn <BL, Long>("Id");

	HBox ButtonsContainer = new HBox();
	
	Button AddClientButton = new Button("Ajouter le client");
	Button CancelButton = new Button("Annuler");

	private void addStylesToNodes() {
		scene.getStylesheets().add("assets/css/styles.css");
		root.getStyleClass().add("root");
		
		TitleLabel.getStyleClass().add("titleLabel");
		TitleLabel.setMinWidth(windowWidth);
		
		Container.getStyleClass().add("container");
		Container.setSpacing(20);
		
		BLDetailsLabel.getStyleClass().add("titleLabel");
		PaymentLabel.getStyleClass().add("titleLabel");
		
		BLTableView.getStyleClass().add("tableView");
		
		ButtonsContainer.getStyleClass().add("buttonsContainer");
		ButtonsContainer.setSpacing(10);
	}

	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Ajouté un nouveau bon de livraison");
		window.getIcons().add(new Image("file:store.jpg"));
		window.initModality(Modality.APPLICATION_MODAL);  
	}
	
	private void appendNodesToWindow() {
		root.requestFocus();
		root.getChildren().add(TitleLabel);

		DateInput.setValue(LocalDate.now());
		BLDetailsLabel.setMinWidth(300);
		BLDetailsLabel.setPadding(new Insets(0, 10, 0 ,10));;
		ClientTextField.setEditable(false);
		BLDetails.getChildren().addAll(BLDetailsLabel, ClientLabel, ClientTextField, DateLabel, DateInput);
		BLDetails.setSpacing(10);
		Top.setLeft(BLDetails);
		
		PaymentLabel.setMinWidth(300);
		PaymentLabel.setPadding(new Insets(0, 10, 0 ,10));
		Payment.getChildren().addAll(PaymentLabel, TotalHTLabel, TVA1Label, TVA2Label, TotalLabel);
		Payment.setSpacing(10);
		Top.setRight(Payment);
		Container.getChildren().add(Top);
		
		BLTableView.getColumns().addAll(columnId);
		BLTableView.setItems(BLObservableList);
		
		Container.getChildren().add(BLTableView);
		
		ButtonsContainer.getChildren().addAll(AddClientButton, CancelButton);
		Container.getChildren().add(ButtonsContainer);
		
		root.getChildren().add(Container);
	}
	
	private void addEvents() {
		CancelButton.setOnAction(event -> {
			window.close();
		});
		ClientTextField.setOnMouseClicked(event -> {
			System.out.println("clicked");
		});
		window.setOnCloseRequest(event -> {
			event.consume();
		});
	}
	
	public NouveauBonDeLivraison() {
		addStylesToNodes();
		initWindow();
		appendNodesToWindow();
		addEvents();
		
		window.show();
	}
}
