package isebase.cognito.tourpilot_apk.Data.Address;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;

public class AddressDAO extends BaseObjectDAO<Address> {

	public AddressDAO(ConnectionSource connectionSource,
			Class<Address> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
