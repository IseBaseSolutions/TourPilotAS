package isebase.cognito.tourpilot.Data.RelatedQuestionSetting;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

public class RelatedQuestionSettingDAO extends BaseObjectDAO<RelatedQuestionSetting> {

	public RelatedQuestionSettingDAO(ConnectionSource connectionSource,
			Class<RelatedQuestionSetting> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
