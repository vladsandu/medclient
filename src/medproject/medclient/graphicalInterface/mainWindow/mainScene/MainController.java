package medproject.medclient.graphicalInterface.mainWindow.mainScene;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.graphicalInterface.mainWindow.Navigator;

public class MainController implements ControllerInterface{

	private DataLoader dataLoader;
	private ExecutorService executor;

	@FXML TabPane mainTabPane;
	@FXML Tab personsTab;
	
	@Override
	public void init(DataLoader dataLoader, ExecutorService executor) {
		this.dataLoader = dataLoader;
		this.executor = executor;

		loadTabContent();
	}
	
	private void loadTabContent(){
		personsTab.setContent(Navigator.getPaneFromScene(Navigator.PERSON_TAB_SCENE));
	}
	
	@FXML protected void onSelectPersonsTab(){
		if(personsTab.isSelected()){
		}
		else{	
		}
	}

}
