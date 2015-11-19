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

public class DataLoader implements Runnable{

	private final Logger LOG = LogWriter.getLogger("DataLoader");

	private final Thread thread;  
	private final AtomicBoolean connectionStatus;
	//TODO: Move connectionStatus to the connectionThread and callback methods etc.

	private final List<Request> pendingRequests;
	private final LinkedBlockingQueue<Request> completedRequests;

	private final NetConnectionThread connectionThread;
	private final MainWindow mainWindow;

	public DataLoader(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		connectionThread = new NetConnectionThread(this);
		connectionThread.setAddress(new InetSocketAddress("localhost", 1338));

		pendingRequests = Collections.synchronizedList(new ArrayList<Request>());
		completedRequests = new LinkedBlockingQueue<Request>();
		
		connectionStatus = new AtomicBoolean(false);
		thread = new Thread(this);
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


	}

	public void processReceivedRequest(Request processingRequest){
			completedRequests.add(processingRequest);
	}

	public void checkLoginWindowConnection(){
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

	public boolean getConnectionStatus(){
		return connectionStatus.get();
	}

	public void setConnectionStatus(Boolean value){
		connectionStatus.set(value);
	}	

	public List<Request> getPendingRequests() {
		return pendingRequests;
	}
}