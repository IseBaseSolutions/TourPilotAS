package isebase.cognito.tourpilot.Data.PatientRemark;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;

public class PatientRemarkDAO extends BaseObjectDAO<PatientRemark> {

	public PatientRemarkDAO(ConnectionSource connectionSource,
			Class<PatientRemark> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
