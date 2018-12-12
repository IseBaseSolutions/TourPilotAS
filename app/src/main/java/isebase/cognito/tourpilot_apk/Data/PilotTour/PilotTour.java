package isebase.cognito.tourpilot_apk.Data.PilotTour;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "PilotTours")
public class PilotTour extends BaseObject {

	public static final String PLAN_DATE_FIELD = "plan_date";
	public static final String IS_COMMON_TOUR_FIELD = "is_common_tour";
	public static final String TOUR_ID_FIELD = "tour_id";

	@DatabaseField(dataType = DataType.DATE_LONG, columnName = PLAN_DATE_FIELD)
	private Date planDate;
	
	public Date getPlanDate() {
		return planDate == null ? planDate = DateUtils.EmptyDate : planDate;
	}
	
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	
	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_COMMON_TOUR_FIELD)
	private boolean isCommonTour;
	
	public boolean getIsCommonTour() {
		return isCommonTour;
	}

	public void setIsCommonTour(boolean isCommonTour) {
		this.isCommonTour = isCommonTour;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = TOUR_ID_FIELD)
	private int tourID;
	
	public int getTourID() {
		return tourID;
	}
	
	public void setTourID(int tourID) {
		this.tourID = tourID;
	}
	
	public PilotTour() {
		clear();
	}
	
	public PilotTour(String[] resultArray) {
		clear();
		setId(Integer.parseInt(resultArray[0]));
		setTourID(Integer.parseInt(resultArray[1]));
		setIsCommonTour(resultArray[2].equals("1"));
		setPlanDate(new Date(Long.parseLong(resultArray[3])));
		setName(resultArray[4]);
		setCheckSum(Long.parseLong(resultArray[5]));
		setWasSent(resultArray[6].equals("1"));
		setServerTime(resultArray[7].equals("1"));
	}
	
	public boolean isActual() {
		Date a = new Date(2014, 6, 13);
		Date b = new Date(2014, 6, 14);
		long c = b.getTime() - a.getTime();
		Date planDate = DateUtils.getStartOfDay(getPlanDate());
		Date date = DateUtils.getStartOfDay(DateUtils.getSynchronizedTime());
		if (date.getTime() - planDate.getTime() > c)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		String dayOfTheWeek = DateUtils.WeekDateFormat.format(getPlanDate());
		return String.format("%s - %s", getName(), dayOfTheWeek);
	}
	
	@Override
	public String forServer() {
		return "";
	}
	

}
