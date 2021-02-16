package application;

import java.util.Collections;
import java.util.function.Consumer;

import application.modals.Confirmation;
import application.modals.NOTIF_TYPE;
import application.modals.Notification;
import dao.ClientDaoImpl;
import dao.UserDaoImpl;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Client;
import model.User;
import utils.Security;
import utils.Validators;

public class Login {
	VBox root = new VBox();
	double windowWidth = 400;
	double windowHeight = 375;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	Consumer<Void> ValidUserCallback;
	
	public void setValidUserCallback (Consumer <Void> callback) {
		this.ValidUserCallback = callback;
	}
	
	ImageView AvatarImage = new ImageView("assets/img/login.png");
	Label LoginLabel = new Label("S'AUTHENITFIER");
	VBox AvatarWrapper = new VBox(AvatarImage, LoginLabel);
	
	VBox Container = new VBox();
	
	Label UserNameLabel = new Label("Nom d'utilisateur:");
	TextField UserNameTextField = new TextField();
	
	Label PasswordLabel = new Label("Mot de pass:");
	PasswordField PasswordTextField = new PasswordField();
	
	HBox ButtonContainer = new HBox();
	Button SubmitButton = new Button("S'authentifier");

	private void addStylesToNodes() {
		scene.getStylesheets().add("assets/css/styles.css");
		root.getStyleClass().add("loginRoot");
		
		AvatarWrapper.setAlignment(Pos.BASELINE_CENTER);
		
		AvatarImage.setFitWidth(100);
		AvatarImage.setFitHeight(100);
		LoginLabel.setAlignment(Pos.BASELINE_CENTER);
		
		Container.getStyleClass().add("loginContainer");
		Container.setSpacing(20);
		
		ButtonContainer.getStyleClass().add("buttonsContainer");
		ButtonContainer.setSpacing(10);
	}

	private void initWindow() {
		window.setScene(scene);
		window.setTitle("S'authentifier");
		window.getIcons().add(new Image("file:store.jpg"));
		window.initModality(Modality.APPLICATION_MODAL);
		
	}
	private void appendNodesToWindow() {
		
		Container.getChildren().add(AvatarWrapper);
		Container.getChildren().addAll(UserNameLabel, UserNameTextField);
		Container.getChildren().addAll(PasswordLabel, PasswordTextField);
		
		ButtonContainer.getChildren().addAll(SubmitButton);
		Container.getChildren().add(ButtonContainer);
		
		root.getChildren().add(Container);
	}
	
	private void addEvents() {
		SubmitButton.setOnAction(event -> {
			if (isValidForm()) {
				User user = new User(UserNameTextField.getText(), Security.encrypt(PasswordTextField.getText().getBytes(), "SHA-512"));
				if (new UserDaoImpl().isValidUser(user)) {
					ValidUserCallback.accept(null);
					window.close();
				} else {
					PasswordTextField.setText("");
					new Notification(NOTIF_TYPE.ERROR, "Le nom d'utilisateur ou mot de pass est invalide.");
				}
			}
		});
	}
	
	private boolean isValidForm() {
		boolean isValid = true;
		
		isValid = isValid && isValidUserName();
		
		return isValid;
	}
	
	private boolean isValidUserName() {
		boolean isValid = true;
		if (!Validators.isName(UserNameTextField.getText())){
			if (!UserNameTextField.getStyleClass().contains("invalidTextField")) {
				UserNameTextField.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			UserNameTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		return isValid;
	}
	
	private void addTextFieldsValidators() {
		UserNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			isValidUserName();
		});
	}
	
	public Login() {
		addStylesToNodes();
		initWindow();
		appendNodesToWindow();
		addEvents();
		addTextFieldsValidators();
		window.show();
	}
}
