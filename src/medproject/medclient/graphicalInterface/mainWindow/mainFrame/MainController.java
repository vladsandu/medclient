package medproject.medclient.graphicalInterface.mainWindow.mainFrame;

import java.util.concurrent.ExecutorService;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;

public class MainController implements ControllerInterface{

	private DataLoader dataLoader;
	private ExecutorService updateExecutor;
	
	@FXML private StackPane childrenHolder;
	@FXML private Label connection_status_label;
	
	public void setScene(Node node) {
		childrenHolder.getChildren().setAll(node);
	}

	public void modifyConnectionStatusLabel(boolean status){
		if(status)
			connection_status_label.setText("Connected");
		else
			connection_status_label.setText("Not Connected");
	}
	
	@Override
	public void init(DataLoader dataLoader, ExecutorService executor){
		this.dataLoader = dataLoader;
		this.updateExecutor = executor;
	}

	public DataLoader getDataLoader() {
		return dataLoader;
	}

	public ExecutorService getUpdateExecutor() {
		return updateExecutor;
	}
	
}
