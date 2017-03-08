package isebase.cognito.tourpilot.Data.Patient;

import isebase.cognito.tourpilot.Data.Address.Address;
import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;
import isebase.cognito.tourpilot.Data.Employment.Employment;
import isebase.cognito.tourpilot.DataBase.HelperFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.support.ConnectionSource;

public class PatientDAO extends BaseObjectDAO<Patient> {

	public PatientDAO(ConnectionSource connectionSource,
			Class<Patient> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	@Override
	public void afterLoad(Patient item) {
		item.address = HelperFactory.getHelper().getAddressDAO().load(item.getAddressID());
	}
	
	@Override
	public void afterLoad(List<Patient> items) {
		int[] addressIDs = new int[items.size()];
		for(int i = 0;i< items.size(); i++)
			addressIDs[i] = items.get(i).getAddressID();

		List<Address> addresses = HelperFactory.getHelper().getAddressDAO().loadByIds(addressIDs);
		for(Patient pat : items){
			for(Address address : addresses){
				if(address.getId() == pat.getAddressID()){
					pat.address = address;
					break;
				}
			}
		}		
	}
	
	@Override
	public void beforeSave(Patient item) {
		HelperFactory.getHelper().getAddressDAO().save(item.address);
		item.setAddressID(item.address.getId());
	}
	
	@Override
	public long getCheckSumByRequest() {
		try {
			return queryRawValue(String.format(
					"SELECT SUM(checksum) FROM %s WHERE was_sent = 0 AND is_additional = 0",
					tableInfo.getTableName()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}


	public List<Patient> loadByPilotTourID(long tourPilotID) {
			String strSQL = String.format("SELECT t1.* " +
					"FROM %1$s as t1 " +
					"INNER JOIN %2$s as t2 ON t1._id = t2.patient_id " +
					"WHERE t2.%3$s = %4$d GROUP BY t2._id "
					, HelperFactory.getHelper().getPatientDAO().getTableInfo().getTableName()
					, HelperFactory.getHelper().getEmploymentDAO().getTableInfo().getTableName()
					, Employment.PILOT_TOUR_ID_FIELD
					, tourPilotID);
			GenericRawResults<String[]> rawResults = null;
			List<Patient> patients = new ArrayList<Patient>();
			try {
				rawResults = queryRaw(strSQL);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			for (String[] resultArray : rawResults) 
				patients.add(new Patient(resultArray));	
			return patients;
	}
	
	public List<Patient> loadPatientsByPilotTourID(long tourPilotID) {
		String strSQL = String.format("SELECT %5$s " +
				"FROM %1$s as t1 " +
				"INNER JOIN %2$s as t2 ON t1._id = t2.patient_id " +
				"WHERE t2.%3$s = %4$d AND t1._id < 999900 GROUP BY t2._id "
				, HelperFactory.getHelper().getPatientDAO().getTableInfo().getTableName()
				, HelperFactory.getHelper().getEmploymentDAO().getTableInfo().getTableName()
				, Employment.PILOT_TOUR_ID_FIELD
				, tourPilotID
				, getSelectString());
		GenericRawResults<String[]> rawResults = null;
		List<Patient> patients = new ArrayList<Patient>();
		try {
			rawResults = queryRaw(strSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		for (String[] resultArray : rawResults) 
			patients.add(new Patient(resultArray));	
		return patients;
	}
	
	private String getSelectString() {
		String strSelect = "";
		strSelect += "t1." + Patient.SURNAME_FIELD + ", ";
		strSelect += "t1." + Patient.BIRTH_DATE_FIELD + ", ";
		strSelect += "t1." + Patient.SEX_FIELD + ", ";
		strSelect += "t1." + Patient.RELATIVES_ID_FIELD + ", ";
		strSelect += "t1." + Patient.DOCTORS_ID_FIELD + ", ";
		strSelect += "t1." + Patient.CATALOG_SA_TYPE_FIELD + ", ";
		strSelect += "t1." + Patient.CATALOG_PR_TYPE_FIELD + ", ";
		strSelect += "t1." + Patient.IS_ADDITIONAL_FIELD + ", ";
		strSelect += "t1." + Patient.CATALOG_PK_TYPE_FIELD + ", ";
		strSelect += "t1." + Patient.CATALOG_KK_TYPE_FIELD + ", ";
		strSelect += "t1." + Patient.ADDRESS_ID_FIELD + ", ";
		strSelect += "t1." + Patient.NAME_FIELD + ", ";
		strSelect += "t1." + Patient.ID_FIELD + ", ";
		strSelect += "t1." + Patient.CHECK_SUM_FIELD + ", ";
		strSelect += "t1." + Patient.IS_SERVER_TIME_FIELD + ", ";
		strSelect += "t1." + Patient.WAS_SENT_FIELD;
		return strSelect;
	}
	
}
