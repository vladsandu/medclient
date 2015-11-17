package medproject.medclient._clientRunner;

import java.io.IOException;
import java.net.InetSocketAddress;

import javafx.application.Application;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.loginWindow.LoginWindow;
import medproject.medclient.graphicalInterface.mainWindow.MainWindow;
import medproject.medclient.logging.LogWriter;
import medproject.medclient.netHandler.NetConnectionThread;


public class MainClient extends Application{	

	@Override
	public void start(Stage primaryWindow) throws Exception {

		LogWriter.useFileLogging();
		
		final LoginWindow loginWindow = new LoginWindow();

		final MainWindow mainWindow = new MainWindow();

		final DataLoader dataLoader = new DataLoader(loginWindow, mainWindow);
		

		try {
			dataLoader.start();
			loginWindow.startWindow(dataLoader);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {

		launch(args);    
	}
}