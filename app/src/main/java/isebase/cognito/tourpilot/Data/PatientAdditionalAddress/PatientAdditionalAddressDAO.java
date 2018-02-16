package isebase.cognito.tourpilot.Data.PatientAdditionalAddress;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;

public class PatientAdditionalAddressDAO extends BaseObjectDAO<PatientAdditionalAddress> {

	public PatientAdditionalAddressDAO(ConnectionSource connectionSource,
                                       Class<PatientAdditionalAddress> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

    public boolean isAdditionalAddressesAssigned(int patientID){
        if(patientID <= 0){
            return false;
        }
        return loadByPatientID(patientID).size() > 0;
    }

	public List<PatientAdditionalAddress> loadByPatientID(int patientID){
		try {
			QueryBuilder<PatientAdditionalAddress, Integer> queryBuilder = queryBuilder();
			queryBuilder.where().eq(PatientAdditionalAddress.PATIENT_ID_FIELD, patientID);
            List<PatientAdditionalAddress> result = queryBuilder.query();
            Collections.sort(result);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
}
