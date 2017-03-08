package isebase.cognito.tourpilot.Data.SelectionPeriod;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.DataInterfaces.Job.IJob;
import isebase.cognito.tourpilot.StaticResources.StaticResources;

import java.util.Date;

public class SelectionPeriod implements IJob {

	private Date startTime;
	private Date stopTime;
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public SelectionPeriod() {
		
	}
	
	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String text() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String time() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String timeInterval() {
		return StaticResources.getBaseContext().getString(R.string.select_period);
	}

	@Override
	public boolean wasSent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Date startTime() {
		return getStartTime();
	}

	@Override
	public Date stopTime() {
		return getStopTime();
	}

}
