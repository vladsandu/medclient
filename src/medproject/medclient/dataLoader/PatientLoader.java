package medproject.medclient.dataLoader;

import java.util.logging.Logger;

import medproject.medclient.logging.LogWriter;
import medproject.medlibrary.concurrency.Request;
import medproject.medlibrary.concurrency.RequestCodes;

public class PatientLoader {
	private final Logger LOG = LogWriter.getLogger(this.getClass().getName());
	private final DataLoader dataLoader;
	
	public PatientLoader(DataLoader dataLoader){
		this.dataLoader = dataLoader;
	}
	
	public void processRequest(Request request){
		switch(request.getREQUEST_CODE()){
		case RequestCodes.PATIENT_LIST_REQUEST:
			processPatientListRequest(request);
			break;
		}
	}

	private void processPatientListRequest(Request request) {
		// TODO Auto-generated method stub
		
	}

}
