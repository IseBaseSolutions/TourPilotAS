package isebase.cognito.tourpilot.Data.ExtraCategory;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;

import java.sql.SQLException;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class ExtraCategoryDAO extends BaseObjectDAO<ExtraCategory> {

	public ExtraCategoryDAO(ConnectionSource connectionSource,
			Class<ExtraCategory> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
	
	public void deleteByEmploymentID(int employmentID) {
		DeleteBuilder<ExtraCategory, Integer> deleteBuilder = deleteBuilder();
		try {
			deleteBuilder.where().eq(BaseObject.ID_FIELD, employmentID);
			deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
