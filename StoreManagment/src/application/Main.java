package application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	BorderPane root = new BorderPane();
	Scene scene = new Scene(root, 1000, 600);
	MenuBar menuBar = new MenuBar();

	private void createMenuAndAddEvents() {
		// menus map syntaxe: 
		// {
		// 	 "Menu name1": ["Menu item 1", "Menu item 2", ...],
		// 	 "Menu name2": [],
		//	 ...
		// }
		Map <String, ArrayList<String>> menus = new LinkedHashMap <String, ArrayList<String>> ();
		
		// Adding Products menu and it's submenus
		menus.put("Produits", new ArrayList <String> ());
		menus.get("Produits").add("Nouveau produit");
		menus.get("Produits").add("Liste des produits");
		
		// Adding Clients menu and it's submenus
		menus.put("Clients", new ArrayList <String> ());
		menus.get("Clients").add("Nouveau client");
		menus.get("Clients").add("Liste des clients");
		
		// Adding sales menu and it's submenus
		menus.put("Ventes", new ArrayList <String> ());
		menus.get("Ventes").add("Nouveau bon de livraison");
		menus.get("Ventes").add("Liste des bons de livraison");
		
		// Adding payments menu and it's submenus
		menus.put("Paiements", new ArrayList <String> ());
		
		// Adding inventory menu and it's submenus
		menus.put("Inventaire", new ArrayList <String> ());
		
		// Adding Help menu and it's submenus
		menus.put("Aide", new ArrayList <String> ());
		
		// Creating the menus and menus items from the Map and adding Events
		for (Map.Entry <String, ArrayList<String>> entry : menus.entrySet()) {
			Menu menu = new Menu(entry.getKey());
			
			if (!entry.getValue().isEmpty()) {
				for (String item : entry.getValue()) {
					MenuItem menuItem = new MenuItem(item);
					menuItem.setOnAction(event -> {
						// Create the name of class to invoke when action occures
						try {
							Class.forName(createNameOfClassFromMenuItem(item)).getConstructor().newInstance();
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
					menu.getItems().add(menuItem);				
				}
			}
			
			menuBar.getMenus().add(menu);
		}

		root.setTop(menuBar);
	}
	
	private String createNameOfClassFromMenuItem(String menuItemString) {
		String [] strs = menuItemString.split(" ");
		String resultString = "application.";
		for (String str : strs) {
			char [] strCharArray = str.toCharArray();
			strCharArray[0] = Character.toUpperCase(strCharArray[0]);
			for (char c : strCharArray) {
				resultString += c;
			}
		}
		return resultString;
	}
	
	private void addStylesToNodes() {
		scene.getStylesheets().add("assets/css/styles.css");
		root.getStyleClass().add("main");		
	}

	@Override
	public void start(Stage window) { 
		try {
			createMenuAndAddEvents();
			addStylesToNodes();
			window.setScene(scene);
			window.setTitle("Gestion de magasin");
			window.getIcons().add(new Image("file:store.jpg"));
			window.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
