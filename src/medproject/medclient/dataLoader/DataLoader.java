package medproject.medclient.dataLoader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import medproject.medclient.graphicalInterface.loginWindow.LoginWindow;
import medproject.medclient.graphicalInterface.mainWindow.MainWindow;
import medproject.medclient.logging.LogWriter;
import medproject.medclient.netHandler.NetConnectionThread;
import medproject.medlibrary.concurrency.Request;
import medproject.medlibrary.concurrency.RequestCodes;

public class DataLoader implements Runnable{

	private final Logger logger = LogWriter.getLogger("DataLoader");
	
	private final Thread thread;  
	private final AtomicBoolean connectionStatus;

	
	private Request currentRequest;
	private Boolean currentRequestCompleted = true;
	private Boolean currentRequestSent = false;

	private final List<Request> pendingRequests;
	private final LinkedBlockingQueue<Request> completedRequests;

	private final NetConnectionThread connectionThread;
	
	public DataLoader(LoginWindow loginWindow, MainWindow mainWindow) {

		connectionThread = new NetConnectionThread(this);
		connectionThread.setAddress(new InetSocketAddress("localhost", 1338));

		pendingRequests = Collections.synchronizedList(new ArrayList<Request>());
		completedRequests = new LinkedBlockingQueue<Request>();
		connectionStatus = new AtomicBoolean(false);
		thread = new Thread(this);
	}
	
	public boolean getConnectionStatus(){
		return connectionStatus.get();
	}
	
	public void setConnectionStatus(Boolean value){
		connectionStatus.set(value);
	}
	
	@Override
	public void run(){
		
			while(!Thread.interrupted()){  
				Request request = null;
				try {
					request = completedRequests.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				processCompletedRequest(request);
						
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
		
		
	}

	public void processNewRequest(Request processingRequest){
		if(processingRequest.getREQUEST_STATUS() == RequestCodes.REQUEST_UNAUTHORIZED){
		
		}
		else{
			completedRequests.add(processingRequest);
			
			if(processingRequest.getREQUEST_CODE() == currentRequest.getREQUEST_CODE())
				setCurrentRequestCompleted(true);
		}
	}
	
	public void checkLoginWindowConnection(){
	}
	
	public void start() throws IOException{	
		logger.info("Starting data loader");
		this.thread.start();
		connectionThread.start();
	}
	
	public void stop(){
		logger.info("Stopping data loader");
		thread.interrupt();
		connectionThread.stop();
	}

	public List<Request> getPendingRequests() {
		return pendingRequests;
	}

	public Request getCurrentRequest() {
		return currentRequest;
	}

	public void setCurrentRequest(Request currentRequest) {
		this.currentRequest = currentRequest;
	}

	public Boolean getCurrentRequestCompleted() {
		return currentRequestCompleted;
	}

	public void setCurrentRequestCompleted(Boolean currentRequestCompleted) {
		this.currentRequestCompleted = currentRequestCompleted;
	}

	public Boolean getCurrentRequestSent() {
		return currentRequestSent;
	}

	public void setCurrentRequestSent(Boolean currentRequestSent) {
		this.currentRequestSent = currentRequestSent;
	}
}
