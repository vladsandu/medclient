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
	
	public ExaminationWindow(Examination examination){
		this.examination = examination;
		
		if(examination == null)
			setTitle("Adauga Consultatie");
		else
			setTitle("Vizualizeaza Consultatia");
		
		setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                close();
            }
        });
		setScene(Navigator.getScene(Navigator.EXAMINATION_WINDOW_SCENE, this));
		initStyle(StageStyle.UTILITY);
		initModality(Modality.APPLICATION_MODAL);   
	}
	
	
	
}
