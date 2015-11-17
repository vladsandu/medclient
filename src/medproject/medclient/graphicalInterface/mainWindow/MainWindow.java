package medproject.medclient.graphicalInterface.mainWindow;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.mainWindow.mainFrame.MainController;

public class MainWindow{
	
	public MainController mainController;
	public DataLoader dataLoader;
	private ExecutorService updateExecutor;
	
	private Stage primaryWindow;
	
	public void startWindow(DataLoader dataLoader) throws Exception {
		this.updateExecutor = Executors.newSingleThreadScheduledExecutor();
		this.dataLoader = dataLoader;
		start();
	}

	public void start() throws Exception {
		primaryWindow = new Stage();
		Scene scene = createScene(loadMainPane());
		
		primaryWindow.setTitle("Messenger");
        primaryWindow.setScene(scene);
        primaryWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
                stop();
            }
        });  
        primaryWindow.show();
        
    }
	
	public void stop(){
		dataLoader.stop();
		primaryWindow.close();
	}
	
	public void modifyConnectionStatus(boolean connectionStatus){
	}
	
	public void runAndWait(final Runnable runnable) {
		// running this from the FAT 
		if (Platform.isFxApplicationThread()) {
			runnable.run();
			return;
		}
		// running this from another thread
		try {
			FutureTask<Void> future = new FutureTask<>(runnable, null);
			Platform.runLater(future);
			future.get();
		}
		catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Pane loadMainPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();
 
        Pane mainPane = (Pane) loader.load(
            getClass().getResourceAsStream(
                Navigator.MAIN_FRAME
            )
        );
 
        MainController mainController = loader.getController();
        mainController.init(dataLoader, updateExecutor);
        
        Navigator.setMainController(mainController);
        Navigator.loadScene(Navigator.LOGIN_SCENE);
        return mainPane;
    }
	
	private Scene createScene(Pane mainPane) {
        Scene scene = new Scene(
            mainPane
        );
 
        scene.getStylesheets().setAll(
            getClass().getResource("style.css").toExternalForm()
        );
        
        
        return scene;
    }
	
	public MainController getLoginController() {
		return mainController;
	}

	public ExecutorService getUpdateExecutor() {
		return updateExecutor;
	}

}
