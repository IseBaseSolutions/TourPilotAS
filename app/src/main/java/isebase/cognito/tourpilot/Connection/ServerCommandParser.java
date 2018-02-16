package isebase.cognito.tourpilot.Connection;

import isebase.cognito.tourpilot.Data.PatientAdditionalAddress.PatientAdditionalAddress;
import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Data.AdditionalTask.AdditionalTask;
import isebase.cognito.tourpilot.Data.AdditionalWork.AdditionalWork;
import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot.Data.Category.Category;
import isebase.cognito.tourpilot.Data.CustomRemark.CustomRemark;
import isebase.cognito.tourpilot.Data.Diagnose.Diagnose;
import isebase.cognito.tourpilot.Data.Doctor.Doctor;
import isebase.cognito.tourpilot.Data.Information.Information;
import isebase.cognito.tourpilot.Data.Link.Link;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.Data.Patient.Patient;
import isebase.cognito.tourpilot.Data.PatientRemark.PatientRemark;
import isebase.cognito.tourpilot.Data.Question.Question;
import isebase.cognito.tourpilot.Data.QuestionSetting.QuestionSetting;
import isebase.cognito.tourpilot.Data.RelatedQuestionSetting.RelatedQuestionSetting;
import isebase.cognito.tourpilot.Data.Relative.Relative;
import isebase.cognito.tourpilot.Data.Task.Task;
import isebase.cognito.tourpilot.Data.Tour.Tour;
import isebase.cognito.tourpilot.Data.TourOncomingInfo.TourOncomingInfo;
import isebase.cognito.tourpilot.Data.Worker.Worker;
import isebase.cognito.tourpilot.EventHandle.SynchronizationHandler;
import isebase.cognito.tourpilot.StaticResources.StaticResources;

import java.util.Date;
import java.util.List;


//import isebase.cognito.tourpilot.Data.AdditionalTask.AdditionalTask;
//import isebase.cognito.tourpilot.Data.Question.Category.Category;
//import isebase.cognito.tourpilot.Data.Question.Link.Link;
//import isebase.cognito.tourpilot.Data.Question.Question.Question;
//import isebase.cognito.tourpilot.Data.Worker.Worker;

public class ServerCommandParser {

	public static final String SERVER_CURRENT_VERSION = "[CURVER]=";
	public static final String SERVER_SET_TIME_KEY = "[SETTIME]=";
	public static final String SERVER_VERSION_LINK = "[VERLINK]=";

	public static final char END_OF_COMMAND = '\0';
	public static final char NEED_TO_ADD = ';';
	public static final char END = '.';
	public static final char TIME = '[';

	public static final char WORKER = 'A';
	public static final char USER_REMARK = 'O';
	public static final char PATIENT_REMARK = 'B';
	public static final char DIAGNOSE = 'D';
	public static final char INFORMATION = 'I';
	public static final char ADDITIONAL_TASK_L = 'L';
	public static final char ADDITIONAL_TASK_Z = 'Z';
	public static final char DOCTOR = 'M';
	public static final char PATIENT = 'P';
	public static final char TASK = 'T';
	public static final char ADDITIONAL_WORK = 'U';
	public static final char WORK = 'W';
	public static final char TOUR = 'R';
	public static final char RELATIVE = 'V';
	public static final char QUESTION = 'Q';
	public static final char CATEGORY = 'Y';
	public static final char LINK = 'J';
	public static final char QUESTION_SETTING = 'X';
	public static final char FREE_TOPIC = '<';
	public static final char FREE_QUESTION = '>';
	public static final char FREE_QUESTION_SETTING = '*';
	public static final char AUTO_QUESTION_SETTING = '^';
	public static final char CUSTOM_REMARK = '#';
	public static final char WAY_POINT = 'C';
	public static final char RELATED_QUESTION_SETTING = '?';
	public static final char TOUR_ONCOMING_INFO = '+';
	public static final char PATIENT_ADDITIONAL_ADDRESS = '|';

	private SynchronizationHandler syncHandler;

	public ServerCommandParser(SynchronizationHandler sh) {
		syncHandler = sh;
	}

	public boolean parseElement(String commandLine, boolean isAutomaticSync) {

		char commandActionType = END_OF_COMMAND;
		char commandType = commandLine.charAt(0);

		boolean blnRes = true;
		if (commandLine.length() > 1)
			commandActionType = commandLine.charAt(1);
		if (commandLine.equals(END))
			blnRes = false;
		if (commandLine.equals(""))
			return false;

		switch (commandType) {
		case END:
			syncHandler.onProgressUpdate(StaticResources.getBaseContext().getString(R.string.done));
			break;
		case TIME:
			if (commandLine.indexOf(SERVER_CURRENT_VERSION) == 0)
				Option.Instance().setPalmVersion(commandLine.substring(SERVER_CURRENT_VERSION.length()));
			if (commandLine.indexOf(SERVER_VERSION_LINK) == 0)
				Option.Instance().setVersionLink(commandLine.substring(SERVER_VERSION_LINK.length()));
			if (commandLine.indexOf(SERVER_SET_TIME_KEY) == 0)
			{
				String s = commandLine.substring(SERVER_SET_TIME_KEY.length());
				Long serverTime = Long.parseLong(s.substring(0, s.length() - 1));
				Option.Instance().setServerTimeDifference(serverTime - (new Date()).getTime());
				Option.Instance().save();
			}
			break;
		case WORKER:
			if (commandActionType == NEED_TO_ADD) {
				Worker item = new Worker(commandLine);
				RecievedObjectSaver.Instance().workersToSave.add(item);
				syncHandler.onProgressUpdate(item.getName() + " OK");
			} else
				addToDeleteList(RecievedObjectSaver.Instance().workersToDelete, getIDFromStr(commandLine));
			break;
		case PATIENT_REMARK:
			if (commandActionType == NEED_TO_ADD) {
				PatientRemark item = new PatientRemark(commandLine);
				RecievedObjectSaver.Instance().patientRemarksToSave.add(item);
				syncHandler.onProgressUpdate(item.getName() + " OK");
			} else 
				addToDeleteList(RecievedObjectSaver.Instance().patientRemarksToDelete, getIDFromStr(commandLine));
			break;
		case DIAGNOSE:
			if (commandActionType == NEED_TO_ADD) {
				Diagnose item = new Diagnose(commandLine);
				RecievedObjectSaver.Instance().diagnosesToSave.add(item);
				syncHandler.onProgressUpdate(item.getName() + " OK");
			} else 
				addToDeleteList(RecievedObjectSaver.Instance().diagnosesToDelete, getIDFromStr(commandLine));
			break;
		case INFORMATION:
			if (commandActionType == NEED_TO_ADD) {
				Information item = new Information(commandLine);
				RecievedObjectSaver.Instance().infromationToSave.add(item);
				syncHandler.onProgressUpdate(item.getName() + " OK");
			} else 
				addToDeleteList(RecievedObjectSaver.Instance().infromationToDelete, getIDFromStr(commandLine));
			break;
		case 'F':
			break;
		case ADDITIONAL_TASK_L:
		case ADDITIONAL_TASK_Z:
			if (commandLine.startsWith("" + ADDITIONAL_TASK_Z))
			commandLine = ADDITIONAL_TASK_L + commandLine.substring(1);
			if (commandActionType == NEED_TO_ADD) {
				AdditionalTask item = new AdditionalTask(commandLine);
				RecievedObjectSaver.Instance().additionalTasksToSave.add(item);
				syncHandler.onProgressUpdate(item.getName() + " OK");
			} else {
				String[] arr = commandLine.split(";");
				commandLine = "L" + arr[1] +";"+ arr[2];
				addToDeleteList(RecievedObjectSaver.Instance().additionalTasksToDelete, getIDFromStr(commandLine));
			}			
			break;
		case DOCTOR:
			if (commandActionType == NEED_TO_ADD) {
				Doctor item = new Doctor(commandLine);
				RecievedObjectSaver.Instance().doctorsToSave.add(item);
				syncHandler.onProgressUpdate(item.getFullName() + " OK");
			} else 
				addToDeleteList(RecievedObjectSaver.Instance().doctorsToDelete, getIDFromStr(commandLine));
			break;
		case PATIENT:
			if (commandActionType == NEED_TO_ADD) {
				Patient item = new Patient(commandLine);
				RecievedObjectSaver.Instance().patientsToSave.add(item);
				syncHandler.onProgressUpdate(item.getFullName() + " OK");
			} else 
				addToDeleteList(RecievedObjectSaver.Instance().patientsToDelete, getIDFromStr(commandLine));
			break;
		case TASK:
			if (commandActionType == NEED_TO_ADD) {
				Task item = new Task(commandLine);
				RecievedObjectSaver.Instance().tasksToSave.add(item);
				syncHandler.onProgressUpdate(item.getName() + " OK");
			} else
				addToDeleteList(RecievedObjectSaver.Instance().tasksToDelete, getIDFromStr(commandLine));
			break;
		case ADDITIONAL_WORK:
			if (commandActionType == NEED_TO_ADD) {
				AdditionalWork item = new AdditionalWork(commandLine);
				RecievedObjectSaver.Instance().additionalWorksToSave.add(item);
				syncHandler.onProgressUpdate(item.getName() + " OK");
			} else 
				addToDeleteList(RecievedObjectSaver.Instance().additionalWorksToDelete, getIDFromStr(commandLine));
			break;
		case TOUR:
			if (commandActionType == NEED_TO_ADD) {
				Tour item = new Tour(commandLine);
				RecievedObjectSaver.Instance().toursToSave.add(item);
				syncHandler.onProgressUpdate(item.getName() + " OK");
			} else
				addToDeleteList(RecievedObjectSaver.Instance().toursToDelete, getIDFromStr(commandLine));
			break;
		case RELATIVE:
			if (commandActionType == NEED_TO_ADD) {
				Relative item = new Relative(commandLine);
				RecievedObjectSaver.Instance().relativesToSave.add(item);
				syncHandler.onProgressUpdate(item.getFullName() + " OK");
			} else 
				addToDeleteList(RecievedObjectSaver.Instance().relativesToDelete, getIDFromStr(commandLine));
			break;
		case CUSTOM_REMARK:
			if (commandActionType == NEED_TO_ADD) {
				CustomRemark item = new CustomRemark(commandLine);
				RecievedObjectSaver.Instance().customRemarksToSave.add(item);
				syncHandler.onProgressUpdate(item.getName() + " OK");
			} else 
				addToDeleteList(RecievedObjectSaver.Instance().customRemarksToDelete, getIDFromStr(commandLine));
			break;
		case QUESTION:
			if (commandActionType == NEED_TO_ADD) {
				Question item = new Question(commandLine);
				RecievedObjectSaver.Instance().questionsToSave.add(item);
				syncHandler.onProgressUpdate(item.getName() + " OK");
			} else
				addToDeleteList(RecievedObjectSaver.Instance().questionsToDelete, getIDFromStr(commandLine));
			break;
		case CATEGORY:
			if (commandActionType == NEED_TO_ADD) {
				Category item = new Category(commandLine);
				RecievedObjectSaver.Instance().categoriesToSave.add(item);
				syncHandler.onProgressUpdate(item.getName() + " OK");
			} else
				addToDeleteList(RecievedObjectSaver.Instance().categoriesToDelete, getIDFromStr(commandLine));
			break;
		case LINK:
			if (commandActionType == NEED_TO_ADD) {
				Link item = new Link(commandLine);
				RecievedObjectSaver.Instance().linksToSave.add(item);
				syncHandler.onProgressUpdate(item.getName() + " OK");
			} else
				addToKeyDeleteList(RecievedObjectSaver.Instance().linksToDelete, getKeyFromStr(commandLine));
			break;
		case QUESTION_SETTING:
			if (commandActionType == NEED_TO_ADD) {
				QuestionSetting item = new QuestionSetting(commandLine);
				RecievedObjectSaver.Instance().questionSettingsToSave.add(item);
				syncHandler.onProgressUpdate(item.getName() + " OK");
			} else
				addToDeleteList(RecievedObjectSaver.Instance().questionSettingsToDelete, getIDFromStr(commandLine));
			break;
		case RELATED_QUESTION_SETTING:
			if (commandActionType == NEED_TO_ADD) {
				RelatedQuestionSetting item = new RelatedQuestionSetting(commandLine);
				RecievedObjectSaver.Instance().relatedQuestionSettingsToSave.add(item);
				syncHandler.onProgressUpdate(item.getName() + " OK");
			} else
				addToDeleteList(RecievedObjectSaver.Instance().relatedQuestionSettingsToDelete, getIDFromStr(commandLine));
			break;
		case TOUR_ONCOMING_INFO:
			if (commandActionType == NEED_TO_ADD) {
				TourOncomingInfo item = new TourOncomingInfo(commandLine);
				RecievedObjectSaver.Instance().tourOncomingInfoToSave.add(item);
				syncHandler.onProgressUpdate(item.getName() + " OK");
			} else
				addToDeleteList(RecievedObjectSaver.Instance().tourOncomingInfoToDelete, getIDFromStr(commandLine));
			break;
		case PATIENT_ADDITIONAL_ADDRESS:
			if(commandActionType == NEED_TO_ADD){
				PatientAdditionalAddress additionalAddress = new PatientAdditionalAddress(commandLine);
				RecievedObjectSaver.Instance().patientsAdditionalAddressToSave.add(additionalAddress);
				syncHandler.onProgressUpdate(additionalAddress.getName() + " OK");
			}else{
				addToDeleteList(RecievedObjectSaver.Instance().patientsAdditionalAddressToDelete, getIDFromStr(commandLine));
			}
				break;
		default:
			break;
		}
		return blnRes;
	}
	
	private void addToDeleteList(List<Integer> list, int id) {
		if (id == BaseObject.EMPTY_ID)
			return;
		list.add(id);
		syncHandler.onProgressUpdate(id + " Deleted");
	}
	
	private void addToKeyDeleteList(List<String> list, String key) {
		if (key == "")
			return;
		list.add(key);
		syncHandler.onProgressUpdate(key + " Deleted");
	}
	
	private int getIDFromStr(String str) {
		int retVal = BaseObject.EMPTY_ID;
		try{
			str = str.substring(1, str.length()-1);
			String firstNumber = str.split(";")[0];
			retVal = Integer.parseInt(firstNumber);
		}
		catch(Exception e){
			retVal = BaseObject.EMPTY_ID;
		}
		return retVal;
	}
	
	private String getKeyFromStr(String str) {
		str = str.substring(1, str.length()-1);
		return str.split(";")[0];
	}
	
}
