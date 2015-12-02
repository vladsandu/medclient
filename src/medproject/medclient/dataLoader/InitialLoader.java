package medproject.medclient.dataLoader;

import medproject.medclient.graphicalInterface.Navigator;

public class InitialLoader {

	private final DataLoader dataLoader;

	private final String[] loadingMessages = {
		"Se incarca pacientii",
		"Se incarca consultatiile"
	};
	
	private int currentRequestNumber;
	
	public volatile boolean patientsLoaded = false;
	public volatile boolean examinationsLoaded = false;
	
	public InitialLoader(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
		currentRequestNumber = -1;
	}


	public void loadData(){
		Navigator.loadSceneToMainWindow(Navigator.LOADING_SCENE);
		
		dataLoader.getPatientLoader().loadPatientList();
		dataLoader.getExaminationLoader().loadExaminationList();
		
		increaseProgress();
	}

	private boolean isLoadingComplete(){
		if(patientsLoaded && examinationsLoaded)
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
