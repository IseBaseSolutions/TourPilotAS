package isebase.cognito.tourpilot_apk.Data.Task;

import isebase.cognito.tourpilot_apk.Data.AdditionalTask.AdditionalTask;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.Task.Task.eTaskState;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class TaskDAO extends BaseObjectDAO<Task> {

	public TaskDAO(ConnectionSource connectionSource,
			Class<Task> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
	
	public void createTasks(List<AdditionalTask> additionalTasks){
		List<Task> addedAdditionalTasks = new ArrayList<Task>();
		List<Task> tasks = HelperFactory.getHelper().getTaskDAO().load(Task.EMPLOYMENT_ID_FIELD, String.valueOf(Option.Instance().getEmploymentID()));
		Task normalTask = null;
		for (Task task : tasks) {
			if (!task.isFirstTask() && !task.isLastTask()) {
				normalTask = task;
				break;
			}
		}
		for(AdditionalTask additionalTask : additionalTasks) {
			Task createdTask = null;
			if (normalTask != null)
				createdTask = new Task(additionalTask, normalTask.getPlanDate());
			else
				createdTask = new Task(additionalTask, DateUtils.getSynchronizedTime());
			addedAdditionalTasks.add(createdTask);
		}

		save(addedAdditionalTasks);
	}
	
	public int getFirstSymbol(int employmentID){
		try {
			return (int) queryRawValue(String.format("select substr(leistungs,1,1) as val from Tasks where employment_id = %d limit 1",	employmentID));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public void deleteByEmploymentID(int employmentID) {
		DeleteBuilder<Task, Integer> deleteBuilder = deleteBuilder();
		try {
			deleteBuilder.where().eq(Task.EMPLOYMENT_ID_FIELD, employmentID);
			deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public List<Task> CompareWithExistedTasks(List<Task> items){
		List<Task> retVal = new ArrayList<Task>();
		retVal.addAll(items);
		try {		
			QueryBuilder<Task, Integer> queryBuilder = queryBuilder();
			queryBuilder.where().in(Task.EMPLOYMENT_ID_FIELD, getEmploymentsIDs(items));
			List<Task> existedTasks = queryBuilder.query();
			for(Task item : items){
				for(Task existedTask : existedTasks){
					if(item.isTaskEquals(existedTask) && existedTask.getState() != eTaskState.Empty && existedTask.getWasSent())
					{
						if (retVal.contains(item))
							retVal.remove(item);
						existedTask.setWasSent(false);
						retVal.add(existedTask);
					}
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
	private String getEmploymentsIDs(List<Task> items){
		
		String retVal = "";
		if(items == null)
			return retVal;
		for(Task item : items)
			retVal += String.format("%s%s" 
					, retVal.equals("") ? "" : ", "
					, item.getEmploymentID());
		
		return retVal;
	}
}
