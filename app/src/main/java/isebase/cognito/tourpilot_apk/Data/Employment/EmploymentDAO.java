package isebase.cognito.tourpilot_apk.Data.Employment;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;
import isebase.cognito.tourpilot_apk.Data.EmploymentInterval.EmploymentInterval;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.Patient.Patient;
import isebase.cognito.tourpilot_apk.Data.Task.Task;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;
import isebase.cognito.tourpilot_apk.Utils.Utilizer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class EmploymentDAO extends BaseObjectDAO<Employment> {

	public EmploymentDAO(ConnectionSource connectionSource,
			Class<Employment> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
	
	public void createEmployments() {
		String firstStr = "'%Anfang%'";
		String strSQL = String.format("INSERT INTO %3$s" +
				"(_id, patient_id, name, was_sent, checksum, is_server_time" +
				", pilot_tour_id, date, tour_id, is_done, start_time, stop_time, day_part) SELECT " +
				"t1.employment_id as _id, " +
				"t1.patient_id as patient_id, " +
				"(t2.surname || ', ' || t2.name) as name, " +
				"t1.was_sent as was_sent, " +
				"t1.checksum as checksum, " +
				"t1.is_server_time as is_server_time, " +
				"t1.pilot_tour_id as pilot_tour_id, " +
				"t1.plan_date as date, " +
				"t1.tour_id as tour_id, " +
				"t1.task_state as is_done, " +
				"t3.start_time as start_time, " +
				"t3.stop_time as stop_time, " +
				"t1.name as day_part " +
				"FROM %1$s t1 " +
				"INNER JOIN %2$s t2 on t1.patient_id = t2._id " +
				"LEFT JOIN %4$s t3 on t1.employment_id = t3._id " +
				"WHERE t1.leistungs like %5$s GROUP BY t1.employment_id"
				, HelperFactory.getHelper().getTaskDAO().getTableInfo().getTableName()
				, HelperFactory.getHelper().getPatientDAO().getTableInfo().getTableName()
				, tableInfo.getTableName()
				, HelperFactory.getHelper().getEmploymentIntervalDAO().getTableInfo().getTableName()
				, firstStr);
		try {
			TableUtils.clearTable(connectionSource, dataClass);
//			updateRaw(strSQL);
			HelperFactory.getHelper().getReadableDatabase().execSQL(strSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			callBatchTasks(new Callable<Void>() {
				public Void call() {
					List<EmploymentInterval> employmentsInterval = HelperFactory.getHelper().getEmploymentIntervalDAO().load();
					List<Employment> employments = HelperFactory.getHelper().getEmploymentDAO().loadByIds(Utilizer.getNewIDsString(employmentsInterval));
					for (Employment employment : employments)
						employment.setIsDone(true);
					HelperFactory.getHelper().getEmploymentDAO().save(employments);
					return null;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void afterLoad(final List<Employment> items) {
		try {
			callBatchTasks(new Callable<Void>() {
				public Void call() {
					for (Employment employment : items) {
						employment.setTasks(HelperFactory.getHelper().getTaskDAO().load(Task.EMPLOYMENT_ID_FIELD, employment.getId()));
						employment.setPatient(HelperFactory.getHelper().getPatientDAO().load(employment.getPatientID()));
						employment.setPilotTour(HelperFactory.getHelper().getPilotTourDAO().load((int)employment.getPilotTourID()));
					}
					return null;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void afterLoad(Employment employment) {
		employment.setTasks(HelperFactory.getHelper().getTaskDAO().load(Task.EMPLOYMENT_ID_FIELD, employment.getId()));
		employment.setPatient(HelperFactory.getHelper().getPatientDAO().load(employment.getPatientID()));
		employment.setPilotTour(HelperFactory.getHelper().getPilotTourDAO().load((int)employment.getPilotTourID()));
	}
	
	public List<Employment> loadDoneByPilotTourID(int tourPilotID) {
    	QueryBuilder<Employment, Integer> queryBuilder = queryBuilder();
    	try {
			queryBuilder.where().eq(Employment.PILOT_TOUR_ID_FIELD, tourPilotID).and().eq(Employment.IS_DONE_FIELD, true);
	    	return queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return new ArrayList<Employment>();
	}
	
	public List<Employment> loadNotActualByPilotTourID(long pilotTourID) {
    	QueryBuilder<Employment, Integer> queryBuilder = queryBuilder();
    	try {
			queryBuilder.where().eq(Employment.WAS_SENT_FIELD, true).and().eq(Employment.PILOT_TOUR_ID_FIELD, pilotTourID).and().lt(Employment.DATE_FIELD, DateUtils.getStartOfDay(DateUtils.getSynchronizedTime()));
	    	return queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return new ArrayList<Employment>();
	}
	
    public String getDone()
    {
		List<Employment> employments = LoadDone();
		afterLoad(employments);//////////////////
    	String strEmpls = "";
    	for (Employment employment : employments)
			strEmpls += employment.getDone();
    	return strEmpls;
    }
    
    public List<Employment> LoadDone() {
    	QueryBuilder<Employment, Integer> queryBuilder = queryBuilder();
    	try {
			queryBuilder.where().eq(Employment.WAS_SENT_FIELD, false).and().eq(Employment.IS_DONE_FIELD, true);
	    	return queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return new ArrayList<Employment>();
    }

    
    public static Employment createEmployment(Patient patient) {
    	int id = 1;
    	while (HelperFactory.getHelper().getEmploymentDAO().load(id) != null)
    		id++;
    	Employment employment = new Employment();
    	employment.setId(id);
    	employment.setPatientID(patient.getId());
    	employment.setName(String.format("%s, %s", patient.getSurname(), patient.getName()));
    	employment.setPilotTourID(Option.Instance().getPilotTourID());
    	employment.setDate(DateUtils.getSynchronizedTime());
    	employment.setTourID(HelperFactory.getHelper().getPilotTourDAO().loadPilotTour((int)Option.Instance().getPilotTourID()).getTourID());
    	employment.setDayPart("0");
    	employment.setServerTime(Option.Instance().isTimeSynchronised());
    	employment.setFromMobile(true);
    	
    	HelperFactory.getHelper().getEmploymentDAO().save(employment);
    	Task firstTask = new Task(patient, employment.getId(), employment.getTourID(), true);
    	HelperFactory.getHelper().getTaskDAO().save(firstTask);
    	Task endtTask = new Task(patient, employment.getId(), employment.getTourID(), false);
    	HelperFactory.getHelper().getTaskDAO().save(endtTask);
    	return employment;
    }   
}
    
