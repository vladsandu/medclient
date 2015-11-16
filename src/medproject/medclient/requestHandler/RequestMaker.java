package medproject.medclient.requestHandler;

import java.util.List;

public class RequestMaker {

	List<Request> pendingRequests;
	
	public RequestMaker(List<Request> pendingRequests) {
		this.pendingRequests = pendingRequests;
	}
	
	public void makeRequest(Request currentRequest){
		synchronized(this.pendingRequests){
			System.out.println("s-a facut requestul");
			this.pendingRequests.add(currentRequest);
		}
	}

}
