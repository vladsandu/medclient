package medproject.medclient.dataLoader;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import medproject.medclient.requestHandler.Request;
import medproject.medclient.requestHandler.RequestCodes;
import medproject.medclient.requestHandler.RequestHandler;
import medproject.medclient.requestHandler.RequestMaker;
import medproject.medlibrary.account.Account;

public class DataLoader implements Runnable{

	private Account currentAccount = new Account(-1,"unset", "unset");
	
	private final Thread thread;  
	private final RequestMaker requestMaker;
	
	private final AtomicBoolean connectionStatus;
	
	private LinkedList<Request> loadingList = new LinkedList<Request>();
	private LinkedList<Request> completedRequests;
	
	
	public DataLoader(LoginWindow loginWindow, MainWindow mainWindow, RequestMaker requestMaker, RequestHandler requestHandler, AtomicBoolean connectionStatus) {
		this.loginWindow = loginWindow;
		this.mainWindow = mainWindow;
		this.connectionStatus = connectionStatus;
		
		this.requestMaker = requestMaker;
		
		this.completedRequests = requestHandler.getCompletedRequests();
		
		this.thread = new Thread(this);
	}
	
	public boolean getConnectionStatus(){
		return connectionStatus.get();
	}
	
	
	@Override
	public void run(){
		try{
			
		while(!Thread.interrupted()){  
			checkLoginWindowConnection();
			
			synchronized(this.loadingList){
				if(!loadingList.isEmpty()){	
					for(Request request : this.loadingList)
						this.requestMaker.makeRequest(request);
					
					this.loadingList.clear();
				}
			}
			
			synchronized(this.completedRequests){
				if(!this.completedRequests.isEmpty()){
					for(Request request : this.completedRequests)
						processCompletedRequest(request);
					
					this.completedRequests.clear();
				}
			}
			
			Thread.sleep(20);
		}
		}catch (Exception e){
			Logger.getAnonymousLogger().severe("Event loop terminated!");
		}
	}

	/**
	 * Processes the request and sends it to the appropriate loader.
	 * @param request the Request to be processed.
	 */
	
	private void processCompletedRequest(Request request) {
		
		switch(RequestCodes.requestTypeGetter(request)){
			}	
	}

	public void checkLoginWindowConnection(){
		loginWindow.runAndWait(new Runnable(){
			
			@Override
			public void run() {
				loginWindow.modifyConnectionStatus(getConnectionStatus());
			}
			}
		);
	}
	
	public void start(){	
		this.thread.start();
	}
	
	public void stop(){
		this.thread.interrupt();
	}

	public LinkedList<Request> getLoadingList() {
		return loadingList;
	}

	public Account getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(Account currentAccount) {
		this.currentAccount = currentAccount;
	}
}
