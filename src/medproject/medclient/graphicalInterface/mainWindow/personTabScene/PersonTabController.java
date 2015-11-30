package medproject.medclient.graphicalInterface.mainWindow.personTabScene;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Callback;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.graphicalInterface.addPersonWindow.AddPersonWindow;
import medproject.medclient.graphicalInterface.patientDataWindow.PatientDataWindow;
import medproject.medlibrary.patient.Patient;

public class PersonTabController implements ControllerInterface{

	private DataLoader dataLoader;

	//TODO: Customize cell style according to the patient.
	@FXML TableView<Patient> patientTable;
	@FXML TableColumn<Patient, String> cnpColumn;
	@FXML TableColumn<Patient, String> firstNameColumn;
	@FXML TableColumn<Patient, String> lastNameColumn;
	@FXML TableColumn<Patient, String> genderColumn;
	@FXML TableColumn<Patient, String> categoryColumn;
	@FXML TableColumn<Patient, String> birthDateColumn;
	@FXML TableColumn<Patient, String> deceaseDateColumn;
	@FXML TableColumn<Patient, String> registrationDateColumn;
	@FXML TableColumn<Patient, String> unregistrationDateColumn;
	@FXML TableColumn<Patient, String> countyColumn;
	@FXML TableColumn<Patient, String> cityColumn;
	@FXML TableColumn<Patient, String> streetColumn;

	@FXML RadioButton allInsuredPatientRadio;
	@FXML RadioButton insuredPatientRadio;
	@FXML RadioButton uninsuredPatientRadio;
	@FXML RadioButton allRegisteredPatientRadio;
	@FXML RadioButton registeredPatientRadio;
	@FXML RadioButton unregisteredPatientRadio;

	@FXML TextField patientSearchField;
	@FXML ChoiceBox<String> searchTypeBox;

	@FXML Button adaugaPersoanaButton;
	@FXML Button modificaPersoanaButton;
	@FXML Button stergePersoanaButton;
	@FXML Button reinscriereButton;
	@FXML Button iesireButton;
	@FXML Button decesPersoanaButton;
	@FXML Button tiparireButton;
	@FXML Button exportButton;

	private ToggleGroup insuredRadioGroup, registeredRadioGroup;

	@Override
	public void init(final DataLoader dataLoader, Stage stage) {
		this.dataLoader = dataLoader;

		patientTable.setItems(dataLoader.getPatientList());

		patientTable.setEditable(false);
		setColumnValues();
		setRadioButtonGroups();
		setListeners();
	}

	private void setListeners() {
		patientTable.getSelectionModel().clearSelection();
		
		dataLoader.getPatientList().addListener(new ListChangeListener<Patient>(){

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Patient> event) {
				patientTable.setItems(dataLoader.getPatientList());
			}
		});//useless maybe?

		patientTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Patient>(){

			@Override
			public void changed(ObservableValue<? extends Patient> observable, Patient oldValue, Patient newValue) {
				if(newValue == null){
					stergePersoanaButton.setDisable(true);
					return;
				}
				if(newValue.getPatientRecord().getDeceaseDate() != null){
					iesireButton.setDisable(true);
					stergePersoanaButton.setDisable(false);
					reinscriereButton.setDisable(true);
					decesPersoanaButton.setDisable(true);
				}
				else if(newValue.getRegistrationRecord().isRegistered()){
					iesireButton.setDisable(false);
					stergePersoanaButton.setDisable(true);
					reinscriereButton.setDisable(true);
					decesPersoanaButton.setDisable(false);
				}
				else{
					iesireButton.setDisable(true);
					stergePersoanaButton.setDisable(false);
					reinscriereButton.setDisable(false);
					decesPersoanaButton.setDisable(false);
				}
			}

		});
		modificaPersoanaButton.disableProperty().bind(patientTable.getSelectionModel().selectedItemProperty().isNull());
		stergePersoanaButton.setDisable(true);
		iesireButton.setDisable(true);
		decesPersoanaButton.setDisable(true);
	}

	@FXML protected void onPressAdaugaPersoana(){
		AddPersonWindow addPersonWindow = new AddPersonWindow();
		addPersonWindow.show();
	}

	@FXML protected void onPressModificaPersoana(){
		Patient patient = patientTable.getSelectionModel().getSelectedItem();

		if(patient == null)
			return;

		PatientDataWindow patientDataWindow = new PatientDataWindow(patient);
		patientDataWindow.show();
	}

	@FXML protected void onPressStergePersoana(){
		Patient patient = patientTable.getSelectionModel().getSelectedItem();

		if(patient == null)
			return;
		if(patient.getRegistrationRecord().isRegistered())
			return;

		dataLoader.makeDeletePatientRequest(patient);
	}

	@FXML protected void onPressReinscriere(){
		Patient patient = patientTable.getSelectionModel().getSelectedItem();

		if(patient == null)
			return;
		if(patient.getRegistrationRecord().isRegistered())
			return;

		dataLoader.makeRegisterPatientRequest(patient);
		reinscriereButton.setDisable(true);
	}

	@FXML protected void onPressIesire(){
		Patient patient = patientTable.getSelectionModel().getSelectedItem();

		if(patient == null)
			return;
		if(!patient.getRegistrationRecord().isRegistered())
			return;

		dataLoader.makeUnregisterPatientRequest(patient);
		iesireButton.setDisable(true);
	}

	@FXML protected void onPressDecesPersoana(){
		Patient patient = patientTable.getSelectionModel().getSelectedItem();

		if(patient == null)
			return;
		
		dataLoader.makeDeceasedPatientRequest(patient);
		decesPersoanaButton.setDisable(true);
	}

	@FXML protected void onPressTiparire(){

	}

	@FXML protected void onPressExport(){

	}

	private void setRadioButtonGroups() {
		this.insuredRadioGroup = new ToggleGroup();
		this.registeredRadioGroup = new ToggleGroup();

		allInsuredPatientRadio.setToggleGroup(insuredRadioGroup);
		insuredPatientRadio.setToggleGroup(insuredRadioGroup);
		uninsuredPatientRadio.setToggleGroup(insuredRadioGroup);

		allRegisteredPatientRadio.setToggleGroup(registeredRadioGroup);
		registeredPatientRadio.setToggleGroup(registeredRadioGroup);
		unregisteredPatientRadio.setToggleGroup(registeredRadioGroup);
	}

	private void setColumnValues(){
		cnpColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getPatientRecord().getCNP());
			}
		});

		firstNameColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getPatientRecord().getFirstName());
			}
		});

		lastNameColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getPatientRecord().getLastName());
			}
		});

		genderColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getPatientRecord().getSex().toString());
			}
		});

		categoryColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getPatientRecord().getCategory().toString());
			}
		});

		birthDateColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getPatientRecord().getBirthDateString());
			}
		});

		deceaseDateColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getPatientRecord().getDeceaseDateString());
			}
		});

		registrationDateColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getRegistrationRecord().getRegistrationDateString());
			}
		});

		unregistrationDateColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getRegistrationRecord().getUnregistrationDateString());
			}
		});

		countyColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getPatientRecord().getAddress().getCounty());
			}
		});

		cityColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getPatientRecord().getAddress().getCity());
			}
		});

		streetColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getPatientRecord().getAddress().getStreet());
			}
		});

	}
}
