package isebase.cognito.tourpilot_apk.Data.Category;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Data.QuestionSetting.QuestionSetting;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class CategoryDAO extends BaseObjectDAO<Category>{

	public CategoryDAO(ConnectionSource connectionSource,
			Class<Category> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
	
	public Category loadByCategoryName(String name) {
		QueryBuilder<Category, Integer> queryBilder = queryBuilder();
		List<Category> list = new ArrayList<Category>();
		try {
			list = queryBilder.where().like(BaseObject.NAME_FIELD, name).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (list.size() > 0)
			return list.get(0);
		return null;
	}
	
	public List<Category> loadByQuestionSettings(QuestionSetting questionSettings) {
		QueryBuilder<Category, Integer> queryBilder = queryBuilder();
		List<Category> list = new ArrayList<Category>();
		try {
			list = queryBilder.where().in("_id", questionSettings.getCategoryIDsString()).or().in("_id", questionSettings.getExtraCategoryIDsString()).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Category> loadExtraCategoriesByQuestionSettings(QuestionSetting questionSettings) {
		QueryBuilder<Category, Integer> queryBilder = queryBuilder();
		List<Category> list = new ArrayList<Category>();
		try {
			list = queryBilder.where().not().in("_id", questionSettings.getCategoryIDsString()).and().not().in("_id", questionSettings.getExtraCategoryIDsString()).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}	
	
}
