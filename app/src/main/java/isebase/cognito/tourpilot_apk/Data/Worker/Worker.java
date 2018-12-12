package isebase.cognito.tourpilot_apk.Data.Worker;

import isebase.cognito.tourpilot_apk.Connection.ServerCommandParser;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;
import isebase.cognito.tourpilot_apk.Utils.NCryptor;
import isebase.cognito.tourpilot_apk.Utils.StringParser;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Workers")
public class Worker extends BaseObject {

	public static final String IS_USE_GPS_FIELD = "is_use_gps";
	public static final String ACTUAL_DATE_FIELD = "actualDate";
	public static final String IS_ACTIVE_FIELD = "is_active";
	public static final String PHONE_FIELD = "phone";
	public static final String PRIVATE_PHONE_FIELD = "private_phone";
	public static final String MOBILE_PHONE_FIELD = "mobile_phone";
	public static final String IS_SENDING_INFO_ALLOWED = "is_sending_info_allowed";

	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_USE_GPS_FIELD)
	private boolean isUseGPS;
	
	public boolean isUseGPS() {
		return isUseGPS;
	}

	public void setUseGPS(boolean isUseGPS) {
		this.isUseGPS = isUseGPS;
	}
	
	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_ACTIVE_FIELD)
	private boolean isActive;
	
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_SENDING_INFO_ALLOWED)
	private boolean isSendingInfoAllowed;	

	public boolean isSendingInfoAllowed() {
		return isSendingInfoAllowed;
	}

	public void setSendingInfoAllowed(boolean isSendingInfoAllowed) {
		this.isSendingInfoAllowed = isSendingInfoAllowed;
	}

	@DatabaseField(dataType = DataType.DATE_LONG, columnName = ACTUAL_DATE_FIELD)
	public Date actualDate;
	
	public Date getActualDate() {
		return actualDate == null ? actualDate = DateUtils.EmptyDate : actualDate;
	}

	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = PHONE_FIELD)
	private String workPhone;
	
	public String getWorkPhone() {
		return workPhone;
	}
	
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = PRIVATE_PHONE_FIELD)
	private String privatePhone;
	
	public String getPrivatePhone() {
		return privatePhone;
	}
	
	public void setPrivatePhone(String privatePhone) {
		this.privatePhone = privatePhone;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = MOBILE_PHONE_FIELD)
	private String mobilePhone;
	
	public String getMobilePhone() {
		return mobilePhone;
	}
	
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	public Worker() {
		clear();
	}
	
    public Worker(String strInitString)
    {
        this(strInitString, DateUtils.getSynchronizedTime());
    }

    public Worker(String strInitString, Date _actualDate) {
    	clear();
        StringParser initString = new StringParser(strInitString);
        initString.next(";");
        setId(Integer.parseInt(initString.next(";")));
        setName(initString.next(";"));
        setUseGPS(Integer.parseInt(initString.next(";")) == 1 ? true : false);
        setActive(Integer.parseInt(initString.next(";")) == 1 ? true : false);  
        setPrivatePhone(initString.next(";"));
        setWorkPhone(initString.next(";"));
        setMobilePhone(initString.next(";"));
        setSendingInfoAllowed(Integer.parseInt(initString.next("~")) == 1 ? true : false);
        setCheckSum(Long.parseLong(initString.next()));
        setActualDate(_actualDate);
    }
	
	@Override
	protected void clear() {
		super.clear();
		setUseGPS(false);
    	setActualDate(new Date());
        setActive(false);
        setSendingInfoAllowed(false);
        setWorkPhone("");
        setMobilePhone("");
        setPrivatePhone("");
	}
	
    @Override
    public String toString() {
    	return getName();
    }
    
    @Override
    public String forServer()
    {
        NCryptor nCryptor = new NCryptor();
        String strValue = new String(ServerCommandParser.WORKER + ";");
        strValue += nCryptor.LToNcode(getId()) + ";";
        strValue += nCryptor.LToNcode(getCheckSum());
        return strValue;
    }
    
    public boolean checkPIN(String strPin){
    	if (strPin.equals(""))
			return false;
		Long pin = Long.parseLong(strPin);
		long num = 0;
		int numArray[] = new int[] { 1, 3, 5, 7, 13, 0x11 };
		try {
			byte byteText[] = getName().getBytes("latin1");
			for (int i = 0; i < byteText.length; i++)
				num += (byteText[i]) * numArray[i % 6];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (num == pin || Option.Instance().testPin.equals(strPin));
    }

}
