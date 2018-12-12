package isebase.cognito.tourpilot_apk.Data.Worker;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.support.ConnectionSource;

public class WorkerDAO extends BaseObjectDAO<Worker> {
	
	public WorkerDAO(ConnectionSource connectionSource,
			Class<Worker> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
	
	public List<Worker> loadActive() {
		return load(Worker.IS_ACTIVE_FIELD, true);
	}

}
