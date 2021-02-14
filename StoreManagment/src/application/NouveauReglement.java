package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import application.modals.Confirmation;
import application.modals.NOTIF_TYPE;
import application.modals.Notification;
import dao.LigneCommandeDaoImpl;
import dao.ReglementDaoImpl;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.BL;
import model.Banque;
import model.Reglement;
import utils.Validators;

public class NouveauReglement {
	VBox root = new VBox();
	double windowWidth = 1210;
	double windowHeight = 550;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	Consumer<List<Reglement>> ReglementCallBack;
	
	public void setReglementCallBack(Consumer<List<Reglement>> callback) {
		this.ReglementCallBack = callback;
	}
	
	BL bl;
	
	boolean saved = false;
	
	List <Reglement> onlineReglements = new ArrayList<>();
	ObservableList <Reglement> ReglementObservableList = FXCollections.observableArrayList();
	
	Label TitleLabel = new Label("NOUVEAU REGLEMENT");
	
	VBox Container = new VBox();
	
	HBox DataWrapper = new HBox();
	
	VBox LeftBar = new VBox();
	VBox TableWrapper = new VBox();
	
	VBox BLDetails = new VBox();
	
	Label BLDetailsLabel = new Label("DETAILS DE BON DE LIVRAISON");
	
	Label BLIdLabel = new Label("Id du bon de livraison:");
	TextField BLIdTextField = new TextField();
	
	Label ClientLabel = new Label("Client:");
	TextField ClientTextField = new TextField();
	
	Label DateLabel = new Label("Date:");
	DatePicker DateInput = new DatePicker();
	
	VBox Payment = new VBox();
	
	Label PaymentLabel = new Label("REGLEMENT DE BON DE LIVRAISON");
	
	GridPane PaymentContainer = new GridPane();
	Label TotalToPayLabel = new Label("Total à payer: ");
	Label TotalToPayValue = new Label("0.0");
	Label TotalPayedLabel = new Label("Total payé: ");
	Label TotalPayedValue = new Label("0.0");
	Label RestToPayLable = new Label("Reste: ");
	Label RestToPayValue = new Label("0.0");

	Button AddReglementButton = new Button();
	Button DeleteReglementButton = new Button();
	
	HBox TopButtonsHboxContainer = new HBox(AddReglementButton, DeleteReglementButton);
	
	TableView <Reglement> ReglementTableView = new TableView<Reglement>();
	TableColumn <Reglement, String> columnReglementId = new TableColumn <Reglement, String>("Id");
	TableColumn <Reglement, LocalDate> columnReglementDate = new TableColumn <Reglement, LocalDate>("Date");
	TableColumn <Reglement, Double> columnReglementMontant = new TableColumn <Reglement, Double>("Mantant");
	TableColumn <Reglement, String> columnReglementType = new TableColumn <Reglement, String>("Type");
	TableColumn <Reglement, Long> columnReglementNumeroCheque = new TableColumn <Reglement, Long>("Numero du chèque");
	TableColumn <Reglement, LocalDate> columnReglementDateEcheance = new TableColumn <Reglement, LocalDate>("Date");
	TableColumn <Reglement, String> columnReglementBanque = new TableColumn <Reglement, String>("Banque");
	TableColumn <Reglement, String> columnReglementNom = new TableColumn <Reglement, String>("Nom");
	
	Button SaveReglementButton = new Button("Enregistrer");
	Button CancelButton = new Button("Fermer");
	HBox BottomLeftButtonsHboxContainer = new HBox(SaveReglementButton, CancelButton);
	
	Button DeleteButton = new Button("Supprimer");
	HBox BottomRightButtonsHboxContainer = new HBox(DeleteButton);

	BorderPane BottomButtonsBorderPaneContainer = new BorderPane(null, null, BottomRightButtonsHboxContainer, null, BottomLeftButtonsHboxContainer);
	private void addStylesToNodes() {
		scene.getStylesheets().add("assets/css/styles.css");
		root.getStyleClass().add("root");
		
		TitleLabel.getStyleClass().add("titleLabel");
		TitleLabel.setMinWidth(windowWidth);
		
		Container.getStyleClass().add("container");
		Container.setSpacing(20);
		
		BLDetailsLabel.getStyleClass().add("titleLabel");
		BLIdTextField.setCursor(Cursor.HAND);
		
		PaymentLabel.getStyleClass().add("titleLabel");
		
		TopButtonsHboxContainer.getStyleClass().add("buttonsContainer");
		TopButtonsHboxContainer.setSpacing(10);
		TopButtonsHboxContainer.setAlignment(Pos.BASELINE_RIGHT);
		
		ReglementTableView.getStyleClass().add("tableView");
		
		BottomLeftButtonsHboxContainer.getStyleClass().add("buttonsContainer");
		BottomLeftButtonsHboxContainer.setSpacing(10);
		
		BottomRightButtonsHboxContainer.getStyleClass().add("buttonsContainer");
		BottomRightButtonsHboxContainer.setSpacing(10);
	}

	private boolean isValidForm() {
		boolean isValid = true;
		if (Validators.isEmpty(BLIdTextField)) {
			if (!BLIdTextField.getStyleClass().contains("invalidTextField")) {
				BLIdTextField.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			BLIdTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		if (ReglementObservableList.size() == 0) {
			isValid = false;
		}
		SaveReglementButton.setDisable(!isValid);
		return isValid;
	}
	
	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Ajouté un nouveau règlement");
		window.getIcons().add(new Image("file:store.jpg"));
		window.initModality(Modality.APPLICATION_MODAL);  
	}
	
	private void appendNodesToWindow() {
		root.requestFocus();
		root.getChildren().add(TitleLabel);
		

		BLDetailsLabel.setMinWidth(300);
		BLDetailsLabel.setPadding(new Insets(0, 10, 0 ,10));
		BLIdTextField.setEditable(false);
		ClientTextField.setDisable(true);
		DateInput.setDisable(true);
		
		BLDetails.getChildren().addAll(BLDetailsLabel, BLIdLabel, BLIdTextField, ClientLabel, ClientTextField, DateLabel, DateInput);
		BLDetails.setSpacing(10);
		
		PaymentLabel.setMinWidth(300);
		PaymentLabel.setPadding(new Insets(0, 10, 0 ,10));
		PaymentContainer.add(TotalToPayLabel, 0, 0);
		PaymentContainer.add(TotalToPayValue, 1, 0);
		PaymentContainer.add(TotalPayedLabel, 0, 2);
		PaymentContainer.add(TotalPayedValue, 1, 2);
		PaymentContainer.add(RestToPayLable, 0, 3);
		PaymentContainer.add(RestToPayValue, 1, 3);
		
		Payment.getChildren().addAll(PaymentLabel, PaymentContainer);
		Payment.setSpacing(10);
		
		LeftBar.getChildren().addAll(BLDetails, Payment);
		LeftBar.setSpacing(10);
				
		AddReglementButton.setGraphic(new ImageView("assets/img/add.png"));
		AddReglementButton.setDisable(true);
		DeleteReglementButton.setGraphic(new ImageView("assets/img/delete.png"));
		DeleteReglementButton.setDisable(true);
		
		ReglementTableView.getColumns().addAll(columnReglementId, columnReglementDate, columnReglementMontant, columnReglementType, columnReglementNumeroCheque, columnReglementDateEcheance, columnReglementBanque, columnReglementNom);
		ReglementTableView.setItems(ReglementObservableList);
		
		TableWrapper.getChildren().addAll(TopButtonsHboxContainer, ReglementTableView);
		TableWrapper.setSpacing(10);
				
		DataWrapper.getChildren().addAll(LeftBar, TableWrapper);
		DataWrapper.setSpacing(10);
		Container.getChildren().add(DataWrapper);
		
		SaveReglementButton.setDisable(true);
		
		DeleteButton.setDisable(true);
		Container.getChildren().add(BottomButtonsBorderPaneContainer);
		
		root.getChildren().add(Container);
	}
	
	private void updateColumns() {
		columnReglementId.setCellValueFactory(row -> {
			String id = "-";
			
			if (row.getValue().getId() != -1)
				id = row.getValue().getId() + "";
			
			return new SimpleStringProperty(id);
		});
		columnReglementId.setPrefWidth(50);
		
		columnReglementDate.setCellValueFactory(new PropertyValueFactory <Reglement, LocalDate> ("date"));
		columnReglementDate.setPrefWidth(100);
		
		columnReglementMontant.setCellValueFactory(new PropertyValueFactory <Reglement, Double> ("montant"));
		columnReglementMontant.setPrefWidth(100);
		
		columnReglementType.setCellValueFactory(new PropertyValueFactory <Reglement, String> ("type"));
		columnReglementType.setPrefWidth(100);
		
		columnReglementNumeroCheque.setCellValueFactory(new PropertyValueFactory <Reglement, Long> ("numero_cheque"));
		columnReglementNumeroCheque.setPrefWidth(181);
		
		columnReglementDateEcheance.setCellValueFactory(new PropertyValueFactory <Reglement, LocalDate> ("date_echeance"));
		columnReglementDateEcheance.setPrefWidth(100);
		
		columnReglementBanque.setCellValueFactory(row -> {
			Banque banque = row.getValue().getBanque();
			if (banque != null) {
				return new SimpleStringProperty(banque.getAbreviation() + "");
			}
			return new SimpleStringProperty("");
		});
		columnReglementBanque.setPrefWidth(75);
		
		columnReglementNom.setCellValueFactory(new PropertyValueFactory <Reglement, String> ("nom"));
		columnReglementNom.setPrefWidth(150);
	}
	
	private void addEvents() {
		AddReglementButton.setOnAction(event -> {
			NouvelleLigneDeReglement nouvelleLigneDeReglement = new NouvelleLigneDeReglement(Double.parseDouble(RestToPayValue.getText()));
			
			nouvelleLigneDeReglement.setReglementLineSelectCallBack(reglement -> {
				ReglementObservableList.add(reglement);
				updatePayment();
				isValidForm();
				DeleteReglementButton.setDisable(false);
			});
		});
		DeleteReglementButton.setOnAction(event -> {
			if (ReglementObservableList.size() != 0) {
				TableViewSelectionModel <Reglement> selectionModel = ReglementTableView.getSelectionModel();
				ObservableList<Integer> selectedIndexs = selectionModel.getSelectedIndices();
				if (selectedIndexs.isEmpty()) {
					new Notification(NOTIF_TYPE.WARNING, "Veuillez sélectionner un règlement avant de cliquer sur supprimer.");
				} else {
					Confirmation confirmation = new Confirmation("Supprimer le règlement", "Vous êtes sûr de vouloir supprimer ce règlement?");
					confirmation.setResponseCallBack(response -> {
						if (response == true) {
							ReglementObservableList.remove((int) selectedIndexs.get(0));
							updatePayment();
							isValidForm();
							if (ReglementObservableList.size() == 0) {
								DeleteReglementButton.setDisable(true);
								new Notification(NOTIF_TYPE.INFO, "Veuillez ajouter des lignes de règlement pour pouvoir enregistrer le règlement.");
							}
						}
					});
				}
			}
		});
		CancelButton.setOnAction(event -> {
			if (!SaveReglementButton.isDisabled()) { // if form is valid but not saved
				Confirmation confirmation = new Confirmation("Modifications non enregistrées", "Voulez-vous fermer sans enregister?");
				confirmation.setResponseCallBack(response -> {
					if (response == true) {
						window.close();
					}
				});
			} else { 
				if (!Validators.isEmpty(BLIdTextField) // if form is valid but saved
						&& ReglementObservableList.size() != 0) {
					window.close();
				} else if (!Validators.isEmpty(BLIdTextField) // if form is not valid, not saved but got changes
						|| ReglementObservableList.size() != 0) {
					Confirmation confirmation = new Confirmation("Modifications non enregistrées", "Voulez-vous fermer sans enregister?");
					confirmation.setResponseCallBack(response -> {
						if (response == true) {
							window.close();
						}
					});			
				} else { // if form is valid and saved and got no changes or
					     // if form is not valid not saved and got no changes
					window.close();
				}
			}
		});
		SaveReglementButton.setOnAction(event -> {
			ReglementDaoImpl rdi = new ReglementDaoImpl();
			List <Reglement> reglements = new ArrayList<>(ReglementObservableList);
			
			// create each command ligne for the new BL in the database
			for (Reglement rg: reglements) {
				if (rg.getId() == -1) {
					// add the new lines to db
					rg.setBL(bl);
					rg.setId(rdi.add(rg).getId());						
				} else {
					// edit the ones that existed before 
					rdi.edit(rg);
					
					// remove the edited lines from onlineReglements 
					// so that only the lines that should be deleted will stay
					onlineReglements.removeIf(reglement -> {
						return reglement.getId() == rg.getId();
					});
				}
			}
			
			
			// delete all the lines on onlineReglements from database since they no longer
			// exist in the table
			for (Reglement rg: onlineReglements) {
				rdi.delete(rg.getId());
			}
			
			// keep track of what is on the database using an array list
			// so that if one line is deleted from the app we can delete it on db when
			// user clicks on save
			onlineReglements.clear();
			onlineReglements.addAll(ReglementObservableList);
			
			// update the observable list and refresh the window
			ReglementObservableList.setAll(reglements);
			
			// disable save button
			SaveReglementButton.setDisable(true);
			DeleteButton.setDisable(false);
			if (ReglementCallBack != null) {
				ReglementCallBack.accept(reglements);
			}
			new Notification(NOTIF_TYPE.SUCCESS, "Le règlement est enregistré avec succès");
		});
		
		BLIdTextField.setOnMouseClicked(event -> {
			SelectionnerUnBonDeLivraison selectionnerUnBonDeLivraison = new SelectionnerUnBonDeLivraison(bl);
			selectionnerUnBonDeLivraison.setBLSelectCallBack(bl -> {
				this.bl = bl;
				setDefaultValues();
				
			});
		});
		
		// add listner on the empty table pane for adding new command line
		ReglementTableView.setOnMouseClicked(event -> {
			if (ReglementObservableList.isEmpty()) {
				if (event.getTarget() instanceof StackPane || event.getTarget() instanceof Text) {
					NouvelleLigneDeReglement nouvelleLigneDeReglement = new NouvelleLigneDeReglement(Double.parseDouble(RestToPayValue.getText()));
					
					nouvelleLigneDeReglement.setReglementLineSelectCallBack(reglement -> {
						ReglementObservableList.add(reglement);
						updatePayment();
						isValidForm();
						DeleteReglementButton.setDisable(false);
					});
				}
			}
		});
		
		// add listners on table rows for adding or modifying command lines
		ReglementTableView.setRowFactory(tv -> {
			TableRow <Reglement> row = new TableRow <Reglement> ();
			row.setOnMouseClicked(event -> {
				if (row.isEmpty()) {
					NouvelleLigneDeReglement nouvelleLigneDeReglement = new NouvelleLigneDeReglement(Double.parseDouble(RestToPayValue.getText()));
					
					nouvelleLigneDeReglement.setReglementLineSelectCallBack(reglement -> {
						ReglementObservableList.add(reglement);
						updatePayment();
						isValidForm();
						DeleteReglementButton.setDisable(false);
						
					});
				} else if (event.getClickCount() == 2) {
					ModifierLigneDeReglement modifierLigneDeReglement = new ModifierLigneDeReglement(Double.parseDouble(RestToPayValue.getText()), row.getItem());
					
					modifierLigneDeReglement.setReglementLineModifyCallBack(reglement -> {
						ReglementObservableList.set(row.getIndex(), reglement);
						updatePayment();
						isValidForm();
					});
					modifierLigneDeReglement.setReglementLineDeleteCallBack(reglement -> {
						ReglementObservableList.remove(row.getIndex());
						updatePayment();
						isValidForm();
						if (ReglementObservableList.size() == 0) {
							DeleteReglementButton.setDisable(true);
							new Notification(NOTIF_TYPE.INFO, "Veuillez ajouter des lignes de règlement pour pouvoir enregistrer le règlement.");							
						}
					});
				}
			});
			return row;
		});
		DeleteButton.setOnAction(event -> {
			Confirmation confirmation = new Confirmation("Supprimer le règlement", "Vous êtes sûr de vouloir supprimer ce règlement?");
			confirmation.setResponseCallBack(response -> {
				if (response == true) {
					new ReglementDaoImpl().deleteAll(bl.getId());
					if (ReglementCallBack != null) {
						ReglementCallBack.accept(onlineReglements);						
					}
					window.close();
				}
			});
		});
		window.setOnCloseRequest(event -> {
			event.consume();
		});
	}
	
	private void updatePayment() {
		double TotalToPay, TotalPayed = 0;
		
		
		for(Reglement rg: ReglementObservableList) {
			TotalPayed += rg.getMontant();
		}
		TotalPayedValue.setText(String.format("%.2f", TotalPayed));

		if (bl != null) {
			TotalToPay = new LigneCommandeDaoImpl().getTotal(bl.getId());
			TotalToPay += TotalToPay * 0.07 + TotalToPay * 0.2;
			if (TotalToPay != -1.0) {
				TotalToPayValue.setText(String.format("%.2f", TotalToPay));
				RestToPayValue.setText(String.format("%.2f", TotalToPay - TotalPayed));				
			} else {
				TotalToPayValue.setText(String.format("%.2f", 0.0));
				RestToPayValue.setText(String.format("%.2f", -TotalPayed));
			}
			
		} else {
			TotalToPayValue.setText(String.format("%.2f", 0.0));
			RestToPayValue.setText(String.format("%.2f", -TotalPayed));
		}
		window.show();
	}
	
	private void setDefaultValues() {
		BLIdTextField.setText(bl.getId() + "");
		ClientTextField.setText(bl.getClient().getLastName() + " " + bl.getClient().getFirstName());
		DateInput.setValue(bl.getDate());
		
		// populating bl payments
		List<Reglement> reglements = new ReglementDaoImpl().getAll(bl.getId());
		ReglementObservableList.setAll(reglements);
		
		// keep track of online data
		onlineReglements.clear();
		onlineReglements.addAll(reglements);
		
		// if no paiments disable payment line delete button
		DeleteReglementButton.setDisable(reglements.size() == 0);
		updatePayment();
		
		// enable adding reglements
		AddReglementButton.setDisable(false);
		DeleteButton.setDisable(reglements.size() == 0);
		isValidForm();
	}
	public NouveauReglement() {
		addStylesToNodes();
		initWindow();
		appendNodesToWindow();
		updateColumns();
		addEvents();
		window.show();
	}
	
	public NouveauReglement(BL bl) {
		this.bl = bl;
		addStylesToNodes();
		initWindow();
		appendNodesToWindow();
		updateColumns();
		addEvents();
		BLIdTextField.setDisable(true);
		setDefaultValues();
		window.show();
	}
}
