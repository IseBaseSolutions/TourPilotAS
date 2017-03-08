package isebase.cognito.tourpilot.Data.PilotTour;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;
import isebase.cognito.tourpilot.Data.Employment.Employment;
import isebase.cognito.tourpilot.DataBase.HelperFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.support.ConnectionSource;

public class PilotTourDAO extends BaseObjectDAO<PilotTour> {

	public PilotTourDAO(ConnectionSource connectionSource,
			Class<PilotTour> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public List<PilotTour> loadPilotTours() {
		String strSQL = String.format("SELECT " +
				"t1.%3$s as Id, " +
				"t2._id as tour_id, " +
				"t2.is_common_tour, " +
				"min(t1.date) as plan_date, " +
				"t2.name as name, " +
				"t2.checksum as checksum, " +
				"t2.was_sent as was_sent, " +
				"t2.is_server_time as is_server_time " +
				"FROM %1$s t1 INNER JOIN %2$s t2 ON t1.tour_id = t2._id " +
				"GROUP BY t1.%3$s",
				HelperFactory.getHelper().getEmploymentDAO().getTableInfo().getTableName(),
				HelperFactory.getHelper().getTourDAO().getTableInfo().getTableName(),
				Employment.PILOT_TOUR_ID_FIELD);
		GenericRawResults<String[]> rawResults = null;
		List<PilotTour> pilotTours = new ArrayList<PilotTour>();
		try {
			rawResults = queryRaw(strSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		for (String[] resultArray : rawResults) 
			pilotTours.add(new PilotTour(resultArray));	
		return pilotTours;		
	}
	
	public List<PilotTour> loadPilotToursMax() {
		String strSQL = String.format("SELECT " +
				"t1.%3$s as Id, " +
				"t2._id as tour_id, " +
				"t2.is_common_tour, " +
				"max(t1.date) as plan_date, " +
				"t2.name as name, " +
				"t2.checksum as checksum, " +
				"t2.was_sent as was_sent, " +
				"t2.is_server_time as is_server_time " +
				"FROM %1$s t1 INNER JOIN %2$s t2 ON t1.tour_id = t2._id " +
				"GROUP BY t1.%3$s",
				HelperFactory.getHelper().getEmploymentDAO().getTableInfo().getTableName(),
				HelperFactory.getHelper().getTourDAO().getTableInfo().getTableName(),
				Employment.PILOT_TOUR_ID_FIELD);
		GenericRawResults<String[]> rawResults = null;
		List<PilotTour> pilotTours = new ArrayList<PilotTour>();
		try {
			rawResults = queryRaw(strSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		for (String[] resultArray : rawResults) 
			pilotTours.add(new PilotTour(resultArray));	
		return pilotTours;		
	}
	
	public PilotTour loadPilotTour(long pilotTourID) {
		String strSQL = String.format("SELECT " +
				"t1.%3$s as Id, " +
				"t2._id as tour_id, " +
				"t2.is_common_tour, " +
				"min(t1.date) as plan_date, " +
				"t2.name as name, " +
				"t2.checksum as checksum, " +
				"t2.was_sent as was_sent, " +
				"t2.is_server_time as is_server_time " +
				"FROM %1$s t1 INNER JOIN %2$s t2 ON t1.tour_id = t2._id " +
				"WHERE t1.%3$s = %4$d " +
				"GROUP BY t1.%3$s",
				HelperFactory.getHelper().getEmploymentDAO().getTableInfo().getTableName(),
				HelperFactory.getHelper().getTourDAO().getTableInfo().getTableName(),
				Employment.PILOT_TOUR_ID_FIELD,
				pilotTourID);
		GenericRawResults<String[]> rawResults = null;
		try {
			rawResults = queryRaw(strSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		for (String[] resultArray : rawResults) 
			return new PilotTour(resultArray);	
		return null;
	}

	
}
