package medproject.medclient.dataLoader;

import java.util.List;
import java.util.logging.Logger;

import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.concurrency.Request;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.concurrency.RequestStatus;
import medproject.medlibrary.examination.Diagnosis;
import medproject.medlibrary.examination.Examination;
import medproject.medlibrary.logging.LogWriter;

//TODO: REFACTOR all loaders extend loader class that has common methods
public class DiagnosisLoader {
	private final Logger LOG = LogWriter.getLogger(this.getClass().getName());
	private final DataLoader dataLoader;

	public DiagnosisLoader(DataLoader dataLoader){
		this.dataLoader = dataLoader;
	}

	public void processRequest(Request request){
		switch(request.getREQUEST_CODE()){
		case RequestCodes.DIAGNOSIS_LIST_REQUEST:
			processDiagnosisListRequest(request);	break;
		default:	
			processGUITaskRequest(request);			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void processDiagnosisListRequest(Request request) {
		LOG.info(request.getMessage());

		if(request.getStatus() == RequestStatus.REQUEST_COMPLETED){
			List<Diagnosis> diagnosisList = (List<Diagnosis>) request.getDATA();

			for(Diagnosis diagnosis : diagnosisList)
				dataLoader.addDiagnosis(diagnosis);

			dataLoader.getInitialLoader().setDiagnosisLoaded(true);
		}
		else{
			GUIUtils.showErrorDialog("Diagnosis List Error", request.getMessage());
			//fatal	
		}
	}

	private void processGUITaskRequest(Request request) {
		LOG.info(request.getMessage());

		if(request.getStatus() != RequestStatus.REQUEST_COMPLETED){
			
			GUIUtils.showErrorDialog("Error", request.getMessage());
		}

		dataLoader.processGuiTask(request);
	}

	public void loadDiagnosisList() {
		dataLoader.makeRequest(new Request(RequestCodes.DIAGNOSIS_LIST_REQUEST, null));
	}

}
