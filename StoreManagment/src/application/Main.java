package application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dao.IUserDao;
import dao.UserDaoImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;
import utils.Security;


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
		menus.get("Paiements").add("Nouveau reglement");
		menus.get("Paiements").add("Liste des reglements");
		
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
	
//	private void testDao() {
//		System.out.println("/* testing user dao */");
//		IUserDao iud = new UserDaoImpl();
//		
//		System.out.println("// getting all users");
//		List<User> users = iud.getAll();
//		System.out.println(users);
//		
//		System.out.println("// adding user");
//		User usr1 = new User("admin", Security.encrypt("admin".getBytes(), "SHA-512"));
//		System.out.println(usr1);
//		usr1 = iud.add(usr1);
//		System.out.println("new usr1: " + usr1);
//		
//		System.out.println("// editing user");
//		usr1.setName("sherlockomar");
//		usr1.setPassword(Security.encrypt("passwd".getBytes(), "SHA-512"));
//		iud.edit(usr1);
//		System.out.println("edit usr1: " + iud.getOne(usr1.getId()));
//		
//		System.out.println("/* show all users*/");
//		System.out.println(iud.getAll());
//		
//		System.out.println("// delete user");
//		iud.delete(usr1.getId());
//		System.out.println("delete usr1" + iud.getAll());
//	}

	@Override
	public void start(Stage window) { 
		try {
			addStylesToNodes();
			window.setScene(scene);
			window.setTitle("Gestion de magasin");
			window.getIcons().add(new Image("file:store.jpg"));
			window.show();
			Login login = new Login();
			login.setValidUserCallback(n -> {
				createMenuAndAddEvents();				
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
