package isebase.cognito.tourpilot_apk.Data.UserRemark;

import isebase.cognito.tourpilot_apk.Connection.SentObjectVerification;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;
import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "UserRemarks")
public class UserRemark extends BaseObject {

	public static final String PATIENT_ID_FIELD = "patient_id";
	public static final String WORKER_ID_FIELD = "worker_id";
	public static final String DATE_FIELD = "date";
	public static final String CHECKED_IDS_FIELD = "checked_ids";
	public static final String CHECKBOXES_FIELD = "checkboxes";

	@DatabaseField(dataType = DataType.INTEGER, columnName = PATIENT_ID_FIELD)
	private int patientID;
	
	public int getPatientID() {
		return patientID;
	}
	public void setPatientID(int patientID) {
		this.patientID = patientID;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = WORKER_ID_FIELD)
	private int workerID;
	
	public int getWorkerID() {
		return workerID;
	}
	
	public void setWorkerID(int workerID) {
		this.workerID = workerID;
	}
	
	@DatabaseField(dataType = DataType.DATE_LONG, columnName = DATE_FIELD)
	private Date date;
	
	public Date getDate() {
		return date == null ? date = DateUtils.EmptyDate : date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = CHECKBOXES_FIELD)
	private int checkboxes;
	
	public int getCheckboxes() {
		return checkboxes;
	}
	public void setCheckboxes(int checkboxes) {
		this.checkboxes = checkboxes;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = CHECKED_IDS_FIELD, defaultValue = "")
	private String checkedIDs;
	
	public String getCheckedIDs() {
		return checkedIDs == null ?  checkedIDs = "" : checkedIDs;
	}
	
	public void setCheckedIDs(String checkedIDs) {
		this.checkedIDs = checkedIDs;
	}
	
	public String[] getCheckedIDsArr() {
		return getCheckedIDs().split(",");
	}
	
	public UserRemark(){
		clear();
	}
	
    public UserRemark(int EmploymentID, int workerID,  int patientID
    		, boolean chkContact, boolean chkMed, boolean chkVisit
            , boolean chkOther, String strRemark)
    {
        setId(EmploymentID);
        setWorkerID(workerID);
        setPatientID(patientID);
        checkboxes = 0;
        if (chkContact) 
        	checkboxes += 1;
        if (chkMed) 
        	checkboxes += 2;
        if (chkVisit) 
        	checkboxes += 4;
        if (chkOther) 
        	checkboxes += 8;
        setDate(DateUtils.getSynchronizedTime());
        setName(strRemark);
    }

//    public UserRemark(String strInitString) {
//    	clear();
//        StringParser initString = new StringParser(strInitString);
//        initString.next(";");
//        setId(Integer.parseInt(initString.next(";")));
//        setPatientID(Integer.parseInt(initString.next(";")));
//        setDate(new Date(Long.parseLong(initString.next(";"))));
//        setCheckboxes(Integer.parseInt(initString.next(";")));
//        setName(initString.next(";"));
//        setServerTime(initString.next(";").equals("True"));
//        setWasSent(initString.next().equals("True"));
//    }  
    
//    public String toString(){
//		if (!isServerTime() && Option.Instance().isTimeSynchronised())
//		{
//			setDate(DateUtils.getSynchronizedTime(getDate()));
//			setServerTime(true);
//		}
//		HelperFactory.getHelper().getUserRemarkDAO().save(this);
//        String strValue = new String("O;");
//        strValue += getId() + ";";
//        strValue += getPatientID() + ";";
//        strValue += String.valueOf(getDate()) + ";";
//        strValue += getCheckboxes() + ";";
//        strValue += getName() + ";";
//        strValue += (isServerTime() ? "True" : "False") + ";";
//        strValue += (getWasSent() ? "True" : "False");
//        return strValue;
//    }    
    
    public String getDone()
    {
//		if (!isServerTime() && Option.Instance().isTimeSynchronised())
//		{
//			setDate(DateUtils.getSynchronizedTime(getDate()));
//			setServerTime(true);
//			HelperFactory.getHelper().getUserRemarkDAO().save(this);
//		}
        String strValue = new String("O;");
        strValue += getWorkerID() + ";";
        strValue += getPatientID() + ";";
        strValue += DateUtils.toDateTime(getDate()) + ";";
        strValue += getCheckboxes() + ";";
        strValue += getName() + ";";
        strValue += getCheckedIDs();
        SentObjectVerification.Instance().sentUserRemarks.add(this);
        return strValue;
    }
    
	@Override
	public String forServer() {       
	    return "";
	}

	public void setCheckboxes(boolean chkContact
			, boolean chkMed, boolean chkVisit, boolean chkOther ){
        checkboxes = 0;
        if (chkContact) 
        	checkboxes += 1;
        if (chkMed) 
        	checkboxes += 2;
        if (chkVisit) 
        	checkboxes += 4;
        if (chkOther) 
        	checkboxes += 8;
	}

	@Override
	public void clear(){
		super.clear();
		setPatientID(EMPTY_ID);
		setDate(DateUtils.EmptyDate);
		setCheckboxes(0);
		setCheckedIDs("");
	}
	
}
