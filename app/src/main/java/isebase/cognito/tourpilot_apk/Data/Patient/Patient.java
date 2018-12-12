package isebase.cognito.tourpilot_apk.Data.Patient;

import isebase.cognito.tourpilot_apk.Connection.ServerCommandParser;
import isebase.cognito.tourpilot_apk.Data.Address.IAddressable;
import isebase.cognito.tourpilot_apk.Data.Address.Address;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Utils.NCryptor;
import isebase.cognito.tourpilot_apk.Utils.StringParser;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Patient.TABLE_NAME)
public class Patient extends BaseObject implements IAddressable {

	public static final String TABLE_NAME = "Patients";

	public static final String IS_ADDITIONAL_FIELD = "is_additional";
	public static final String SURNAME_FIELD = "surname";
	public static final String ADDRESS_ID_FIELD = "address_id";
		
	public static final String SEX_FIELD = "sex";
	public static final String DOCTORS_ID_FIELD = "doctor_ids";
	public static final String RELATIVES_ID_FIELD = "relative_ids";

	public static final String ADDITIONAL_ADDRESS_ID_FIELD = "additional_address_ids";
	
	public static final String CATALOG_KK_TYPE_FIELD = "catalog_kk_type";
	public static final String CATALOG_PK_TYPE_FIELD = "catalog_pk_type";
	public static final String CATALOG_SA_TYPE_FIELD = "catalog_sa_type";
	public static final String CATALOG_PR_TYPE_FIELD = "catalog_pr_type";

	public static final int ADDITIONAL_WORK_CODE = 999900;
	
	public static final String BIRTH_DATE_FIELD = "birth_date";
	
	public Address address;
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = ADDRESS_ID_FIELD)
	private int addressID;
	
	public int getAddressID() {
		return addressID;
	}

	public void setAddressID(int addressID) {
		this.addressID = addressID;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = SURNAME_FIELD)
	private String surname;
	
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = SEX_FIELD)
	private String sex;

	public String getSex() {
		return sex;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = DOCTORS_ID_FIELD)
	private String doctorIDs;
	
	public String getStrDoctorsIDs() {
		return doctorIDs;
	}

	public void setStrDoctorsIDs(String doctorIDs) {
		this.doctorIDs = doctorIDs;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = RELATIVES_ID_FIELD)
	private String relativeIDs;
	
	public String getStrRelativeIDs() {
		return relativeIDs;
	}

	public void setStrRelativeIDs(String relativeIDs) {
		this.relativeIDs = relativeIDs;
	}
	
	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_ADDITIONAL_FIELD)
	private boolean isAdditional;
	
	public boolean getIsAdditional() {
		return isAdditional;
	}

	public void setIsAdditional(boolean isAdditional) {
		this.isAdditional = isAdditional;
	}
	@DatabaseField(dataType = DataType.INTEGER, columnName = CATALOG_KK_TYPE_FIELD)
	private int btyp_kk;
	
	public int getKK() {
		return btyp_kk;
	}

	public void setKK(int btyp_kk) {
		this.btyp_kk = btyp_kk;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = CATALOG_PK_TYPE_FIELD)
	private int btyp_pk;
	
	public int getPK() {
		return btyp_pk;
	}

	public void setPK(int btyp_pk) {
		this.btyp_pk = btyp_pk;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = CATALOG_PR_TYPE_FIELD)
	private int btyp_pr;
	
	public int getPR() {
		return btyp_pr;
	}

	public void setPR(int btyp_pr) {
		this.btyp_pr = btyp_pr;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = CATALOG_SA_TYPE_FIELD)
	private int btyp_sa;

	public int getSA() {
		return btyp_sa;
	}

	public void setSA(int btyp_sa) {
		this.btyp_sa = btyp_sa;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = BIRTH_DATE_FIELD)
	private String birthdate;	
	
	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	@DatabaseField(dataType = DataType.STRING, columnName = ADDITIONAL_ADDRESS_ID_FIELD)
	private String additionalAddressIDs;

	public String getAdditionalAddressIDs(){
		return additionalAddressIDs;
	}

	public void setAdditionalAddressIDs(String additionalAddressIDs){
		this.additionalAddressIDs = additionalAddressIDs;
	}

	public boolean isAdditionalWork(){
		return getId() > ADDITIONAL_WORK_CODE;
	}
	
	public boolean isMan() {
		return getSex().contains("Herr");
	}
	
	public Patient() {
		clear();
	}
	
	public Patient(String[] resultArray) {
		clear();
		setSurname(resultArray[0]);
		setBirthdate(resultArray[1]);
		setSex(resultArray[2]);
		setStrRelativeIDs(resultArray[3]);
		setStrDoctorsIDs(resultArray[4]);
		setSA(Integer.parseInt(resultArray[5]));
		setPR(Integer.parseInt(resultArray[6]));
		setIsAdditional(resultArray[7].equals("1"));
		setPK(Integer.parseInt(resultArray[8]));
		setKK(Integer.parseInt(resultArray[9]));
		setAddressID(Integer.parseInt(resultArray[10]));
		setName(resultArray[11]);
		setId(Integer.parseInt(resultArray[12]));
		setCheckSum(Long.parseLong(resultArray[13]));
		setServerTime(resultArray[14].equals("1"));
		setWasSent(resultArray[15].equals("1"));
		setAdditionalAddressIDs(resultArray[16]);
	}
	
	public Patient(String initString) {
		clear();
		address = new Address();
		NCryptor ncryptor = new NCryptor();
		StringParser parsingString = new StringParser(initString);
		parsingString.next(";");
		setId(Integer.parseInt(parsingString.next(";")));
		setSurname(parsingString.next(";"));
		setName(parsingString.next(";"));
		String sexStr = parsingString.next(";");
		if(sexStr.length() > 0)
			setSex(sexStr.substring(1, sexStr.length()));
		else
			setSex("");
		address.setStreet(parsingString.next(";"));
		address.setZip(parsingString.next(";"));
		address.setCity(parsingString.next(";"));
		address.setPhone(parsingString.next(";"));
		address.setPrivatePhone(parsingString.next(";"));
		address.setMobilePhone(parsingString.next(";"));
		setBirthdate(parsingString.next(";"));
		
		setKK(parseInt(parsingString.next("+")));
		setPK(parseInt(parsingString.next("+")));
		setSA(parseInt(parsingString.next("+")));
		setPR(parseInt(parsingString.next(";")));
				
		setStrDoctorsIDs(parsingString.next(";"));
		setStrRelativeIDs(parsingString.next(";"));

		setAdditionalAddressIDs(parsingString.next("~"));

		setCheckSum(ncryptor.NcodeToL(parsingString.next()));
	}

	@Override
	public String toString() {
		return String.format("%s\n%s\n%s,%s\n", getFullName(), address.getStreet(), address.getZip(), address.getCity());
	}
	
	public String getFullName() {
		return String.format("%s %s", getSurname(), getName());
	}

	@Override
	public String forServer() {
		if (getIsAdditional())
			return "";
		NCryptor ncryptor = new NCryptor();
		String strValue = new String(ServerCommandParser.PATIENT + ";");
		strValue += ncryptor.LToNcode(getId()) + ";";
		strValue += ncryptor.LToNcode(getCheckSum());
		return strValue;
	}

	private int parseInt(String strVal){
		if (strVal.equals(""))
			return EMPTY_ID;
		return Integer.parseInt(strVal);	
	}
	
    public String FullClearName() 
    {	
    	String arr[] = getName().split(" ");
    	String fullClearName = new String();
    	for (String str : arr)
    		if (!str.contains("("))
    			fullClearName += fullClearName.equals(new String()) ? str : " " + str;
    	return fullClearName; 
    }

	@Override
	protected void clear() {
		super.clear();
		address = new Address();
		setIsAdditional(false);
		setSurname("");
		setAddressID(EMPTY_ID);
		setSex("");
		setStrDoctorsIDs("");
		setStrRelativeIDs("");
		setKK(EMPTY_ID);
		setPK(EMPTY_ID);
		setSA(EMPTY_ID);
		setPR(EMPTY_ID);
		setBirthdate("");
		setAdditionalAddressIDs("");
	}

	@Override
	public Address getAddress() {
		return address;
	}

	public Boolean isAdditionalAddressAssigned(){
		return additionalAddressIDs != "";
	}
}
