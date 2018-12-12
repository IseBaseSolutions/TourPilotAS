package isebase.cognito.tourpilot_apk.Data.Address;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Address")
public class Address {

	public static final String ID_FIELD = "_id";
	public static final String STREET_FIELD = "street";
	public static final String ZIP_FIELD = "zip";
	public static final String CITY_FIELD = "city";
	public static final String PHONE_FIELD = "phone";
	public static final String PRIVATE_PHONE_FIELD = "private_phone";
	public static final String MOBILE_PHONE_FIELD = "mobile_phone";
	
	@DatabaseField(generatedId = true, columnName = ID_FIELD)
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@DatabaseField(dataType = DataType.STRING, columnName = STREET_FIELD)
	private String street;
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = ZIP_FIELD)
	private String zip;
	
	public String getZip() {
		return zip;
	}
	
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = CITY_FIELD)
	private String city;
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = PHONE_FIELD)
	private String phone;
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
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
	
	public Address(){
		clear();
	}
	
	private void clear(){
		setId(0);//NewBaseObject.EMPTY_ID);
		setStreet("");
		setCity("");
		setPhone("");
		setPrivatePhone("");
		setMobilePhone("");
		setZip("");
	}
	
	public String getRealPhone()
	{
		return getOnlyNumbers(phone);
	}
	
	public String getRealPrivatePhone()
	{
		return getOnlyNumbers(privatePhone);
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
	
	public String getAddressData()
	{
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
	
}
