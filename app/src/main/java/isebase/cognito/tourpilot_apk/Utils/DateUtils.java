package isebase.cognito.tourpilot_apk.Utils;

import isebase.cognito.tourpilot_apk.Data.Option.Option;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    
    public static final int MILLISECONDS_IN_SECOND = 1000;
    public static final int SECONDS_IN_MINUTE = 60;
    public static final int MINUTES_IN_HOUR = 60;
    public static final int HOUR_IN_DAY = 24;
    
    public static final int MILLISECONDS_IN_DAY = 
    		MILLISECONDS_IN_SECOND * 
    		SECONDS_IN_MINUTE * 
    		MINUTES_IN_HOUR * 
    		HOUR_IN_DAY;

    public static final Date EmptyDate = new Date(0);
    
    public static final SimpleDateFormat WayPointDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
    public static final SimpleDateFormat FileNameFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    public static final SimpleDateFormat DateTimeFormat = new SimpleDateFormat("dd.MM.yyyy/HH:mm:ss");
    public static final SimpleDateFormat HourMinutesFormat = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat HourMinutesSecondsFormat = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat MinutesSecondsFormat = new SimpleDateFormat("mm:ss");
    public static final SimpleDateFormat DateFormat = new SimpleDateFormat("dd.MM.yyyy");
    public static final SimpleDateFormat BackupDateFormat = new SimpleDateFormat("yyyy.MM.dd");
    public static final SimpleDateFormat WeekDateFormat = new SimpleDateFormat("EEE dd.MM");
    public static final  SimpleDateFormat DateHourMinutesFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
    
    public static Date getDateOnly(Date date){
    	return parseDateOnly(date);
    }
    
    public static Date getTodayDateOnly(){
    	return parseDateOnly(getSynchronizedTime());
    }
    
    public static Date getTodayDateTime(){
    	return new Date();
    }
    
    private static Date parseDateOnly(Date date){
    	Date retVal = date;
    	try {
    		retVal = DateFormat.parse(DateFormat.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return retVal;	
    }
    
    public static Date parseTimeOnly(Date date){
    	Date retVal = date;
    	try {
    		retVal = HourMinutesSecondsFormat.parse(HourMinutesSecondsFormat.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return retVal;    	
    }
    
	private static long timeDiff = 0L;
	
	public static Date GetServerDateTime() {
		return getLocalDate(getLocalTime(new Date()) + timeDiff);
	}
	
    public static Date getLocalDate(long milliseconds)
    {
    	Date localDate = new Date(milliseconds - (Calendar.getInstance().get(Calendar.ZONE_OFFSET)
				  + Calendar.getInstance().get(Calendar.DST_OFFSET)));
    	localDate.setSeconds(0);
    	return localDate;
    }
    
    public static long getLocalTime(Date value)
    {
        return value.getTime() + (Calendar.getInstance().get(Calendar.ZONE_OFFSET)
        						+ Calendar.getInstance().get(Calendar.DST_OFFSET));
    }
    
	public static Date getSynchronizedTime() {
		return getLocalDate(Option.Instance().getServerTimeDifference() + (new Date()).getTime());
	}
	
	public static Date getSynchronizedTime(Date date) {
		return getLocalDate(Option.Instance().getServerTimeDifference() + date.getTime());
	}
    
    public static String toDateTime(Date data)
    {
    	return DateTimeFormat.format(data);
    }
    
    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static Date getAverageDate(Date startDate, Date endDate)
    {
    	return new Date((startDate.getTime() + endDate.getTime())/2);
    }
    	
    public static String formatDate(Date date, String format){
    	SimpleDateFormat df = new SimpleDateFormat(format);
    	return df.format(date);
    }
    
    public static boolean isToday(Date date){
    	return DateFormat.format(date).equals(DateFormat.format(getSynchronizedTime()));
    }
        
    public static int millisecondsToMinutes(long milliseconds){
    	return Math.round(((float)milliseconds / (float)MILLISECONDS_IN_SECOND) / (float)SECONDS_IN_MINUTE);
    }
    
    public static int getInterval(Date timeStart, Date timeStop) {
    	timeStart.setSeconds(0);
    	timeStop.setSeconds(0);
		return DateUtils.millisecondsToMinutes(timeStop.getTime() - timeStart.getTime());
	}
    
    public static Date parseTime(String timeString) {        
        try {        	
            return HourMinutesFormat.parse(timeString);
        } catch (java.text.ParseException e) {
        	return new Date(0);
        }
     }    
}
