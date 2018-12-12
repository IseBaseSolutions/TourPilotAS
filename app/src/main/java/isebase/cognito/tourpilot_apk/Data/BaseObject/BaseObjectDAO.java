package isebase.cognito.tourpilot_apk.Data.BaseObject;

//import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Utils.Utilizer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class BaseObjectDAO<T> extends BaseDaoImpl<T, Integer> {

	protected BaseObjectDAO(ConnectionSource connectionSource,
			Class<T> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public List<T> load() {
		try {
			return queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<T>();
	}

	public T load(int id) {
		try {
			return queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public long getCheckSumByRequest() {
		try {
			return queryRawValue(String.format(
					"SELECT SUM(checksum) FROM %s WHERE was_sent = 0",
					getTableInfo().getTableName()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public List<T> loadByIds(int[] ids) {
		return loadByIds(Utilizer.getIDsString(ids));
	}

	public List<T> loadAllByIDs(String ids) {
		List<T> items = loadByIds(ids);
		afterLoad(items);
		return items;
	}

	public List<T> loadByIds(String ids) {
		try {
			QueryBuilder<T, Integer> queryBuilder = queryBuilder();
			queryBuilder.where().in("_id", ids);
			return queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<T>();
	}
	
	public List<T> sortByStrIDs(List<T> items, String strIDs) {
		List<T> sortedItems = new ArrayList<T>();
		String[] arr = strIDs.contains(" ") ? strIDs.split(", ") : strIDs.split(",");
		for(int i = 0; i < arr.length; i++)
			for (T item : items)
				if (Integer.parseInt(arr[i]) == ((BaseObject)item).getId())
					sortedItems.add(item);
		return sortedItems;
	}

	public List<T> load(String field, Object object) {
		try {
			QueryBuilder<T, Integer> queryBuilder = queryBuilder();
			queryBuilder.where().eq(field, object);
			return queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<T>();
	}
	
	public List<T> load(String groupBy, String having, String orderBy, boolean ascending) {
		try {
			QueryBuilder<T, Integer> queryBuilder = queryBuilder();
			if (groupBy != null)
				queryBuilder.groupBy(groupBy);
			if(having != null)
				queryBuilder.having(having);
			if(orderBy != null)
				queryBuilder.orderBy(orderBy, ascending);
//			queryBuilder.groupBy(groupBy).having(having).orderBy(orderBy, ascending);
			return queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<T>();
	}

	public void beforeSave(T item) {
	}

	public void afterSave(T item) {
	}

	public void save(T item) {
		try {
			beforeSave(item);
			createOrUpdate(item);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void save(final List<T> items) {
		try {
			callBatchTasks(new Callable<Void>() {
				public Void call() {
					for (T item : items)
						save(item);
					return null;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void clearTable() {
		try {
			callBatchTasks(new Callable<Void>() {
				public Void call() {
					delete(load());
					return null;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<T> loadAll() {
		List<T> items = load();
		afterLoad(items);
		return items;
	}

	public T loadAll(int id) {
		T item = load(id);
		afterLoad(item);
		return item;
	}
	
	public List<T> loadAll(String groupBy, String having, String orderBy) { ///////
		List<T> items = load(groupBy, having, orderBy, true);
		afterLoad(items);
		return items;
	}
	
	public List<T> loadAll(String field, Object object) {
		List<T> items = new ArrayList<T>();
		try {
			QueryBuilder<T, Integer> queryBuilder = queryBuilder();
			queryBuilder.where().eq(field, object);
			items = queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		afterLoad(items);
		return items;
	}

	public void afterLoad(List<T> items) {
	}

	public void afterLoad(T item) {
	}
	
	public void delete(int id) {
		try {
			deleteById(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteByIds(List<Integer> ids) {
		try {
			deleteIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int delete(Collection<T> items) {
		try {
			return super.delete(items);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public String getDone() {
		List<T> elements = load(BaseObject.WAS_SENT_FIELD, false);
		String strDone = "";
		for (T element : elements)
			strDone += ((BaseObject) element).getDone() + "\0";
		return strDone;
	}

	public void updateNotSent() {
		UpdateBuilder<T, Integer> updateBuilder = updateBuilder();
		try {
			updateBuilder.where().eq("was_sent", false);
			updateBuilder.updateColumnValue("was_sent", true);
			updateBuilder.update();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateNotSent(Iterable<? extends BaseObject> items){
		String ids = Utilizer.getNewIDsString(items);
		UpdateBuilder<T, Integer> updateBuilder = updateBuilder();
		if(ids != "")
			try {
				updateBuilder.where().in("_id", ids);
				updateBuilder.updateColumnValue("was_sent", true);
				updateBuilder.update();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public String forServer() {
		String strResult = "";
		try {
			List<T> items = queryForAll();
			for (T item : items) {
				String forServer = ((BaseObject) item).forServer();
				if (forServer.length() > 0)
					strResult += forServer + "\0";
			}
			strResult += ".\0";
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return strResult;
	}

}
