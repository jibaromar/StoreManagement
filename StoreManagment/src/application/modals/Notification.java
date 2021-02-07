package application.modals;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Notification {
	VBox root = new VBox();
	double windowWidth = 450;
	double windowHeight = 120;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();	
	
	Text Message = new Text("");
	
	Button OK = new Button("OK");
	BorderPane ButtonsContainer = new BorderPane(OK, null, null, null, null);
	
	VBox Container = new VBox(Message, ButtonsContainer);
	
	private void initWindow(String title) {
		window.setScene(scene);
		window.setTitle(title);
		window.getIcons().add(new Image("file:store.jpg"));
	}
	
	private void addNodeStyles() {
		scene.getStylesheets().add("assets/css/styles.css");
		root.getStyleClass().add("root");
		
		Container.getStyleClass().add("container");
		Container.setSpacing(20);
		
		ButtonsContainer.getStyleClass().add("buttonsContainer");
	}
	
	private void appendNodesToWindow() {
		Message.setWrappingWidth(windowWidth - 40);
		OK.setMinWidth(windowWidth / 2.5);
		root.getChildren().add(Container);
	}
	
	private void addEvents() {
		OK.setOnAction(event -> {
			window.close();
		});
	}
	
	public Notification(NOTIF_TYPE t, String message) {
		String title = "";
		switch(t) {
			case INFO: title = "Information"; break;
			case WARNING: title = "Attention!"; break;
			case ERROR: title = "Erreur"; break;
			case SUCCESS: title = "Succès";
		}
		initWindow(title);
		Message.setText(message);
		addNodeStyles();
		appendNodesToWindow();
		addEvents();
		window.show();
		
	}
}
