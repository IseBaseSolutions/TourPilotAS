package isebase.cognito.tourpilot.Data.BaseObject;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class BaseObject {

	public final static String ID_FIELD = "_id";
	public final static String NAME_FIELD = "name";
	public final static String CHECK_SUM_FIELD = "checksum";
	public final static String WAS_SENT_FIELD = "was_sent";
	public final static String IS_SERVER_TIME_FIELD = "is_server_time";
	
	public static final int EMPTY_ID = -1;

	@DatabaseField(generatedId = true, allowGeneratedIdInsert = true, columnName = ID_FIELD)
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@DatabaseField(dataType = DataType.STRING, columnName = NAME_FIELD)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@DatabaseField(dataType = DataType.LONG, columnName = CHECK_SUM_FIELD)
	private long checkSum;

	public long getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(long checkSum) {
		this.checkSum = checkSum;
	}

	@DatabaseField(dataType = DataType.BOOLEAN, columnName = WAS_SENT_FIELD)
	private boolean wasSent;

	public boolean getWasSent() {
		return wasSent;
	}

	public void setWasSent(boolean wasSent) {
		this.wasSent = wasSent;
	}
	
	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_SERVER_TIME_FIELD)
	private boolean isServerTime;

	public boolean isServerTime() {
		return isServerTime;
	}

	public void setServerTime(boolean isServerTime) {
		this.isServerTime = isServerTime;
	}

	public BaseObject() {
		
	}
	
	protected void clear() {
		id = 0;//EMPTY_ID;
		name = "";
		checkSum = 0;
		wasSent = false;
		isServerTime = true;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public String forServer() {
		return "";
	}
	
	public String getDone() {
		return "";
	}

}
