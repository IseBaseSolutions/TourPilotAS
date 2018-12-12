package isebase.cognito.tourpilot_apk.Data.Option;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.j256.ormlite.support.ConnectionSource;

public class OptionDAO extends BaseObjectDAO<Option> {

	public OptionDAO(ConnectionSource connectionSource,
			Class<Option> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
	
	public Option loadOption() {
		List<Option> options = new ArrayList<Option>();
		try {
			options = load();
//			String strSQL = "SELECT * FROM OPTIONS";
//			GenericRawResults<String[]> rawResults = null;
//			try {
//				rawResults = queryRaw(strSQL);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			for (String[] resultArray : rawResults)
//				return new Option(resultArray);
			if (options.size() > 0)
				return options.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Option newOption = new Option();
		save(newOption);
		return newOption;
	}

}
