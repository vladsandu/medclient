package medproject.medclient.dataLoader;

import java.util.logging.Logger;

import medproject.medclient.graphicalInterface.mainWindow.Navigator;
import medproject.medclient.logging.LogWriter;
import medproject.medlibrary.concurrency.Request;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.concurrency.RequestStatus;

public class LoginLoader {
	private final Logger LOG = LogWriter.getLogger(this.getClass().getName());
	private final DataLoader dataLoader;
	
	public LoginLoader(DataLoader dataLoader){
		this.dataLoader = dataLoader;
	}
	
	public void processRequest(Request request){
		switch(request.getREQUEST_CODE()){
		case RequestCodes.LOGIN_REQUEST:
			processLoginRequest(request);
			break;
		}
	}

	private void processLoginRequest(Request request) {
		LOG.info(request.getMessage());
		if(request.getStatus() == RequestStatus.REQUEST_COMPLETED){
			dataLoader.makeInitialLoadingRequest();
			dataLoader.makeWindowRequest(new Runnable(){

				@Override
				public void run() {
					Navigator.loadScene(Navigator.LOADING_SCENE);
				}
				
			});
		}
		else{
			//error operator doesn't exist
		}
	}
}