package isebase.cognito.tourpilot_apk.Data.Employment;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Connection.SentObjectVerification;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Data.EmploymentInterval.EmploymentInterval;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.Patient.Patient;
import isebase.cognito.tourpilot_apk.Data.PilotTour.PilotTour;
import isebase.cognito.tourpilot_apk.Data.Task.Task;
import isebase.cognito.tourpilot_apk.Data.Task.Task.eTaskState;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.DataInterfaces.Job.IJob;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Employments")
public class Employment extends BaseObject implements IJob {

	public static final String PATIENT_ID_FIELD = "patient_id";
	public static final String PILOT_TOUR_ID_FIELD = "pilot_tour_id";
	public static final String TOUR_ID_FIELD = "tour_id"; 
	public static final String DATE_FIELD = "date";
	public static final String IS_DONE_FIELD = "is_done";
	public static final String START_TIME_FIELD = "start_time";
	public static final String STOP_TIME_FIELD = "stop_time";
	public static final String DAY_PART_FIELD = "day_part";
	public static final String IS_FROM_MOBILE_FIELD = "from_mobile";
	
	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_FROM_MOBILE_FIELD)
	private boolean isFromMobile;
	
	public boolean isFromMobile() {
		return isFromMobile;
	}
	
	public void setFromMobile(boolean isFromMobile) {
		this.isFromMobile = isFromMobile;
	}

	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_DONE_FIELD)
	private boolean isDone;
	
	public boolean getIsDone() {
		return isDone;
	}

	public void setIsDone(boolean isDone) {
		this.isDone = isDone;
	}
	
	@DatabaseField(dataType = DataType.DATE_LONG, columnName = DATE_FIELD)
	private Date date;
	
	public Date getDate() {
		return date == null ? date = DateUtils.EmptyDate : date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = TOUR_ID_FIELD)
	private int tourID;
	
	public int getTourID() {
		return tourID;
	}

	public void setTourID(int id) {
		this.tourID = id;
	}

	@DatabaseField(dataType = DataType.LONG, columnName = PILOT_TOUR_ID_FIELD)
	private long pilotTourID;
	
	public long getPilotTourID() {
		return pilotTourID;
	}

	public void setPilotTourID(long pilotTourID) {
		this.pilotTourID = pilotTourID;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = PATIENT_ID_FIELD)
	private int patientID;	

	public int getPatientID() {
		return patientID;
	}

	public void setPatientID(int patientID) {
		this.patientID = patientID;
	}

	@DatabaseField(dataType = DataType.DATE_LONG, columnName = START_TIME_FIELD)	
	private Date startTime;

	
	public Date getStartTime() {
		return startTime == null ? startTime = DateUtils.EmptyDate : startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@DatabaseField(dataType = DataType.DATE_LONG, columnName = STOP_TIME_FIELD)	
	private Date stopTime;

	public Date getStopTime() {
		return stopTime == null ? stopTime = DateUtils.EmptyDate : stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}	
	
	@DatabaseField(dataType = DataType.STRING, columnName = DAY_PART_FIELD)	
	private String dayPart;
	
	public String getDayPart() {
		return dayPart.contains("Einsatzende") ? (dayPart = dayPart.replace("Einsatzende ", "")) : (dayPart = dayPart.replace("Einsatzbeginn ", ""));
	}

	public void setDayPart(String dayPart) {
		this.dayPart = dayPart;
	}

	private List<Task> tasks;
	
	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	private Patient patient;
	
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	private PilotTour pilotTour;

	public PilotTour getPilotTour() {
		return pilotTour;
	}

	public void setPilotTour(PilotTour pilotTour) {
		this.pilotTour = pilotTour;
	}
	
	public Employment() {
		tasks =  new ArrayList<Task>();
	}
	
	@Override
	public String toString() {
		return getName() + "  " + DateUtils.HourMinutesFormat.format(getDate());
	}

	public String getTime() {
		return DateUtils.HourMinutesFormat.format(startTime()) + "\n" + DateUtils.HourMinutesFormat.format(stopTime());
	}
		
	@Override
	public String forServer() {
		return "";
	}
	
    public String getDone()
    {
        if (!getIsDone())
        	return "";
        String strValue = "";
        String strEmpl = "E;";
        strEmpl += Option.Instance().getWorkerID() + ";";
        strEmpl += getPatientID() + ";";
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
        for (Task task : tasks)
        {
    		if (!task.isServerTime() && Option.Instance().isTimeSynchronised()) {
//            	task.setRealDate(DateUtils.getSynchronizedTime(task.getRealDate()));
            	task.setServerTime(true);
    			if (task.isFirstTask())	{
    		        EmploymentInterval emplInt = HelperFactory.getHelper().getEmploymentIntervalDAO().load(getId());
    		        if (emplInt != null) {
		    			emplInt.setStartTime(task.getManualDate().equals(DateUtils.EmptyDate) ? task.getRealDate() : task.getManualDate());
		    			HelperFactory.getHelper().getEmploymentIntervalDAO().save(emplInt);
    		        }
    			}
    			if (task.isLastTask()) {
    		        EmploymentInterval emplInt = HelperFactory.getHelper().getEmploymentIntervalDAO().load(getId());
    		        if (emplInt != null) {
		    			emplInt.setStopTime(task.getManualDate().equals(DateUtils.EmptyDate) ? task.getRealDate() : task.getManualDate());
		    			HelperFactory.getHelper().getEmploymentIntervalDAO().save(emplInt);
    		        }
    			}
    		}    		
        	String strTask = strEmpl;
            strTask += format.format(task.getPlanDate()) + ";"; // date only
            strTask += task.getPlanDate().toString().substring(11,13); // time hours
            strTask += task.getPlanDate().toString().substring(14,16) + ";"; // time minutes
            strTask += task.getLeistungs() + ";";
            strTask += (task.getState().equals(eTaskState.Done) ? "ja" : "nein"); 
            String qulityResult = task.getQualityResult().equals("") ? "" : "$" + task.getQualityResult();
            strTask += qulityResult;
            strTask += ";";
            strTask += (DateUtils.EmptyDate.equals(task.getManualDate()) ? DateUtils.toDateTime(task.getRealDate())
            		: DateUtils.toDateTime(task.getManualDate())) + ";";
            strTask += (DateUtils.EmptyDate.equals(task.getManualDate()) ? "" : DateUtils.toDateTime(task.getRealDate()));

            strTask += (isFromMobile() && task.isFirstTask())
            		? (";" + getId()+ "" + Option.Instance().getWorkerID() + "" + Option.Instance().getPilotTourID() + "" + getPatientID() +""+ DateUtils.HourMinutesSecondsFormat.format(startTime) +""+ DateUtils.HourMinutesSecondsFormat.format(stopTime)) 
            				: "";
    		strTask += "\0";
            strValue += strTask;
            SentObjectVerification.Instance().sentTasks.add(task);
        }
        HelperFactory.getHelper().getTaskDAO().save(tasks);
        SentObjectVerification.Instance().sentEmployments.add(this);
        return strValue;
    }
    
    public Task getFirstTask() {
    	if (tasks.size() == 0)
    		return null;
    	if (tasks.get(0).getLeistungs().contains("Anfang"))
    		return tasks.get(0);
    	for (Task task : tasks)
    		if (task.getLeistungs().contains("Anfang"))
    			return task;
    	return null;
    }
    
    public Task getLastTask() {
    	if (tasks.size() == 0)
    		return null;
    	if (tasks.get(tasks.size()-1).getLeistungs().contains("Ende"))
    		return tasks.get(tasks.size()-1);
    	for (Task task : tasks)
    		if (task.getLeistungs().contains("Ende"))
    			return task;
    	return null;
    }
    
    public void clearConnectedData() {
		HelperFactory.getHelper().getTaskDAO().deleteByEmploymentID(getId());
		HelperFactory.getHelper().getUserRemarkDAO().deleteByEmploymentID(getId());
		HelperFactory.getHelper().getEmploymentVerificationDAO().deleteByEmploymentID(getId());
		HelperFactory.getHelper().getEmploymentIntervalDAO().deleteByEmploymentID(getId());
		HelperFactory.getHelper().getAnswerDAO().deleteByEmploymentID(getId());
		HelperFactory.getHelper().getAnsweredCategoryDAO().deleteByEmploymentID(getId());
		HelperFactory.getHelper().getExtraCategoryDAO().deleteByEmploymentID(getId());
		HelperFactory.getHelper().getQuestionSettingDAO().deleteByEmploymentID(getId());
		HelperFactory.getHelper().getInformationDAO().deleteByEmploymentID(getId());
    }

	@Override
	public String timeInterval() {
		return String.format("%s-%s", (getStartTime().equals(DateUtils.EmptyDate) 
				? StaticResources.getBaseContext().getString(R.string.def_empty_time) 
						: DateUtils.HourMinutesFormat.format(getStartTime())), 
				(getStopTime().equals(DateUtils.EmptyDate) 
						? StaticResources.getBaseContext().getString(R.string.def_empty_time)
								: DateUtils.HourMinutesFormat.format(getStopTime())));
	}

	@Override
	public boolean isDone() {
		return getIsDone();
	}

	public boolean isAdditionalWork(){
		return getPatientID() > Patient.ADDITIONAL_WORK_CODE;
	}
	
	@Override
	public String text() {
		return isAdditionalWork() ? getName().replace(", ", "") : getName();
	}

	@Override
	public String time() {
		return getTime();
	}
	
	@Override
	public boolean wasSent() {
		return getWasSent();
	}

	@Override
	public Date startTime() {
		return getStartTime().equals(DateUtils.EmptyDate) ? getPlanStartTime() : getStartTime();
	}

	@Override
	public Date stopTime() {
		return getStopTime().equals(DateUtils.EmptyDate) ? getPlanStopTime() : getStopTime();
	}
	
	public boolean isWork() {
		return getPatientID() > 999900;
	}
	
	private Date getPlanStartTime() {
		if (tasks == null || tasks.size() == 0)
			return getDate();
		return getFirstTask().getPlanDate();
	}
	
	private Date getPlanStopTime() {
		if (tasks == null || tasks.size() == 0)
			return getDate();
		return getLastTask().getPlanDate();
	}

}
