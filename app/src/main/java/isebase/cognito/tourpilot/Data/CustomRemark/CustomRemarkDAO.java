package isebase.cognito.tourpilot.Data.CustomRemark;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;

public class CustomRemarkDAO extends BaseObjectDAO<CustomRemark> {

	public CustomRemarkDAO(ConnectionSource connectionSource,
			Class<CustomRemark> dataClass) throws SQLException {
		super(connectionSource, dataClass);
		// TODO Auto-generated constructor stub
	}

}
