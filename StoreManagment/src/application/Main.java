package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import application.modals.NOTIF_TYPE;
import application.modals.Notification;
import dao.BanqueDaoImpl;
import dao.IBanqueDao;
import dao.IReglementDao;
import dao.ReglementDaoImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Banque;
import model.Reglement;


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
		menus.get("Paiements").add("Nouveau paiement");
		
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
//		System.out.println("/* testing banque dao */");
//		IBanqueDao ibd = new BanqueDaoImpl();
//		
//		System.out.println("// getting all banques");
//		List<Banque> bqs = ibd.getAll();
//		System.out.println(bqs);
//		
//		System.out.println("/* testing reglement dao */");
//		IReglementDao ird = new ReglementDaoImpl();
//		
//		System.out.println("// adding reglement espece");
//		Reglement rg1 = new Reglement(LocalDate.now(), 1000);
//		System.out.println(rg1);
//		rg1 = ird.add(rg1);
//		System.out.println("new rg1: " + rg1);
//		
//		System.out.println("// adding reglement cheque");
//		Reglement rg2 = new Reglement(LocalDate.now(), 1000, 1, LocalDate.now(), bqs.get(0), "Omar JIBAR");
//		System.out.println(rg2);
//		rg2 = ird.add(rg2);
//		System.out.println("new rg2: " + rg2);
//		
//		System.out.println("// editing rg1");
//		rg1.setType("CHEQUE");
//		rg1.setNumero_cheque(2L);
//		rg1.setDate_echance(LocalDate.now());
//		rg1.setBanque(bqs.get(1));
//		rg1.setNom("Abdelwahab Naji");
//		ird.edit(rg1);
//		System.out.println("edit rg1: " + ird.getOne(rg1.getId()));
//		
//		System.out.println("// edititng rg2");
//		rg2.setType("ESPECE");
//		rg2.setNumero_cheque(null);
//		rg2.setDate_echance(null);
//		rg2.setBanque(null);
//		rg2.setNom(null);
//		ird.edit(rg2);
//		System.out.println("edit rg2: " + ird.getOne(rg2.getId()));
//		
//		System.out.println("/* show all */");
//		System.out.println(ird.getAll());
//		
//		System.out.println("// delete rg1");
//		ird.delete(rg1.getId());
//		System.out.println("delete rg1" + ird.getAll());
//		
//		System.out.println("// delete rg2");
//		ird.delete(rg2.getId());
//		System.out.println("delete rg2" + ird.getAll());
//	}

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
