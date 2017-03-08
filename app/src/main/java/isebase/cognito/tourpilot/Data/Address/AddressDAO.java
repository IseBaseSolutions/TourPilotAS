package isebase.cognito.tourpilot.Data.Address;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;

public class AddressDAO extends BaseObjectDAO<Address> {

	public AddressDAO(ConnectionSource connectionSource,
			Class<Address> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
