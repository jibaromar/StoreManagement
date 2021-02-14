package application;

import java.util.Collections;
import java.util.function.Consumer;

import application.modals.Confirmation;
import application.modals.NOTIF_TYPE;
import application.modals.Notification;
import dao.ClientDaoImpl;
import javafx.geometry.Pos;
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
import utils.Validators;

public class AfficherClient {
	VBox root = new VBox();
	double windowWidth = 700;
	double windowHeight = 550;
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
	
	Button EditClientButton = new Button("Modifier");
	Button DeleteButton = new Button("Supprimer");
	
	HBox LeftButtonsContainer = new HBox();
	Button SubmitButton = new Button("Modifier");
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
		window.initModality(Modality.APPLICATION_MODAL);
		
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
		
		RightButtonsContainer.getChildren().addAll(EditClientButton, DeleteButton);
		Container.getChildren().add(RightButtonsContainer);
		
		LeftButtonsContainer.getChildren().addAll(SubmitButton, CancelButton);		
		
		root.getChildren().add(Container);
	}
	
	private void addEvents() {
		DeleteButton.setOnAction(event -> {
			Confirmation confirmation = new Confirmation("Supprimer client", "Vous êtes sûr de vouloir supprimer ce client?");
			confirmation.setResponseCallBack(response -> {
				if (response == true) {
					if (new ClientDaoImpl().delete(client.getId())) {
						ClientDeleteCallback.accept(client);
					}
					window.close();
				}
			});
		});
		SubmitButton.setOnAction(event -> {
			if (isValidForm()) {
				Confirmation confirmation = new Confirmation("Modifier le client", "Vous êtes sûr de vouloir modifier les données de ce client?");
				confirmation.setResponseCallBack(response -> {
					if (response == true) {
						client.setLastName(LastNameTextField.getText());
						client.setFirstName(FirstNameTextField.getText());
						client.setPhone(PhoneTextField.getText());
						client.setEmail(EmailTextField.getText());
						client.setAddress(AddressTextField.getText());
						
						if (new ClientDaoImpl().edit(client)) {
							ClientEditCallback.accept(client);
						}
						window.close();
					}
				});
			} else {
				new Notification(NOTIF_TYPE.ERROR, "Les données du formulaire ne sont pas valide.");
			}
		});
		CancelButton.setOnAction(event -> {
			Confirmation confirmation = new Confirmation("Annuler les modifications", "Vous êtes sûr de vouloir annuler les modifications sur ce client?");
			confirmation.setResponseCallBack(response -> {
				if (response == true) {
					setDefaultValues();
					
					// enable window close button
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
				}
			});
		});
		EditClientButton.setOnAction(event -> {
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
	
	private boolean isValidForm() {
		boolean isValid = true;
		if (!Validators.isName(LastNameTextField.getText())){
			if (!LastNameTextField.getStyleClass().contains("invalidTextField")) {
				LastNameTextField.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			LastNameTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		if (!Validators.isName(FirstNameTextField.getText())){
			if (!FirstNameTextField.getStyleClass().contains("invalidTextField")) {
				FirstNameTextField.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			FirstNameTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		if (!Validators.isPhone(PhoneTextField.getText())){
			if (!PhoneTextField.getStyleClass().contains("invalidTextField")) {
				PhoneTextField.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			PhoneTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		if (!Validators.isEmail(EmailTextField.getText())){
			if (!EmailTextField.getStyleClass().contains("invalidTextField")) {
				EmailTextField.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			EmailTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		if (Validators.isEmpty(AddressTextField.getText())){
			if (!AddressTextField.getStyleClass().contains("invalidTextField")) {
				AddressTextField.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			AddressTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		return isValid;
	}
	
	private void addTextFieldsValidators() {
		LastNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
				isValidForm();
		});
		FirstNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
				isValidForm();
		});
		PhoneTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			isValidForm();
		});
		EmailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
				isValidForm();
		});
		AddressTextField.textProperty().addListener((observable, oldValue, newValue) -> {
				isValidForm();
		});
	}
	
	private void setDefaultValues() {
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
		setDefaultValues();
		appendNodesToWindow();
		addEvents();
		addTextFieldsValidators();
		window.show();
	}
}
