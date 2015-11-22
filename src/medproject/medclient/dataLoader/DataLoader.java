package medproject.medclient.dataLoader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import medproject.medclient.graphicalInterface.mainWindow.MainWindow;
import medproject.medclient.logging.LogWriter;
import medproject.medclient.netHandler.NetConnectionThread;
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

	private final NetConnectionThread connectionThread;
	private final MainWindow mainWindow;

	private final InitialLoader initialLoader;
	private final LoginLoader loginLoader;
	private final PatientLoader patientLoader;

	private final List<Patient> patientList;
	
	public DataLoader(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		connectionThread = new NetConnectionThread(this);
		connectionThread.setAddress(new InetSocketAddress("localhost", 1338));

		pendingRequests = Collections.synchronizedList(new ArrayList<Request>());
		completedRequests = new LinkedBlockingQueue<Request>();

		initialLoader = new InitialLoader(this);
		loginLoader = new LoginLoader(this);
		patientLoader = new PatientLoader(this);

		patientList = Collections.synchronizedList(new ArrayList<Patient>());
		
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
	
	public List<Patient> getPatientList(){
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
}