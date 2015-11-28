package medproject.medclient.graphicalInterface.patientDataWindow.patientRecordScene;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.graphicalInterface.patientDataWindow.PatientDataWindow;
import medproject.medlibrary.patient.Patient;

public class PatientRecordController implements ControllerInterface{

	private DataLoader dataLoader;
	private Patient patient;
	
	@FXML TextField firstNameTextField;
	
	@Override
	public void init(DataLoader dataLoader, Stage stage) {
		this.dataLoader = dataLoader;
		patient = ((PatientDataWindow)stage).getPatient();
	}
	
}
