package isebase.cognito.tourpilot_apk.Data.PatientRemark;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;

public class PatientRemarkDAO extends BaseObjectDAO<PatientRemark> {

	public PatientRemarkDAO(ConnectionSource connectionSource,
			Class<PatientRemark> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
