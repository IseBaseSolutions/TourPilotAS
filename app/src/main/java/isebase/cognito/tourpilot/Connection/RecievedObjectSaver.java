package isebase.cognito.tourpilot.Connection;


import isebase.cognito.tourpilot.Data.AdditionalTask.AdditionalTask;
import isebase.cognito.tourpilot.Data.AdditionalWork.AdditionalWork;
import isebase.cognito.tourpilot.Data.Category.Category;
import isebase.cognito.tourpilot.Data.CustomRemark.CustomRemark;
import isebase.cognito.tourpilot.Data.Diagnose.Diagnose;
import isebase.cognito.tourpilot.Data.Doctor.Doctor;
import isebase.cognito.tourpilot.Data.Information.Information;
import isebase.cognito.tourpilot.Data.Link.Link;
import isebase.cognito.tourpilot.Data.Patient.Patient;
import isebase.cognito.tourpilot.Data.PatientAdditionalAddress.PatientAdditionalAddress;
import isebase.cognito.tourpilot.Data.PatientRemark.PatientRemark;
import isebase.cognito.tourpilot.Data.Question.Question;
import isebase.cognito.tourpilot.Data.QuestionSetting.QuestionSetting;
import isebase.cognito.tourpilot.Data.RelatedQuestionSetting.RelatedQuestionSetting;
import isebase.cognito.tourpilot.Data.Relative.Relative;
import isebase.cognito.tourpilot.Data.Task.Task;
import isebase.cognito.tourpilot.Data.Tour.Tour;
import isebase.cognito.tourpilot.Data.TourOncomingInfo.TourOncomingInfo;
import isebase.cognito.tourpilot.Data.UserRemark.UserRemark;
import isebase.cognito.tourpilot.Data.Work.Work;
import isebase.cognito.tourpilot.Data.Worker.Worker;
import isebase.cognito.tourpilot.DataBase.HelperFactory;

import java.util.ArrayList;
import java.util.List;

public class RecievedObjectSaver {
	
	public List<Worker> workersToSave = new ArrayList<Worker>();
	public List<UserRemark> userRemarksToSave = new ArrayList<UserRemark>();
	public List<PatientRemark> patientRemarksToSave = new ArrayList<PatientRemark>();
	public List<Diagnose> diagnosesToSave = new ArrayList<Diagnose>();	
	public List<Information> infromationToSave = new ArrayList<Information>();
	public List<AdditionalTask> additionalTasksToSave = new ArrayList<AdditionalTask>();
	public List<Doctor> doctorsToSave = new ArrayList<Doctor>();
	public List<Patient> patientsToSave = new ArrayList<Patient>();
	public List<Task> tasksToSave = new ArrayList<Task>();
	public List<AdditionalWork> additionalWorksToSave = new ArrayList<AdditionalWork>();
	public List<Work> worksToSave = new ArrayList<Work>();
	public List<Tour> toursToSave = new ArrayList<Tour>();
	public List<Relative> relativesToSave = new ArrayList<Relative>();
	public List<Question> questionsToSave = new ArrayList<Question>();
	public List<Category> categoriesToSave = new ArrayList<Category>();
	public List<Link> linksToSave = new ArrayList<Link>();
	public List<QuestionSetting> questionSettingsToSave = new ArrayList<QuestionSetting>();	
	public List<CustomRemark> customRemarksToSave = new ArrayList<CustomRemark>();
	public List<RelatedQuestionSetting> relatedQuestionSettingsToSave = new ArrayList<RelatedQuestionSetting>();	
	public List<TourOncomingInfo> tourOncomingInfoToSave = new ArrayList<TourOncomingInfo>();
	public List<PatientAdditionalAddress> patientsAdditionalAddressToSave = new ArrayList<PatientAdditionalAddress>();
	
	public List<Integer> workersToDelete = new ArrayList<Integer>();
	public List<Integer> userRemarksToDelete = new ArrayList<Integer>();
	public List<Integer> patientRemarksToDelete = new ArrayList<Integer>();
	public List<Integer> diagnosesToDelete = new ArrayList<Integer>();	
	public List<Integer> infromationToDelete = new ArrayList<Integer>();
	public List<Integer> additionalTasksToDelete = new ArrayList<Integer>();
	public List<Integer> doctorsToDelete = new ArrayList<Integer>();
	public List<Integer> patientsToDelete = new ArrayList<Integer>();
	public List<Integer> tasksToDelete = new ArrayList<Integer>();
	public List<Integer> additionalWorksToDelete = new ArrayList<Integer>();
	public List<Integer> worksToDelete = new ArrayList<Integer>();
	public List<Integer> toursToDelete = new ArrayList<Integer>();
	public List<Integer> relativesToDelete = new ArrayList<Integer>();
	public List<Integer> questionsToDelete = new ArrayList<Integer>();
	public List<Integer> categoriesToDelete = new ArrayList<Integer>();
	public List<String> linksToDelete = new ArrayList<String>();
	public List<Integer> questionSettingsToDelete = new ArrayList<Integer>();	
	public List<Integer> customRemarksToDelete = new ArrayList<Integer>();
	public List<Integer> relatedQuestionSettingsToDelete = new ArrayList<Integer>();
	public List<Integer> tourOncomingInfoToDelete = new ArrayList<Integer>();
	public List<Integer> patientsAdditionalAddressToDelete = new ArrayList<Integer>();

//	public static final char FREE_TOPIC = '<';
//	public static final char FREE_QUESTION = '>';
//	public static final char FREE_QUESTION_SETTING = '*';
//	public static final char AUTO_QUESTION_SETTING = '^';
	
	private static RecievedObjectSaver instance;
	
	public static RecievedObjectSaver Instance() {
		return instance == null ? instance = new RecievedObjectSaver() : instance;
	}
	
	public void save() {		
		if (workersToSave.size() > 0)
			HelperFactory.getHelper().getWorkerDAO().save(workersToSave);
		if (userRemarksToSave.size() > 0)
			HelperFactory.getHelper().getUserRemarkDAO().save(userRemarksToSave);
		if (patientRemarksToSave.size() > 0)
			HelperFactory.getHelper().getPatientRemarkDAO().save(patientRemarksToSave);
		if (diagnosesToSave.size() > 0)
			HelperFactory.getHelper().getDiagnoseDAO().save(diagnosesToSave);
		if (infromationToSave.size() > 0)
			HelperFactory.getHelper().getInformationDAO().save(infromationToSave);
		if (additionalTasksToSave.size() > 0)
			HelperFactory.getHelper().getAdditionalTaskDAO().save(additionalTasksToSave);
		if (doctorsToSave.size() > 0)
			HelperFactory.getHelper().getDoctorODA().save(doctorsToSave);
		if (patientsToSave.size() > 0)
			HelperFactory.getHelper().getPatientDAO().save(patientsToSave);
		if (tasksToSave.size() > 0){
			//HelperFactory.getHelper().getTaskDAO().save(tasksToSave);
			List<Task> comparedTasksToSave = HelperFactory.getHelper().getTaskDAO().CompareWithExistedTasks(tasksToSave);
			if(comparedTasksToSave.size() > 0)
				HelperFactory.getHelper().getTaskDAO().save(comparedTasksToSave);
		}
			
		if (additionalWorksToSave.size() > 0)
			HelperFactory.getHelper().getAdditionalWorkDAO().save(additionalWorksToSave);
		if (worksToSave.size() > 0)
			HelperFactory.getHelper().getWorkDAO().save(worksToSave);
		if (toursToSave.size() > 0)
			HelperFactory.getHelper().getTourDAO().save(toursToSave);
		if (relativesToSave.size() > 0)
			HelperFactory.getHelper().getRelativeDAO().save(relativesToSave);
		if (questionsToSave.size() > 0)
			HelperFactory.getHelper().getQuestionDAO().save(questionsToSave);
		if (categoriesToSave.size() > 0)
			HelperFactory.getHelper().getCategoryDAO().save(categoriesToSave);
		if (linksToSave.size() > 0)
			HelperFactory.getHelper().getLinkDAO().save(linksToSave);
		if (questionSettingsToSave.size() > 0)
			HelperFactory.getHelper().getQuestionSettingDAO().save(questionSettingsToSave);
		if (customRemarksToSave.size() > 0)
			HelperFactory.getHelper().getCustomRemarkDAO().save(customRemarksToSave);
		if (relatedQuestionSettingsToSave.size() > 0)
			HelperFactory.getHelper().getRelatedQuestionSettingDAO().save(relatedQuestionSettingsToSave);
		if (tourOncomingInfoToSave.size() > 0)
			HelperFactory.getHelper().getTourOncomingInfoDAO().save(tourOncomingInfoToSave);

		if (patientsAdditionalAddressToSave.size() > 0)
			HelperFactory.getHelper().getPatientAdditionalAddressDAO().save(patientsAdditionalAddressToSave);


		if (workersToDelete.size() > 0)
			HelperFactory.getHelper().getWorkerDAO().deleteByIds(workersToDelete);
		if (userRemarksToDelete.size() > 0)
			HelperFactory.getHelper().getUserRemarkDAO().deleteByIds(userRemarksToDelete);
		if (patientRemarksToDelete.size() > 0)
			HelperFactory.getHelper().getPatientRemarkDAO().deleteByIds(patientRemarksToDelete);
		if (diagnosesToDelete.size() > 0)
			HelperFactory.getHelper().getDiagnoseDAO().deleteByIds(diagnosesToDelete);
		if (infromationToDelete.size() > 0)
			HelperFactory.getHelper().getInformationDAO().deleteByIds(infromationToDelete);
		if (additionalTasksToDelete.size() > 0)
			HelperFactory.getHelper().getAdditionalTaskDAO().deleteByIds(additionalTasksToDelete);
		if (doctorsToDelete.size() > 0)
			HelperFactory.getHelper().getDoctorODA().deleteByIds(doctorsToDelete);
		if (patientsToDelete.size() > 0)
			HelperFactory.getHelper().getPatientDAO().deleteByIds(patientsToDelete);
		if (tasksToDelete.size() > 0)
			HelperFactory.getHelper().getTaskDAO().deleteByIds(tasksToDelete);
		if (additionalWorksToDelete.size() > 0)
			HelperFactory.getHelper().getAdditionalWorkDAO().deleteByIds(additionalWorksToDelete);
		if (worksToDelete.size() > 0)
			HelperFactory.getHelper().getWorkDAO().deleteByIds(worksToDelete);
		if (toursToDelete.size() > 0)
			HelperFactory.getHelper().getTourDAO().deleteByIds(toursToDelete);
		if (relativesToDelete.size() > 0)
			HelperFactory.getHelper().getRelativeDAO().deleteByIds(relativesToDelete);
		if (questionsToDelete.size() > 0)
			HelperFactory.getHelper().getQuestionDAO().deleteByIds(questionsToDelete);
		if (categoriesToDelete.size() > 0)
			HelperFactory.getHelper().getCategoryDAO().deleteByIds(categoriesToDelete);
		if (linksToDelete.size() > 0)
			HelperFactory.getHelper().getLinkDAO().deleteByKeys(linksToDelete);
		if (questionSettingsToDelete.size() > 0)
			HelperFactory.getHelper().getQuestionSettingDAO().deleteByIds(questionSettingsToDelete);
		if (customRemarksToDelete.size() > 0)
			HelperFactory.getHelper().getCustomRemarkDAO().deleteByIds(customRemarksToDelete);
		if (relatedQuestionSettingsToDelete.size() > 0)
			HelperFactory.getHelper().getRelatedQuestionSettingDAO().deleteByIds(relatedQuestionSettingsToDelete);
		if (tourOncomingInfoToDelete.size() > 0)
			HelperFactory.getHelper().getTourOncomingInfoDAO().deleteByIds(tourOncomingInfoToDelete);
		if (patientsAdditionalAddressToDelete.size() > 0)
			HelperFactory.getHelper().getPatientAdditionalAddressDAO().deleteByIds(patientsAdditionalAddressToDelete);
		clear();
	}
	
	public void clear() {
		if (workersToSave.size() > 0)
			workersToSave.clear();
		
		if (userRemarksToSave.size() > 0)
			userRemarksToSave.clear();
		
		if (patientRemarksToSave.size() > 0)
			patientRemarksToSave.clear();
		
		if (diagnosesToSave.size() > 0)
			diagnosesToSave.clear();
		
		if (infromationToSave.size() > 0)
			infromationToSave.clear();
		
		if (additionalTasksToSave.size() > 0)
			additionalTasksToSave.clear();
		
		if (doctorsToSave.size() > 0)
			doctorsToSave.clear();
		
		if (patientsToSave.size() > 0)
			patientsToSave.clear();
		
		if (tasksToSave.size() > 0)
			tasksToSave.clear();
		
		if (additionalWorksToSave.size() > 0)
			additionalWorksToSave.clear();
		
		if (worksToSave.size() > 0)
			worksToSave.clear();
		
		if (toursToSave.size() > 0)
			toursToSave.clear();
		
		if (relativesToSave.size() > 0)
			relativesToSave.clear();
		
		if (questionsToSave.size() > 0)
			questionsToSave.clear();
		
		if (categoriesToSave.size() > 0)
			categoriesToSave.clear();
		
		if (linksToSave.size() > 0)
			linksToSave.clear();
		
		if (questionSettingsToSave.size() > 0)
			questionSettingsToSave.clear();	
		
		if (customRemarksToSave.size() > 0)
			customRemarksToSave.clear();
		
		if (relatedQuestionSettingsToSave.size() > 0)
			relatedQuestionSettingsToSave.clear();
		
		if (tourOncomingInfoToSave.size() > 0)
			tourOncomingInfoToSave.clear();

		if (patientsAdditionalAddressToSave.size() > 0)
			patientsAdditionalAddressToSave.clear();
		
		if (workersToDelete.size() > 0)
			workersToDelete.clear();
		
		if (userRemarksToDelete.size() > 0)
			userRemarksToDelete.clear();
		
		if (patientRemarksToDelete.size() > 0)
			patientRemarksToDelete.clear();
		
		if (diagnosesToDelete.size() > 0)
			diagnosesToDelete.clear();
		
		if (infromationToDelete.size() > 0)
			infromationToDelete.clear();
		
		if (additionalTasksToDelete.size() > 0)
			additionalTasksToDelete.clear();
		
		if (doctorsToDelete.size() > 0)
			doctorsToDelete.clear();
		
		if (patientsToDelete.size() > 0)
			patientsToDelete.clear();
		
		if (tasksToDelete.size() > 0)
			tasksToDelete.clear();
		
		if (additionalWorksToDelete.size() > 0)
			additionalWorksToDelete.clear();
		
		if (worksToDelete.size() > 0)
			worksToDelete.clear();
		
		if (toursToDelete.size() > 0)
			toursToDelete.clear();
		
		if (relativesToDelete.size() > 0)
			relativesToDelete.clear();
		
		if (questionsToDelete.size() > 0)
			questionsToDelete.clear();
		
		if (categoriesToDelete.size() > 0)
			categoriesToDelete.clear();
		
		if (linksToDelete.size() > 0)
			linksToDelete.clear();
		
		if (questionSettingsToDelete.size() > 0)
			questionSettingsToDelete.clear();	
		
		if (customRemarksToDelete.size() > 0)
			customRemarksToDelete.clear();
		
		if (relatedQuestionSettingsToDelete.size() > 0)
			relatedQuestionSettingsToDelete.clear();
		
		if (tourOncomingInfoToDelete.size() > 0)
			tourOncomingInfoToDelete.clear();

		if (patientsAdditionalAddressToDelete.size() > 0)
			patientsAdditionalAddressToDelete.clear();
		
	}
	

}
