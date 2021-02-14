package application;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import application.modals.Confirmation;
import application.modals.NOTIF_TYPE;
import application.modals.Notification;
import dao.BanqueDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Banque;
import model.Reglement;
import utils.Validators;
import model.Produit;

public class ModifierLigneDeReglement {
	VBox root = new VBox();
	double windowWidth = 900;
	double windowHeight = 350;
	Scene scene = new Scene(root, windowWidth, windowHeight);
	Stage window = new Stage();
	
	private Consumer<Reglement> ReglementLineModifyCallBack;
	
	public void setReglementLineModifyCallBack(Consumer <Reglement> callback) {
		this.ReglementLineModifyCallBack = callback;
	}
	
	private Consumer<Reglement> ReglementLineDeleteCallBack;
	
	public void setReglementLineDeleteCallBack(Consumer <Reglement> callback) {
		this.ReglementLineDeleteCallBack = callback;
	}
	
	List <Banque> banques;
	Double rest;
	Reglement rg;
	
	ObservableList <Produit> ProductsObservableList = FXCollections.observableArrayList();
	
	VBox Container = new VBox();
	
	Label TitleLabel = new Label("NOUVELLE LIGNE DE REGLEMENT");
	
	Label DateLabel = new Label("Date:");
	DatePicker DateInput = new DatePicker(LocalDate.now());
	
	Label MontantLabel = new Label("Montant:");
	TextField MontantTextField = new TextField();
	
	Label TypeLabel = new Label("Type:");
	ComboBox<String> TypeComboBox = new ComboBox<>();
	
	Label NumeroChequeLabel = new Label("Numéro du chèque:");
	TextField NumeroChequeTextField = new TextField();
	
	Label DateEcheanceLabel = new Label("Date échéance:");
	DatePicker DateEcheanceInput = new DatePicker(LocalDate.now());
	
	Label BanqueLabel = new Label("Banque:");
	ComboBox<Banque> BanqueComboBox = new ComboBox<>();
	
	Label NomLabel = new Label("Nom:");
	TextField NomTextField = new TextField();
		
	Button EditReglementButton = new Button("Modifier");
	Button CancelButton = new Button("Annuler");
	HBox BottomLeftButtonsHboxContainer = new HBox(EditReglementButton, CancelButton);
	
	Button DeleteButton = new Button("Supprimer");
	HBox BottomRightButtonsHboxContainer = new HBox(DeleteButton);

	BorderPane BottomButtonsBorderPaneContainer = new BorderPane(null, null, BottomRightButtonsHboxContainer, null, BottomLeftButtonsHboxContainer);
	
	private void addStylesToNodes() {
		scene.getStylesheets().add("assets/css/styles.css");
		
		Container.getStyleClass().add("container");
		Container.setSpacing(15);
		
		TitleLabel.getStyleClass().add("titleLabel");
		TitleLabel.setMinWidth(windowWidth);
		
		TypeComboBox.getStyleClass().add("ComboBox");
		BanqueComboBox.getStyleClass().add("ComboBox");
		
		BottomLeftButtonsHboxContainer.getStyleClass().add("buttonsContainer");
		BottomLeftButtonsHboxContainer.setSpacing(10);
		
		BottomRightButtonsHboxContainer.getStyleClass().add("buttonsContainer");
		BottomRightButtonsHboxContainer.setSpacing(10);
		
	}
	
	private void initWindow() {
		window.setScene(scene);
		window.setTitle("Nouvelle ligne de règlement");
		window.getIcons().add(new Image("file:store.jpg"));
		window.initModality(Modality.APPLICATION_MODAL);
		window.setX(100);
		window.setY(200);
	}
	
	private void getBanques() {
		banques = new BanqueDaoImpl().getAll();
	}

	private void appendNodesToWindow() {
		root.getChildren().addAll(TitleLabel);

		TypeComboBox.getItems().addAll("ESPECE", "CHEQUE");
		TypeComboBox.setValue("ESPECE");
		Container.getChildren().addAll(DateLabel, DateInput,
				MontantLabel, MontantTextField, 
				TypeLabel, TypeComboBox);
		
		Container.getChildren().addAll(BottomButtonsBorderPaneContainer);
		
		root.getChildren().add(Container);
	}
	
	private void addBanquesToComboBox() {
		BanqueComboBox.getItems().clear();
		BanqueComboBox.setCellFactory(new Callback<ListView<Banque>, ListCell<Banque>>() {
 
            @Override
            public ListCell<Banque> call(ListView<Banque> param) {
                final ListCell<Banque> cell = new ListCell<Banque>() {
 
                    @Override
                    protected void updateItem(Banque item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getNom());
                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
		BanqueComboBox.setButtonCell(new ListCell<Banque>() {
			 
            @Override
            protected void updateItem(Banque item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getNom());
                } else {
                    setText(null);
                }
            }
        });
		for (Banque c: banques) {
			BanqueComboBox.getItems().add(c);			
		}
	}

	private boolean isValidForm() {
		boolean isValid;
		isValid = isValidMontant();
		isValid = isValid && isValidType();
		if (isValid && TypeComboBox.getValue().equals("CHEQUE")) {
			isValid = isValid && isValidNumeroCheque();
			isValid = isValid && isValidBanque();
			isValid = isValid && isValidNom();
		}
		return isValid;
	}
	
	private boolean isValidMontant() {
		boolean isValid = true;
		
		if (!Validators.isDouble(MontantTextField.getText())){
			if (!MontantTextField.getStyleClass().contains("invalidTextField")) {
				MontantTextField.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			MontantTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		
		return isValid;
	}
	
	private boolean isValidType() {
		boolean isValid = true;
				
		if (TypeComboBox.getValue() == null){
			if (!TypeComboBox.getStyleClass().contains("invalidTextField")) {
				TypeComboBox.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			TypeComboBox.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		
		return isValid;
	}
	
	private boolean isValidNumeroCheque() {
		boolean isValid = true;
		if (!Validators.isInteger(NumeroChequeTextField.getText())){
			if (!NumeroChequeTextField.getStyleClass().contains("invalidTextField")) {
				NumeroChequeTextField.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			NumeroChequeTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		return isValid;
	}
	
	private boolean isValidBanque() {
		boolean isValid = true;
		
		if (BanqueComboBox.getValue() == null){
			if (!BanqueComboBox.getStyleClass().contains("invalidTextField")) {
				BanqueComboBox.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			BanqueComboBox.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		
		return isValid;
	}
	
	private boolean isValidNom() {
		boolean isValid = true;
		
		if (!Validators.isName(NomTextField.getText())){
			if (!NomTextField.getStyleClass().contains("invalidTextField")) {
				NomTextField.getStyleClass().add("invalidTextField");						
			}
			isValid = false;
		} else {
			NomTextField.getStyleClass().removeAll(Collections.singleton("invalidTextField"));
		}
		
		return isValid;
	}
	
	private void addEvents() {
		DeleteButton.setOnAction(event -> {
			Confirmation confirmation = new Confirmation("Supprimer la ligne de règlement", "Vous êtes sûr de vouloir supprimer cette ligne de règlement?");
			confirmation.setResponseCallBack(response -> {
				if (response == true) {
					ReglementLineDeleteCallBack.accept(rg);
					window.close();
				}
			});
		});
		
		CancelButton.setOnAction(event -> {
			Confirmation confirmation = new Confirmation("Quitter", "Vous êtes sûr de vouloir annuler la modification de cette ligne de règlement?");
			confirmation.setResponseCallBack(response -> {
				if (response == true) {
					window.close();
				}
			});
		});
		
		EditReglementButton.setOnAction(event -> {
			if (isValidForm()) {
				Confirmation confirmation = new Confirmation("Modifier la ligne de règlement", "Vous êtes sûr de vouloir modifier cette ligne de règlement?");
				confirmation.setResponseCallBack(response -> {
					if (response == true) {
						rg.setDate(DateInput.getValue());
						rg.setMontant(Double.parseDouble(MontantTextField.getText()));
						if (TypeComboBox.getValue().equals("CHEQUE")) {
							rg.setType("CHEQUE");
							rg.setNumero_cheque(Long.parseLong(NumeroChequeTextField.getText()));
							rg.setDate_echeance(DateEcheanceInput.getValue());
							rg.setBanque(BanqueComboBox.getValue());
							rg.setNom(NomTextField.getText());
						} else {
							rg.setType("ESPECE");
							rg.setNumero_cheque(null);
							rg.setDate_echeance(null);
							rg.setBanque(null);
							rg.setNom(null);
						}
						ReglementLineModifyCallBack.accept(rg);
						window.close();
					}
				});
			} else {
				new Notification(NOTIF_TYPE.ERROR, "Les données du formulaire sont invalide.");
			}
		});
	}
	
	public void addEventsToFields() {
		MontantTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			isValidMontant();
		});
		TypeComboBox.setOnAction(event -> {
			isValidType();
			updateLayout(TypeComboBox.getValue());
		});
		NumeroChequeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			isValidNumeroCheque();
		});
		BanqueComboBox.setOnAction(event -> isValidBanque());
		NomTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			isValidNom();
		});
	}
	
	public void updateLayout(String type) {
		if (type == null) {
			return;
		}
		if (type.equals("ESPECE")) {
			Container.getChildren().removeAll(NumeroChequeLabel, NumeroChequeTextField, 
					DateEcheanceLabel, DateEcheanceInput, 
					BanqueLabel, BanqueComboBox, 
					NomLabel, NomTextField);
			window.setHeight(375);
		} else if (type.equals("CHEQUE")) {
			Container.getChildren().remove(BottomButtonsBorderPaneContainer);
			Container.getChildren().addAll(NumeroChequeLabel, NumeroChequeTextField, 
					DateEcheanceLabel, DateEcheanceInput, 
					BanqueLabel, BanqueComboBox, 
					NomLabel, NomTextField, BottomButtonsBorderPaneContainer	);
			window.setHeight(650);
		}
	}
	
	private void setDefaultValues() {
		DateInput.setValue(rg.getDate());
		MontantTextField.setText(rg.getMontant() + "");
		TypeComboBox.setValue(rg.getType());
		if(rg.getType().equals("CHEQUE")) {
			updateLayout("CHEQUE");
			NumeroChequeTextField.setText(rg.getNumero_cheque() + "");
			DateEcheanceInput.setValue(rg.getDate_echeance());
			
			for (Banque b: banques) {
				if (b.getId() == rg.getBanque().getId()) {
					BanqueComboBox.setValue(b);					
				}
			}
			
			NomTextField.setText(rg.getNom());			
		}
	}
	
	public ModifierLigneDeReglement(Double rest, Reglement rg) {
		this.rg = new Reglement(rg);
		this.rest = rest;
		initWindow();
		addStylesToNodes();
		appendNodesToWindow();
		getBanques();
		addBanquesToComboBox();
		addEvents();
		addEventsToFields();
		setDefaultValues();
		window.show();
	}
}
