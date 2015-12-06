package medproject.medclient.graphicalInterface.examinationWindow.diagnosisTabScene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.graphicalInterface.diagnosisWindow.DiagnosisWindow;
import medproject.medclient.graphicalInterface.examinationWindow.ExaminationWindow;
import medproject.medlibrary.diagnosis.Diagnosis;
import medproject.medlibrary.examination.Examination;
import medproject.medlibrary.graphics.structures.DiagnosisTableView;

public class DiagnosisTabController implements ControllerInterface{
	private DataLoader dataLoader;
	private Stage stage;
	
	private DiagnosisTableView diagnosisTable;
	private ObservableList<Diagnosis> diagnosisList;
	
	@FXML Button adaugaDiagnosticButton;
	@FXML Button modificaDiagnosticButton;
	@FXML Button stergeDiagnosticButton;
	@FXML ScrollPane diagnosticScrollPane;
	
	@FXML Button adaugaSimptomButton;
	@FXML Button modificaSimptomButton;
	@FXML Button stergeSimptomButton;
	@FXML ScrollPane simptomScrollPane;
	
	@Override
	public void init(DataLoader dataLoader, Stage stage) {
		this.dataLoader = dataLoader;
		this.stage = stage;
		diagnosisTable = new DiagnosisTableView();
		diagnosisList = FXCollections.observableArrayList();
		setTables();
	}
	
	private void setTables() {
		Examination currentExamination = ((ExaminationWindow) stage).getExamination();
		diagnosisList.addAll(currentExamination.getDiagnosisList());
		diagnosisTable.setItems(diagnosisList);
		
		diagnosticScrollPane.setContent(diagnosisTable);
	}

	public void closeWindow() {
		dataLoader.makeWindowRequest(new Runnable(){

			@Override
			public void run() {
				stage.close();	
			}
			
		});
	}
	
	@FXML protected void onPressAdaugaDiagnostic(){
		//if(diagnosisTable.getSelectionModel().getSelectedItem() == null)
			//return;
		
		DiagnosisWindow diagnosisWindow = new DiagnosisWindow(this, diagnosisTable.getSelectionModel().getSelectedItem());
		diagnosisWindow.show();
	}

	@FXML protected void onPressModificaDiagnostic(){
		
	}

	@FXML protected void onPressStergeDiagnostic(){
		
	}
	
	@FXML protected void onPressAdaugaSimptom(){
		
	}

	@FXML protected void onPressModificaSimptom(){
		
	}

	@FXML protected void onPressStergeSimptom(){
		
	}
		
}
