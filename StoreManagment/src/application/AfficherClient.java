package application;

import java.util.function.Consumer;

import dao.CategorieDaoImpl;
import dao.ClientDaoImpl;
import dao.ProduitDaoImpl;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Categorie;
import model.Client;

public class AfficherClient {
	VBox root = new VBox();
	double windowWidth = 700;
	double windowHeight = 600;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	Client client = null;
	
	private Consumer<Client> ClientDeleteCallback ;

    public void setClientDeleteCallback(Consumer<Client> callback) {
        this.ClientDeleteCallback = callback ;
    }
    
    private Consumer<Client> ClientEditCallback;

    public void setClientEditCallback(Consumer<Client> callback) {
        this.ClientEditCallback = callback ;
    }
	
	
	Label TitleLabel = new Label("");
	
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

	HBox RightButtonsContainer = new HBox();
	
	Button EditProductButton = new Button("Modifier");
	Button DeleteButton = new Button("Supprimer");
	
	HBox LeftButtonsContainer = new HBox();
	Button SubmitButton = new Button("Modifier le client");
	Button CancelButton = new Button("Annuler");

	private void addStylesToNodes() {
		scene.getStylesheets().add("assets/css/styles.css");
		root.getStyleClass().add("root");
		
		TitleLabel.getStyleClass().add("titleLabel");
		TitleLabel.setMinWidth(windowWidth);
		
		Container.getStyleClass().add("container");
		Container.setSpacing(20);
		
		RightButtonsContainer.getStyleClass().add("buttonsContainer");
		RightButtonsContainer.setSpacing(10);
		RightButtonsContainer.setAlignment(Pos.CENTER_RIGHT);
		
		LeftButtonsContainer.getStyleClass().add("buttonsContainer");
		LeftButtonsContainer.setSpacing(10);
	}

	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Détail du client");
		window.getIcons().add(new Image("file:store.jpg"));
//		window.initModality(Modality.APPLICATION_MODAL);  
	}
	private void appendNodesToWindow() {
		root.getChildren().add(TitleLabel);

		LastNameTextField.setDisable(true);		
		Container.getChildren().addAll(LastNameLabel, LastNameTextField);
		FirstNameTextField.setDisable(true);
		Container.getChildren().addAll(FirstNameLabel, FirstNameTextField);
		PhoneTextField.setDisable(true);
		Container.getChildren().addAll(PhoneLabel, PhoneTextField);
		EmailTextField.setDisable(true);
		Container.getChildren().addAll(EmailLabel, EmailTextField);
		AddressTextField.setDisable(true);
		Container.getChildren().addAll(AddressLabel, AddressTextField);
		
		RightButtonsContainer.getChildren().addAll(EditProductButton, DeleteButton);
		Container.getChildren().add(RightButtonsContainer);
		
		LeftButtonsContainer.getChildren().addAll(SubmitButton, CancelButton);		
		
		root.getChildren().add(Container);
	}
	
	private void addEvents() {
		DeleteButton.setOnAction(event -> {
			if (new ClientDaoImpl().delete(client.getId())) {
				ClientDeleteCallback.accept(client);
			}
			window.close();
		});
		SubmitButton.setOnAction(event -> {
			client.setLastName(LastNameTextField.getText());
			client.setFirstName(FirstNameTextField.getText());
			client.setPhone(PhoneTextField.getText());
			client.setEmail(EmailTextField.getText());
			client.setAddress(AddressTextField.getText());

			if (new ClientDaoImpl().edit(client)) {
				ClientEditCallback.accept(client);
			}
			window.close();
		});
		CancelButton.setOnAction(event -> {
			// disable window close button
			window.setOnCloseRequest(e -> {
				window.close();
			});
					
			// change titles
			window.setTitle("Détail du client");
			TitleLabel.setText(client.getLastName() + " " + client.getFirstName());
						
			// disable text fields
			LastNameTextField.setDisable(true);
			FirstNameTextField.setDisable(true);
			PhoneTextField.setDisable(true);
			EmailTextField.setDisable(true);
			AddressTextField.setDisable(true);
						
			// removing bottom buttons
			Container.getChildren().remove(LeftButtonsContainer);
			Container.getChildren().add(RightButtonsContainer);
		});
		EditProductButton.setOnAction(event -> {
			// disable window close button
			window.setOnCloseRequest(e -> {
				e.consume();
			});
			
			// change titles
			window.setTitle("Modifié détail du client");
			TitleLabel.setText("MODIFIER: " + client.getLastName() + " " + client.getFirstName());
			
			// enable text fields
			LastNameTextField.setDisable(false);
			FirstNameTextField.setDisable(false);
			PhoneTextField.setDisable(false);
			EmailTextField.setDisable(false);
			AddressTextField.setDisable(false);
			
			// removint top buttons
			Container.getChildren().remove(RightButtonsContainer);
			Container.getChildren().add(LeftButtonsContainer);
		});
	}
	
	private void setDefaultValues(Client client) {
		TitleLabel.setText(client.getLastName() + " " + client.getFirstName());
		
		LastNameTextField.setText(client.getLastName());
		FirstNameTextField.setText(client.getFirstName());
		PhoneTextField.setText(client.getPhone());
		EmailTextField.setText(client.getEmail());
		AddressTextField.setText(client.getAddress());
	}
	
	public AfficherClient(Client client) {
		this.client = client;
		addStylesToNodes();
		initWindow();
		setDefaultValues(client);
		appendNodesToWindow();
		addEvents();
		
		window.show();
	}
}
