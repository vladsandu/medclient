package medproject.medclient.graphicalInterface.mainWindow.personTabScene;

import java.util.concurrent.ExecutorService;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medlibrary.patient.Patient;

public class PersonTabController implements ControllerInterface{

	private DataLoader dataLoader;
	private ExecutorService executor;

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
	public void init(DataLoader dataLoader, ExecutorService executor) {
		this.dataLoader = dataLoader;
		this.executor = executor;

		patientTable.setItems(dataLoader.getPatientList());
		patientTable.setEditable(false);
		setColumnValues();
		setRadioButtonGroups();
	}

	@FXML protected void onPressAdaugaPersoana(){
	}

	@FXML protected void onPressModificaPersoana(){

	}

	@FXML protected void onPressStergePersoana(){

	}

	@FXML protected void onPressReinscriere(){

	}

	@FXML protected void onPressIesire(){

	}

	@FXML protected void onPressDecesPersoana(){

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
				return new ReadOnlyObjectWrapper<String>(p.getValue().getPatientRecord().getSex());
			}
		});

		categoryColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getCategory().toString());
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
