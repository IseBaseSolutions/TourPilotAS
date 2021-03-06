package isebase.cognito.tourpilot_apk.Data.AdditionalWork;

import com.j256.ormlite.table.DatabaseTable;

import isebase.cognito.tourpilot_apk.Connection.ServerCommandParser;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Utils.NCryptor;
import isebase.cognito.tourpilot_apk.Utils.StringParser;

@DatabaseTable(tableName = "AdditionalWorks")
public class AdditionalWork extends BaseObject {

	public AdditionalWork(){
		clear();
	}
	
	public AdditionalWork(String initString) {
		clear();
		StringParser parsingString = new StringParser(initString);		
		parsingString.next(";");
        setId(Integer.parseInt(parsingString.next(";")));
        setName(parsingString.next("~"));
        setCheckSum(Long.parseLong(parsingString.next()));
	}
	
	@Override
    public String forServer(){
    	NCryptor ncryptor = new NCryptor();
        String strValue = new String(ServerCommandParser.ADDITIONAL_WORK + ";");
        strValue += getId() + ";";
        strValue += ncryptor.LToNcode(getCheckSum());
        return strValue;
    }

}
