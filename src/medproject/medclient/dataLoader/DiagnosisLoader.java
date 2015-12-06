package medproject.medclient.dataLoader;

import java.util.List;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.concurrency.Request;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.concurrency.RequestStatus;
import medproject.medlibrary.diagnosis.Diagnosis;
import medproject.medlibrary.diagnosis.DiagnosisInfo;
import medproject.medlibrary.logging.LogWriter;

//TODO: REFACTOR all loaders extend loader class that has common methods
public class DiagnosisLoader {
	private final Logger LOG = LogWriter.getLogger(this.getClass().getName());
	private final DataLoader dataLoader;
	private final ObservableList<DiagnosisInfo> diagnosisInfoList;
	
	public DiagnosisLoader(DataLoader dataLoader){
		this.dataLoader = dataLoader;
		diagnosisInfoList = FXCollections.observableArrayList();
	}

	public void processRequest(Request request){
		switch(request.getREQUEST_CODE()){
		case RequestCodes.DIAGNOSIS_LIST_REQUEST:
			processDiagnosisListRequest(request);	break;
		case RequestCodes.DIAGNOSIS_INFO_LIST_REQUEST:
			processDiagnosisInfoListRequest(request);
		default:	
			processGUITaskRequest(request);			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void processDiagnosisInfoListRequest(Request request){
		LOG.info(request.getMessage());

		if(request.getStatus() == RequestStatus.REQUEST_COMPLETED){
			List<DiagnosisInfo> list = (List<DiagnosisInfo>) request.getDATA();

			for(DiagnosisInfo diagnosis : list)
				diagnosisInfoList.add(diagnosis);
			
			dataLoader.getInitialLoader().setDiagnosisInfoLoaded(true);
		}
		else{
			GUIUtils.showErrorDialog("Diagnosis Information List Error", request.getMessage());
			//fatal	
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
	
	public void loadDiagnosisInfoList() {
		dataLoader.makeRequest(new Request(RequestCodes.DIAGNOSIS_INFO_LIST_REQUEST, null));
	}
	
	public DiagnosisInfo getDiagnosisInfoForID(int diagnosisID){
		for(DiagnosisInfo info : diagnosisInfoList){
			if(info.getID() == diagnosisID)
				return info;
		}
		
		return null;
	}

	public ObservableList<DiagnosisInfo> getDiagnosisInfoList() {
		return diagnosisInfoList;
	}
}
