package medproject.medclient._clientRunner;

import java.io.IOException;
import java.net.InetSocketAddress;

import javafx.application.Application;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.loginWindow.LoginWindow;
import medproject.medclient.graphicalInterface.mainWindow.MainWindow;
import medproject.medclient.netHandler.NetConnectionThread;


public class MainClient extends Application{	

	@Override
	public void start(Stage primaryWindow) throws Exception {

		final LoginWindow loginWindow = new LoginWindow();

		final MainWindow mainWindow = new MainWindow();

		final DataLoader dataLoader = new DataLoader(loginWindow, mainWindow);

		final NetConnectionThread connectionThread = new NetConnectionThread(dataLoader);
		connectionThread.setAddress(new InetSocketAddress("localhost", 1338));

		

		try {
			connectionThread.start();
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