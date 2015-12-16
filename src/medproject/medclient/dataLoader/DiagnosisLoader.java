package medproject.medclient.dataLoader;

import java.util.List;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import medproject.medclient.concurrency.DiagnosisTabTask;
import medproject.medclient.graphicalInterface.examinationWindow.diagnosisTabScene.DiagnosisTabController;
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
			dataLoader.processGuiTask(request);			break;
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

	public void loadDiagnosisList() {
		dataLoader.makeRequest(new Request(RequestCodes.DIAGNOSIS_LIST_REQUEST, null));
	}
	
	public void loadDiagnosisInfoList() {
		dataLoader.makeRequest(new Request(RequestCodes.DIAGNOSIS_INFO_LIST_REQUEST, null));
	}
	
	public void makeAddDiagnosisRequest(DiagnosisTabController controller, Diagnosis diagnosis, int pin){
		dataLoader.makeRequest(new Request(RequestCodes.ADD_DIAGNOSIS_REQUEST, diagnosis, pin));
		dataLoader.addGuiTask(new DiagnosisTabTask(
				dataLoader, RequestCodes.ADD_DIAGNOSIS_REQUEST, diagnosis, "Se adauga diagnosticul...", controller));	
	}

	public void makeDeleteDiagnosisRequest(DiagnosisTabController controller, Diagnosis diagnosis, int pin){
		dataLoader.makeRequest(new Request(RequestCodes.DELETE_DIAGNOSIS_REQUEST, diagnosis.getID(), pin));		
		dataLoader.addGuiTask(new DiagnosisTabTask(
				dataLoader, RequestCodes.DELETE_DIAGNOSIS_REQUEST, diagnosis, "Se sterge diagnosticul...", controller));	
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
