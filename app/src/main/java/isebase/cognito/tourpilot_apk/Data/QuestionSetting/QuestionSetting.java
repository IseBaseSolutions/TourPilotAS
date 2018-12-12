package isebase.cognito.tourpilot_apk.Data.QuestionSetting;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;
import isebase.cognito.tourpilot_apk.Utils.NCryptor;
import isebase.cognito.tourpilot_apk.Utils.StringParser;

import java.text.ParseException;
import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "QuestionSettings")
public class QuestionSetting extends BaseObject {
	
	public static final String PATIENT_ID_FIELD = "patient_id";
	public static final String WORKER_ID_FIELD = "worker_id";
	public static final String PILOT_TOUR_ID_FIELD = "pilot_tour_id";
	public static final String DATE_FIELD = "date";
	public static final String QUESTIONS_ID_FIELD = "question_ids_string";
	public static final String CATEGORIES_ID_FIELD = "category_ids_string";
	
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
	
	@DatabaseField(dataType = DataType.LONG, columnName = PILOT_TOUR_ID_FIELD)
	private long pilotTourID;
	
	public long getPilotTourID() {
		return pilotTourID;
	}
	
	public void setPilotTourID(long pilotTourID) {
		this.pilotTourID = pilotTourID;
	}
	
	@DatabaseField(dataType = DataType.DATE_LONG, columnName = DATE_FIELD)
	private Date date;
	
	public Date getDate() {
		return date == null ? date = DateUtils.EmptyDate : date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = QUESTIONS_ID_FIELD)
	private String questionsID;
	
	public String getQuestionIDsString() {
		return questionsID;
	}
	
	public void setQuestionIDsString(String questionsID) {
		this.questionsID = questionsID;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = CATEGORIES_ID_FIELD)
	private String categoriesID;
	
	public String getCategoryIDsString() {
		return categoriesID;
	}
	
	public void setCategoryIDsString(String categoryIDsString) {
		this.categoriesID = categoryIDsString;
	}
	
	private String extraCategoriesID;
	
	public String getExtraCategoryIDsString() {
		return extraCategoriesID;
	}

	public void setExtraCategoriesID(String extraCategoriesID) {
		this.extraCategoriesID = extraCategoriesID;
	}
	
	public QuestionSetting() {
		clear();
	}
	
    public QuestionSetting(String initString)
    {
    	clear();
    	StringParser parsingString = new StringParser(initString);
    	parsingString.next(";");
        setPatientID(Integer.parseInt(parsingString.next(";")));
        setWorkerID(Integer.parseInt(parsingString.next(";")));
        setId(Integer.parseInt(parsingString.next(";"))); //employmentID
        setPilotTourID(Integer.parseInt(parsingString.next(";")));
        try {
			setDate(DateUtils.DateFormat.parse(parsingString.next(";")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        setQuestionIDsString(parsingString.next(";"));
        setCategoryIDsString(parsingString.next("~"));
       	setCheckSum(Long.parseLong(parsingString.next()));
    }

    public String forServer()
    {
    	NCryptor ncryptor = new NCryptor();
    	String strValue = new String("X;");
        strValue += ncryptor.LToNcode(getId()) + ";";
        strValue += ncryptor.LToNcode(getCheckSum());
        return strValue;
    }
    
    @Override
    protected void clear() {
    	super.clear();
    	setExtraCategoriesID("");
    }
}
