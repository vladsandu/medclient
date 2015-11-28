package medproject.medclient.graphicalInterface.patientDataWindow.patientDataScene;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.graphicalInterface.Navigator;
import medproject.medclient.graphicalInterface.patientDataWindow.PatientDataWindow;
import medproject.medlibrary.patient.Patient;

public class PatientDataController implements ControllerInterface{

	private DataLoader dataLoader;
	private Stage stage;
	
	private Patient patient;
	
	@FXML TabPane mainTabPane;
	@FXML Tab recordTab;
	
	@Override
	public void init(DataLoader dataLoader, Stage stage) {
		this.dataLoader = dataLoader;
		this.stage = stage;
		patient = ((PatientDataWindow)stage).getPatient();
		loadTabContent();
	}
	
	private void loadTabContent(){
		recordTab.setContent(Navigator.getPaneFromScene(Navigator.PATIENT_RECORD_SCENE, stage));
	}
	
}
