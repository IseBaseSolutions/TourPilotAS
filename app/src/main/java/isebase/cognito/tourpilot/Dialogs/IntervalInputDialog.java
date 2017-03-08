package isebase.cognito.tourpilot.Dialogs;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Data.SelectionPeriod.SelectionPeriod;
import isebase.cognito.tourpilot.Utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class IntervalInputDialog extends BaseDialog  {

	private SelectionPeriod selectedPeriod = new SelectionPeriod();
	private TimePicker tpStartTime;
	private TimePicker tpStopTime;
	
	private int minHour;
    private int minMinute;
    private int minTotalMinute;
    
    private int maxHour;
    private int maxMinute;
    private int maxTotalMinute;
    
    private int getStartTotalMinute(){
    	return tpStartTime.getCurrentHour() * DateUtils.MINUTES_IN_HOUR + tpStartTime.getCurrentMinute();
    }
    
    private int getStopTotalMinute(){
    	return tpStopTime.getCurrentHour() * DateUtils.MINUTES_IN_HOUR + tpStopTime.getCurrentMinute();
    }
    
    PatientsDialog patientsDialog;
    
    String title;
    
	public IntervalInputDialog(String title) {
		this.title = title;
	}

	public void setSelectedPeriod(SelectionPeriod selectedPeriod) {
		this.selectedPeriod = selectedPeriod;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setMinMaxTime();
		
	    View customView = getActivity().getLayoutInflater().inflate(R.layout.custom_time_picker, null);

	    tpStartTime = (TimePicker) customView.findViewById(R.id.tpStartTime);
	    tpStartTime.setIs24HourView(true);
	    tpStartTime.setCurrentHour(selectedPeriod.startTime().getHours());
	    tpStartTime.setCurrentMinute(selectedPeriod.startTime().getMinutes());
	    
	    tpStartTime.setOnTimeChangedListener(new OnTimeChangedListener() {
	    	
			private boolean needToHandle = true;
			
			private void setHourMinutes(int hours, int minutes){
				needToHandle = false;
				tpStartTime.setCurrentHour(hours);
				tpStartTime.setCurrentMinute(minutes);
				needToHandle = true;
			}
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				if(!needToHandle)
					return;
				if (getStartTotalMinute() < minTotalMinute)
					setHourMinutes(minHour, minMinute);
				
				if(getStartTotalMinute() > maxTotalMinute)
					setHourMinutes(maxHour, maxMinute);
				
				if(getStartTotalMinute() > getStopTotalMinute())
					setHourMinutes(tpStopTime.getCurrentHour(), tpStopTime.getCurrentMinute());
	
			}
		});
	    
	    tpStopTime = (TimePicker) customView.findViewById(R.id.tpStopTime);
	    tpStopTime.setIs24HourView(true);
	    tpStopTime.setCurrentHour(selectedPeriod.stopTime().getHours());
	    tpStopTime.setCurrentMinute(selectedPeriod.stopTime().getMinutes());
	    tpStopTime.setOnTimeChangedListener(new OnTimeChangedListener() {
			
	    	private boolean needToHandle = true;
			
			private void setHourMinutes(int hours, int minutes){
				needToHandle = false;
				tpStopTime.setCurrentHour(hours);
				tpStopTime.setCurrentMinute(minutes);
				needToHandle = true;
			}
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				if(!needToHandle)
					return;
				
				if (getStopTotalMinute() < minTotalMinute)
					setHourMinutes(minHour, minMinute);
				
				if(getStopTotalMinute() > maxTotalMinute)
					setHourMinutes(maxHour, maxMinute);
				
				if(getStartTotalMinute() > getStopTotalMinute())
					setHourMinutes(tpStartTime.getCurrentHour(), tpStartTime.getCurrentMinute());

			}
		});
	    
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
				.setView(customView)
				.setTitle(title)
				.setPositiveButton(isebase.cognito.tourpilot.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								listener.onDialogPositiveClick(IntervalInputDialog.this);
							}
						});
		return adb.create();
	}
	
	private void setMinMaxTime() {
		minMinute = selectedPeriod.getStartTime().getMinutes();
		minHour = selectedPeriod.getStartTime().getHours();
		minTotalMinute = minHour * DateUtils.MINUTES_IN_HOUR + minMinute;
		
		maxMinute = selectedPeriod.getStopTime().getMinutes();
		maxHour = selectedPeriod.getStopTime().getHours();
		maxTotalMinute = maxHour * DateUtils.MINUTES_IN_HOUR + maxMinute;
	}
	
    public Date getStartDate() {
    	return getDateFromTicker(tpStartTime.getCurrentHour(), tpStartTime.getCurrentMinute());
    }
    
    public Date getStopDate() {
    	return getDateFromTicker(tpStopTime.getCurrentHour(), tpStopTime.getCurrentMinute());
    }
	
	private Date getDateFromTicker(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
	}

}
