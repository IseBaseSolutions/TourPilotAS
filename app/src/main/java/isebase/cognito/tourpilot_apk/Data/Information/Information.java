package isebase.cognito.tourpilot_apk.Data.Information;

import isebase.cognito.tourpilot_apk.Connection.ServerCommandParser;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;
import isebase.cognito.tourpilot_apk.Utils.StringParser;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Informations")
public class Information extends BaseObject {

	public static final String EMPLOYMENT_ID_FIELD = "employment_id";
	public static final String INFORMATION_ID_FIELD = "information_id";
	public static final String FROM_DATE_FIELD = "from_date";
	public static final String TILL_DATE_FIELD = "till_date";
	public static final String READ_TIME_FIELD = "read_time";
	public static final String IS_FROM_SERVER_FIELD = "is_from_server";
	public static final String IS_DONE = "is_done";

	@DatabaseField(dataType = DataType.LONG, columnName = EMPLOYMENT_ID_FIELD)
	private long employmentID;
	
	public long getEmploymentID() {
		return employmentID;
	}

	public void setEmploymentID(long employmentID) {
		this.employmentID = employmentID;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = INFORMATION_ID_FIELD)
	private int informationID;
	
	public int getInformationID() {
		return informationID;
	}

	public void setInformationID(int informationID) {
		this.informationID = informationID;
	}
	
	@DatabaseField(dataType = DataType.DATE_LONG, columnName = FROM_DATE_FIELD)
	private Date fromDate;
	
	public Date getFromDate() {
		return fromDate == null ? fromDate = DateUtils.EmptyDate : fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	@DatabaseField(dataType = DataType.DATE_LONG, columnName = TILL_DATE_FIELD)
	private Date tillDate;
	
	public Date getTillDate() {
		return tillDate == null ? tillDate = DateUtils.EmptyDate : tillDate;
	}

	public void setTillDate(Date tillDate) {
		this.tillDate = tillDate;
	}
	
	@DatabaseField(dataType = DataType.DATE_LONG, columnName = READ_TIME_FIELD)
	private Date readTime;
	
	public Date getReadTime() {
		return readTime == null ? readTime = DateUtils.EmptyDate : readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	
	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_FROM_SERVER_FIELD)
	private boolean isFromServer;

	public boolean getIsFromServer() {
		return isFromServer;
	}

	public void setIsFromServer(boolean isFromServer) {
		this.isFromServer = isFromServer;
	}

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_DONE)
    private boolean isDone;

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

	public Information() {
		clear();
	}

	public Information(String initString) {
		clear();
		StringParser parsingString = new StringParser(initString);
		parsingString.next(";");
		setIsFromServer(true);
		setInformationID(Integer.parseInt(parsingString.next(";")));
		setEmploymentID(Long.parseLong(parsingString.next(";")));
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmm");
		try {
			setFromDate(format.parse(parsingString.next(";") + "0000"));
			setTillDate(format.parse(parsingString.next(";") + "2359"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		setName(parsingString.next("~"));
		setCheckSum(Long.parseLong(parsingString.next()));
	}

	@Override
	public String forServer() {
		String strValue = new String(ServerCommandParser.INFORMATION + ";");
		strValue += String.format("%d;%d", getId(), getEmploymentID()) + ";";
		strValue += getCheckSum();

        return strValue;
	}

	@Override
	protected void clear() {
		super.clear();
		setInformationID(EMPTY_ID);
		setFromDate(DateUtils.EmptyDate);
		setTillDate(DateUtils.EmptyDate);
		setReadTime(DateUtils.EmptyDate);
		setIsFromServer(false);

	}

	public boolean isActualInfo(Date date) {
		return date.getTime() >= getFromDate().getTime()
				&& date.getTime() <= getTillDate().getTime();
	}

    public String getDone() {
	    if(getIsDone())
	        return "";

        String strValue = new String(ServerCommandParser.INFORMATION + ";");
        strValue +=  getInformationID() + ";";
        strValue +=  Option.Instance().getWorkerID() + ";";
        strValue += DateUtils.DateFormat.format(getReadTime()) + " ";
        strValue += DateUtils.HourMinutesSecondsFormat.format(getReadTime()) + ";";
        strValue += getCheckSum() + ";";
        return strValue;
    }
}
