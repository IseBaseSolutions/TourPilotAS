package isebase.cognito.tourpilot.Data.WayPoint;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;

public class WayPointDAO extends BaseObjectDAO<WayPoint> {

	public WayPointDAO(ConnectionSource connectionSource,
			Class<WayPoint> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
