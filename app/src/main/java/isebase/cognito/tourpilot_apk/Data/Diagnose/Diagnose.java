package isebase.cognito.tourpilot_apk.Data.Diagnose;

import com.j256.ormlite.table.DatabaseTable;

import isebase.cognito.tourpilot_apk.Connection.ServerCommandParser;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Utils.NCryptor;
import isebase.cognito.tourpilot_apk.Utils.StringParser;

@DatabaseTable(tableName = "Diagnoses")
public class Diagnose extends BaseObject {

	public Diagnose(){
		clear();
	}
	
    public Diagnose(String initString) {
    	clear();
		StringParser parsingString = new StringParser(initString);
		parsingString.next(";");
        setId(Integer.parseInt(parsingString.next(";")));
        setName(parsingString.next("~"));
		setCheckSum(Long.parseLong(parsingString.next()));
    }
 
    @Override
    public String forServer() {
        NCryptor ncryptor = new NCryptor();
        String strValue = new String(ServerCommandParser.DIAGNOSE + ";");
        strValue += ncryptor.LToNcode(getId()) + ";";
        strValue += ncryptor.LToNcode(getCheckSum());
        return strValue;
    }

}
