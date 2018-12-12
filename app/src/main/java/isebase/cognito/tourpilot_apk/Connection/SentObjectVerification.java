package isebase.cognito.tourpilot_apk.Connection;

import isebase.cognito.tourpilot_apk.Data.Answer.Answer;
import isebase.cognito.tourpilot_apk.Data.Employment.Employment;
import isebase.cognito.tourpilot_apk.Data.EmploymentVerification.EmploymentVerification;
import isebase.cognito.tourpilot_apk.Data.Task.Task;
import isebase.cognito.tourpilot_apk.Data.UserRemark.UserRemark;
import isebase.cognito.tourpilot_apk.Data.WayPoint.WayPoint;
import isebase.cognito.tourpilot_apk.Data.Work.Work;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;

import java.util.ArrayList;
import java.util.List;

public class SentObjectVerification {
	
	private static SentObjectVerification instance;
	
	public List<Task> sentTasks = new ArrayList<Task>();
	public List<Employment> sentEmployments = new ArrayList<Employment>();
	public List<Work> sentWorks = new ArrayList<Work>();
	public List<UserRemark> sentUserRemarks = new ArrayList<UserRemark>();
	public List<EmploymentVerification> sentEmploymentVerifications = new ArrayList<EmploymentVerification>();
	public List<WayPoint> sentWayPoints = new ArrayList<WayPoint>();
	public List<Answer> sentAnswers = new ArrayList<Answer>();
	
	public static SentObjectVerification Instance() {
		return instance == null ? instance = new SentObjectVerification() : instance;
	}
	
	public void setWasSent() {
		HelperFactory.getHelper().getTaskDAO().updateNotSent(sentTasks);
		HelperFactory.getHelper().getEmploymentDAO().updateNotSent(sentEmployments);
		HelperFactory.getHelper().getWorkDAO().updateNotSent(sentWorks);
		HelperFactory.getHelper().getUserRemarkDAO().updateNotSent(sentUserRemarks);
		HelperFactory.getHelper().getEmploymentVerificationDAO().updateNotSent(sentEmploymentVerifications);
		HelperFactory.getHelper().getReadableDatabase().execSQL("delete from "+ HelperFactory.getHelper().getWayPointDAO().getTableInfo().getTableName());
		HelperFactory.getHelper().getAnswerDAO().updateNotSent(sentAnswers);
		clear();
	}
	
	public void clear(){
		sentTasks.clear();
		sentEmployments.clear();
		sentWorks.clear();
		sentUserRemarks.clear();
		sentEmploymentVerifications.clear();
		sentWayPoints.clear();
		sentAnswers.clear();
	}

}
