package isebase.cognito.tourpilot_apk.Data.Tour;

import java.sql.SQLException;
import com.j256.ormlite.support.ConnectionSource;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;

public class TourDAO extends BaseObjectDAO<Tour> {

	public TourDAO(ConnectionSource connectionSource,
			Class<Tour> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
	
}
