package isebase.cognito.tourpilot_apk.Data.Question;

import isebase.cognito.tourpilot_apk.Connection.ServerCommandParser;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Data.Patient.Patient;
import isebase.cognito.tourpilot_apk.Utils.NCryptor;
import isebase.cognito.tourpilot_apk.Utils.StringParser;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Questions")
public class Question extends BaseObject implements IQuestionable {

	
	public static final String OWNER_IDS_FIELD = "owner_ids_string";
	public static final String KEY_ANSWER_FIELD = "key_answer";
	
	@DatabaseField(dataType = DataType.STRING, columnName = OWNER_IDS_FIELD)
	private String ownerIDsString;
	
	public String getOwnerIDsString() {
		return ownerIDsString;
	}

	public void setOwnerIDsString(String ownerIDsString) {
		this.ownerIDsString = ownerIDsString;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = KEY_ANSWER_FIELD)
	private int keyAnswer;
	
	public int getKeyAnswer() {
		return keyAnswer;
	}

	public void setKeyAnswer(int keyAnswer) {
		this.keyAnswer = keyAnswer;
	}
	
	public String[] getOwnerIDs() {
		return ownerIDsString.split(",");
	}


	
	public boolean isSubQuestion() {
		return !getOwnerIDsString().equals("-1");
	}

	public Question() {
		clear();
	}
	
    public Question(String initString)
    {
    	clear();    	
    	StringParser parsingString = new StringParser(initString);
    	parsingString.next(";");
        setId(Integer.parseInt(parsingString.next(";")));
        setName(parsingString.next(";"));
        setOwnerIDsString(parsingString.next(";"));
        setKeyAnswer(Integer.parseInt(parsingString.next("~")));
        setCheckSum(Long.parseLong(parsingString.next()));
    }
    
    public Question(String[] resultArray, String[] columNames)
    {
    	clear();
		for (int i = 0; i < resultArray.length; i++) {
			if (columNames[i].equals(ID_FIELD))
		    	setId(Integer.parseInt(resultArray[i]));
			else if (columNames[i].equals(NAME_FIELD))
		    	setName(resultArray[i]);
			else if (columNames[i].equals(CHECK_SUM_FIELD))
		    	setCheckSum(Long.parseLong(resultArray[i]));
			else if (columNames[i].equals(WAS_SENT_FIELD))
		    	setWasSent(resultArray[i].equals("1"));
			else if (columNames[i].equals(IS_SERVER_TIME_FIELD))
		    	setServerTime(resultArray[i].equals("1"));
			else if (columNames[i].equals(OWNER_IDS_FIELD))
		    	setOwnerIDsString(resultArray[i]);
			else if (columNames[i].equals(KEY_ANSWER_FIELD))
		    	setKeyAnswer(Integer.parseInt(resultArray[i]));
		}
    }
    
    public String forServer()
    {
    	NCryptor ncryptor = new NCryptor();
    	String strValue = new String(ServerCommandParser.QUESTION + ";");
        strValue += ncryptor.LToNcode(getId()) + ";";
        strValue += ncryptor.LToNcode(getCheckSum());
        return strValue;
    }
    
    public String getNameWithKeyWords(Patient patient) {
    	String name = "";
    	String[] arr = getName().split(" ");
    	for (int i = 0; i < arr.length; i++)
    	{
    		name += (name.equals("") ? "" : " ");
    		if (arr[i].contains("#"))
    		{
    			if (arr[i].contains("@"))
    				name += (patient.getSex().contains("Herr") ? arr[i].split("@")[0].replace("#", "") : arr[i].split("@")[1].replace("#", ""));
    			else
    				name += (arr[i].contains("pat.zuname") ? patient.getSurname() : patient.getName().split(" ")[0]);
    		}
    		else
    			name += arr[i];   
    	}
    	return name;
    }

	@Override
	public String name() {
		return getName();
	}


}
