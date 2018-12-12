package isebase.cognito.tourpilot_apk.Data.Link;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Utils.NCryptor;
import isebase.cognito.tourpilot_apk.Utils.StringParser;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Links")
public class Link extends BaseObject {

	public static final String QUESTION_ID_FIELD = "question_id";
	public static final String CATEGORY_ID_FIELD = "category_id";
	
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

	public Link() {
		clear();
	}
	
    public Link(String initString)
    {
    	clear();
    	StringParser parsingString = new StringParser(initString);
    	parsingString.next(";");
        setQuestionID(Integer.parseInt(parsingString.next(";")));
        setCategoryID(Integer.parseInt(parsingString.next("~")));
        setCheckSum(Integer.parseInt(parsingString.next()));
    }

    public String forServer()
    {
    	NCryptor ncryptor = new NCryptor();
    	String strValue = new String("J;");
        strValue += getQuestionID() + "~" + getCategoryID() + ";";
        strValue += ncryptor.LToNcode(getCheckSum());
        return strValue;
    }
    
}
