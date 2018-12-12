package isebase.cognito.tourpilot_apk.Activity;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseTimeSyncActivity;
import isebase.cognito.tourpilot_apk.Data.AdditionalWork.AdditionalWork;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.Patient.Patient;
import isebase.cognito.tourpilot_apk.Data.Work.Work;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialogListener;
import isebase.cognito.tourpilot_apk.Dialogs.PatientsDialog;
import isebase.cognito.tourpilot_apk.Dialogs.WorkStopDialog;
import isebase.cognito.tourpilot_apk.Dialogs.WorkTypeDialog;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AdditionalWorksActivity extends BaseTimeSyncActivity implements BaseDialogListener {

	private DialogFragment workInputDialog;
	private DialogFragment workStopDialog;
	private PatientsDialog patientsDialog;

	private List<AdditionalWork> additionalWorks;
	private List<Patient> patients;
	String[] patientNames;
	boolean[] selectedPatients;
	
	private AdditionalWork addWork;
	private Work work;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_additional_works);
			reloadData();
			fillUp();
			fillUpTitle();
			switchTolatest();
			setTimeSync(true);
		}
		catch(Exception ex){
			ex.printStackTrace();
			criticalClose();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void fillUp() {
		ListView listView = (ListView) findViewById(R.id.lvAdditionalWorks);
		ArrayAdapter<AdditionalWork> adapter = new ArrayAdapter<AdditionalWork>(
				this, android.R.layout.simple_list_item_1, additionalWorks);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				addWork = additionalWorks.get(position);
				workInputDialog = new WorkTypeDialog(addWork.getName());
				workInputDialog.show(getSupportFragmentManager(), "addWorkDialog");
			}
			
		});
	}

	private void fillUpTitle() {
		setTitle(R.string.menu_additional_work);
	}

	private void reloadData() {
		additionalWorks = HelperFactory.getHelper().getAdditionalWorkDAO().load();
		patients = HelperFactory.getHelper().getPatientDAO().loadPatientsByPilotTourID(Option.Instance().getPilotTourID());
		if (Option.Instance().getWorkID() != -1)
			work = HelperFactory.getHelper().getWorkDAO().load(Option.Instance().getWorkID());
	}

	private void switchTolatest() { 
		if (Option.Instance().getWorkID() != -1)
		{
			workStopDialog = new WorkStopDialog(work.getName(), work.startTime());
			workStopDialog.show(getSupportFragmentManager(), "stopDialog");
		}
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		if (dialog == patientsDialog)
		{
			work.setPatientIDs(patientsDialog.getSelectedPatientIDs());
			work.setIsDone(true);
			HelperFactory.getHelper().getWorkDAO().save(work);
			Option.Instance().setWorkID(BaseObject.EMPTY_ID);
			Option.Instance().save();
			startPatientsActivity();
		}
		if (dialog == workInputDialog)
		{
			work = new Work(DateUtils.getSynchronizedTime(), addWork.getId(), 
					Option.Instance().getPilotTourID(), addWork.getName(), Option.Instance().getWorkerID());
//			work.setServerTime(Option.Instance().isTimeSynchronised());
			work.setServerTime(true);
			HelperFactory.getHelper().getWorkDAO().save(work);
			Option.Instance().setWorkID(work.getId());
			Option.Instance().save();
			workStopDialog = new WorkStopDialog(addWork.getName(), work.startTime());
			workStopDialog.setCancelable(false);
			workStopDialog.show(getSupportFragmentManager(), "stopDialog");
		}
		if (dialog == workStopDialog)
		{
			work.setStopTime(DateUtils.getSynchronizedTime());
//			work.setServerTime(Option.Instance().isTimeSynchronised());
			work.setServerTime(true);
			HelperFactory.getHelper().getWorkDAO().save(work);
			patientsDialog = new PatientsDialog(patients, work.getName());
			patientsDialog.setCancelable(false);
			patientsDialog.show(getSupportFragmentManager(), "patientsDialog");
		}
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		if (dialog == workInputDialog)
			startManualInputActivity();
	}
	
	@Override
	protected void startManualInputActivity() {
		Intent manualInputActivity = new Intent(getApplicationContext(), ManualInputActivity.class);
		manualInputActivity.putExtra("addWorkID", addWork.getId());
		startActivity(manualInputActivity);
	}

}
