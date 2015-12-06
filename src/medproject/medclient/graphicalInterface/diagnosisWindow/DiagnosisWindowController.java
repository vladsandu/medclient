package medproject.medclient.graphicalInterface.diagnosisWindow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Predicate;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medlibrary.diagnosis.Diagnosis;
import medproject.medlibrary.diagnosis.DiagnosisInfo;
import medproject.medlibrary.graphics.structures.DiagnosisInfoTableView;
import medproject.medlibrary.patient.PatientRecord;

public class DiagnosisWindowController implements ControllerInterface{
	
	@FXML ScrollPane diagnosisScrollPane;
	@FXML Button adaugaButton, anuleazaButton;
	@FXML TextField searchTextField, dataIntrareTextField, dataIesireTextField;
	@FXML RadioButton codRadio, numeRadio, toateRadio;
	@FXML TextArea observatiiTextArea;
	@FXML CheckBox activCheckBox;
	
	private ToggleGroup searchGroup;
	
	
	private ObservableList<PatientRecord> patientRecordList;
	private DataLoader dataLoader;
	private Stage stage;
	private DiagnosisInfoTableView diagnosisInfoTable;
	private boolean isDiagnosisNull;
	@Override
	public void init(DataLoader dataLoader, Stage stage) {
		this.dataLoader = dataLoader;
		this.stage = stage;
		diagnosisInfoTable = new DiagnosisInfoTableView();
		this.patientRecordList = FXCollections.observableArrayList();
	
		setRadioGroups();
		setTable();
		setDefaultValues();
		
		adaugaButton.disableProperty().bind(diagnosisInfoTable.getSelectionModel().selectedItemProperty().isNull());
	}

	private void setDefaultValues() {
		Diagnosis diagnosis = ((DiagnosisWindow) stage).getDiagnosis();
		
		if(diagnosis == null)
			isDiagnosisNull = true;
		else
			isDiagnosisNull = false;
		
		if(!isDiagnosisNull){
			adaugaButton.setText("Actualizeaza");
			dataIntrareTextField.setText(diagnosis.getStartDate().toString());
			dataIesireTextField.setText(diagnosis.getEndDateString());
			activCheckBox.setSelected(diagnosis.isActive());
			observatiiTextArea.setText(diagnosis.getObservations());
			searchTextField.setText(Integer.toString(diagnosis.getID()));
		}
		else{
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();

			activCheckBox.setSelected(true);
			dataIntrareTextField.setText(dateFormat.format(date));
			dataIesireTextField.setText("-");
		}
		
		dataIntrareTextField.setEditable(false);
		dataIesireTextField.setEditable(false);
	}

	private void setRadioGroups() {
		searchGroup = new ToggleGroup();

		codRadio.setToggleGroup(searchGroup);
		codRadio.setUserData("COD");
		codRadio.setSelected(true);

		numeRadio.setToggleGroup(searchGroup);
		numeRadio.setUserData("NUME");

		toateRadio.setToggleGroup(searchGroup);
		toateRadio.setUserData("TOATE");
	}

	private void setTable() {
		diagnosisScrollPane.setContent(diagnosisInfoTable);

		final FilteredList<DiagnosisInfo> filteredData = new FilteredList<DiagnosisInfo>(dataLoader.getDiagnosisLoader().getDiagnosisInfoList());

		searchTextField.textProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
				filteredData.setPredicate(new Predicate<DiagnosisInfo>(){

					@Override
					public boolean test(DiagnosisInfo diagnosis) {
						if(newValue == null)
							return true;
						if(newValue.isEmpty())
							return true;
						if(diagnosis == null)
							return false;
			
						String searchString = newValue.toLowerCase();
						
						switch(searchGroup.getSelectedToggle().getUserData().toString()){
						case "COD":
							if(Integer.toString(diagnosis.getID()).toLowerCase().contains(searchString))
								return true;
							break;
						case "NUME":
							if(diagnosis.getName().toLowerCase().contains(searchString))
								return true;
							break;
						case "TOATE":
							if(Integer.toString(diagnosis.getID()).toLowerCase().contains(searchString) ||
									diagnosis.getName().toLowerCase().contains(searchString))
								return true;
							break;
						}
						
						return false;
					}
				});
			}
		});
		
		SortedList<DiagnosisInfo> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(diagnosisInfoTable.comparatorProperty());

		diagnosisInfoTable.setItems(sortedData);
	}
	
	@FXML protected void onActionActivCheckBox(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();

		if(activCheckBox.isSelected()){
			dataIntrareTextField.setText(dateFormat.format(date));
			dataIesireTextField.setText("-");
		}
		else{
			dataIesireTextField.setText(dateFormat.format(date));
		}
	}

	@FXML protected void onPressAdauga(){
	//	
	}
	
	@FXML protected void onPressAnuleaza(){
		closeWindow();
	}

	public void closeWindow(){
		dataLoader.makeWindowRequest(new Runnable(){

			@Override
			public void run() {
				stage.close();	
			}
			
		});
	}
}
