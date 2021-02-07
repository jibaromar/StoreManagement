package application;

import java.util.Collections;

import application.modals.Confirmation;
import application.modals.NOTIF_TYPE;
import application.modals.Notification;
import dao.ClientDaoImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class NouveauClient {
	VBox root = new VBox();
	double windowWidth = 700;
	double windowHeight = 550;
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
	
	Button AddClientButton = new Button("Ajouter");
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
			Confirmation confirmation = new Confirmation("Annuler l'ajout du client", "Vous êtes sûr de vouloir annuler l'ajout de ce client?");
			confirmation.setResponseCallBack(response -> {
				if (response == true) {
					window.close();
				}
			});
		});
		AddClientButton.setOnAction(event -> {
			if (isValidForm()) {
				Confirmation confirmation = new Confirmation("Ajouter le nouveau client", "Vous êtes sûr de vouloir ajouter le nouveau client?");
				confirmation.setResponseCallBack(response -> {
					if (response == true) {
						Client client = new Client(0, LastNameTextField.getText(), FirstNameTextField.getText(), PhoneTextField.getText(), EmailTextField.getText(), AddressTextField.getText());
						new ClientDaoImpl().add(client);
						window.close();
					}
				});
			} else {
				new Notification(NOTIF_TYPE.ERROR, "Les données du formulaire sont invalide.");
			}
		});
		window.setOnCloseRequest(event -> {
			event.consume();
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
		LastNameTextField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!Validators.isName(newValue)) {
					if (!LastNameTextField.getStyleClass().contains("invalidTextField")) {
						if (!LastNameTextField.getStyleClass().contains("invalidTextField")) {
							LastNameTextField.getStyleClass().add("invalidTextField");						
						}
					}
				} else {
					LastNameTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
				}
			}
		});
		FirstNameTextField.textProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!Validators.isName(newValue)) {
					if (!FirstNameTextField.getStyleClass().contains("invalidTextField")) {
						FirstNameTextField.getStyleClass().add("invalidTextField");
					}
				} else {
					FirstNameTextField.getStyleClass().removeAll("invalidTextField");
				}
			}
		});
		PhoneTextField.textProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!Validators.isPhone(newValue)) {
					if (!PhoneTextField.getStyleClass().contains("invalidTextField")) {
						PhoneTextField.getStyleClass().add("invalidTextField");
					}
				} else {
					PhoneTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
				}
			}
		});
		EmailTextField.textProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!Validators.isEmail(newValue)) {
					if (!EmailTextField.getStyleClass().contains("invalidTextField")) {
						EmailTextField.getStyleClass().add("invalidTextField");
					}
				} else {
					EmailTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
				}
			}
		});
	}
	
	public NouveauClient() {
		addStylesToNodes();
		initWindow();
		appendNodesToWindow();
		addEvents();
		addTextFieldsValidators();
		window.show();
	}
}
