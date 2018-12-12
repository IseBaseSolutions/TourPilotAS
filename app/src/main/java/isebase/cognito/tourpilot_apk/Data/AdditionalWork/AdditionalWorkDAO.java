package isebase.cognito.tourpilot_apk.Data.AdditionalWork;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;

public class AdditionalWorkDAO extends BaseObjectDAO<AdditionalWork> {

	public AdditionalWorkDAO(ConnectionSource connectionSource,
			Class<AdditionalWork> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
