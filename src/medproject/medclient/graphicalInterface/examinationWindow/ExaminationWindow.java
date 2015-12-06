package medproject.medclient.graphicalInterface.examinationWindow;

import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import medproject.medclient.graphicalInterface.Navigator;
import medproject.medlibrary.examination.Examination;

public class ExaminationWindow extends Stage{
	
	private final Examination examination;
	private final boolean newExamination;
	
	public ExaminationWindow(Examination examination, boolean newExamination){
		this.examination = examination;
		this.newExamination = newExamination;
		
		if(examination == null)
			setTitle("Adauga Consultatie");
		else
			setTitle("Vizualizeaza Consultatia");
		
		setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                close();
            }
        });
		if(examination == null)
			setScene(Navigator.getScene(Navigator.ADD_EXAMINATION_SCENE, this));
		else
			setScene(Navigator.getScene(Navigator.MAIN_EXAMINATION_SCENE, this));
		
		initModality(Modality.APPLICATION_MODAL);   
	}

	public Examination getExamination() {
		return examination;
	}

	public boolean isNewExamination() {
		return newExamination;
	}
}
