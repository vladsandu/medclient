package medproject.medclient.dataLoader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import medproject.medclient.graphicalInterface.mainWindow.MainWindow;
import medproject.medclient.netHandler.NetConnectionThread;
import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.concurrency.CustomTask;
import medproject.medlibrary.concurrency.Request;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.concurrency.RequestStatus;
import medproject.medlibrary.diagnosis.Diagnosis;
import medproject.medlibrary.examination.Examination;
import medproject.medlibrary.logging.LogWriter;
import medproject.medlibrary.medication.Medication;
import medproject.medlibrary.patient.Address;
import medproject.medlibrary.patient.Patient;
import medproject.medlibrary.prescription.Prescription;

public class DataLoader implements Runnable{

	private final Logger LOG = LogWriter.getLogger("DataLoader");

	private final Thread thread;  
	private final AtomicBoolean connectionStatus;
	//TODO: Move connectionStatus to the connectionThread and callback methods etc.

	private final List<Request> pendingRequests;
	private final LinkedBlockingQueue<Request> completedRequests;
	private final List<CustomTask> guiTasks;

	private final NetConnectionThread connectionThread;
	private final MainWindow mainWindow;

	private final InitialLoader initialLoader;
	private final LoginLoader loginLoader;
	private final PatientLoader patientLoader;
	private final ExaminationLoader examinationLoader;
	private final DiagnosisLoader diagnosisLoader;
	private final PrescriptionLoader prescriptionLoader;
	private final MedicationLoader medicationLoader;

	private final ObservableList<Patient> patientList;
	private final ObservableList<Examination> examinationList;

	private final ExecutorService executor;

	public DataLoader(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		executor = Executors.newSingleThreadExecutor();

		connectionThread = new NetConnectionThread(this);
		connectionThread.setAddress(new InetSocketAddress("localhost", 1338));

		guiTasks = Collections.synchronizedList(new ArrayList<CustomTask>());
		pendingRequests = Collections.synchronizedList(new ArrayList<Request>());
		completedRequests = new LinkedBlockingQueue<Request>();

		initialLoader = new InitialLoader(this);
		loginLoader = new LoginLoader(this);
		patientLoader = new PatientLoader(this);
		examinationLoader = new ExaminationLoader(this);
		diagnosisLoader = new DiagnosisLoader(this);
		prescriptionLoader = new PrescriptionLoader(this);
		medicationLoader = new MedicationLoader(this);

		patientList = FXCollections.observableArrayList();
		examinationList = FXCollections.observableArrayList();

		connectionStatus = new AtomicBoolean(false);
		thread = new Thread(this);

	}

	public void start() throws IOException{	
		LOG.info("Starting data loader");
		thread.start();
		connectionThread.start();
	}

	public void stop(){
		LOG.info("Stopping data loader");
		thread.interrupt();
		connectionThread.stop();
	}

	@Override
	public void run(){

		while(!Thread.interrupted()){  
			Request request = null;
			try {
				request = completedRequests.take();
				processCompletedRequest(request);
			} catch (InterruptedException e) {
				LOG.severe("Data Loader thread interrupted");
			}
		}
	}

	/**
	 * Processes the request and sends it to the appropriate loader.
	 * @param request the Request to be processed.
	 */

	private void processCompletedRequest(Request request) {
		switch(RequestCodes.getRequestType(request)){
		case RequestCodes.LOGIN_TYPE_REQUEST:
			loginLoader.processRequest(request);
			break;
		case RequestCodes.PATIENT_TYPE_REQUEST:
			patientLoader.processRequest(request);
			break;
		case RequestCodes.EXAMINATION_TYPE_REQUEST:
			examinationLoader.processRequest(request);
			break;
		case RequestCodes.DIAGNOSIS_TYPE_REQUEST:
			diagnosisLoader.processRequest(request);
			break;
		case RequestCodes.PRESCRIPTION_TYPE_REQUEST:
			prescriptionLoader.processRequest(request);
			break;
		case RequestCodes.MEDICATION_TYPE_REQUEST:
			medicationLoader.processRequest(request);
			break;
		}
	}

	public void processReceivedRequest(Request processingRequest){

		if(processingRequest.getREQUEST_CODE() == connectionThread.getCurrentRequest().getREQUEST_CODE()){
			connectionThread.setCurrentRequestCompleted(true);
		}

		completedRequests.add(processingRequest);
	}

	public void makeRequest(Request request){
		pendingRequests.add(request);
	}


	public void makeWindowRequest(Runnable runnable){
		mainWindow.runAndWait(runnable);
	}

	public void addPatient(final Patient patient, boolean isLoading){
		if(isLoading){
			patientList.add(patient);
			return;
		}

		makeWindowRequest(new Runnable(){

			@Override
			public void run() {
				patientList.add(patient);	
			}

		});
	}
	//Refactor
	public void addExamination(Examination examination) {
		for(Patient patient : patientList){
			if(patient.getPatientID() == examination.getPatientID()){
				patient.addExamination(examination);
				examination.setPatient(patient);
				examinationList.add(examination);
				return;
			}
		}

		LOG.severe("Couldn't find patient matching the examination");
		GUIUtils.showErrorDialog("Warning", "The data might be corrupted! You should restart the application.");

	}

	public void addDiagnosis(Diagnosis diagnosis) {
		Examination examination = examinationLoader.getExaminationByID(diagnosis.getExaminationID());

		if(examination == null){
			LOG.severe("Couldn't find examination matching the diagnosis");
			GUIUtils.showErrorDialog("Warning", "The data might be corrupted! You should restart the application.");	
		}
		else{
			examination.addDiagnosis(diagnosis);
			diagnosis.setDiagnosisInfo(diagnosisLoader.getDiagnosisInfoForID(diagnosis.getDiagnosisID()));
		}
	}

	public void addPrescription(Prescription prescription) throws IOException {
		Examination examination = examinationLoader.getExaminationByID(prescription.getExaminationID());

		if(examination == null){
			throw new IOException("Couldn't find examination matching the prescription");
		}
		else{
			examination.addPrescription(prescription);
			prescription.setExamination(examination);
		}
	}

	public void addMedication(Medication medication) {
		Prescription prescription = prescriptionLoader.getPrescriptionByID(medication.getPrescriptionID());

		if(prescription == null){
			LOG.severe("Couldn't find prescription matching the medication");
			GUIUtils.showErrorDialog("Warning", "The data might be corrupted! You should restart the application.");	
		}
		else{
			prescription.addMedication(medication);
			medication.setDrug(medicationLoader.getDrugForID(medication.getDrugID()));
		}		
	}


	public void deletePatient(Patient patient) {
		//TODO: delete the consultations as well
		patientList.remove(patient);
	}

	public void deleteDiagnosis(Diagnosis diagnosis) {
		Examination examination = examinationLoader.getExaminationByID(diagnosis.getExaminationID());

		if(examination == null){
			GUIUtils.showErrorDialog("Error", "The examination doesn't exist");
		}

		for(Iterator<Diagnosis> it = examination.getDiagnosisList().iterator(); it.hasNext();){
			Diagnosis currentDiagnosis = it.next();

			if(currentDiagnosis.getID() == diagnosis.getID()){
				it.remove();
				return;
			}
		}

		GUIUtils.showErrorDialog("Error", "The diagnosis doesn't exist.");
	}

	public void deleteMedication(Medication medication) {
		Prescription prescription = prescriptionLoader.getPrescriptionByID(medication.getPrescriptionID());

		if(prescription == null){
			GUIUtils.showErrorDialog("Error", "The prescription doesn't exist");
		}

		for(Iterator<Medication> it = prescription.getMedicationList().iterator(); it.hasNext();){
			Medication currentMedication = it.next();

			if(currentMedication.getID() == medication.getID()){
				it.remove();
				return;
			}
		}

		GUIUtils.showErrorDialog("Error", "The medication doesn't exist.");
	}

	public void deleteExamination(Examination examination) {
		Patient patient = patientLoader.getPatientByID(examination.getPatientID());

		if(patient == null){
			GUIUtils.showErrorDialog("Error", "Couldn't find the patient for this examination");
			return;
		}
		
		for(Iterator<Examination> it = patient.getExaminationList().iterator(); it.hasNext();){
			Examination tempExamination = it.next();
			
			if(tempExamination.getExaminationID() == examination.getExaminationID()){
				it.remove();
				examinationList.remove(examination);
				return;
			}
		}
		
		GUIUtils.showErrorDialog("Error", "Couldn't find the examination");
	}

	public void updatePatientAddress(final Address address){
		for(final Patient patient : patientList){
			if(patient.getPatientRecord().getPERSON_ID() == address.getPersonID()){
				makeWindowRequest(new Runnable(){

					@Override
					public void run() {
						patient.getPatientRecord().setAddress(address);
						int position = patientList.indexOf(patient);
						patientList.set(position, null);
						patientList.set(position, patient);
					}

				});
				break;
			}
		}
	}

	public void unregisterPatient(Patient patient, Date unregistrationDate) {
		patient.getRegistrationRecord().setUnregistrationDate(unregistrationDate);
		patient.getRegistrationRecord().setRegistered(false);

		updatePatientList(patient);
	}

	public void registerPatient(Patient patient, Date registrationDate) {
		patient.getRegistrationRecord().setRegistrationDate(registrationDate);
		patient.getRegistrationRecord().setUnregistrationDate(null);
		patient.getRegistrationRecord().setRegistered(true);

		updatePatientList(patient);
	}

	public void makePatientDeceased(Patient patient, Date deceaseDate) {
		patient.getRegistrationRecord().setUnregistrationDate(deceaseDate);
		patient.getRegistrationRecord().setRegistered(false);
		patient.getPatientRecord().setDeceaseDate(deceaseDate);

		updatePatientList(patient);
	}

	private void updatePatientList(Patient patient){
		int position = patientList.indexOf(patient);
		if(position != -1){
			//patientList.set(position, null);
			patientList.set(position, patient);
		}
	}


	public ObservableList<Patient> getPatientList(){
		return patientList;
	}

	void addGuiTask(CustomTask task){
		guiTasks.add(task);
		executor.execute(task);
	}
	
	public void processGuiTask(Request request){
		if(request.getStatus() != RequestStatus.REQUEST_COMPLETED){

			GUIUtils.showErrorDialog("Error", request.getMessage());
		}

		synchronized(guiTasks){
			for(Iterator<CustomTask> it = guiTasks.iterator(); it.hasNext();){
				CustomTask task = it.next();
				if(task.getRequestCode() == request.getREQUEST_CODE()){
					task.setData(request.getDATA());
					task.setRequestStatus(request.getStatus());
					task.getLatch().countDown();
					it.remove();
					break;
				}
			}
		}
	}

	public boolean getConnectionStatus(){
		return connectionStatus.get();
	}

	public void setConnectionStatus(Boolean value){
		connectionStatus.set(value);
	}	

	public List<Request> getPendingRequests() {
		return pendingRequests;
	}

	public InitialLoader getInitialLoader() {
		return initialLoader;
	}

	public PatientLoader getPatientLoader() {
		return patientLoader;
	}

	public MainWindow getMainWindow() {
		return mainWindow;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public List<CustomTask> getGuiTasks() {
		return guiTasks;
	}

	public ExaminationLoader getExaminationLoader() {
		return examinationLoader;
	}

	public DiagnosisLoader getDiagnosisLoader() {
		return diagnosisLoader;
	}

	public PrescriptionLoader getPrescriptionLoader() {
		return prescriptionLoader;
	}

	public MedicationLoader getMedicationLoader() {
		return medicationLoader;
	}

	public ObservableList<Examination> getExaminationList() {
		return examinationList;
	}

	public LoginLoader getLoginLoader() {
		return loginLoader;
	}
}