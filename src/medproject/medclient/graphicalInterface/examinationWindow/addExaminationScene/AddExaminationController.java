package medproject.medclient.graphicalInterface.examinationWindow.addExaminationScene;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Predicate;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medlibrary.examination.Examination;
import medproject.medlibrary.examination.ExaminationType;
import medproject.medlibrary.graphics.structures.PatientTableView;
import medproject.medlibrary.patient.Patient;

public class AddExaminationController implements ControllerInterface{
	private DataLoader dataLoader;
	private Stage stage;
	private PatientTableView patientTable;
	
	@FXML ScrollPane patientTableScrollPane;
	@FXML Button continuaButton;
	@FXML Button anuleazaButton;
	@FXML ChoiceBox<String> searchTypeBox;
	@FXML TextField patientSearchField;
	
	@FXML TextField dataTextField;
	@FXML TextField oraTextField;
	@FXML ChoiceBox<ExaminationType> tipConsultatieChoiceBox;
	@Override
	public void init(DataLoader dataLoader, Stage stage) {
		this.dataLoader = dataLoader;
		this.stage = stage;
		patientTable = new PatientTableView();
		
		setTableFilters();
		setDateTextFields();
		setTypeChoiceBox();
		patientTableScrollPane.setContent(patientTable);
	}
	
	private void setTypeChoiceBox() {
		for(ExaminationType type : ExaminationType.values()){
			tipConsultatieChoiceBox.getItems().add(type);
		}
		
		tipConsultatieChoiceBox.getSelectionModel().select(0);
	}

	private void setDateTextFields() {
		Date date = new Date();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		dataTextField.setText(dateFormat.format(date).toString());
		dataTextField.setEditable(false);
		
		dateFormat = new SimpleDateFormat("HH:mm:ss");
		oraTextField.setText(dateFormat.format(date).toString());	
		oraTextField.setEditable(false);
	}

	private void setTableFilters() {
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
							return false;
						if(newValue.isEmpty())
							return false;
						if(patient == null)
							return false;
						if(!patient.getRegistrationRecord().isRegistered())
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
		SortedList<Patient> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(patientTable.comparatorProperty());
		
		patientTable.setItems(sortedData);
		
		patientSearchField.setText(null);
		continuaButton.disableProperty().bind(patientTable.getSelectionModel().selectedItemProperty().isNull());
	}

	@FXML protected void onPressContinua(){
		Patient currentPatient = patientTable.getSelectedPatient();
		
		if(currentPatient == null)
			return;

		Examination examination = new Examination(currentPatient.getPatientID(), tipConsultatieChoiceBox.getSelectionModel().getSelectedItem());
		dataLoader.makeAddExaminationRequest(this, examination, 1234);
	}
	
	@FXML protected void onPressAnuleaza(){
		stage.close();
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
