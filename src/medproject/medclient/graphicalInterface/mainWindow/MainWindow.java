package medproject.medclient.graphicalInterface.mainWindow;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;

public class MainWindow{

	public DataLoader dataLoader;
	private ExecutorService updateExecutor;
	
	private Stage primaryWindow;
	MainWindowController mainController;
	
	public void startWindow(DataLoader dataLoader) throws Exception {
		this.dataLoader = dataLoader;
		this.updateExecutor = Executors.newSingleThreadScheduledExecutor();
		start();
	}

	public void start() throws Exception {
		primaryWindow = new Stage();
		Scene scene = createScene(loadMainPane());
		
		primaryWindow.setTitle("Messenger");
        primaryWindow.setScene(scene);
        primaryWindow.show();
        
    }
	
	private Pane loadMainPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();
 
        Pane mainPane = (Pane) loader.load(
            getClass().getResourceAsStream(
                "./mainWindow.fxml"
            )
        );
 
        mainController = loader.getController();
        mainController.init(dataLoader, updateExecutor);
        
        return mainPane;
    }
	
	private Scene createScene(Pane mainPane) {
        Scene scene = new Scene(
            mainPane
        );
 
        /*scene.getStylesheets().setAll(
            getClass().getResource("style.css").toExternalForm()
        );
        */
        
        return scene;
    }
	

	
	public void runAndWait(final Runnable runnable) {
		// running this from the FAT 
		if (Platform.isFxApplicationThread()) {
			runnable.run();
			return;
		}
		FutureTask<Void> future = new FutureTask<>(runnable, null);
		Platform.runLater(future);
	}

	public Stage getPrimaryWindow() {
		return primaryWindow;
	}

	public MainWindowController getMainController() {
		return mainController;
	}
	
}
