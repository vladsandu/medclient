package medproject.medclient.graphicalInterface.diagnosisWindow;

import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import medproject.medclient.graphicalInterface.Navigator;
import medproject.medclient.graphicalInterface.examinationWindow.diagnosisTabScene.DiagnosisTabController;
import medproject.medlibrary.diagnosis.Diagnosis;

public class DiagnosisWindow extends Stage{

	private final Diagnosis diagnosis;
	private final DiagnosisTabController examinationController;
	
	public DiagnosisWindow(DiagnosisTabController examinationController, Diagnosis diagnosis){
		this.diagnosis = diagnosis;
		this.examinationController = examinationController;
		
		setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				close();
			}
		});

		if(diagnosis == null){
			setTitle("Adauga Diagnostic"); //TODO: All strings loaded from a file
		}
		else{
			setTitle("Modifica Diagnostic"); //TODO: All strings loaded from a file
		}
		
		setScene(Navigator.getScene(Navigator.DIAGNOSIS_WINDOW_SCENE, this));
		
		initStyle(StageStyle.UTILITY);
		initModality(Modality.APPLICATION_MODAL);   
	}

	public Diagnosis getDiagnosis() {
		return diagnosis;
	}

	public DiagnosisTabController getExaminationController() {
		return examinationController;
	}
}
