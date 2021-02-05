package application;

import java.util.function.Consumer;

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

public class AfficherCategorie {
	VBox root = new VBox();
	double windowWidth = 700;
	double windowHeight = 600;
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
	Button SubmitButton = new Button("Modifier la categorie");
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
	
	private void addEvents() {
		DeleteButton.setOnAction(event -> {
			if (new CategorieDaoImpl().delete(categorie.getId())) {
				CategorieDeleteCallback.accept(categorie);
			}
			window.close();
		});
		SubmitButton.setOnAction(event -> {
			categorie.setLabel(LabelTextField.getText());
			categorie.setDescription(DescriptionTextArea.getText());

			if (new CategorieDaoImpl().edit(categorie)) {
				CategorieEditCallback.accept(categorie);
			}
			window.close();
		});
		CancelButton.setOnAction(event -> {
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
