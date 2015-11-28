package medproject.medclient.graphicalInterface.addPersonWindow;

import java.util.List;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.patient.PatientRecord;

public class AddPersonController implements ControllerInterface{

	@FXML Button searchButton;
	@FXML Button adaugaPacientButton;
	@FXML Button anuleazaButton;
	@FXML TextField cnpTextField;

	@FXML TableView<PatientRecord> patientRecordTable;
	@FXML TableColumn<PatientRecord, String> cnpColumn;
	@FXML TableColumn<PatientRecord, String> firstNameColumn;
	@FXML TableColumn<PatientRecord, String> lastNameColumn;
	@FXML TableColumn<PatientRecord, String> genderColumn;
	@FXML TableColumn<PatientRecord, Integer> ageColumn;
	@FXML TableColumn<PatientRecord, String> categoryColumn;
	@FXML TableColumn<PatientRecord, String> insuranceColumn;
	@FXML TableColumn<PatientRecord, String> birthDateColumn;
	@FXML TableColumn<PatientRecord, String> countyColumn;
	@FXML TableColumn<PatientRecord, String> cityColumn;
	
	private ObservableList<PatientRecord> patientRecordList;
	private DataLoader dataLoader;
	private Stage stage;
	
	@Override
	public void init(DataLoader dataLoader, Stage stage) {
		this.dataLoader = dataLoader;
		this.stage = stage;
		this.patientRecordList = FXCollections.observableArrayList();
		patientRecordTable.setItems(patientRecordList);
		setColumnValues();
		setTextFieldListener();
		resetInterface();
	}

	private void setTextFieldListener() {
		cnpTextField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
		    	if(newValue.length() != 13)
		    		searchButton.setDisable(true);
		    	else
		    		searchButton.setDisable(false);
		    }
		});
	}

	@FXML protected void onPressSearch(){
		dataLoader.makePatientRecordByCNPRequest(this, cnpTextField.getText());
		searchButton.setDisable(true);
	}
	
	@FXML protected void onPressAnuleaza(){
		stage.close();
	}

	@FXML protected void onPressAdaugaPacient(){
		//pin
		PatientRecord currentRecord = patientRecordTable.getSelectionModel().getSelectedItem();
		int pin = GUIUtils.makeIntDialog("Introdu PIN-ul", "Codul Pin: ", 4);
		dataLoader.makeAddPatientRequest(this, currentRecord.getPERSON_ID(), pin);
		
		adaugaPacientButton.disableProperty().unbind();
		adaugaPacientButton.setDisable(true);
		anuleazaButton.setDisable(true);
		searchButton.setDisable(true);
	}

	public void closeWindow(){
		dataLoader.makeWindowRequest(new Runnable(){

			@Override
			public void run() {
				onPressAnuleaza();	
			}
			
		});
	}
	
	public void updateRecordList(final List<PatientRecord> list){
		dataLoader.getMainWindow().runAndWait(new Runnable(){

			@Override
			public void run() {
				patientRecordList.clear();
				patientRecordList.addAll(list);
			}
		});
	}
	
	private void setColumnValues(){
		cnpColumn.setCellValueFactory(new Callback<CellDataFeatures<PatientRecord, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<PatientRecord, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getCNP());
			}
		});

		firstNameColumn.setCellValueFactory(new Callback<CellDataFeatures<PatientRecord, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<PatientRecord, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getFirstName());
			}
		});

		lastNameColumn.setCellValueFactory(new Callback<CellDataFeatures<PatientRecord, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<PatientRecord, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getLastName());
			}
		});

		genderColumn.setCellValueFactory(new Callback<CellDataFeatures<PatientRecord, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<PatientRecord, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getSex().toString());
			}
		});

		birthDateColumn.setCellValueFactory(new Callback<CellDataFeatures<PatientRecord, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<PatientRecord, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getBirthDateString());
			}
		});

		countyColumn.setCellValueFactory(new Callback<CellDataFeatures<PatientRecord, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<PatientRecord, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getAddress().getCounty());
			}
		});

		cityColumn.setCellValueFactory(new Callback<CellDataFeatures<PatientRecord, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<PatientRecord, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getAddress().getCity());
			}
		});

	}

	public void resetInterface() {
		searchButton.setDisable(true);
		anuleazaButton.setDisable(false);
		adaugaPacientButton.disableProperty().bind(patientRecordTable.getSelectionModel().selectedItemProperty().isNull());	
	}

}
