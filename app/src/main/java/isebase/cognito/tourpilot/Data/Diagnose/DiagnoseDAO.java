package isebase.cognito.tourpilot.Data.Diagnose;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;

public class DiagnoseDAO extends BaseObjectDAO<Diagnose> {

	public DiagnoseDAO(ConnectionSource connectionSource,
			Class<Diagnose> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
