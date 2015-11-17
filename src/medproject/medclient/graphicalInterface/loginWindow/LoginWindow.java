package medproject.medclient.graphicalInterface.loginWindow;

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
import medproject.medclient.graphicalInterface.loginWindow.loginScene.LoginController;
import medproject.medclient.graphicalInterface.loginWindow.mainFrame.MainLoginController;

public class LoginWindow{

	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	//the controllers will ask for updates via executorServices and update themselves
	public LoginController loginController;
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
        primaryWindow.show();
        
    }
	
	public void stop(){
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
                LoginNavigator.MAIN_FRAME
            )
        );
 
        MainLoginController mainController = loader.getController();
        mainController.init(dataLoader, updateExecutor);
        
        LoginNavigator.setMainLoginController(mainController);
        LoginNavigator.loadScene(LoginNavigator.LOGIN_SCENE);
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
	
	public LoginController getLoginController() {
		return loginController;
	}

	public ExecutorService getUpdateExecutor() {
		return updateExecutor;
	}

}
