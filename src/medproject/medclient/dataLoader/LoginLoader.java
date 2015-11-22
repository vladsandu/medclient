package medproject.medclient.dataLoader;

import java.util.logging.Logger;

import medproject.medclient.graphicalInterface.mainWindow.Navigator;
import medproject.medclient.logging.LogWriter;
import medproject.medlibrary.account.LoginStructure;
import medproject.medlibrary.account.OperatorType;
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

	public void makeLoginRequest(String operatorName, String password){
		//FIXME: USe PBKDF2 to encrypt the password at entry and store the password in its encrypted state.
		//FIXME: Overwrite password array with useless things in order to delete it from RAM. (An immutable string gets garbage collected)
		//FIXME: Do the operator type in the settings
		dataLoader.makeRequest(new Request(RequestCodes.LOGIN_REQUEST, 
				new LoginStructure(operatorName, password, OperatorType.MEDIC.getOperatorID())).setWaitForReply(true));
	}

	private void processLoginRequest(Request request) {
		LOG.info(request.getMessage());
		if(request.getStatus() == RequestStatus.REQUEST_COMPLETED){
			dataLoader.makeInitialLoadingRequest();
		}
		else{
			//error operator doesn't exist
		}
	}
}
