package isebase.cognito.tourpilot.Data.AdditionalTask;

import isebase.cognito.tourpilot.Connection.ServerCommandParser;
import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot.Utils.NCryptor;
import isebase.cognito.tourpilot.Utils.StringParser;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "AdditionalTasks")
public class AdditionalTask extends BaseObject {

	public static final int WEIGHT = 1;
	public static final int DETECT_RESPIRATION = 2;
	public static final int BALANCE = 3;
	public static final int BLUTZUCKER = 4;
	public static final int TEMPERATURE = 7;
	
	public static final int BLUTDRUCK = 5;
	
	public static final int PULS = 6;
	
	public static final String CATALOG_TYPE_FIELD = "catalog_type";
	public static final String QUALITY_FIELD = "quality"; 
    
	@DatabaseField(dataType = DataType.INTEGER, columnName = CATALOG_TYPE_FIELD)
    private int catalogType;
	
    public int getCatalogType() {
    	return catalogType;
    }
    
    public void setCatalogType(int catalogType) {
    	this.catalogType = catalogType;  
    }

    @DatabaseField(dataType = DataType.INTEGER, columnName = QUALITY_FIELD)
    private int quality;
    
    public int getQuality() {
    	return quality;
    }

    public void setQuality(int quality) {
    	this.quality = quality;
    }
    
    public AdditionalTask(){
    	clear();
    }
    
	public AdditionalTask(String initString) {
		clear();
		StringParser parsingString = new StringParser(initString);
		parsingString.next(";");
		setCatalogType(Integer.parseInt(parsingString.next(";")));
        setId(Integer.parseInt(parsingString.next(";")));
        setName(parsingString.next(";"));
        setQuality(Integer.parseInt(parsingString.next("~")));
        setCheckSum(Long.parseLong(parsingString.next()));
	}
	
	@Override
    public String forServer()
    {
    	NCryptor nCryptor = new NCryptor();
        String strValue = new String(ServerCommandParser.ADDITIONAL_TASK_Z + ";");
        strValue += catalogType + ";" + getId() + ";";
        strValue += nCryptor.LToNcode(getCheckSum());
        return strValue;
    }
    
	@Override
	public String toString() {
		return getName();
	}
	
    @Override
    protected void clear() {
    	super.clear();
    	setCatalogType(EMPTY_ID);
    	setQuality(EMPTY_ID);
    }

}
