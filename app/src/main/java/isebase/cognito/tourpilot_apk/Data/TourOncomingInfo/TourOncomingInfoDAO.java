package isebase.cognito.tourpilot_apk.Data.TourOncomingInfo;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class TourOncomingInfoDAO extends BaseObjectDAO<TourOncomingInfo> {

	public TourOncomingInfoDAO(ConnectionSource connectionSource,
			Class<TourOncomingInfo> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
	
	public TourOncomingInfo LoadByOwnerID(int ownerID, int type) {
    	QueryBuilder<TourOncomingInfo, Integer> queryBuilder = queryBuilder();
    	List<TourOncomingInfo> list = new ArrayList<TourOncomingInfo>();
    	try {
			queryBuilder.where().eq(TourOncomingInfo.OWNER_ID_FIELD, ownerID).and().eq(TourOncomingInfo.INFO_TYPE, type);
			list = queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	if (list.size() > 0)
    		return list.get(0);
    	return null;
	}
	
}
