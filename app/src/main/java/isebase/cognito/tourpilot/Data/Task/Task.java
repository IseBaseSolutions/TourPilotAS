package isebase.cognito.tourpilot.Data.Task;

import isebase.cognito.tourpilot.Connection.ServerCommandParser;
import isebase.cognito.tourpilot.Data.AdditionalTask.AdditionalTask;
import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot.Data.Employment.Employment;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.Data.Patient.Patient;
import isebase.cognito.tourpilot.Data.PilotTour.PilotTour;
import isebase.cognito.tourpilot.DataBase.HelperFactory;
import isebase.cognito.tourpilot.Utils.DateUtils;
import isebase.cognito.tourpilot.Utils.StringParser;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Tasks")
public class Task extends BaseObject {

	public static final String STATE_FIELD = "task_state";
	public static final String WORKER_ID_FIELD = "worker_id";
	public static final String PLAN_DATE_FIELD = "plan_date";
	public static final String LEISTUNGS_FIELD = "leistungs";
	public static final String MINUTE_PRICE_FIELD = "minute_price";
	public static final String TOUR_ID_FIELD = "tour_id";
	public static final String PATIENT_ID_FIELD = "patient_id";
	public static final String IS_ADDITIONAL_TASK = "additional_task";
	public static final String ADDITIONAL_TASK_ID = "additional_task_id";
	public static final String EMPLOYMENT_ID_FIELD = "employment_id";
	public static final String PILOT_TOUR_ID = "pilot_tour_id";
	public static final String REAL_DATE_FIELD = "real_date";
	public static final String MANUAL_DATE_FIELD = "manual_date";
	public static final String QUALITY_FIELD = "quality";
	public static final String QUALITY_RESULT_FIELD = "quality_result";
	public static final String CATALOG_FIELD = "catalog";

	public enum eTaskState {
		Empty, Done, UnDone
	}

	@DatabaseField(dataType = DataType.ENUM_STRING, columnName = STATE_FIELD, unknownEnumName = "Empty")
	private eTaskState taskState = eTaskState.Empty;
	
	public eTaskState getState() {
		return taskState;
	}
	
	public boolean IsDone(){
		return taskState == eTaskState.Done;
	}
	
	public boolean UnDone(){
		return !IsDone();
	}

	public void setState(eTaskState taskState) {
		this.taskState = taskState;
	}

	@DatabaseField(dataType = DataType.DATE_LONG, columnName = PLAN_DATE_FIELD)
	private Date planDate;
	
	public Date getPlanDate() {
		return planDate == null ? planDate = DateUtils.EmptyDate : planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	@DatabaseField(dataType = DataType.DATE_LONG, columnName = REAL_DATE_FIELD)
	private Date realDate;
	
	public Date getRealDate() {
		return realDate == null ? realDate = DateUtils.EmptyDate : realDate;
	}

	public void setRealDate(Date realDate) {
		this.realDate = realDate;
	}
	
	@DatabaseField(dataType = DataType.DATE_LONG, columnName = MANUAL_DATE_FIELD)
	private Date manualDate;
	
	public Date getManualDate() {
		return manualDate == null ? manualDate = DateUtils.EmptyDate : manualDate;
	}

	public void setManualDate(Date manualDate) {
		this.manualDate = manualDate;
	}

	@DatabaseField(dataType = DataType.STRING, columnName = LEISTUNGS_FIELD)
	private String leistungs;
	
	public String getLeistungs() {
		return leistungs;
	}

	public void setLeistungs(String leistungs) {
		this.leistungs = leistungs;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = QUALITY_RESULT_FIELD)
	private String qualityResult;
	
	public void setQualityResult(String qualityResult){
		this.qualityResult = qualityResult;
	}
	
	public String getQualityResult(){
		return qualityResult;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = WORKER_ID_FIELD)
	private int workerID;
	
	public int getWorkerID() {
		return workerID;
	}

	public void setWorkerID(int workerID) {
		this.workerID = workerID;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = MINUTE_PRICE_FIELD)
	private int minutePrice;
	
	public int getMinutePrice() {
		return minutePrice;
	}

	public void setMinutePrice(int minutePrice) {
		this.minutePrice = minutePrice;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = ADDITIONAL_TASK_ID)
	private int additionalTaskID;
	
	public int getAditionalTaskID () {
		return additionalTaskID;
	}

	public void setAditionalTaskID(int additionalTaskID) {
		this.additionalTaskID = additionalTaskID;
	}	
	
	@DatabaseField(dataType = DataType.LONG, columnName = PILOT_TOUR_ID)
	private long pilotTourID;
	
	public long getPilotTourID() {
		return pilotTourID;
	}

	public void setPilotTourID(long pilotTourID) {
		this.pilotTourID = pilotTourID;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = QUALITY_FIELD)
	private int quality;
	
	public void setQuality(int quality){
		this.quality = quality;
	}

	public int getQuality(){
		return quality;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = CATALOG_FIELD)
	private int catalog;
	
	public void setCatalog(int catalog){
		this.catalog = catalog;
	}

	public int getCatalog(){
		return this.catalog;
	}
	
	@DatabaseField(dataType = DataType.LONG, columnName = EMPLOYMENT_ID_FIELD)
	private long employmentID;
	
	public long getEmploymentID() {
		return employmentID;
	}

	public void setEmploymentID(long employmentID) {
		this.employmentID = employmentID;
	}	
	
	@DatabaseField(dataType = DataType.LONG, columnName = TOUR_ID_FIELD)
	private long tourID;
	
	public long getTourID() {
		return tourID;
	}

	public void setTourID(long tourID) {
		this.tourID = tourID;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = PATIENT_ID_FIELD)
	private int patientID;
	
	public int getPatientID() {
		return patientID;
	}

	public void setPatientID(int patientID) {
		this.patientID = patientID;
	}

	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_ADDITIONAL_TASK)
	private boolean isAdditionaltask;
	
	public boolean getIsAdditionalTask() {
		return isAdditionaltask;
	}

	public void setIsAdditionalTask(boolean isAdditionaltask) {
		this.isAdditionaltask = isAdditionaltask;
	}

	public Task() {
		clear();
	}
	
	public Task(Patient patient, int employmentID, int tourID, boolean isFirst) {
		clear();
		setName(String.format("[Einsatz%s %s]", isFirst ? "beginn" : "ende", patient.FullClearName()));
		setPlanDate(DateUtils.getSynchronizedTime());
		setWorkerID(Option.Instance().getWorkerID());
		setPilotTourID(Option.Instance().getPilotTourID());
		setEmploymentID(employmentID);
		setTourID(tourID);
		setPatientID(patient.getId());
		setLeistungs(isFirst ? String.format("Anfang-%d-%d-%d", tourID, employmentID, 0) : String.format("Ende-%d-%d-%d", tourID, employmentID, 0));
		setState(eTaskState.Empty);
		setServerTime(false);
	}

	public Task(AdditionalTask additionalTask, Date planDate){
		clear();
		setAditionalTaskID(additionalTask.getId());
		setIsAdditionalTask(true);
		setName(additionalTask.getName());
		setPlanDate(planDate);
		setWorkerID(Option.Instance().getWorkerID());
		setEmploymentID(Option.Instance().getEmploymentID());
		PilotTour pilotTour = HelperFactory.getHelper().getPilotTourDAO().loadPilotTour(Option.Instance().getPilotTourID());
		setTourID(pilotTour.getTourID());
		Employment employment = HelperFactory.getHelper().getEmploymentDAO().load((int)getEmploymentID());
		setPatientID(employment.getPatientID());
		setQuality(additionalTask.getQuality());
		setQualityResult("");
		setMinutePrice(-1);
		SimpleDateFormat ddMMyyyyFormat = new SimpleDateFormat("ddMMyyyy");
		String lstStr = "";
		lstStr = employment.isFromMobile() ? employment.getDayPart() : HelperFactory.getHelper().getTaskDAO().getFirstSymbol(employment.getId()) + "";
		lstStr += additionalTask.getCatalogType();
		lstStr += "Z";
		if ( additionalTask.getCatalogType() < 10 ) lstStr += "0";
		lstStr += additionalTask.getCatalogType();
        if ( additionalTask.getId() < 100 ) lstStr += "0";
        if ( additionalTask.getId() < 10 ) lstStr += "0";
        lstStr += additionalTask.getId();
        lstStr += ddMMyyyyFormat.format(getPlanDate());
		setLeistungs(lstStr);
	}
	
	public Task(String initString) {
		clear();
		StringParser parsingString = new StringParser(initString);
		setQuality(0);
		setQualityResult("");
		setManualDate(DateUtils.EmptyDate);
		setRealDate(DateUtils.EmptyDate);
		parsingString.next(";");
		setWorkerID(Integer.parseInt(parsingString.next(";")));
		String strDate = parsingString.next(";");
		String strTime = parsingString.next(";");
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmm");
		try {
			setPlanDate(format.parse(strDate + strTime));
		} catch (Exception e) {
			e.printStackTrace();
		}
		setPatientID(Integer.parseInt(parsingString.next(";")));
		setLeistungs(parsingString.next(";"));
		String str = parsingString.next(";");
		if (str.contains("@")) {
			setMinutePrice(Integer.parseInt(str.substring(1)));
			str = parsingString.next(";");
		} else
			setMinutePrice(-1);
		if (str.startsWith("[")) {
			if (!str.contains("Einsatz")) {
				if (getLeistungs().contains("Anfang"))
					str = "[Einsatzbeginn " + str.substring(1);
				if (getLeistungs().contains("Ende"))
					str = "[Einsatzende " + str.substring(1);
			}
			setName(str);
			parsingString.next(";");
			setState(eTaskState.Empty);
			setPilotTourID(getPilotTourIDFromLeist());
		} else {
			setName(str);
			setState(eTaskState.Empty);
			setAditionalTaskID(getAddTaskIDFromLeist());
			setQuality(getQualityFromLeist());
			setCatalog(getCatalogFromLeist());
			AdditionalTask additionalTask = HelperFactory.getHelper().getAdditionalTaskDAO().load(getAddTaskIDFromLeist());
			setName(additionalTask != null ? additionalTask.getName() : "NO_NAME");
		}
		setTourID(Long.parseLong(parsingString.next(";")));
		setEmploymentID(Long.parseLong(parsingString.next("~")));
		setCheckSum(Long.parseLong(parsingString.next()));
	}
	
	@Override
	protected void clear() {
		super.clear();
		setQuality(0);
		setQualityResult("");
		setState(eTaskState.Empty);
		setPlanDate(DateUtils.EmptyDate);
		setLeistungs("");
		setTourID(0);
		setPatientID(EMPTY_ID);
		setIsAdditionalTask(false);
		setRealDate(DateUtils.EmptyDate);
		setManualDate(DateUtils.EmptyDate);
	}

	@Override
	public String forServer() {
		if (getWasSent())
			return new String();
		String strValue = new String(ServerCommandParser.TASK + ";");
		strValue += getId() + ";";
		strValue += getCheckSum();
		return strValue;
	}
	
    private int getAddTaskIDFromLeist() {
        return Integer.valueOf(leistungs.split("\\+")[3]);
    }
    
    private int getCatalogFromLeist() {
        return Integer.valueOf(leistungs.split("\\+")[1]);
    }
    
    private int getQualityFromLeist() {
    	String quality = leistungs.split("\\+")[4];
    	int retVal = 0;
    	try{
    		retVal = Integer.valueOf(quality);;
    	}
    	catch(Exception ex){
    		retVal = 0;
    	}
		return retVal;
    }
    
    public int getPilotTourIDFromLeist()
    {
    	int pilotTourID = 0;
    	String[] strArr = {""};
    	try {
    		strArr = leistungs.split("\\+");
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	if (strArr.length == 0 && !leistungs.contains("Anfang") && !leistungs.contains("Ende"))
    		return pilotTourID;
    	return Integer.parseInt(strArr[1]);
    }
    
	public String getDayPart(){
		return getName().substring(15, getName().length()-1);
	}
    
    public boolean isLastTask() {
    	return getLeistungs().contains("Ende");
    }
    
    public boolean isFirstTask() {
    	return getLeistungs().contains("Anfang");
    }
    
    public boolean isTaskEquals(Task inputTask){
    
    	if(inputTask != null && getEmploymentID() == inputTask.getEmploymentID()
				&& getPatientID() == inputTask.getPatientID()
				&& getTourID() == inputTask.getTourID()
				&& getLeistungs().equals(inputTask.getLeistungs())
				&& getCheckSum() == inputTask.getCheckSum())
    		return true;
    	return false;
    }

}
