package isebase.cognito.tourpilot.Data.EmploymentVerification;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;

import java.sql.SQLException;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class EmploymentVerificationDAO extends BaseObjectDAO<EmploymentVerification> {

	public EmploymentVerificationDAO(ConnectionSource connectionSource,
			Class<EmploymentVerification> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
	
	public void deleteByEmploymentID(int employmentID) {
		DeleteBuilder<EmploymentVerification, Integer> deleteBuilder = deleteBuilder();
		try {
			deleteBuilder.where().eq(EmploymentVerification.EMPLOYMENT_ID_FIELD, employmentID);
			deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
