package isebase.cognito.tourpilot_apk.Data.UserRemark;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;

import java.sql.SQLException;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class UserRemarkDAO extends BaseObjectDAO<UserRemark> {

	public UserRemarkDAO(ConnectionSource connectionSource,
			Class<UserRemark> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
	
	public void deleteByEmploymentID(int employmentID) {
		DeleteBuilder<UserRemark, Integer> deleteBuilder = deleteBuilder();
		try {
			deleteBuilder.where().eq(BaseObject.ID_FIELD, employmentID);
			deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
