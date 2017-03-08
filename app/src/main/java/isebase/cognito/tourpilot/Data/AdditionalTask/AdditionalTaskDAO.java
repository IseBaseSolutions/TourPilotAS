package isebase.cognito.tourpilot.Data.AdditionalTask;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.support.ConnectionSource;

public class AdditionalTaskDAO extends BaseObjectDAO<AdditionalTask> {

	public AdditionalTaskDAO(ConnectionSource connectionSource,
			Class<AdditionalTask> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
	
	public List<AdditionalTask> loadByCatalog(int catalogID){
		return load(AdditionalTask.CATALOG_TYPE_FIELD, catalogID);
	}

}
