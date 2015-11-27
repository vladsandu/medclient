package medproject.medclient.dataLoader;

import medproject.medclient.graphicalInterface.Navigator;

public class InitialLoader {

	private final DataLoader dataLoader;

	private final String[] loadingMessages = {
		"Se incarca pacientii"	
	};
	
	private int currentRequestNumber;
	
	public volatile boolean patientsLoaded = false;
	
	public InitialLoader(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
		currentRequestNumber = -1;
	}


	public void loadData(){
		Navigator.loadScene(Navigator.LOADING_SCENE);
		
		dataLoader.getPatientLoader().loadPatientList();
		increaseProgress();
	}

	private boolean isLoadingComplete(){
		if(patientsLoaded)
			return true;
		return false;
	}

	public void setPatientsLoaded(boolean patientsLoaded) {
		this.patientsLoaded = patientsLoaded;
		increaseProgress();
		
	}
	
	private void increaseProgress(){
		currentRequestNumber++;
		dataLoader.getMainWindow().setLoadingProgress((double)currentRequestNumber / (double)loadingMessages.length);
		
		if(currentRequestNumber < loadingMessages.length)
			setLoadingMessage(loadingMessages[currentRequestNumber]);
		
		if(isLoadingComplete())
			Navigator.loadScene(Navigator.MAIN_SCENE);
		
	}

	private void setLoadingMessage(String value){
		dataLoader.getMainWindow().setLoadingMessage(value);
	}
}
