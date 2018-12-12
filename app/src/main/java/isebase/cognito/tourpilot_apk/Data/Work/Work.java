package isebase.cognito.tourpilot_apk.Data.Work;

import isebase.cognito.tourpilot_apk.Connection.SentObjectVerification;
import isebase.cognito.tourpilot_apk.Connection.ServerCommandParser;
import isebase.cognito.tourpilot_apk.Data.AdditionalWork.AdditionalWork;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.Patient.Patient;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.DataInterfaces.Job.IJob;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Works")
public class Work extends BaseObject implements IJob {

	public static final String ADDITIONAL_WORK_ID_FIELD = "add_work_id";
	public static final String PILOT_TOUR_ID_FIELD = "pilot_tour_id";
	public static final String START_TIME_FIELD = "start_time";
	public static final String STOP_TIME_FIELD = "stop_time";
	public static final String MANUAL_TIME_FIELD = "manual_time";
	public static final String PATIENTS_ID_FIELD = "patient_ids";
	public static final String IS_DONE_FIELD = "is_done";
	public static final String WORKER_ID = "worker_id";
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = ADDITIONAL_WORK_ID_FIELD)
	private int additionalWorkID;
	
	public int getAdditionalWorkID() {
		return additionalWorkID;
	}

	public void setAdditionalWorkID(int additionalWorkID) {
		this.additionalWorkID = additionalWorkID;
	}
	
	@DatabaseField(dataType = DataType.LONG, columnName = PILOT_TOUR_ID_FIELD)
	private long pilotTourID;
	
	public long getPilotTourID() {
		return pilotTourID;
	}

	public void setPilotTourID(long pilotTourID) {
		this.pilotTourID = pilotTourID;
	}
	
	
	@DatabaseField(dataType = DataType.LONG, columnName = WORKER_ID)
	private long workerID;
	
	public long getWorkerID() {
		return workerID;
	}

	public void setWorkerID(long workerID) {
		this.workerID = workerID;
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
	
	@DatabaseField(dataType = DataType.DATE_LONG, columnName = MANUAL_TIME_FIELD)
    private Date manualTime;

    public Date getManualTime() {
		return manualTime == null ? manualTime = DateUtils.EmptyDate : manualTime;
	}

	public void setManualTime(Date manualTime) {
		this.manualTime = manualTime;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = PATIENTS_ID_FIELD)
	private String patientsID;
	
	public String getPatientIDs() {
		return patientsID;
	}

	public void setPatientIDs(String patientIDs) {
		this.patientsID = patientIDs;
	}
	
	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_DONE_FIELD)
	private boolean isDone;
	
	public boolean getIsDone() {
		return isDone;
	}
	
	public void setIsDone(boolean isDone) {
		this.isDone = isDone;
	}	
	
	private AdditionalWork additionalWork;
	
	private List<Patient> patients = new ArrayList<Patient>(); 
	
	public List<Patient> getPatients() {
		return patients;
	}

	public void setPatients(List<Patient> patients) {
		this.patients = patients;
	}

	public AdditionalWork getAdditionalWork() {
		return additionalWork;
	}
	
	public void setAdditionalWork(AdditionalWork additionalWork) {
		this.additionalWork = additionalWork;
	}

    public Work()
    {
    	clear();
    }
	
    public Work(Date startDate, int addWorkID, long pilotTourID, String name, int workerID)
    {
    	clear();
        setStartTime(startDate);
        setAdditionalWorkID(addWorkID);
        setPilotTourID(pilotTourID);
        setName(name);
        setWorkerID(workerID);
    }
    
    public Work(Date startDate, Date stopDate, Date manualtime, int addWorkID, long pilotTourID, String name, int workerID)
    {
    	clear();
        setStartTime(startDate);
        setStopTime(stopDate);
        setManualTime(manualtime);
        setAdditionalWorkID(addWorkID);
        setPilotTourID(pilotTourID);
        setName(name);
        setWorkerID(workerID);
    }

    
	@Override
	public String forServer() {	
		if (!isServerTime() && Option.Instance().isTimeSynchronised())
		{
			setStartTime(DateUtils.getSynchronizedTime(getStartTime()));
			setStopTime(DateUtils.getSynchronizedTime(getStopTime()));
			setServerTime(true);
			HelperFactory.getHelper().getWorkDAO().save(this);
		}
        String strValue = new String(ServerCommandParser.WORK + ";");
        strValue += getWorkerID() + ";";
        strValue += DateUtils.toDateTime(getStartTime()) + ";";
        strValue += DateUtils.toDateTime(getStopTime()) + ";";
        strValue += getAdditionalWorkID() + ";";
        strValue += getPilotTourID() + ";";
        strValue += getPatientIDs() + ";";
        strValue += !getManualTime().equals(DateUtils.EmptyDate) 
        		? DateUtils.toDateTime(DateUtils.getSynchronizedTime(getManualTime())) 
        		: ""; 
        return strValue;
	}
	
	public String getDone() {
		SentObjectVerification.Instance().sentWorks.add(this);
		return forServer();
	}
	
	@Override
	protected void clear() {
		setManualTime(DateUtils.EmptyDate);
		setPatientIDs("");
		super.clear();
	}
	
	public String getTime() {
		return DateUtils.HourMinutesFormat.format(getStartTime()) + "\n" + DateUtils.HourMinutesFormat.format(getStopTime());
	}

	@Override
	public String timeInterval() {
		return String.format("%s-%s", DateUtils.HourMinutesFormat.format(getStartTime()), 
				DateUtils.HourMinutesFormat.format(getStopTime()));
	}

	@Override
	public boolean isDone() {
		return getIsDone();
	}

	@Override
	public String text() {
		return getName();
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
		return getStartTime();
	}

	@Override
	public Date stopTime() {
		return getStopTime();
	}

	
}
