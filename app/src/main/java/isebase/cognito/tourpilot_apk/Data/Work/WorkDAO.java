package isebase.cognito.tourpilot_apk.Data.Work;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class WorkDAO extends BaseObjectDAO<Work> {

	public WorkDAO(ConnectionSource connectionSource,
			Class<Work> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
	
	@Override
	public void afterLoad(final List<Work> items) {
		try {
			callBatchTasks(new Callable<Void>() {
				public Void call() {
					for (Work work : items)
					{
						work.setAdditionalWork(HelperFactory.getHelper().getAdditionalWorkDAO().load(work.getAdditionalWorkID()));
						work.setPatients(HelperFactory.getHelper().getPatientDAO().loadByIds(work.getPatientIDs()));
					}
					return null;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public List<Work> loadDoneByPilotTourID(int tourPilotID) {
		QueryBuilder<Work, Integer> queryBuilder = queryBuilder();
		try {
			queryBuilder.where().eq(Work.PILOT_TOUR_ID_FIELD, tourPilotID).and().eq(Work.IS_DONE_FIELD, true);
			return queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<Work>();
	}
	
	public void deleteNotActual() {
    	DeleteBuilder<Work, Integer> deleteBuilder = deleteBuilder();
    	try {
    		deleteBuilder.where().eq(Work.WAS_SENT_FIELD, true).and().lt(Work.START_TIME_FIELD, DateUtils.getStartOfDay(DateUtils.getSynchronizedTime()));
	    	deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
