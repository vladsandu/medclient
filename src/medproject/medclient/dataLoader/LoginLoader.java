package medproject.medclient.dataLoader;

import medproject.medlibrary.concurrency.Request;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.concurrency.RequestStatus;

public class LoginLoader {

	private final DataLoader dataLoader;
	
	public LoginLoader(DataLoader dataLoader){
		this.dataLoader = dataLoader;
	}
	
	public void processRequest(Request request){
		switch(request.getREQUEST_CODE()){
		case RequestCodes.OPERATOR_LOOKUP_REQUEST:
			processOperatorLookupRequest(request);
		}
	}

	private void processOperatorLookupRequest(Request request) {
		if(request.getStatus() == RequestStatus.REQUEST_COMPLETED){
			//do the login
		}
		else{
			//error operator doesn't exist
		}
	}
}
