package medproject.medclient.dataLoader;

import java.util.List;
import java.util.logging.Logger;

import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.concurrency.Request;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.concurrency.RequestStatus;
import medproject.medlibrary.examination.Examination;
import medproject.medlibrary.logging.LogWriter;
import medproject.medlibrary.patient.Patient;

public class ExaminationLoader {
	private final Logger LOG = LogWriter.getLogger(this.getClass().getName());
	private final DataLoader dataLoader;

	public ExaminationLoader(DataLoader dataLoader){
		this.dataLoader = dataLoader;
	}

	public void processRequest(Request request){
		switch(request.getREQUEST_CODE()){
		case RequestCodes.EXAMINATION_LIST_REQUEST:
			processExaminationListRequest(request);	break;
		case RequestCodes.ADD_EXAMINATION_REQUEST:
			processGUITaskRequest(request);			break;
		default:	
			processGUITaskRequest(request);			break;
		}
	}
	
	public Examination getExaminationByID(int id){
		for(Patient patient : dataLoader.getPatientList())
			for(Examination examination : patient.getExaminationList())
				if(examination.getExaminationID() == id)
					return examination;
		
		return null;
	}

	@SuppressWarnings("unchecked")
	private void processExaminationListRequest(Request request) {
		LOG.info(request.getMessage());

		if(request.getStatus() == RequestStatus.REQUEST_COMPLETED){
			List<Examination> examinationList = (List<Examination>) request.getDATA();

			for(Examination examination : examinationList)
				dataLoader.addExamination(examination);

			dataLoader.getInitialLoader().setExaminationsLoaded(true);
		}
		else{
			GUIUtils.showErrorDialog("Examination List Error", request.getMessage());
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

	public void loadExaminationList() {
		dataLoader.makeRequest(new Request(RequestCodes.EXAMINATION_LIST_REQUEST, null));
	}

	public void makeAddExaminationRequest(Examination examination, int pin) {
		dataLoader.makeRequest(new Request(RequestCodes.ADD_EXAMINATION_REQUEST, examination, pin));
	}
}
