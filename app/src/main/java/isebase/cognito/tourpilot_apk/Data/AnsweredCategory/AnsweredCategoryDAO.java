package isebase.cognito.tourpilot_apk.Data.AnsweredCategory;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;
import isebase.cognito.tourpilot_apk.Data.Option.Option;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class AnsweredCategoryDAO extends BaseObjectDAO<AnsweredCategory> {

	public AnsweredCategoryDAO(ConnectionSource connectionSource,
			Class<AnsweredCategory> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
	
	public List<AnsweredCategory> LoadByEmploymentID(int employmentID) {
		return load(AnsweredCategory.EMPLOYMENT_ID_FIELD, employmentID);
	}
	
	public AnsweredCategory loadByCategoryID(int categoryID) {
		QueryBuilder<AnsweredCategory, Integer> queryBuilder = queryBuilder();
		List<AnsweredCategory> list = new ArrayList<AnsweredCategory>();
		try {
			list = queryBuilder.where().eq(AnsweredCategory.CATEGORY_ID_FIELD, categoryID).and().eq(AnsweredCategory.EMPLOYMENT_ID_FIELD, Option.Instance().getEmploymentID()).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (list.size() > 0)
			return list.get(0);
		return null;
	}
	
	public void deleteByEmploymentID(int employmentID) {
		DeleteBuilder<AnsweredCategory, Integer> deleteBuilder = deleteBuilder();
		try {
			deleteBuilder.where().eq(AnsweredCategory.EMPLOYMENT_ID_FIELD, employmentID);
			deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
