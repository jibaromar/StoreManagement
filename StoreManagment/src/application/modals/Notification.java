package application.modals;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Notification {
	VBox root = new VBox();
	double windowWidth = 450;
	double windowHeight = 200;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();	
	
	ImageView Icon;
	
	Text Message = new Text("");
	
	Button OK = new Button("OK");
	BorderPane ButtonsContainer = new BorderPane(OK);
	
	VBox Container = new VBox(Message, ButtonsContainer);
	
	private void initWindow(String title) {
		window.setScene(scene);
		window.setTitle(title);
		window.getIcons().add(new Image("file:store.jpg"));
	}
	
	private void addNodeStyles() {
		Icon.setFitWidth(60);
		Icon.setFitHeight(60);
		
		Message.setWrappingWidth(windowWidth - 40);
		Message.setTextAlignment(TextAlignment.CENTER);
		
		OK.setMinWidth(windowWidth / 2.5);
		
		scene.getStylesheets().add("assets/css/styles.css");
		root.getStyleClass().add("root");
		
		Container.getStyleClass().add("container");
		Container.setSpacing(20);
		
		ButtonsContainer.getStyleClass().add("buttonsContainer");
	}
	
	private void appendNodesToWindow() {
		Container.getChildren().add(0, new BorderPane(Icon));
		root.getChildren().add(Container);
	}
	
	private void addEvents() {
		OK.setOnAction(event -> {
			window.close();
		});
	}
	
	public Notification(NOTIF_TYPE t, String message) {
		String title = "";
		String icon = "";
		String color = "";
		switch(t) {
			case INFO: title = "Information"; icon = "assets/img/info.png"; color = "#acdae2"; break;
			case WARNING: title = "Attention!"; icon = "assets/img/warning.png"; color = "#ffd557"; break;
			case ERROR: title = "Erreur"; icon = "assets/img/error.png"; color = "#ee4641"; break;
			case SUCCESS: title = "Succès"; icon = "assets/img/success.png"; color = "#96df73";
		}
		initWindow(title);
		Icon = new ImageView(icon);
		Message.setText(message);
		OK.setStyle("-fx-text-fill: black; -fx-background-color: " + color + ";");
		addNodeStyles();
		appendNodesToWindow();
		addEvents();
		window.show();
		
	}
}
