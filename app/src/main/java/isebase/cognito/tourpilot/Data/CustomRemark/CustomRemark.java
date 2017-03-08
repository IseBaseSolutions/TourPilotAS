package isebase.cognito.tourpilot.Data.CustomRemark;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot.Utils.NCryptor;
import isebase.cognito.tourpilot.Utils.StringParser;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "CustomRemarks")
public class CustomRemark extends BaseObject {

public static final String POSITION_NUMBER_FIELD = "pos_number";
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = POSITION_NUMBER_FIELD)
	int posNumber;
	
	public int getPosNumber() {
		return posNumber;
	}

	public void setPosNumber(int posNumber) {
		this.posNumber = posNumber;
	}
	
	boolean textInput;

	
	public CustomRemark(){
		clear();
	}
	
	public CustomRemark(int id, String name, int posNumber){
		clear();
		setId(id);
		setName(name);
		setPosNumber(posNumber);
	}
	
	public CustomRemark(String strInitString) {
		clear();
		StringParser InitString = new StringParser(strInitString);
		InitString.next(";");
		setId(Integer.parseInt(InitString.next(";")));
		setName(InitString.next(";"));
		setPosNumber(Integer.parseInt(InitString.next("~")));
		setCheckSum(Integer.parseInt(InitString.next()));
	}
    
    public String toString(){
        return getName();
    }
    
	@Override
	public String forServer() {       
    	NCryptor ncryptor = new NCryptor();
    	String strValue = new String("#;");
        strValue += ncryptor.LToNcode(getId()) + ";";
        strValue += ncryptor.LToNcode(getCheckSum());
        return strValue;
	}

	@Override
	public void clear(){
		super.clear();
		setPosNumber(EMPTY_ID);
	}

}
