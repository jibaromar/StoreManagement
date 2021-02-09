package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import application.modals.Confirmation;
import application.modals.NOTIF_TYPE;
import application.modals.Notification;
import dao.BLDaoImpl;
import dao.LigneCommandeDaoImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.BL;
import model.LigneCommande;

public class AfficherBL {
	VBox root = new VBox();
	double windowWidth = 921;
	double windowHeight = 600;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	Consumer<BL> BLDeleteCallback;
	
	public void setBLDeleteCallback(Consumer<BL> callback) {
		this.BLDeleteCallback = callback;
	}

	Consumer<BL> BLEditCallback;
	
	public void setBLEditCallback(Consumer<BL> callback) {
		this.BLEditCallback = callback;
	}
	
	BL tmpBL, bl;

	List <LigneCommande> onlineLignesCommande = new ArrayList<>();
	ObservableList <LigneCommande> LigneCommandeObservableList = FXCollections.observableArrayList();
	
	Label TitleLabel = new Label("BON DE LIVRAISON N° DE");
	
	VBox Container = new VBox();
	
	BorderPane Top = new BorderPane();
	
	VBox BLDetails = new VBox();
	
	Label BLDetailsLabel = new Label("DETAILS DE BON DE LIVRAISON");
	
	Label BLIdLabel = new Label("Id du bon de livraison:");
	TextField BLIdTextField = new TextField();
	
	Label ClientLabel = new Label("Client:");
	TextField ClientTextField = new TextField();
	
	Label DateLabel = new Label("Date:");
	DatePicker DateInput = new DatePicker();
	
	Button AddCommandLineButton = new Button();
	Button DeleteCommandLineButton = new Button();
	
	HBox TopButtonsHboxContainer = new HBox(AddCommandLineButton, DeleteCommandLineButton);
	
	VBox Payment = new VBox();
	
	Label PaymentLabel = new Label("REGLEMENT DE BON DE LIVRAISON");
	
	GridPane PaymentContainer = new GridPane();
	Label TotalHTLabel = new Label("Total HT: ");
	Label TotalHT = new Label("0.0");
	Label TVA1Label = new Label("TVA 7%: ");
	Label TVA1 = new Label("0.0");
	Label TVA2Label = new Label("TVA 20%: ");
	Label TVA2 = new Label("0.0");
	Label TotalTTCLabel = new Label("TotalTTC: ");
	Label TotalTTC = new Label("0.0");
	
	TableView <LigneCommande> LignesCommandeTableView = new TableView<LigneCommande>();
	TableColumn <LigneCommande, String> columnId = new TableColumn <LigneCommande, String>("Id");
	TableColumn <LigneCommande, String> columnProduitDesignation = new TableColumn <LigneCommande, String>("Designation");
	TableColumn <LigneCommande, String> columnProduitCategorieLabel = new TableColumn <LigneCommande, String>("Catégorie");
	TableColumn <LigneCommande, String> columnProduitPrixVente = new TableColumn <LigneCommande, String>("Prix");
	TableColumn <LigneCommande, Long> columnQte = new TableColumn <LigneCommande, Long>("Quantité");
	TableColumn <LigneCommande, String> columnTotal = new TableColumn <LigneCommande, String>("Sous-total");

	Button SaveBLButton = new Button("Enregistrer");
	Button CloseButton = new Button("Fermer");
	Button CancelButton = new Button("Annuler");
	HBox BottomLeftButtonsHboxContainer = new HBox(CloseButton);
	
	Button EditButton = new Button("Modifier");
	Button DeleteButton = new Button("Supprimer");
	HBox BottomRightButtonsHboxContainer = new HBox(EditButton, DeleteButton);

	BorderPane BottomButtonsBorderPaneContainer = new BorderPane(null, null, BottomRightButtonsHboxContainer, null, BottomLeftButtonsHboxContainer);
	
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
		
		AddCommandLineButton.setGraphic(new ImageView("assets/img/add.png"));
		
		DeleteCommandLineButton.setGraphic(new ImageView("assets/img/delete.png"));
		
		TopButtonsHboxContainer.getStyleClass().add("buttonsContainer");
		TopButtonsHboxContainer.setSpacing(10);
		TopButtonsHboxContainer.setAlignment(Pos.BASELINE_RIGHT);
		
		LignesCommandeTableView.getStyleClass().add("tableView");
		
		BottomLeftButtonsHboxContainer.getStyleClass().add("buttonsContainer");
		BottomLeftButtonsHboxContainer.setSpacing(10);
		
		BottomRightButtonsHboxContainer.getStyleClass().add("buttonsContainer");
		BottomRightButtonsHboxContainer.setSpacing(10);
	}

	private boolean isValidForm() {
		boolean isValid = true;
		if (ClientTextField.getText().equals("")
				|| LigneCommandeObservableList.size() == 0) {
			isValid = false;
		}
		SaveBLButton.setDisable(!isValid);
		return isValid;
	}

	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Détail du bon de livraison");
		window.getIcons().add(new Image("file:store.jpg"));
	}
	
	private void appendNodesToWindow() {
		root.requestFocus();
		root.getChildren().add(TitleLabel);

		BLDetailsLabel.setMinWidth(300);
		BLDetailsLabel.setPadding(new Insets(0, 10, 0 ,10));;
		BLIdTextField.setDisable(true);
		ClientTextField.setEditable(false);
		ClientTextField.setDisable(true);
		DateInput.setValue(LocalDate.now());
		DateInput.setDisable(true);
		BLDetails.getChildren().addAll(BLDetailsLabel, BLIdLabel, BLIdTextField, ClientLabel, ClientTextField, DateLabel, DateInput);
		BLDetails.setSpacing(10);
		Top.setLeft(BLDetails);
		
		PaymentLabel.setMinWidth(300);
		PaymentLabel.setPadding(new Insets(0, 10, 0 ,10));
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

		Container.getChildren().add(BottomButtonsBorderPaneContainer);
		
		root.getChildren().add(Container);
	}
	
	private void updateColumns() {
		columnId.setCellValueFactory(row -> {
			String id = "-";
			
			if (row.getValue().getId() != -1)
				id = row.getValue().getId() + "";
			
			return new SimpleStringProperty(id);
		});
		columnId.setPrefWidth(50);
		
		// adjust columns widths so the id column can fit
		columnProduitDesignation.setPrefWidth(295);
		
		columnProduitDesignation.setCellValueFactory(row -> {
			return new SimpleStringProperty(row.getValue().getProduit().getDesignation());
		});
		columnProduitCategorieLabel.setPrefWidth(175);
		
		columnProduitCategorieLabel.setCellValueFactory(row -> {
			return new SimpleStringProperty(row.getValue().getProduit().getCategorie().getLabel() + "");
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
		columnTotal.setPrefWidth(132);
	}
	
	private void addEvents() {
		DateInput.valueProperty().addListener((Observable, oldDate, newDate) -> {
			isValidForm();
		});
		
		AddCommandLineButton.setOnAction(event -> {
			NouvelleLigneDeCommande nouvelleLigneDeCommande = new NouvelleLigneDeCommande();
			
			nouvelleLigneDeCommande.setCommandLineSelectCallBack(ligneCommande -> {
				LigneCommandeObservableList.add(ligneCommande);
				updatePayment();
				isValidForm();
				DeleteCommandLineButton.setDisable(false);
			});
		});
		
		DeleteCommandLineButton.setOnAction(event -> {
			if (LigneCommandeObservableList.size() != 0) {
				TableViewSelectionModel <LigneCommande> selectionModel = LignesCommandeTableView.getSelectionModel();
				ObservableList<Integer> selectedIndexs = selectionModel.getSelectedIndices();
				if (selectedIndexs.isEmpty()) {
					new Notification(NOTIF_TYPE.WARNING, "Veuillez sélectionner une ligne de commande avant de cliquer sur supprimer.");
				} else {
					Confirmation confirmation = new Confirmation("Supprimer la ligne de commande", "Vous êtes sûr de vouloir supprimer cette ligne de commande?");
					confirmation.setResponseCallBack(response -> {
						if (response == true) {
							LigneCommandeObservableList.remove((int) selectedIndexs.get(0));
							updatePayment();
							isValidForm();
							if (LigneCommandeObservableList.size() == 0) {
								DeleteCommandLineButton.setDisable(true);
								new Notification(NOTIF_TYPE.WARNING, "Veuillez ajouter des lignes de commande pour pouvoir enregistrer le bon de commande.");
							}
						}
					});
				}
			}
		});
		
		CloseButton.setOnAction(event -> {
			window.close();
		});
		
		SaveBLButton.setOnAction(event -> {
			LigneCommandeDaoImpl lcdi = new LigneCommandeDaoImpl();
			List <LigneCommande> lignesCommande = new ArrayList<>(LigneCommandeObservableList);
			
			// update the client and the date of the BL in the database
			tmpBL.setDate(DateInput.getValue());
			new BLDaoImpl().edit(tmpBL);
			
			// update the command lines associated to the BL
			for (LigneCommande lc: lignesCommande) {
				if (lc.getId() == -1) {
					// add the new lines to db
					lc.setBL(tmpBL);
					lc.setId(lcdi.add(lc).getId());						
				} else {
					// edit the ones that existed before 
					lcdi.edit(lc);
					
					// remove the edited lines from onlineLignesCommande 
					// so that only the lines that should be deleted will stay
					onlineLignesCommande.removeIf(ligneCommande -> {
						return ligneCommande.getId() == lc.getId();
					});
				}
			}
			
			// delete all the lines on onlineLignesCommande from database since they no longer
			// exist in the table
			for (LigneCommande lc: onlineLignesCommande) {
				lcdi.delete(lc.getId());
			}
			
			// keep track of what is on the database using an array list
			// so that if one line is deleted from the app we can delete it on db when
			// user clicks on save
			onlineLignesCommande.clear();
			onlineLignesCommande.addAll(LigneCommandeObservableList);
			
			// update the observable list and refresh the window
			LigneCommandeObservableList.setAll(lignesCommande);
			BLEditCallback.accept(tmpBL);
			
			enableFields(false);
			
			// disable save button
			SaveBLButton.setDisable(true);
			new Notification(NOTIF_TYPE.SUCCESS, "Le bon de livraision est enregistré avec succés");
		});
		
		ClientTextField.setOnMouseClicked(event -> {
			SelectionnerUnClient selectionnerUnClient = new SelectionnerUnClient(tmpBL.getClient());
			selectionnerUnClient.setClientSelectCallBack(client -> {
				tmpBL.setClient(client);
				ClientTextField.setText(client.getLastName() + " " + client.getFirstName());
				TitleLabel.setText("MODIFIER: BON DE LIVRAISON N° " + tmpBL.getId() + " DE " + tmpBL.getClient().getLastName() + " " + tmpBL.getClient().getFirstName());
				isValidForm();
			});
		});
		
		EditButton.setOnAction(event -> {
			enableFields(true);
		});
		
		CancelButton.setOnAction(event -> {
			if (!SaveBLButton.isDisabled()) { // if form is valid but not saved
				Confirmation confirmation = new Confirmation("Modifications non enregistrées", "Voulez-vous fermer sans enregister?");
				confirmation.setResponseCallBack(response -> {
					if (response == true) {
						tmpBL = new BL(bl);
						setDefaultValues();
						LigneCommandeObservableList.setAll(onlineLignesCommande);
						enableFields(false);
					}
				});
			} else { 
				if (!ClientTextField.getText().equals("") // if form is valid but saved
						&& LigneCommandeObservableList.size() != 0) {
					tmpBL = new BL(bl);
					setDefaultValues();
					LigneCommandeObservableList.setAll(onlineLignesCommande);
					enableFields(false);
				} else if (!ClientTextField.getText().equals("") // if form is not valid, not saved but got changes
						|| LigneCommandeObservableList.size() != 0) {
					Confirmation confirmation = new Confirmation("Modifications non enregistrées", "Voulez-vous fermer sans enregister?");
					confirmation.setResponseCallBack(response -> {
						if (response == true) {
							tmpBL = new BL(bl);
							setDefaultValues();
							LigneCommandeObservableList.setAll(onlineLignesCommande);
							enableFields(false);
						}
					});		
				} else { // if form is valid and saved and got no changes or
					     // if form is not valid not saved and got no changes
					tmpBL = new BL(bl);
					setDefaultValues();
					LigneCommandeObservableList.setAll(onlineLignesCommande);
					enableFields(false);
				}
			}
		});
		
		DeleteButton.setOnAction(event -> {
			Confirmation confirmation = new Confirmation("Supprimer le bon de commande", "Vous êtes sûr de vouloir supprimer ce bon de commande?");
			confirmation.setResponseCallBack(response -> {
				if (response == true) {
					new LigneCommandeDaoImpl().deleteAll(tmpBL.getId());
					new BLDaoImpl().delete(tmpBL.getId());
					BLDeleteCallback.accept(tmpBL);
					window.close();
				}
			});
		});
	}
	
	private void addTableViewEvents(boolean b) {
		if (b == true) {
			// add listner on the empty table pane for adding new command line
			LignesCommandeTableView.setOnMouseClicked(event -> {
				if (LigneCommandeObservableList.isEmpty()) {
					if (event.getTarget() instanceof StackPane || event.getTarget() instanceof Text) {
						NouvelleLigneDeCommande nouvelleLigneDeCommande = new NouvelleLigneDeCommande();
						
						nouvelleLigneDeCommande.setCommandLineSelectCallBack(ligneCommande -> {
							LigneCommandeObservableList.add(ligneCommande);
							updatePayment();
							isValidForm();
							DeleteCommandLineButton.setDisable(false);
						});
					}
				}
			});
			
			// add listners on table rows for adding or modifying command lines
			LignesCommandeTableView.setRowFactory(tv -> {
				TableRow <LigneCommande> row = new TableRow <LigneCommande> ();
				row.setOnMouseClicked(event -> {
					if (row.isEmpty()) {
						NouvelleLigneDeCommande nouvelleLigneDeCommande = new NouvelleLigneDeCommande();
						
						nouvelleLigneDeCommande.setCommandLineSelectCallBack(ligneCommande -> {
							LigneCommandeObservableList.add(ligneCommande);
							updatePayment();
							isValidForm();
							DeleteCommandLineButton.setDisable(false);
						});
					} else if (event.getClickCount() == 2) {
						ModifierLigneDeCommande modifierLigneDeCommande = new ModifierLigneDeCommande(row.getItem());
						
						modifierLigneDeCommande.setCommandLineModifyCallBack(ligneCommande -> {
							LigneCommandeObservableList.set(row.getIndex(), ligneCommande);
							updatePayment();
							isValidForm();
						});
						modifierLigneDeCommande.setCommandLigneDeleteCallBack(ligneCommande -> {
							LigneCommandeObservableList.remove(row.getIndex());
							updatePayment();
							isValidForm();
							if (LigneCommandeObservableList.size() == 0) {
								DeleteCommandLineButton.setDisable(true);
								new Notification(NOTIF_TYPE.WARNING, "Veuillez ajouter des lignes de commande pour pouvoir enregistrer le bon de commande.");
							}
						});
					}
				});
				return row;
			});
		} else {
			LignesCommandeTableView.setOnMouseClicked(null);
			LignesCommandeTableView.setRowFactory(tv -> {
				TableRow <LigneCommande> row = new TableRow <LigneCommande> ();
				row.setOnMouseClicked(null);
				return row;
			});
		}
		LignesCommandeTableView.refresh();
	}
	
	private void enableFields(boolean b) {
		ClientTextField.setDisable(!b);
		DateInput.setDisable(!b);
		if (b == true) {
			window.setTitle("Modifier les détail du bon de livraison");
			TitleLabel.setText("MODIFIER: BON DE LIVRAISON N° " + tmpBL.getId() + " DE " + tmpBL.getClient().getLastName() + " " + tmpBL.getClient().getFirstName());
			window.setOnCloseRequest(event -> {
				event.consume();
			});
			SaveBLButton.setDisable(true);
			Container.getChildren().add(1, TopButtonsHboxContainer);
			BottomLeftButtonsHboxContainer.getChildren().addAll(SaveBLButton, CancelButton);
			BottomLeftButtonsHboxContainer.getChildren().remove(CloseButton);
			BottomRightButtonsHboxContainer.getChildren().remove(EditButton);
		} else {
			window.setOnCloseRequest(event -> {
				window.close();
			});
			Container.getChildren().remove(TopButtonsHboxContainer);
			BottomLeftButtonsHboxContainer.getChildren().removeAll(SaveBLButton, CancelButton);
			BottomLeftButtonsHboxContainer.getChildren().add(CloseButton);
			BottomRightButtonsHboxContainer.getChildren().add(0, EditButton);
		}
		addTableViewEvents(b);
		window.show();
	}
	
	private void getLignesCommande() {
		LigneCommandeDaoImpl ligneCommandeDaoImpl = new LigneCommandeDaoImpl();
		LigneCommandeObservableList.setAll(ligneCommandeDaoImpl.getAll(tmpBL.getId()));
		
		onlineLignesCommande.clear();
		onlineLignesCommande.addAll(LigneCommandeObservableList);
	}
	
	private void updatePayment() {
		double TotalHTValue = 0, TVA1Value, TVA2Value;
		for(LigneCommande lc: LigneCommandeObservableList) {
			TotalHTValue += lc.getProduit().getSellingPrice() * lc.getQuantity();
		}
		TVA1Value = TotalHTValue * 0.07;
		TVA2Value = TotalHTValue * 0.2;
		TotalHT.setText(String.format("%,.2f", TotalHTValue));
		TVA1.setText(String.format("%,.2f", TVA1Value));
		TVA2.setText(String.format("%,.2f", TVA2Value));
		TotalTTC.setText(String.format("%,.2f", TotalHTValue + TVA1Value + TVA2Value));
	}
	
	private void setDefaultValues() {
		BLIdTextField.setText(tmpBL.getId() + "");
		ClientTextField.setText(tmpBL.getClient().getLastName() + " " + tmpBL.getClient().getFirstName());
		DateInput.setValue(tmpBL.getDate());
		TitleLabel.setText("BON DE LIVRAISON N° " + tmpBL.getId() + " DE " + tmpBL.getClient().getLastName() + " " + tmpBL.getClient().getFirstName());
	}
	
	public AfficherBL(BL bl) {
		this.tmpBL = new BL(bl);
		this.bl = bl;
		addStylesToNodes();
		initWindow();
		appendNodesToWindow();
		getLignesCommande();
		updateColumns();
		setDefaultValues();
		updatePayment();
		addEvents();
		
		window.show();
	}
}
