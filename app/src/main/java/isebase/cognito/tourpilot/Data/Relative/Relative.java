package isebase.cognito.tourpilot.Data.Relative;

import isebase.cognito.tourpilot.Connection.ServerCommandParser;
import isebase.cognito.tourpilot.Data.Address.IAddressable;
import isebase.cognito.tourpilot.Data.Address.Address;
import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot.Utils.NCryptor;
import isebase.cognito.tourpilot.Utils.StringParser;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Relatives")
public class Relative extends BaseObject implements IAddressable {

	public static final String SURNAME_FIELD = "surname";
	public static final String SHIP_FIELD = "ship";
	public static final String ADDRESS_ID_FIELD = "address_id";
	public static final String FAMILY_STATE_FIELD = "family_state";
	
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
	
	@DatabaseField(dataType = DataType.STRING, columnName = SHIP_FIELD)
	private String ship;
	
	public String getShip() {
		return ship;
	}

	public void setShip(String ship) {
		this.ship = ship;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = FAMILY_STATE_FIELD)
	private String familyState;

	public String getFamilyState() {
		return familyState;
	}

	public void setFamilyState(String familyState) {
		this.familyState = familyState;
	}
	
	public Relative(){
		clear();
	}
	
	public Relative(String initString) {
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
		setShip(parsingString.next(";"));
		setFamilyState(parsingString.next("~"));
		setCheckSum(Long.parseLong(parsingString.next()));
	}

	public String getFullName() {
		return String.format("%s %s", getSurname(), getName());
	}

	@Override
	public String forServer() {
		NCryptor ncryptor = new NCryptor();
		String strValue = new String(ServerCommandParser.RELATIVE + ";");
		strValue += ncryptor.LToNcode(getId()) + ";";
		strValue += ncryptor.LToNcode(getCheckSum());
		return strValue;
	}
	
	@Override
	protected void clear() {
		super.clear();
		address = new Address();
		setSurname("");
		setShip("");
		setAddressID(EMPTY_ID);
	}
	
	@Override
	public String toString(){
		return String.format("%s\n%s\n%s,%s\n",getFullName(),address.getStreet(),address.getZip(),address.getCity());
	}

	@Override
	public Address getAddress() {
		return address;
	}

}
