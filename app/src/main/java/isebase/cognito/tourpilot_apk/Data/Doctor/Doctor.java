package isebase.cognito.tourpilot_apk.Data.Doctor;

import isebase.cognito.tourpilot_apk.Connection.ServerCommandParser;
import isebase.cognito.tourpilot_apk.Data.Address.IAddressable;
import isebase.cognito.tourpilot_apk.Data.Address.Address;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Utils.NCryptor;
import isebase.cognito.tourpilot_apk.Utils.StringParser;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Doctors")
public class Doctor extends BaseObject implements IAddressable {


	public static final String SURNAME_FIELD = "surname";
	public static final String ADDRESS_ID_FIELD = "address_id";
	public static final String SPECIALITY_FIELD = "speciality";
	public static final String NOTE_FIELD = "note";

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
	
	@DatabaseField(dataType = DataType.STRING, columnName = SPECIALITY_FIELD)
	private String speciality;
	
	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = NOTE_FIELD)
	private String note;
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public Doctor() {
		clear();
	}

	public Doctor(String initString) {
		clear();
		address = new Address();
		StringParser parsingString = new StringParser(initString);
		parsingString.next(";");
		setId(Integer.parseInt(parsingString.next(";")));
		setSurname(parsingString.next(";"));
		setName(parsingString.next(";"));
		address.setStreet(parsingString.next(";"));
		address.setZip(parsingString.next(";"));
		address.setCity(parsingString.next(";"));
		address.setPhone(parsingString.next(";"));
		address.setPrivatePhone(parsingString.next(";"));
		address.setMobilePhone(parsingString.next(";"));
		setSpeciality(parsingString.next(";"));
		setNote(parsingString.next("~"));
		setCheckSum(Long.parseLong(parsingString.next()));
	}

	public String getFullName() {
		return String.format("%s %s", getSurname(), getName());
	}

	@Override
	public String toString() {
		return String.format("%s\n%s\n%s,%s\n", getFullName(),
				address.getStreet(), address.getZip(), address.getCity());
	}

	@Override
	public String forServer() {
		NCryptor ncryptor = new NCryptor();
		String strValue = new String(ServerCommandParser.DOCTOR + ";");
		strValue += ncryptor.LToNcode(getId()) + ";";
		strValue += ncryptor.LToNcode(getCheckSum());
		return strValue;
	}

	@Override
	protected void clear() {
		super.clear();
		address = new Address();
		setAddressID(EMPTY_ID);
		setSurname("");
	}

	@Override
	public Address getAddress() {
		return address;
	}

}
