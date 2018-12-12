 package isebase.cognito.tourpilot_apk.Activity;
 
import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot_apk.Connection.AutoUpdate;
import isebase.cognito.tourpilot_apk.Connection.ConnectionAsyncTask;
import isebase.cognito.tourpilot_apk.Connection.ConnectionStatus;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Data.Employment.Employment;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.PilotTour.PilotTour;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialog;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialogListener;
import isebase.cognito.tourpilot_apk.Dialogs.BaseInfoDialog;
import isebase.cognito.tourpilot_apk.EventHandle.SynchronizationHandler;
import isebase.cognito.tourpilot_apk.Gps.Service.GPSLogger;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
 
 public class SynchronizationActivity extends BaseActivity implements BaseDialogListener {
 	
	/** Members **/
	 
	private ArrayAdapter<String> adapter;
	private ConnectionStatus connectionStatus;
	private ConnectionAsyncTask connectionTask;
	private ListView lvConnectionLog;
	private ProgressBar progressBar;
	private TextView progressText;
 	private SynchronizationHandler syncHandler;
 	
 	/** Override **/

 	@Override
 	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);

 		setContentView(R.layout.activity_synchronization);
 		initControls();
 		initAdapter();
 		syncHandler = new SynchronizationHandler() {
 			
 			@Override
			public void onSynchronizedFinished(boolean isOK, String text) {
				if(!text.equals("")){
					adapter.insert(DateUtils.DateHourMinutesFormat.format(new Date()) + " " + text, 0);	
					if(!isOK){
						progressText.setText(text);
					}
				}
				if (isInterrupted()) {
					showInterruptDialog();
				}
				else if (isLicenseOver()) {
					showLicenseOverDialog();
				}
				else if (isNewVersionAvailable())
 				{
					showNewVersionAvilableDialog();
					return;
 				}
				else if ((!connectionStatus.lastExecuteOK) && isPilotTourPresent())
				{
					startPatientsActivity();
				}
				else if(isOK) {
					if(isWorkerPresent()) {
						HelperFactory.getHelper().getEmploymentDAO().createEmployments();
						clearOldInfo();
					}
					switchToNextActivity();					
				}
 			}
 			
 			@Override
 			public void onItemSynchronized(String text) {
				adapter.insert(DateUtils.DateHourMinutesFormat.format(DateUtils.GetServerDateTime()) + " " + text, 0);	
				connectionStatus.nextState();
				connectionTask = new ConnectionAsyncTask(connectionStatus);
				connectionTask.execute(); 
 			}
 			
 			@Override
 			public void onProgressUpdate(String text, int progress){
 				progressBar.setProgress(progress);
 				progressText.setText(text);
 			}

			@Override
			public void onProgressUpdate(String text) {
				progressBar.setMax(connectionStatus.getTotalProgress());				
			}				
 		};	
	
		connectionStatus = new ConnectionStatus(syncHandler);
		connectionTask = new ConnectionAsyncTask(connectionStatus);
		connectionTask.execute();
 	}
 	
 	@Override
 	public void onBackPressed() {
		connectionTask.terminate();
		if (Option.Instance().getPilotTourID() == BaseObject.EMPTY_ID
				&& Option.Instance().getWorkerID() != -1 
				&& HelperFactory.getHelper().getPilotTourDAO().loadPilotTours().size() > 0) {
			Intent intent = new Intent(getBaseContext(), ToursActivity.class);
			startActivity(intent);
		}
		super.onBackPressed();
 	}
 	
 	@Override
 	protected void onResume() {
 		super.onResume();
 	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		if (dialog.getTag().equals("newVersionDialog"))
		{
			AutoUpdate autoUpdate = new AutoUpdate();
			autoUpdate.execute();	
		}
		else if (dialog.getTag().equals("licenseOverDialog"))
		{
			finish();
		}
		else if (dialog.getTag().equals("interruptDialog"))
		{
			switchToNextActivity();
		}
		else if (dialog.getTag().equals("closeDialog"))
		{
			close();
		}
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		if (dialog.getTag().equals("newVersionDialog"))
		{
			Option.Instance().setWorkerID(BaseObject.EMPTY_ID);
			Option.Instance().save();
			finish();
		}
	}

	/** Internal **/
	
	private void initControls() {
		
		lvConnectionLog = (ListView) findViewById(R.id.lvSyncText);
		progressBar = (ProgressBar) findViewById(R.id.pbSync);
		progressText = (TextView)findViewById(R.id.tvProgress);
	}
	
	private void initAdapter() {
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new ArrayList<String>());
		lvConnectionLog.setAdapter(adapter);
		adapter.add(getString(R.string.waitng_sockets_to_close));
	}
	

	public void close() {
		moveTaskToBack(true);
		finish();		
	}
	
	private void clearOldInfo() {
		List<PilotTour> pilotTours = HelperFactory.getHelper().getPilotTourDAO().loadPilotToursMax();
		if (pilotTours.size() == 0) {
			if (isMyServiceRunning(GPSLogger.class))
				stopService(new Intent(this, GPSLogger.class));			
//			Option.Instance().setTourActivity(false);
			Option.Instance().setPilotTourID(BaseObject.EMPTY_ID);
			Option.Instance().setWorkerID(BaseObject.EMPTY_ID);
			return;
		}
		for (PilotTour pilotTour : pilotTours) {
			if (pilotTour.isActual())
				continue;
			List<Employment> employments = HelperFactory.getHelper().getEmploymentDAO().loadNotActualByPilotTourID(pilotTour.getId());
			for (Employment employment : employments) {
				employment.clearConnectedData();
				if (Option.Instance().getEmploymentID() == employment.getId())
					Option.Instance().setEmploymentID(BaseObject.EMPTY_ID);
			}
			HelperFactory.getHelper().getWorkDAO().deleteNotActual();
			HelperFactory.getHelper().getEmploymentDAO().delete(employments);
			if (Option.Instance().getPilotTourID() == pilotTour.getId())
				Option.Instance().setPilotTourID(BaseObject.EMPTY_ID);
		}

	}
	
	private boolean isInterrupted() {
		return connectionStatus.getAnswerFromServer().equals("INTERRUPT");
	}
	
	private void showInterruptDialog() {
		if (!isCurrentAcivity())
			return;
		BaseInfoDialog interruptDialog = new BaseInfoDialog(getString(R.string.attention), getString(R.string.dialog_interrupt));
		interruptDialog.show(getSupportFragmentManager(), "interruptDialog");
	}
	
	private boolean isLicenseOver() {
		return connectionStatus.getAnswerFromServer().equals("OVER");
	}
	
	private void showLicenseOverDialog() {
		if (!isCurrentAcivity())
			return;
		BaseInfoDialog licenseOverDialog = new BaseInfoDialog(getString(R.string.attention), getString(R.string.dialog_license_over));
		licenseOverDialog.setCancelable(false);
		licenseOverDialog.show(getSupportFragmentManager(), "licenseOverDialog");
	}
	
	private boolean isNewVersionAvailable() {
		return Option.Instance().getPalmVersion() != null 
					&& Integer.parseInt(Option.Instance().getPalmVersion()) > Integer.parseInt(Option.Instance().getVersion());
	}
	
	private void showNewVersionAvilableDialog() {
		if (!isCurrentAcivity())
			return;
		DialogFragment newVersionDialog = new BaseDialog(getString(R.string.dialog_new_version), getString(R.string.dialog_update_program));
		newVersionDialog.setCancelable(false);
		newVersionDialog.show(getSupportFragmentManager(), "newVersionDialog");
	}
	
	private boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	private boolean isCurrentAcivity() {
		ActivityManager am = (ActivityManager)getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		return cn.getClassName().equals(SynchronizationActivity.class.getName());
	}
	
}
