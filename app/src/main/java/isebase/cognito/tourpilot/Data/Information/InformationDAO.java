package isebase.cognito.tourpilot.Data.Information;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.DataBase.HelperFactory;
import isebase.cognito.tourpilot.Utils.DateUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class InformationDAO extends BaseObjectDAO<Information> {

	public InformationDAO(ConnectionSource connectionSource,
			Class<Information> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public static String getInfoStr(List<Information> infoList, Date date, boolean isFromMenu) {
		String strInfos = "";
		for(Information info : infoList) {
			if(DateUtils.isToday(info.getReadTime()) && !isFromMenu || !info.isActualInfo(date))
				continue;
			strInfos += (strInfos.equals("") ? "" : "\n") + info.getName();
			info.setReadTime(DateUtils.getSynchronizedTime());
			info.setServerTime(Option.Instance().isTimeSynchronised());
		}
		if(strInfos.equals(""))
			return "";
		HelperFactory.getHelper().getInformationDAO().save(infoList);
		return strInfos;
	}
	
	public void deleteByEmploymentID(int employmentID) {
		DeleteBuilder<Information, Integer> deleteBuilder = deleteBuilder();
		try {
			deleteBuilder.where().eq(Information.EMPLOYMENT_ID_FIELD, employmentID);
			deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
