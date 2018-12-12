package isebase.cognito.tourpilot_apk.Data.RelatedQuestionSetting;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

public class RelatedQuestionSettingDAO extends BaseObjectDAO<RelatedQuestionSetting> {

	public RelatedQuestionSettingDAO(ConnectionSource connectionSource,
			Class<RelatedQuestionSetting> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
