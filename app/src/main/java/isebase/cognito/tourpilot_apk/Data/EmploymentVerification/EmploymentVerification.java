package isebase.cognito.tourpilot_apk.Data.EmploymentVerification;

import isebase.cognito.tourpilot_apk.Connection.SentObjectVerification;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Data.EmploymentInterval.EmploymentInterval;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "EmploymentVerifications")
public class EmploymentVerification extends BaseObject {

	public static final String EMPLOYMENT_ID_FIELD = "employment_id";
	public static final String WORKER_ID_FIELD = "worker_id";
	public static final String PATIENT_ID_FIELD = "patient_id";
	public static final String DATE_BEGIN_FIELD = "date_begin";
	public static final String DATE_END_FIELD = "date_end";
	public static final String DONE_TASKS_ID_FIELD = "done_tasks_ids";
	public static final String UNDONE_TASKS_ID_FIELD = "undone_tasks_ids";
	public static final String MARKS_FIELD = "user_remarks_marks";
	public static final String PFLEGE_FIELD = "pflege";

	@DatabaseField(dataType = DataType.LONG, columnName = EMPLOYMENT_ID_FIELD)
	private long employmentID;
	
	public long getEmploymentID() {
		return employmentID;
	}

	public void setEmploymentID(long employmentID) {
		this.employmentID = employmentID;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = WORKER_ID_FIELD)
	private int workerID;
	
	public int getWorkerID() {
		return workerID;
	}

	public void setWorkerID(int workerID) {
		this.workerID = workerID;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = PATIENT_ID_FIELD)
	private int patientID;
	
	public int getPatientID() {
		return patientID;
	}

	public void setPatientID(int patientID) {
		this.patientID = patientID;
	}
	
	@DatabaseField(dataType = DataType.DATE_LONG, columnName = DATE_BEGIN_FIELD)
	private Date dateBegin;
	
	public Date getDateBegin() {
//		if (dateBegin == null)
//			dateBegin = DateUtils.EmptyDate;
//		return dateBegin.getTime();
		return dateBegin == null ? dateBegin = DateUtils.EmptyDate : dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	
	@DatabaseField(dataType = DataType.DATE_LONG, columnName = DATE_END_FIELD)
	private Date dateEnd;
	
	public Date getDateEnd() {
//		if (dateEnd == null)
//			dateEnd = DateUtils.EmptyDate;
//		return dateEnd.getTime();
		return dateEnd == null ? dateEnd = DateUtils.EmptyDate : dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	@DatabaseField(dataType = DataType.STRING, columnName = DONE_TASKS_ID_FIELD)
	private String doneTasksIDs;
	
	public String getDoneTasksIDs() {
		return doneTasksIDs;
	}

	public void setDoneTasksIDs(String doneTasksIDs) {
		this.doneTasksIDs = doneTasksIDs;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = UNDONE_TASKS_ID_FIELD)
	private String undoneTasksIDs;
	
	public String getUnDoneTasksIDs() {
		return undoneTasksIDs;
	}

	public void setUnDoneTasksIDs(String undoneTasksIDs) {
		this.undoneTasksIDs = undoneTasksIDs;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = MARKS_FIELD)
	private String userRemarksMarks;
	
	public String getUserRemarksMarks() {
		return userRemarksMarks;
	}

	public void setUserRemarksMarks(String userRemarksMarks) {
		this.userRemarksMarks = userRemarksMarks;
	}
	
	@DatabaseField(dataType = DataType.BOOLEAN, columnName = PFLEGE_FIELD)
	private boolean isPflege;

	public boolean isPflege() {
		return isPflege;
	}

	public void setPflege(boolean isPflege) {
		this.isPflege = isPflege;
	}

	public EmploymentVerification(long employmentID, int workerID, int patientID,
			Date dateBegin, Date dateEnd,
			String doneTasksIDs, String undoneTasksIDs, String userRemarksMarks, boolean pflege) {
		setEmploymentID(employmentID);
		setWorkerID(workerID);
		setPatientID(patientID);
		setDateBegin(dateBegin);
		setDateEnd(dateEnd);
		setDoneTasksIDs(doneTasksIDs);
		setUnDoneTasksIDs(undoneTasksIDs);
		setUserRemarksMarks(userRemarksMarks);
		setPflege(pflege);
	}

	public EmploymentVerification() {

	}

	@Override
	public String getDone() {
		String strValue = "";
		strValue += "S;";
		strValue += employmentID + ";";
		strValue += workerID + ";";
		strValue += patientID + ";";
		EmploymentInterval empl = HelperFactory.getHelper().getEmploymentIntervalDAO()
                .load((int)employmentID);
		strValue += DateUtils.DateFormat.format(empl.getStartTime()) + " " +
                DateUtils.HourMinutesFormat.format(empl.getStartTime()) + ";";
		strValue += DateUtils.DateFormat.format(empl.getStopTime()) + " " +
                DateUtils.HourMinutesFormat.format(empl.getStopTime()) + ";";
		strValue += doneTasksIDs + ";";
		strValue += undoneTasksIDs + ";";
		strValue += userRemarksMarks + ";";
		strValue += isPflege + ";";
		strValue += (employmentID + "" + Option.Instance().getWorkerID() + "" +
                Option.Instance().getPilotTourID() + "" + patientID +""+
                DateUtils.HourMinutesSecondsFormat.format(empl.getStartTime()) +
                ""+ DateUtils.HourMinutesSecondsFormat.format(empl.getStopTime()));
		SentObjectVerification.Instance().sentEmploymentVerifications.add(this);
		return strValue;
	}

}
