package isebase.cognito.tourpilot.DataInterfaces.Job;

import java.util.Date;

public interface IJob {

	public boolean isDone();
	
	public String text();
	
	public String time();
	
	public String timeInterval();
	
	public Date startTime();
	
	public Date stopTime();
	
	public boolean wasSent();	
	
}
