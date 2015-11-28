package medproject.medclient.graphicalInterface.patientDataWindow.patientRecordScene;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.graphicalInterface.patientDataWindow.PatientDataWindow;
import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.patient.Address;
import medproject.medlibrary.patient.BloodType;
import medproject.medlibrary.patient.Gender;
import medproject.medlibrary.patient.Patient;
import medproject.medlibrary.patient.PatientCategory;
import medproject.medlibrary.patient.PatientStatus;
import medproject.medlibrary.patient.RHType;

public class PatientRecordController implements ControllerInterface{

	private DataLoader dataLoader;
	private Stage stage;
	private Patient patient;

	@FXML TextField firstNameTextField;
	@FXML TextField cnpTextField;
	@FXML TextField pidTextField;
	@FXML TextField lastNameTextField;
	@FXML TextField birthDateTextField;
	@FXML TextField deceaseDateTextField;
	@FXML TextField registrationDateTextField;
	@FXML TextField unregistrationDateTextField;
	@FXML TextField countyTextField;
	@FXML TextField cityTextField;
	@FXML TextField phoneTextField;
	@FXML TextField streetTextField;
	@FXML TextField streetNumberTextField;
	@FXML TextField zipCodeTextField;
	@FXML TextField buildingTextField;
	@FXML TextField scaraTextField;
	@FXML TextField apartmentTextField;

	@FXML ChoiceBox<Gender> genderChoiceBox;
	@FXML ChoiceBox nationalityChoiceBox;
	@FXML ChoiceBox<PatientCategory> categoryChoiceBox;
	@FXML ChoiceBox<PatientStatus> patientStatusChoiceBox;
	@FXML ChoiceBox<BloodType> bloodTypeChoiceBox;
	@FXML ChoiceBox<RHType> rhChoiceBox;
	@FXML CheckBox registeredCheckBox;

	@FXML Button saveButton;
	@FXML Button cancelButton;

	@Override
	public void init(DataLoader dataLoader, Stage stage) {
		this.dataLoader = dataLoader;
		this.stage = stage;
		patient = ((PatientDataWindow)stage).getPatient();

		initializeData();
	}


	private void initializeData() {
		cnpTextField.setDisable(true);
		cnpTextField.setText(patient.getPatientRecord().getCNP());

		pidTextField.setDisable(true);
		pidTextField.setText(Integer.toString(patient.getPatientID()));

		firstNameTextField.setDisable(true);
		firstNameTextField.setText(patient.getPatientRecord().getFirstName());
		
		lastNameTextField.setDisable(true);
		lastNameTextField.setText(patient.getPatientRecord().getLastName());

		birthDateTextField.setDisable(true);
		birthDateTextField.setText(patient.getPatientRecord().getBirthDateString());

		deceaseDateTextField.setDisable(true);
		deceaseDateTextField.setText(patient.getPatientRecord().getDeceaseDateString());

		registrationDateTextField.setDisable(true);
		registrationDateTextField.setText(patient.getRegistrationRecord().getRegistrationDateString());

		unregistrationDateTextField.setDisable(true);
		unregistrationDateTextField.setText(patient.getRegistrationRecord().getUnregistrationDateString());

		countyTextField.setText(patient.getPatientRecord().getAddress().getCounty());
		
		cityTextField.setText(patient.getPatientRecord().getAddress().getCity());
		
		streetTextField.setText(patient.getPatientRecord().getAddress().getStreet());

		registeredCheckBox.setDisable(true);
		registeredCheckBox.setSelected(patient.getRegistrationRecord().isRegistered());

		bloodTypeChoiceBox.getItems().addAll(BloodType.values());
		bloodTypeChoiceBox.setDisable(true);
		bloodTypeChoiceBox.getSelectionModel().select(patient.getPatientRecord().getBloodType());
		
		categoryChoiceBox.getItems().addAll(PatientCategory.values());
		categoryChoiceBox.getSelectionModel().select(patient.getPatientRecord().getCategory());
		categoryChoiceBox.setDisable(true);

		patientStatusChoiceBox.getItems().addAll(PatientStatus.values());
		patientStatusChoiceBox.getSelectionModel().select(patient.getPatientRecord().getStatus());
		patientStatusChoiceBox.setDisable(true);
		
		genderChoiceBox.getItems().addAll(Gender.values());
		genderChoiceBox.getSelectionModel().select(patient.getPatientRecord().getSex());
		genderChoiceBox.setDisable(true);

		rhChoiceBox.getItems().addAll(RHType.values());
		rhChoiceBox.getSelectionModel().select(patient.getPatientRecord().getRhType());
		rhChoiceBox.setDisable(true);	
	}

	@FXML protected void onPressSave(){
		//TODO: Checkings
		//TODO: County and city - choice boxes???
		
		if(countyTextField.getText().length() == 0 ||
				cityTextField.getText().length() == 0 ||
				streetTextField.getText().length() == 0){
			GUIUtils.showErrorDialog("Eroare", "Nu ai completat toate datele.");
			return;
		}
		
		cancelButton.setDisable(true);
		saveButton.setDisable(true);
		Address newAddress = new Address(
				patient.getPatientRecord().getPERSON_ID(),
				countyTextField.getText(),
				cityTextField.getText(), 
				streetTextField.getText());
		
		dataLoader.makeUpdatePatientAddressRequest(this, newAddress);
	}

	@FXML protected void onPressCancel(){
		closeWindow();
	}


	public void closeWindow() {
		dataLoader.makeWindowRequest(new Runnable(){

			@Override
			public void run() {
				stage.close();	
			}

		});
	}
}
