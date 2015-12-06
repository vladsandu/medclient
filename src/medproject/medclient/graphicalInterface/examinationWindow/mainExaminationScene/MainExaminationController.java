package medproject.medclient.graphicalInterface.examinationWindow.mainExaminationScene;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.graphicalInterface.Navigator;

public class MainExaminationController implements ControllerInterface{
	private DataLoader dataLoader;
	private Stage stage;
	@FXML Tab diagnosticeTab;
	
	@Override
	public void init(DataLoader dataLoader, Stage stage) {
		this.dataLoader = dataLoader;
		this.stage = stage;
		
		setTabs();
	}

	private void setTabs() {
		diagnosticeTab.setContent(Navigator.getPaneFromScene(Navigator.EXAMINATION_DIAGNOSIS_TAB_SCENE, stage));
	}
}
