package isebase.cognito.tourpilot.Data.QuestionSetting;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;
import isebase.cognito.tourpilot.Data.ExtraCategory.ExtraCategory;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.DataBase.HelperFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class QuestionSettingDAO extends BaseObjectDAO<QuestionSetting> {

	public QuestionSettingDAO(ConnectionSource connectionSource,
			Class<QuestionSetting> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
	
	@Override
	public void afterLoad(QuestionSetting item) {
		ExtraCategory extraCategory = HelperFactory.getHelper().getExtraCategoryDAO().load((int)Option.Instance().getEmploymentID());
		if (extraCategory == null)
			return;
		item.setExtraCategoriesID(extraCategory.getExtraCategoryIDsString());
	}
	
	@Override
	public void afterLoad(final List<QuestionSetting> items) {
		try {
			callBatchTasks(new Callable<Void>() {
				public Void call() {
					for (QuestionSetting questionSetting : items)
						afterLoad(questionSetting);
					return null;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void deleteByEmploymentID(int employmentID) {
		DeleteBuilder<QuestionSetting, Integer> deleteBuilder = deleteBuilder();
		try {
			deleteBuilder.where().eq(BaseObject.ID_FIELD, employmentID);
			deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
