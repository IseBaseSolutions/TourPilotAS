package isebase.cognito.tourpilot_apk.Data.PatientAdditionalAddress;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import isebase.cognito.tourpilot_apk.Connection.ServerCommandParser;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;
import isebase.cognito.tourpilot_apk.Utils.NCryptor;
import isebase.cognito.tourpilot_apk.Utils.StringParser;

/**
 * Created by Kostya on 15.02.2018.
 */

@DatabaseTable(tableName = PatientAdditionalAddress.TABLE_NAME)
public class PatientAdditionalAddress extends BaseObject implements Comparable<PatientAdditionalAddress> {

	public static final String TABLE_NAME = "PatientAdditionalAddress";

	//region Fields

	public static final String PATIENT_ID_FIELD = "patient_id";
	public static final String ART_NAME_FIELD = "art_name";
	public static final String STREET_FIELD = "street";
	public static final String ZIP_FIELD = "zip";
	public static final String CITY_FIELD = "city";
	public static final String PHONE_FIELD = "phone";
	public static final String MOBILE_PHONE_FIELD = "mobile_phone";
	public static final String EMAIL_FIELD = "email";
	public static final String INFO_FIELD = "info";
	public static final String FAX_FIELD = "fax";

	//endregion

	//region Properties

	//region Patient ID
	@DatabaseField(dataType = DataType.INTEGER, columnName = PATIENT_ID_FIELD)
	private int patientID;

	public int getPatientID() {
		return patientID;
	}

	public void setPatientID(int patientID) {
		this.patientID = patientID;
	}
	//endregion

	//region Street
	@DatabaseField(dataType = DataType.STRING, columnName = STREET_FIELD)
	private String street;
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	//endregion

	//region ZIP
	@DatabaseField(dataType = DataType.STRING, columnName = ZIP_FIELD)
	private String zip;
	
	public String getZip() {
		return zip;
	}
	
	public void setZip(String zip) {
		this.zip = zip;
	}
	//endregion

	//region City
	@DatabaseField(dataType = DataType.STRING, columnName = CITY_FIELD)
	private String city;
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	//endregion

	//region Phone
	@DatabaseField(dataType = DataType.STRING, columnName = PHONE_FIELD)
	private String phone;
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	//endregion

	//region Mobile phone
	@DatabaseField(dataType = DataType.STRING, columnName = MOBILE_PHONE_FIELD)
	private String mobilePhone;
	
	public String getMobilePhone() {
		return mobilePhone;
	}
	
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	//endregion

	//region Art
	@DatabaseField(dataType = DataType.STRING, columnName = ART_NAME_FIELD)
	private String artName;

	public String getArtName(){
		return artName;
	}

	public void setArtName(String value){
		artName = value;
	}
	//endregion

	//region Email
	@DatabaseField(dataType = DataType.STRING, columnName = EMAIL_FIELD)
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	//endregion

	//region Info
	@DatabaseField(dataType = DataType.STRING, columnName = INFO_FIELD)
	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	//endregion

	//region Fax
	@DatabaseField(dataType = DataType.STRING, columnName = FAX_FIELD)
	private String fax;

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	//endregion

	//endregion

	public PatientAdditionalAddress(){
		clear();
	}

	public PatientAdditionalAddress(String initString) {
		clear();
		StringParser parsingString = new StringParser(initString);
		parsingString.next(";");
		setId(Integer.parseInt(parsingString.next(";")));
		setPatientID(Integer.parseInt(parsingString.next(";")));
		setArtName(parsingString.next(";"));
		setName(parsingString.next(";"));
        setCity(parsingString.next(";"));
		setStreet(parsingString.next(";"));
		setZip(parsingString.next(";"));
		setPhone(parsingString.next(";"));
		setMobilePhone(parsingString.next(";"));
		setFax(parsingString.next(";"));
		setEmail(parsingString.next(";"));
		setInfo(parsingString.next("~"));
		setCheckSum(new NCryptor().NcodeToL(parsingString.next()));
	}

	@Override
	protected void clear(){
		super.clear();
		setId(0);//NewBaseObject.EMPTY_ID);
		setPatientID(0);
		setStreet("");
		setCity("");
		setPhone("");
		setMobilePhone("");
		setZip("");
		setArtName("");
		setEmail("");
		setInfo("");
		setFax("");
	}

	@Override
	public String forServer() {
		NCryptor ncryptor = new NCryptor();
		String strValue = new String(ServerCommandParser.PATIENT_ADDITIONAL_ADDRESS + ";");
		strValue += ncryptor.LToNcode(getId()) + ";";
		strValue += ncryptor.LToNcode(getCheckSum());
		return strValue;
	}
	
	public String getRealPhone()
	{
		return getOnlyNumbers(phone);
	}

	public String getRealMobilePhone()
	{
		return getOnlyNumbers(mobilePhone);
	}
	
	private String getOnlyNumbers(String val){
		String retVal = "";
		for(char c : val.toCharArray())
			if(Character.isDigit(c))
				retVal += c;
		return retVal;
	}
	
	public String getAddressData()	{
		String address = "";
		if(getStreet().length() > 0)
			address += getStreet();
		if(getZip().length() > 0)
			address += ", " + getZip();
		if(getCity().length() > 0)
			address += ", " + getCity();
		if(address.length() == 0 )
			address = StaticResources.getBaseContext().getString(R.string.err_no_address);
		return address;
	}

	@Override
	public int compareTo(@NonNull PatientAdditionalAddress another) {
		int result = getArtName().compareToIgnoreCase(another.getArtName());
		if (result == 0) {
			result |= getAddressData().compareToIgnoreCase(another.getAddressData());
		}
		return result;
	}
}
