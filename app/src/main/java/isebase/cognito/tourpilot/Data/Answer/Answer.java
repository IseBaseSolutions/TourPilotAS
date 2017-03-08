package isebase.cognito.tourpilot.Data.Answer;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.Data.Patient.Patient;
import isebase.cognito.tourpilot.Data.Question.IQuestionable;
import isebase.cognito.tourpilot.Data.Question.Question;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Answers")
public class Answer extends BaseObject implements IQuestionable {

	public static final String PATIENT_ID_FIELD = "patient_id";
	public static final String QUESTION_ID_FIELD = "question_id";
	public static final String CATEGORY_ID_FIELD = "category_id";
	public static final String EMPLOYMENT_ID_FIELD = "employment_id";
	public static final String WORKER_ID_FIELD = "worker_id";
	public static final String PILOT_TOUR_ID_FIELD = "pilot_tour_id";
	public static final String TYPE_FIELD = "type";
	public static final String ANSWER_ID_FIELD = "answer_id";
	public static final String ANSWER_KEY_FIELD = "answer_id_key";
	public static final String ADDITIONAL_INFO_FIELD = "add_info";

	@DatabaseField(dataType = DataType.INTEGER, columnName = PATIENT_ID_FIELD)
	private int patientID;
	
	public int getPatientID() {
		return patientID;
	}

	public void setPatientID(int patientID) {
		this.patientID = patientID;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = QUESTION_ID_FIELD)
	private int questionID;
	
	public int getQuestionID() {
		return questionID;
	}
	
	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = CATEGORY_ID_FIELD)
	private int categoryID;
	
	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}
	
	@DatabaseField(dataType = DataType.LONG, columnName = EMPLOYMENT_ID_FIELD)
	private long employmentID;
	
	public long getEmploymentID() {
		return employmentID;
	}

	public void setEmploymentID(long emplID) {
		this.employmentID = emplID;
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
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = TYPE_FIELD)
	private int type;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = ANSWER_ID_FIELD)
	private int answerID;
	
	public int getAnswerID() {
		return answerID;
	}

	public void setAnswerID(int answerID) {
		this.answerID = answerID;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = ANSWER_KEY_FIELD)
	private String answerKey;	
	
	public String getAnswerKey() {
		return answerKey;
	}

	public void setAnswerKey(String answerKey) {
		this.answerKey = answerKey;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = ADDITIONAL_INFO_FIELD)
	private String additionalInfo;
	
	public String getAddInfo() {
		return additionalInfo;
	}

	public void setAddInfo(String addInfo) {
		this.additionalInfo = addInfo;
	}
	
	public Answer() {
		clear();
	}
	
	public Answer(int patientID, int questionID, String questionName, int answerID, int categoryID, String answerKey, int type) {
		clear();
		setPatientID(patientID);
		setWorkerID(Option.Instance().getWorkerID());
		setEmploymentID(Option.Instance().getEmploymentID());
		setPilotTourID(Option.Instance().getPilotTourID());
		setAnswerID(answerID);
		setType(type);
		setQuestionID(questionID);
		setCategoryID(categoryID);
		setName(questionName);
		setAnswerKey(answerKey);
	}
	
	public Answer(Patient patient, Question question, int answerID, int categoryID, int type) {
		clear();
		setPatientID(patient.getId());
		setWorkerID(Option.Instance().getWorkerID());
		setEmploymentID(Option.Instance().getEmploymentID());
		setPilotTourID(Option.Instance().getPilotTourID());
		setAnswerID(answerID);
		setType(type);
		setQuestionID(question.getId());
		setCategoryID(categoryID);
		setName(question.getNameWithKeyWords(patient));
	}
	
	public Answer(String[] resultArray, String[] columNames) {
		for (int i = 0; i < resultArray.length; i++) {
			if (columNames[i].equals(PATIENT_ID_FIELD))
				setPatientID(Integer.parseInt(resultArray[i]));			
			else if (columNames[i].equals(QUESTION_ID_FIELD))
				setQuestionID(Integer.parseInt(resultArray[i]));
			else if (columNames[i].equals(CATEGORY_ID_FIELD))
				setCategoryID(Integer.parseInt(resultArray[i]));
			else if (columNames[i].equals(EMPLOYMENT_ID_FIELD))
				setEmploymentID(Integer.parseInt(resultArray[i]));
			else if (columNames[i].equals(PILOT_TOUR_ID_FIELD))
				setPilotTourID(Integer.parseInt(resultArray[i]));
			else if (columNames[i].equals(WORKER_ID_FIELD))
				setWorkerID(Integer.parseInt(resultArray[i]));
			else if (columNames[i].equals(TYPE_FIELD))
				setType(Integer.parseInt(resultArray[i]));
			else if (columNames[i].equals(ANSWER_ID_FIELD))
				setAnswerID(Integer.parseInt(resultArray[i]));
			else if (columNames[i].equals(ANSWER_KEY_FIELD))
				setAnswerKey(resultArray[i]);
			else if (columNames[i].equals(ADDITIONAL_INFO_FIELD))
				setAddInfo(resultArray[i]);
			else if (columNames[i].equals(ID_FIELD))
				setId(Integer.parseInt(resultArray[i]));
			else if (columNames[i].equals(NAME_FIELD))
				setName(resultArray[i]);
			else if (columNames[i].equals(CHECK_SUM_FIELD))
				setCheckSum(Integer.parseInt(resultArray[i]));
			else if (columNames[i].equals(WAS_SENT_FIELD))
				setWasSent(Integer.parseInt(resultArray[i]) == 1);
			else if (columNames[i].equals(IS_SERVER_TIME_FIELD))
				setServerTime(Integer.parseInt(resultArray[i]) == 1);
		}
	}
	
	public int getBradenAnswer() {
		return Integer.parseInt(getAnswerKey().split("#")[0]);
	}
	
	public int getBradenLevel() {
		return getAnswerKey().equals("") ? -1 : Integer.parseInt(getAnswerKey().split("%")[1]);
	}
	
	public int[] getBradenCheckedIndexes() {
		String strArray[] = getAnswerKey().split("%")[0].split("#")[1].split("/");
		int intArray[] = new int[strArray.length];
		for (int i = 0; i < strArray.length; i++)
			intArray[i] = Integer.parseInt(strArray[i]);
		return intArray;
	}
	
	@Override
	public String forServer() {
        String strValue = new String("N;");
        strValue += getPatientID() + ";";
        strValue += getWorkerID() + ";";
        strValue += getEmploymentID() + ";";
        strValue += getPilotTourID() + ";";
        strValue += getType() + ";";
        strValue += getQuestionID() + ";";
        strValue += getAnswerID() + ";";
        strValue += getAnswerKey() + ";";
        strValue += getAddInfo() + ";";
        return strValue;
	}
	
	@Override
	protected void clear() {
		super.clear();
		setAddInfo("");
		setAnswerID(-1);
		setAnswerKey("");
	}

	@Override
	public String name() {
		return getName();
	}
	
}
