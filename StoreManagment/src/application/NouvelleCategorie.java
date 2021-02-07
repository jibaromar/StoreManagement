package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import application.modals.Confirmation;
import application.modals.NOTIF_TYPE;
import application.modals.Notification;
import dao.CategorieDaoImpl;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Categorie;
import utils.Validators;

public class NouvelleCategorie {
	VBox root = new VBox();
	double windowWidth = 700;
	double windowHeight = 450;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	private Consumer<Categorie> CategorieSelectCallback ;

    public void setCategorieSelectCallback(Consumer<Categorie> callback) {
        this.CategorieSelectCallback = callback ;
    }
	
	List <Categorie> categories = new ArrayList <Categorie> ();
	
	Label TitleLabel = new Label("NOUVELLE CATEGORIE");
	
	VBox Container = new VBox();
	
	Label LabelLabel = new Label("Label:");
	TextField LabelTextField = new TextField();
	
	Label DescriptionLabel = new Label("Description:");
	TextArea DescriptionTextArea = new TextArea();

	HBox ButtonsContainer = new HBox();
	
	Button AddCategorieButton = new Button("Ajouter");
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
		window.setTitle("Ajouté une nouvelle catégorie");
		window.getIcons().add(new Image("file:store.jpg"));
		window.initModality(Modality.APPLICATION_MODAL);  
	}
	
	private void appendNodesToWindow() {
		root.getChildren().add(TitleLabel);
		
		Container.getChildren().addAll(LabelLabel, LabelTextField);
		Container.getChildren().addAll(DescriptionLabel, DescriptionTextArea);
		
		ButtonsContainer.getChildren().addAll(AddCategorieButton, CancelButton);
		Container.getChildren().add(ButtonsContainer);
		
		root.getChildren().add(Container);
	}
	
	private void addEvents() {
		CancelButton.setOnAction(event -> {
			Confirmation confirmation = new Confirmation("Quitter", "Vous êtes sûr de vouloir annuler l'ajout de la catégorie?");
			confirmation.setResponseCallBack(response -> {
				if (response == true) {
					window.close();
				}
			});
		});
		AddCategorieButton.setOnAction(event -> {
			if (isValidForm()) {
				Confirmation confirmation = new Confirmation("Ajouter la catégorie", "Vous êtes sûr de vouloir ajouter la nouvelle catégorie?");
				confirmation.setResponseCallBack(response -> {
					if (response == true) {
						Categorie categorie = new Categorie(0L, LabelTextField.getText(), DescriptionTextArea.getText());
						// si la nouvelle categorie est bien ajoutée à la base de données
						// envoyez la à la fenetre mère pour qu'il soit visible dans la liste des
						// categories
						categorie = new CategorieDaoImpl().add(categorie);
						if (categorie != null) {
							// recuperer la nouvelle categorie de la base de données pour avoir son ID
							CategorieSelectCallback.accept(categorie);		
						}
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
		if (Validators.isEmpty(LabelTextField.getText())){
			if (!LabelTextField.getStyleClass().contains("invalidTextField")) {
				LabelTextField.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			LabelTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		if (Validators.isEmpty(DescriptionTextArea.getText())){
			if (!DescriptionTextArea.getStyleClass().contains("invalidTextField")) {
				DescriptionTextArea.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		}
		return isValid;
	}
	
	public NouvelleCategorie() {
		addStylesToNodes();
		initWindow();
		appendNodesToWindow();
		addEvents();
		
		window.show();
	}
}
