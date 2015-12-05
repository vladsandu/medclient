package medproject.medclient.dataLoader;

import medproject.medclient.graphicalInterface.Navigator;

public class InitialLoader {

	private final DataLoader dataLoader;

	private final String[] loadingMessages = {
		"Se incarca lista de medicamente",
		"Se incarca lista de diagnostice",
		"Se incarca pacientii",
		"Se incarca consultatiile",
		"Se incarca diagnosticele",
		"Se incarca retetele",
		"Se incarca medicatia"
	};
	
	private int currentRequestNumber;
	
	public volatile boolean patientsLoaded = false;
	public volatile boolean examinationsLoaded = false;
	public volatile boolean diagnosisLoaded = false;
	public volatile boolean diagnosisInfoLoaded = false;
	public volatile boolean prescriptionsLoaded = false;
	public volatile boolean drugListLoaded = false;
	public volatile boolean medicationLoaded = false;
	
	public InitialLoader(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
		currentRequestNumber = -1;
	}


	public void loadData(){
		Navigator.loadSceneToMainWindow(Navigator.LOADING_SCENE);
		
		dataLoader.getMedicationLoader().loadDrugList();
		dataLoader.getDiagnosisLoader().loadDiagnosisInfoList();
		dataLoader.getPatientLoader().loadPatientList();
		dataLoader.getExaminationLoader().loadExaminationList();
		dataLoader.getDiagnosisLoader().loadDiagnosisList();
		dataLoader.getPrescriptionLoader().loadPrescriptionList();
		dataLoader.getMedicationLoader().loadMedicationList();
		
		increaseProgress();
	}

	private boolean isLoadingComplete(){
		if(patientsLoaded && examinationsLoaded && diagnosisLoaded 
				&& prescriptionsLoaded && diagnosisInfoLoaded && drugListLoaded && medicationLoaded)
			return true;
		return false;
	}
	
	public void setPatientsLoaded(boolean patientsLoaded) {
		this.patientsLoaded = patientsLoaded;
		increaseProgress();	
	}
	
	public void setExaminationsLoaded(boolean examinationsLoaded) {
		this.examinationsLoaded = examinationsLoaded;
		increaseProgress();
	}

	public void setDiagnosisLoaded(boolean diagnosisLoaded) {
		this.diagnosisLoaded = diagnosisLoaded;
		increaseProgress();
	}

	public void setPrescriptionsLoaded(boolean prescriptionsLoaded) {
		this.prescriptionsLoaded = prescriptionsLoaded;
		increaseProgress();
	}

	public void setDiagnosisInfoLoaded(boolean diagnosisInfoLoaded) {
		this.diagnosisInfoLoaded = diagnosisInfoLoaded;
		increaseProgress();
	}

	public void setDrugListLoaded(boolean drugListLoaded) {
		this.drugListLoaded = drugListLoaded;
		increaseProgress();
	}

	public void setMedicationLoaded(boolean medicationLoaded) {
		this.medicationLoaded = medicationLoaded;
		increaseProgress();
	}
	
	private void increaseProgress(){
		currentRequestNumber++;
		dataLoader.getMainWindow().setLoadingProgress((double)currentRequestNumber / (double)loadingMessages.length);
		
		if(currentRequestNumber < loadingMessages.length)
			setLoadingMessage(loadingMessages[currentRequestNumber]);
		
		if(isLoadingComplete())
			Navigator.loadSceneToMainWindow(Navigator.MAIN_SCENE);
		
	}

	private void setLoadingMessage(String value){
		dataLoader.getMainWindow().setLoadingMessage(value);
	}

}
 