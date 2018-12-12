package isebase.cognito.tourpilot_apk.Data.EmploymentInterval;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "EmploymentIntervals")
public class EmploymentInterval extends BaseObject {

	public static final String START_TIME_FIELD = "start_time";
	public static final String STOP_TIME_FIELD = "stop_time";

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
	
	public EmploymentInterval() {
		clear();
	}
	
	public EmploymentInterval(int employmentID, Date startTime, Date stopTime) {
		setId(employmentID);
		this.startTime = startTime;
		this.stopTime = stopTime;
	}
		
}
