
package medproject.medclient.dataLoader;

import java.util.List;
import java.util.logging.Logger;

import medproject.medclient.concurrency.AddExaminationTask;
import medproject.medclient.concurrency.DeleteExaminationTask;
import medproject.medclient.graphicalInterface.examinationWindow.addExaminationScene.AddExaminationController;
import medproject.medclient.graphicalInterface.mainWindow.examinationTabScene.ExaminationTabController;
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
		LOG.info(request.getMessage());
		
		switch(request.getREQUEST_CODE()){
		case RequestCodes.EXAMINATION_LIST_REQUEST:
			processExaminationListRequest(request);	break;
		default:	
			dataLoader.processGuiTask(request);			break;
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

	public void loadExaminationList() {
		dataLoader.makeRequest(new Request(RequestCodes.EXAMINATION_LIST_REQUEST, null));
	}
	
	public void makeAddExaminationRequest(AddExaminationController controller, Examination examination, int pin){
		dataLoader.makeRequest(new Request(RequestCodes.ADD_EXAMINATION_REQUEST, examination, pin));
		dataLoader.addGuiTask(new AddExaminationTask(dataLoader, controller));	
	}
	
	public void makeDeleteExaminationRequest(ExaminationTabController controller, Examination examination, int pin){
		dataLoader.makeRequest(new Request(RequestCodes.DELETE_EXAMINATION_REQUEST, examination.getExaminationID(), pin));
		dataLoader.addGuiTask(new DeleteExaminationTask(dataLoader, controller, examination));	
	}
}
