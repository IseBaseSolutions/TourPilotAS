package isebase.cognito.tourpilot.Data.Link;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class LinkDAO extends BaseObjectDAO<Link>{

	public LinkDAO(ConnectionSource connectionSource, Class<Link> dataClass)
			throws SQLException {
		super(connectionSource, dataClass);
	}
	
	public void deleteByKeys(final List<String> items) {
		final DeleteBuilder<Link, Integer> deleteBuilder = deleteBuilder();
		try {
			callBatchTasks(new Callable<Void>() {
				public Void call() {
					for (String item : items)
						try {
							deleteBuilder.where().eq(Link.QUESTION_ID_FIELD, item.split("~")[0]).and().eq(Link.CATEGORY_ID_FIELD, item.split("~")[1]);
							delete(deleteBuilder.prepare());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return null;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
