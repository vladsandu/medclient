package medproject.medclient.dataLoader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import medproject.medclient.graphicalInterface.mainWindow.MainWindow;
import medproject.medclient.logging.LogWriter;
import medproject.medclient.netHandler.NetConnectionThread;
import medproject.medlibrary.concurrency.CustomTask;
import medproject.medlibrary.concurrency.Request;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.patient.Patient;

public class DataLoader implements Runnable{

	private final Logger LOG = LogWriter.getLogger("DataLoader");

	private final Thread thread;  
	private final AtomicBoolean connectionStatus;
	//TODO: Move connectionStatus to the connectionThread and callback methods etc.

	private final List<Request> pendingRequests;
	private final LinkedBlockingQueue<Request> completedRequests;
	private final List<CustomTask> guiTasks;
	
	private final NetConnectionThread connectionThread;
	private final MainWindow mainWindow;

	private final InitialLoader initialLoader;
	private final LoginLoader loginLoader;
	private final PatientLoader patientLoader;

	private final ObservableList<Patient> patientList;
	private final ExecutorService executor;
	
	public DataLoader(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		executor = Executors.newSingleThreadExecutor();
		
		connectionThread = new NetConnectionThread(this);
		connectionThread.setAddress(new InetSocketAddress("localhost", 1338));

		guiTasks = Collections.synchronizedList(new ArrayList<CustomTask>());
		pendingRequests = Collections.synchronizedList(new ArrayList<Request>());
		completedRequests = new LinkedBlockingQueue<Request>();

		initialLoader = new InitialLoader(this);
		loginLoader = new LoginLoader(this);
		patientLoader = new PatientLoader(this);

		patientList = FXCollections.observableArrayList();
		
		connectionStatus = new AtomicBoolean(false);
		thread = new Thread(this);

	}

	public void start() throws IOException{	
		LOG.info("Starting data loader");
		thread.start();
		connectionThread.start();
	}

	public void stop(){
		LOG.info("Stopping data loader");
		thread.interrupt();
		connectionThread.stop();
	}
	
	@Override
	public void run(){

		while(!Thread.interrupted()){  
			Request request = null;
			try {
				request = completedRequests.take();
				processCompletedRequest(request);
			} catch (InterruptedException e) {
				LOG.severe("Data Loader thread interrupted");
			}
		}
	}

	public void processGuiTask(CustomTask task){
		guiTasks.add(task);
	}
	
	public void makeRequest(Request request){
		pendingRequests.add(request);
	}

	/**
	 * Processes the request and sends it to the appropriate loader.
	 * @param request the Request to be processed.
	 */

	private void processCompletedRequest(Request request) {
		switch(RequestCodes.getRequestType(request)){
		case RequestCodes.LOGIN_TYPE_REQUEST:
			loginLoader.processRequest(request);
			break;
		case RequestCodes.PATIENT_TYPE_REQUEST:
			patientLoader.processRequest(request);
			break;
		}
	}

	public void processReceivedRequest(Request processingRequest){

		if(processingRequest.getREQUEST_CODE() == connectionThread.getCurrentRequest().getREQUEST_CODE()){
			connectionThread.setCurrentRequestCompleted(true);
		}

		completedRequests.add(processingRequest);
	}

	public void makeLoginRequest(String operatorName, String password){
		loginLoader.makeLoginRequest(operatorName, password);
	}

	public void makeWindowRequest(Runnable runnable){
		mainWindow.runAndWait(runnable);
	}

	public void addPatient(Patient patient){
		patientList.add(patient);
	}
	
	public ObservableList<Patient> getPatientList(){
		return patientList;
	}
	
	public boolean getConnectionStatus(){
		return connectionStatus.get();
	}

	public void setConnectionStatus(Boolean value){
		connectionStatus.set(value);
	}	

	public List<Request> getPendingRequests() {
		return pendingRequests;
	}

	public InitialLoader getInitialLoader() {
		return initialLoader;
	}

	public PatientLoader getPatientLoader() {
		return patientLoader;
	}

	public MainWindow getMainWindow() {
		return mainWindow;
	}

	public ExecutorService getExecutor() {
		return executor;
	}
}