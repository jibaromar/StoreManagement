package application;

import java.time.LocalDate;
import java.util.List;

import dao.ClientDaoImpl;
import dao.IBLDao;
import dao.LigneCommandeDaoImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.BL;
import model.Client;
import model.LigneCommande;

public class NouveauBonDeLivraison {
	VBox root = new VBox();
	double windowWidth = 900;
	double windowHeight = 600;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	Client client;
	
	List <LigneCommande> lignesCommande;
	
	ObservableList <LigneCommande> LigneCommandeObservableList = FXCollections.observableArrayList();
	
	Label TitleLabel = new Label("NOUVEAU BON DE LIVRAISON");
	
	VBox Container = new VBox();
	
	BorderPane Top = new BorderPane();
	
	VBox BLDetails = new VBox();
	
	Label BLDetailsLabel = new Label("DETAILS DE BON DE LIVRAISON");
	
	Label ClientLabel = new Label("Client:");
	TextField ClientTextField = new TextField();
	
	Label DateLabel = new Label("Date:");
	DatePicker DateInput = new DatePicker();
	
	VBox Payment = new VBox();
	
	Label PaymentLabel = new Label("REGLEMENT DE BON DE LIVRAISON");
	
	GridPane PaymentContainer = new GridPane();
	Label TotalHTLabel = new Label("Total HT: ");
	Label TotalHT = new Label("Total HT: ");
	Label TVA1Label = new Label("TVA 7%: ");
	Label TVA1 = new Label("0.0");
	Label TVA2Label = new Label("TVA 20%: ");
	Label TVA2 = new Label("0.0");
	Label TotalTTCLabel = new Label("TotalTTC: ");
	Label TotalTTC = new Label("0.0");
	
	TableView <LigneCommande> LignesCommandeTableView = new TableView<LigneCommande>();
	TableColumn <LigneCommande, Long> columnId = new TableColumn <LigneCommande, Long>("Id");
	TableColumn <LigneCommande, String> columnProduitDesignation = new TableColumn <LigneCommande, String>("Designation");
	TableColumn <LigneCommande, String> columnProduitCategorieLabel = new TableColumn <LigneCommande, String>("Catégorie");
	TableColumn <LigneCommande, String> columnProduitPrixVente = new TableColumn <LigneCommande, String>("Prix");
	TableColumn <LigneCommande, Long> columnQte = new TableColumn <LigneCommande, Long>("Quantité");
	TableColumn <LigneCommande, String> columnTotal = new TableColumn <LigneCommande, String>("Sous-total");

	HBox ButtonsContainer = new HBox();
	
	Button AddClientButton = new Button("Ajouter le client");
	Button CancelButton = new Button("Annuler");

	private void addStylesToNodes() {
		scene.getStylesheets().add("assets/css/styles.css");
		root.getStyleClass().add("root");
		
		TitleLabel.getStyleClass().add("titleLabel");
		TitleLabel.setMinWidth(windowWidth);
		
		Container.getStyleClass().add("container");
		Container.setSpacing(20);
		
		BLDetailsLabel.getStyleClass().add("titleLabel");
		ClientTextField.setCursor(Cursor.HAND);
		
		PaymentLabel.getStyleClass().add("titleLabel");
		
		LignesCommandeTableView.getStyleClass().add("tableView");
		
		ButtonsContainer.getStyleClass().add("buttonsContainer");
		ButtonsContainer.setSpacing(10);
	}

	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Ajouté un nouveau bon de livraison");
		window.getIcons().add(new Image("file:store.jpg"));
		window.initModality(Modality.APPLICATION_MODAL);  
	}
	
	private void appendNodesToWindow() {
		root.requestFocus();
		root.getChildren().add(TitleLabel);

		DateInput.setValue(LocalDate.now());
		BLDetailsLabel.setMinWidth(300);
		BLDetailsLabel.setPadding(new Insets(0, 10, 0 ,10));;
		ClientTextField.setEditable(false);
		BLDetails.getChildren().addAll(BLDetailsLabel, ClientLabel, ClientTextField, DateLabel, DateInput);
		BLDetails.setSpacing(10);
		Top.setLeft(BLDetails);
		
		PaymentLabel.setMinWidth(300);
		PaymentLabel.setPadding(new Insets(0, 10, 0 ,10));
		//, TotalHTLabel, TotalHT, TVA1Label, TVA2Label, TotalTTCLabel
		PaymentContainer.add(TotalHTLabel, 0, 0);
		PaymentContainer.add(TotalHT, 1, 0);
		PaymentContainer.add(TVA1Label, 0, 1);
		PaymentContainer.add(TVA1, 1, 1);
		PaymentContainer.add(TVA2Label, 0, 2);
		PaymentContainer.add(TVA2, 1, 2);
		PaymentContainer.add(TotalTTCLabel, 0, 3);
		PaymentContainer.add(TotalTTC, 1, 3);
		
		Payment.getChildren().addAll(PaymentLabel, PaymentContainer);
		Payment.setSpacing(10);
		Top.setRight(Payment);
		
		Container.getChildren().add(Top);
		
		LignesCommandeTableView.getColumns().addAll(columnId, columnProduitDesignation, columnProduitCategorieLabel, columnProduitPrixVente, columnQte, columnTotal);
		LignesCommandeTableView.setItems(LigneCommandeObservableList);
		
		Container.getChildren().add(LignesCommandeTableView);
		
		ButtonsContainer.getChildren().addAll(AddClientButton, CancelButton);
		Container.getChildren().add(ButtonsContainer);
		
		root.getChildren().add(Container);
	}
	
	private void updateColumns() {
		columnId.setCellValueFactory(new PropertyValueFactory <LigneCommande, Long> ("id"));
		columnId.setPrefWidth(50);
		
		columnProduitDesignation.setCellValueFactory(row -> {
			return new SimpleStringProperty(row.getValue().getProduit().getDesignation());
		});
		columnProduitDesignation.setPrefWidth(320);
		
		columnProduitCategorieLabel.setCellValueFactory(row -> {
			return new SimpleStringProperty(row.getValue().getProduit().getCategorieId() + "");
		});
		columnProduitCategorieLabel.setPrefWidth(200);
		
		columnProduitPrixVente.setCellValueFactory(row -> {
			return new SimpleStringProperty(row.getValue().getProduit().getSellingPrice() + "");
		});
		columnProduitPrixVente.setPrefWidth(100);
		
		columnQte.setCellValueFactory(new PropertyValueFactory <LigneCommande, Long> ("quantity"));
		columnQte.setPrefWidth(100);
		
		columnTotal.setCellValueFactory(row -> {
			return new SimpleStringProperty(row.getValue().getProduit().getSellingPrice() * row.getValue().getQuantity() + "");
		});
		columnProduitPrixVente.setPrefWidth(100);
	}
	
	private void addEvents() {
		CancelButton.setOnAction(event -> {	
			window.close();
		});
		ClientTextField.setOnMouseClicked(event -> {
			SelectionnerUnClient selectionnerUnClient = new SelectionnerUnClient(client);
			selectionnerUnClient.setClientSelectCallBack(client -> {
				this.client = client;
				ClientTextField.setText(client.getLastName() + " " + client.getFirstName());
				window.show();
			});
		});
		window.setOnCloseRequest(event -> {
			event.consume();
		});
	}
	
	private void getLignesCommande() {
		LigneCommandeDaoImpl ligneCommandeDaoImpl = new LigneCommandeDaoImpl();
		lignesCommande = ligneCommandeDaoImpl.getAll(1);
		
		LigneCommandeObservableList.addAll(lignesCommande);
	}
	
	private void updatePayment() {
		double TotalHTValue = 0, TVA1Value, TVA2Value, TotalTTCValue;
		for(LigneCommande lc: lignesCommande) {
			TotalHTValue += lc.getProduit().getSellingPrice() * lc.getQuantity();
		}
		TVA1Value = TotalHTValue * 0.07;
		TVA2Value = TotalHTValue * 0.2;
		TotalHT.setText(TotalHTValue + "");
		TVA1.setText(TVA1Value + "");
		TVA2.setText(TVA2Value + "");
		TotalTTC.setText(TotalHTValue + TVA1Value + TVA2Value + "");
	}
	
	public NouveauBonDeLivraison() {
		addStylesToNodes();
		initWindow();
		appendNodesToWindow();
		getLignesCommande();
		updateColumns();
		updatePayment();
		addEvents();
		
		window.show();
	}
}
