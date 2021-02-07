package application.modals;

import java.util.function.Consumer;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Confirmation {
	VBox root = new VBox();
	double windowWidth = 450;
	double windowHeight = 120;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	Consumer<Boolean> ResponseCallBack;
	
	public void setResponseCallBack (Consumer<Boolean> callback) {
		this.ResponseCallBack = callback;
	}
	
	
	Text Message = new Text("");
	
	Button Yes = new Button("Oui");
	Button No = new Button("Non");
	BorderPane ButtonsContainer = new BorderPane(null, null, No, null, Yes);
	
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
		Message.setWrappingWidth(windowWidth - 40);
		ButtonsContainer.getStyleClass().add("buttonsContainer");
	}
	
	private void appendNodesToWindow() {
		Yes.setMinWidth(windowWidth / 2.5);
		No.setMinWidth(windowWidth / 2.5);
		root.getChildren().add(Container);
	}
	
	private void addEvents() {
		Yes.setOnAction(event -> {
			ResponseCallBack.accept(true);
			window.close();
		});
		No.setOnAction(event -> {
			ResponseCallBack.accept(false);
			window.close();
		});
	}
	
	public Confirmation(String title, String message) {
		initWindow(title);
		Message.setText(message);
		addNodeStyles();
		appendNodesToWindow();
		addEvents();
		window.show();
		
	}
}
