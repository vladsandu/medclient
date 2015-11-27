package medproject.medclient.graphicalInterface.addPersonWindow;

import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import medproject.medclient.graphicalInterface.Navigator;

public class AddPersonWindow extends Stage{

	public AddPersonWindow(){
		setTitle("Adauga Pacient"); //TODO: All strings loaded from a file
		setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                close();
            }
        });
		setScene(Navigator.getScene(Navigator.ADD_PERSON_SCENE));
		initStyle(StageStyle.UTILITY);
		initModality(Modality.APPLICATION_MODAL);   
	}
	
	
	
}
