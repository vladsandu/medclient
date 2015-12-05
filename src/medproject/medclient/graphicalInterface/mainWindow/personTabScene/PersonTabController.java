package medproject.medclient.graphicalInterface.mainWindow.personTabScene;

import java.util.function.Predicate;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.graphicalInterface.addPersonWindow.AddPersonWindow;
import medproject.medclient.graphicalInterface.patientDataWindow.PatientDataWindow;
import medproject.medlibrary.graphics.structures.PatientTableView;
import medproject.medlibrary.patient.Patient;

public class PersonTabController implements ControllerInterface{

	private DataLoader dataLoader;

	//TODO: Customize cell style according to the patient.
	TableView<Patient> patientTable;
	
	@FXML ScrollPane tableScrollPane;
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
		patientTable = new PatientTableView();
		tableScrollPane.setContent(patientTable);
		setTableFilters();
		setListeners();
	}

	private void setTableFilters() {

		setRadioButtonGroups();
		
		searchTypeBox.setItems(FXCollections.observableArrayList(
				"Nume", "CNP", "PID"));
		searchTypeBox.getSelectionModel().select(0);
		
		final FilteredList<Patient> filteredData = new FilteredList<Patient>(dataLoader.getPatientList());

		patientSearchField.textProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
				filteredData.setPredicate(new Predicate<Patient>(){

					@Override
					public boolean test(Patient patient) {
						if(newValue == null)
							return true;
						if(newValue.isEmpty())
							return true;
						if(patient == null)
							return false;
			
						String searchString = newValue.toLowerCase();
						
						switch(searchTypeBox.getSelectionModel().getSelectedItem()){
						case "Nume":
							if(patient.getPatientRecord().getFullName().toLowerCase().contains(searchString))
								return true;
							break;
						case "CNP":
							if(patient.getPatientRecord().getCNP().toLowerCase().contains(searchString))
								return true;
							break;
						case "PID":
							if(Integer.toString(patient.getPatientRecord().getPERSON_ID()).toLowerCase().contains(searchString))
								return true;
							break;
						}
						
						return false;
					}
				});
			}
		});
		
		insuredRadioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, final Toggle oldValue, final Toggle newValue) {
				filteredData.setPredicate(new Predicate<Patient>(){

					@Override
					public boolean test(Patient patient) {
						String currentID = newValue.getUserData().toString();
						if(patient == null)
							return false;
						
						boolean insurance = patient.getPatientRecord().isInsured();
						
						if(currentID.equals(allInsuredPatientRadio.getUserData().toString())){
							return true;
						}
						else if(currentID.equals(insuredPatientRadio.getUserData().toString())){
							return insurance;
						}
						else if(currentID.equals(uninsuredPatientRadio.getUserData().toString())){
							return !insurance;
						}
						
						return false;
					}
				});
			}
		});

		registeredRadioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, final Toggle oldValue, final Toggle newValue) {
				filteredData.setPredicate(new Predicate<Patient>(){

					@Override
					public boolean test(Patient patient) {
						String currentID = newValue.getUserData().toString();
						if(patient == null)
							return false;
						
						boolean registered = patient.getRegistrationRecord().isRegistered();
						
						if(currentID.equals(allRegisteredPatientRadio.getUserData().toString())){
							return true;
						}
						else if(currentID.equals(registeredPatientRadio.getUserData().toString())){
							return registered;
						}
						else if(currentID.equals(unregisteredPatientRadio.getUserData().toString())){
							return !registered;
						}
						
						return false;
					}
				});
			}	
		});
		
		
		SortedList<Patient> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(patientTable.comparatorProperty());
		dataLoader.getPatientList().addListener(new ListChangeListener<Patient>(){

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Patient> event) {
				String value = patientSearchField.getText();
				patientSearchField.setText(null);
				patientSearchField.setText(value);
			}
			
		});


		patientTable.setItems(sortedData);
	}


	private void setListeners() {
		patientTable.getSelectionModel().clearSelection();
		
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
		allInsuredPatientRadio.setUserData("allInsuredPatient");
		allInsuredPatientRadio.setSelected(true);
		
		insuredPatientRadio.setToggleGroup(insuredRadioGroup);
		insuredPatientRadio.setUserData("insuredPatient");
		
		uninsuredPatientRadio.setToggleGroup(insuredRadioGroup);
		uninsuredPatientRadio.setUserData("uninsuredPatient");

		allRegisteredPatientRadio.setToggleGroup(registeredRadioGroup);
		allRegisteredPatientRadio.setUserData("allRegisteredPatient");
		allRegisteredPatientRadio.setSelected(true);
		
		registeredPatientRadio.setToggleGroup(registeredRadioGroup);
		registeredPatientRadio.setUserData("registeredPatient");
		
		unregisteredPatientRadio.setToggleGroup(registeredRadioGroup);
		unregisteredPatientRadio.setUserData("unregisteredPatient");
		
	}
}
