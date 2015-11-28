package medproject.medclient.graphicalInterface.patientDataWindow;

import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.Navigator;
import medproject.medlibrary.patient.Patient;

public class PatientDataWindow extends Stage{

	private final Patient patient;
	
	public PatientDataWindow(Patient patient){
		this.patient = patient;

		setTitle("Fisa pacientului: " + 
				patient.getPatientRecord().getFirstName() + " " + 
				patient.getPatientRecord().getLastName());
		setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                close();
            }
        });

		setScene(Navigator.getScene(Navigator.PATIENT_DATA_SCENE, this));
		
		initStyle(StageStyle.UTILITY);
		initModality(Modality.APPLICATION_MODAL);   
	}

	public Patient getPatient() {
		return patient;
	}
	
}
