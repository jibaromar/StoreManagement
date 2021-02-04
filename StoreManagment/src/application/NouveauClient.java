package application;

import dao.ClientDaoImpl;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Client;

public class NouveauClient {
	VBox root = new VBox();
	double windowWidth = 700;
	double windowHeight = 600;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	Label TitleLabel = new Label("NOUVEAU CLIENT");
	
	VBox Container = new VBox();
	
	Label LastNameLabel = new Label("Nom:");
	TextField LastNameTextField = new TextField();
	
	Label FirstNameLabel = new Label("Prénom:");
	TextField FirstNameTextField = new TextField();
	
	Label PhoneLabel = new Label("Téléphone:");
	TextField PhoneTextField = new TextField();
	
	Label EmailLabel = new Label("Email:");
	TextField EmailTextField = new TextField();	
	
	Label AddressLabel = new Label("Adresse:");
	TextField AddressTextField =  new TextField();

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
		
		ButtonsContainer.getStyleClass().add("buttonsContainer");
		ButtonsContainer.setSpacing(10);
	}

	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Ajouté un nouveau client");
		window.getIcons().add(new Image("file:store.jpg"));
		window.initModality(Modality.APPLICATION_MODAL);  
	}
	
	private void appendNodesToWindow() {
		root.getChildren().add(TitleLabel);
		
		Container.getChildren().addAll(LastNameLabel, LastNameTextField);
		Container.getChildren().addAll(FirstNameLabel, FirstNameTextField);
		Container.getChildren().addAll(PhoneLabel, PhoneTextField);
		Container.getChildren().addAll(EmailLabel, EmailTextField);
		Container.getChildren().addAll(AddressLabel, AddressTextField);
		
		ButtonsContainer.getChildren().addAll(AddClientButton, CancelButton);
		Container.getChildren().add(ButtonsContainer);
		
		root.getChildren().add(Container);
	}
	
	private void addEvents() {
		CancelButton.setOnAction(event -> {
			window.close();
		});
		AddClientButton.setOnAction(event -> {
			// DAO
			Client client = new Client(0, LastNameTextField.getText(), FirstNameTextField.getText(), PhoneTextField.getText(), EmailTextField.getText(), AddressTextField.getText());
			new ClientDaoImpl().add(client);
			window.close();
		});
		window.setOnCloseRequest(event -> {
			event.consume();
		});
	}
	
	public NouveauClient() {
		addStylesToNodes();
		initWindow();
		appendNodesToWindow();
		addEvents();
		
		window.show();
	}
}
