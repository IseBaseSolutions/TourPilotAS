package isebase.cognito.tourpilot_apk.Data.Information;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;
import isebase.cognito.tourpilot_apk.Data.Employment.Employment;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class InformationDAO extends BaseObjectDAO<Information> {

	public InformationDAO(ConnectionSource connectionSource,
			Class<Information> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public static String getInfoStr(List<Information> infoList, Date date, boolean isFromMenu) {
		String strInfos = "";
		for(Information info : infoList) {
			if(DateUtils.isToday(info.getReadTime()) && !info.isActualInfo(date))
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

    public static boolean getInfoIsRead(List<Information> infoList, Date date, boolean isFromMenu) {
        for(Information info : infoList) {

            if(isFromMenu)
                return false;
           // else if(DateUtils.isToday(info.getReadTime()) && info.isActualInfo(date))
            else if(info.getReadTime().getDate() == date.getDate())
                return true;
        }
        return false;
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

    public String getDone()
    {
        List<Information> informations = LoadDone();
        afterLoad(informations);
        String strInformations = "";
        for (Information information : informations)
        {
            if(!(information.getIsDone()) && information.isActualInfo(information.getReadTime()))
            {
                strInformations += information.getDone() + "\0";
                information.setIsDone(true);
                HelperFactory.getHelper().getInformationDAO().save(information);
            }
        }

        return strInformations;
    }

    public List<Information> LoadDone() {
        QueryBuilder<Information, Integer> queryBuilder = queryBuilder();
        try {
            queryBuilder.where().eq(Information.WAS_SENT_FIELD, false).and().eq(Information.IS_DONE, false);
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Information>();
    }
}
