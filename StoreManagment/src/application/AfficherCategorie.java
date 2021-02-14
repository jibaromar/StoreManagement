package application;

import java.util.Collections;
import java.util.function.Consumer;

import application.modals.Confirmation;
import application.modals.NOTIF_TYPE;
import application.modals.Notification;
import dao.CategorieDaoImpl;
import javafx.geometry.Pos;
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

public class AfficherCategorie {
	VBox root = new VBox();
	double windowWidth = 700;
	double windowHeight = 450;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	Categorie categorie = null;
	
	private Consumer<Categorie> CategorieDeleteCallback ;

    public void setCategorieDeleteCallback(Consumer<Categorie> callback) {
        this.CategorieDeleteCallback = callback ;
    }
    
    private Consumer<Categorie> CategorieEditCallback;

    public void setCategorieEditCallback(Consumer<Categorie> callback) {
        this.CategorieEditCallback = callback ;
    }
	
	
	Label TitleLabel = new Label("");
	
	VBox Container = new VBox();
	
	Label LabelLabel = new Label("Label:");
	TextField LabelTextField = new TextField();
	
	Label DescriptionLabel = new Label("Description:");
	TextArea DescriptionTextArea = new TextArea();
	
	HBox RightButtonsContainer = new HBox();
	
	Button EditCategorieButton = new Button("Modifier");
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
		window.setTitle("Détail du categorie");
		window.getIcons().add(new Image("file:store.jpg"));
		window.initModality(Modality.APPLICATION_MODAL);
		
	}
	private void appendNodesToWindow() {
		root.getChildren().add(TitleLabel);

		LabelTextField.setDisable(true);		
		Container.getChildren().addAll(LabelLabel, LabelTextField);
		DescriptionTextArea.setDisable(true);
		Container.getChildren().addAll(DescriptionLabel, DescriptionTextArea);
		
		RightButtonsContainer.getChildren().addAll(EditCategorieButton, DeleteButton);
		Container.getChildren().add(RightButtonsContainer);
		
		LeftButtonsContainer.getChildren().addAll(SubmitButton, CancelButton);		
		
		root.getChildren().add(Container);
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
	
	private void addEvents() {
		DeleteButton.setOnAction(event -> {
			Confirmation confirmation = new Confirmation("Supprimer catégorie", "Vous êtes sûr de vouloir supprimer cette catégorie?");
			confirmation.setResponseCallBack(response -> {
				if (response == true) {
					if (new CategorieDaoImpl().delete(categorie.getId())) {
						CategorieDeleteCallback.accept(categorie);
						new Notification(NOTIF_TYPE.SUCCESS, "La categorie est supprimée avec succès.");
					}
					window.close();
				}
			});
		});
		SubmitButton.setOnAction(event -> {
			if (isValidForm()) {
				Confirmation confirmation = new Confirmation("Modifier catégorie", "Vous êtes sûr de vouloir modifier cette catégorie?");
				confirmation.setResponseCallBack(response -> {
					if (response == true) {
						categorie.setLabel(LabelTextField.getText().trim().replaceAll("\\s+", " "));
						categorie.setDescription(DescriptionTextArea.getText().trim().replaceAll("\\s+", " "));

						if (new CategorieDaoImpl().edit(categorie)) {
							CategorieEditCallback.accept(categorie);
							new Notification(NOTIF_TYPE.SUCCESS, "La categorie est modifiée avec succès.");

						}
						window.close();
					}
				});
			} else {
				new Notification(NOTIF_TYPE.ERROR, "Les données du formulaire ne sont pas valide.");
			}
		});
		CancelButton.setOnAction(event -> {
			Confirmation confirmation = new Confirmation("Annuler les modifications", "Vous êtes sûr de vouloir annuler les modifications sur cette catégorie?");
			confirmation.setResponseCallBack(response -> {
				if (response == true) {
					// enable window close button
					window.setOnCloseRequest(e -> {
						window.close();
					});
					
					// change titles
					window.setTitle("Détail du categorie");
					TitleLabel.setText(categorie.getLabel());
								
					// disable text fields
					LabelTextField.setDisable(true);
					DescriptionTextArea.setDisable(true);
								
					// removing bottom buttons
					Container.getChildren().remove(LeftButtonsContainer);
					Container.getChildren().add(RightButtonsContainer);
				}
			});
		});
		EditCategorieButton.setOnAction(event -> {
			// disable window close button
			window.setOnCloseRequest(e -> {
				e.consume();
			});
			
			// change titles
			window.setTitle("Modifié détail du categorie");
			TitleLabel.setText("MODIFIER: " + categorie.getLabel());
			
			// enable text fields
			LabelTextField.setDisable(false);
			DescriptionTextArea.setDisable(false);
			
			// removint top buttons
			Container.getChildren().remove(RightButtonsContainer);
			Container.getChildren().add(LeftButtonsContainer);
		});
	}
	
	private void setDefaultValues(Categorie categorie) {
		TitleLabel.setText(categorie.getLabel());
		
		LabelTextField.setText(categorie.getLabel());
		DescriptionTextArea.setText(categorie.getDescription());
	}
	
	public AfficherCategorie(Categorie categorie) {
		this.categorie = categorie;
		addStylesToNodes();
		initWindow();
		setDefaultValues(categorie);
		appendNodesToWindow();
		addEvents();
		
		window.show();
	}
}
