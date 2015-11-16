package medproject.medclient.requestHandler;

import java.util.LinkedList;
import java.util.List;

import medproject.medclient.netHandler.NetConnectionThread;

public class RequestHandler{
	
	Request currentRequest;
	List<Request> pendingRequests;
	NetConnectionThread netConnectionThread;
	
	LinkedList<Request> completedRequests;
	
	public RequestHandler(NetConnectionThread netConnectionThread) {
		this.netConnectionThread = netConnectionThread;
		this.pendingRequests = netConnectionThread.getPendingRequests();
		this.completedRequests = new LinkedList<Request>();
	}

	public void processRequest(Request processingRequest){
		synchronized(this.pendingRequests){
			currentRequest = this.netConnectionThread.getCurrentRequest();
		}
		if(processingRequest.getREQUEST_STATUS() == RequestCodes.REQUEST_UNAUTHORIZED){
			//loginSHIT
			pendingRequests.add(processingRequest);
		}
		else if(processingRequest.getREQUEST_CODE() == currentRequest.getREQUEST_CODE()){
			
			synchronized(completedRequests){
				completedRequests.add(processingRequest);
			}
			this.netConnectionThread.setCurrentRequestCompleted(true);
		}
		else{
			synchronized(completedRequests){
				completedRequests.add(processingRequest);
			}
		}
	}

	public LinkedList<Request> getCompletedRequests() {
		return completedRequests;
	}

}
