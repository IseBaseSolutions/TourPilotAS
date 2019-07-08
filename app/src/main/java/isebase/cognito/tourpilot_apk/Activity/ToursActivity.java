package isebase.cognito.tourpilot_apk.Activity;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot_apk.Connection.ConnectionAsyncTask;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.PilotTour.PilotTour;
import isebase.cognito.tourpilot_apk.Data.PilotTour.PilotTourComparer;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialog;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialogListener;
import isebase.cognito.tourpilot_apk.Dialogs.BaseInfoDialog;
import isebase.cognito.tourpilot_apk.Gps.Service.GPSLogger;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;
import isebase.cognito.tourpilot_apk.Templates.PilotToursAdapter;
import isebase.cognito.tourpilot_apk.Utils.DataBaseUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.base.Strings;

public class ToursActivity extends BaseActivity implements BaseDialogListener{
	
	private List<PilotTour> pilotTours;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_tours);
			reloadData();		
			fillUpTitle();
			fillUp();
		} catch(Exception e) {
			e.printStackTrace();
			criticalClose();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tour_info, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_db_backup:
				try {
					DataBaseUtils.backup();
					Toast.makeText(StaticResources.getBaseContext()
							, StaticResources.getBaseContext().getString(R.string.db_backup_created)
							, Toast.LENGTH_SHORT).show();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				return true;
			case R.id.action_clear_darabase:
				clearDatabase();
				return true;
			case R.id.action_close_program:
				close();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		logoutWorker();
	}
	
	public void fillUp() {
		ListView listView = (ListView) findViewById(R.id.lvTours);
		PilotToursAdapter adapter = new PilotToursAdapter(this,
				R.layout.row_tour_template, pilotTours);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				saveSelectedTour(pilotTours.get(position));
//				saveTourActivity(false);
				startPatientsActivity();
			}
		});
	}

	public void btlogOutClick(View view) {
		logoutWorker();
	}
	
	public void btStartSyncClick(View view) {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null) {
			startSyncActivity();
			return;
		}
        BaseInfoDialog noConnectionDialog = new BaseInfoDialog(getString(R.string.attention), getString(R.string.dialog_no_connection_sync));
		noConnectionDialog.show(getSupportFragmentManager(), "noConnectionDialog");
	}
	
	private void logoutWorker(){
		if(isAnyItemsNotSended())
			return;
		
		HashMap<String,String> msgDict = new HashMap<String,String>();
		if(!Strings.isNullOrEmpty(Option.Instance().getPin()))
			msgDict.put("removePin", getString(R.string.dialog_remove_pin));
		msgDict.put("dialogBack", getString(R.string.dialog_proof_logout));			
		for(String key : msgDict.keySet()){
			BaseDialog dialog = new BaseDialog(getString(R.string.attention), msgDict.get(key));
			dialog.show(getSupportFragmentManager(), key);
			getSupportFragmentManager().executePendingTransactions();
		}
	}
	
	private Boolean isAnyItemsNotSended(){		 
		String doneItems = ConnectionAsyncTask.getDoneStr(false).toString();
		if(Strings.isNullOrEmpty(doneItems))
			return false;
		BaseInfoDialog dialog = new BaseInfoDialog(getString(R.string.attention), getString(R.string.dialog_do_sync));
		dialog.show(getSupportFragmentManager(), "dialogDoSync");		
		return true;		
	}	
	
	private void logOut() {
		if (isMyServiceRunning(GPSLogger.class))
			stopService(new Intent(this, GPSLogger.class));
		clearPersonalOptions();
		startWorkersActivity();
	}

	private void fillUpTitle() {
		setTitle(Option.Instance().getWorker().getName());
	}

	private void reloadData(){
		pilotTours = HelperFactory.getHelper().getPilotTourDAO().loadPilotTours();
		Collections.sort(pilotTours, new PilotTourComparer());
	}
	
	private void saveSelectedTour(PilotTour pilotTour) {
		Option.Instance().setPilotTourID(pilotTour.getId());
		Option.Instance().save();
	}

	private void clearPersonalOptions() {
		Option.Instance().setWorkerID(BaseObject.EMPTY_ID);
		Option.Instance().setPilotTourID(BaseObject.EMPTY_ID);
		Option.Instance().save();
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		if(dialog.getTag().equals("dialogBack")) {
			logOut();
		}
		else if (dialog.getTag().equals("clearDatabase")) {
			clearDB();			
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		if(dialog.getTag().equals("removePin")){
			Option.Instance().setPin("");
			Option.Instance().save();
		}
	}
	
	private void clearDatabase(){
		BaseDialog dialog = new BaseDialog(getString(R.string.attention), 
				getString(R.string.dialog_clear_database));
		dialog.show(getSupportFragmentManager(), "clearDatabase");
		getSupportFragmentManager().executePendingTransactions();
	}
	
	private void clearDB() {
//		pbClearDB.setVisibility(View.VISIBLE);
//		syncButton.setEnabled(false);
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try{
					HelperFactory.getHelper().clearWorkerData();
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}
							
			@Override
			protected void onPostExecute(Void result) {
//				pbClearDB.setVisibility(View.INVISIBLE);
//				syncButton.setEnabled(true);
				Option.Instance().clearSelected();
				startWorkersActivity();
			}
		}.execute();
	}
	
//	private void saveTourActivity(boolean state) {
//		Option.Instance().setTourActivity(state);
//		Option.Instance().save();
//	}
	
	private boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
}
