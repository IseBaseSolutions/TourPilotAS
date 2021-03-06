package isebase.cognito.tourpilot_apk.Activity;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.AdditionalTasks.CatalogsActivity;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot_apk.Connection.ConnectionAsyncTask;
import isebase.cognito.tourpilot_apk.Connection.ConnectionStatus;
import isebase.cognito.tourpilot_apk.Data.AdditionalEmployment.AdditionalEmployment;
import isebase.cognito.tourpilot_apk.Data.Employment.Employment;
import isebase.cognito.tourpilot_apk.Data.Employment.EmploymentDAO;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.Patient.Patient;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialogListener;
import isebase.cognito.tourpilot_apk.Dialogs.BaseInfoDialog;
import isebase.cognito.tourpilot_apk.EventHandle.SynchronizationHandler;
import isebase.cognito.tourpilot_apk.Templates.AdditionalEmploymentAdapter;
import isebase.cognito.tourpilot_apk.Templates.AdditionalPatientAdapter;
import isebase.cognito.tourpilot_apk.Utils.StringParser;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

public class AdditionalEmploymentsActivity extends BaseActivity implements BaseDialogListener {

	private enum eAdditionalPatientsMode { getIP, removeIP, getCP, removeCP, getAP }
	private eAdditionalPatientsMode additionalEmploymentsMode;
	
	private SynchronizationHandler syncHandler;
	private ConnectionStatus connectionStatus;
	private ConnectionAsyncTask connectionTask;

    BaseInfoDialog noConectionDialog;

	ListView listView;
	
	List<AdditionalEmployment> addEmployments = new ArrayList<AdditionalEmployment>();
	List<Patient> addPatients = new ArrayList<Patient>();
	List<Employment> newEmployments;
	
	DialogFragment noPatientsDialog;
	ProgressBar pbSync;
	public Button btOK;	
	private EditText etFilter;
	
	AdditionalEmploymentAdapter employmentAdapter;
	AdditionalPatientAdapter patientAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_additional_employments);
		initSyncHandler();
 		initControls();
 		initDialogs();
		setMode();
 		if (!isRemovingPatients())
 		{
 			sendFillRequest();
 			return;
 		}
		reloadData();
		fillUp();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	private void fillUp() {
		pbSync.setVisibility(View.INVISIBLE);
		if (isNoPatients())
		{
			noPatientsDialog.show(getSupportFragmentManager(), "noPatientsDialog");			
			getSupportFragmentManager().executePendingTransactions();
			return;
		}	
		listView = (ListView) findViewById(R.id.lvAddEmployments);
		switch(additionalEmploymentsMode) {
		case getAP:
			etFilter.setVisibility(View.VISIBLE);
			patientAdapter = new AdditionalPatientAdapter(this, R.layout.row_single_additional_employment_emplate, addPatients);
			listView.setAdapter(patientAdapter);
			initPatientsFilter();
			break;
		default:
			btOK.setEnabled(true);
			employmentAdapter = new AdditionalEmploymentAdapter(this, R.layout.row_multi_additional_employment_template, addEmployments);
			listView.setAdapter(employmentAdapter);
			break;
		}
	}
	
	private void initPatientsFilter() {
		EditText etFilter = (EditText) findViewById(R.id.etAddEmploymentsFilter);
		etFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable text) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				patientAdapter.getFilter().filter(arg0);
			}
			
		});
	}
	
	private void setMode() {
		additionalEmploymentsMode = eAdditionalPatientsMode.values()[getIntent().getIntExtra("Mode", 0)];
	}
	
	private void sendFillRequest() {
		pbSync.setVisibility(View.VISIBLE);
		btOK.setEnabled(false);
		connectionStatus = new ConnectionStatus(syncHandler);
		connectionStatus.CurrentState = 0;
		connectionTask = new ConnectionAsyncTask(connectionStatus);
		String requestForServer = "";
		switch(additionalEmploymentsMode) {
		case getIP:
			requestForServer = "GET_WIP;";
			break;
		case getCP:
			requestForServer = "GET_CTP;";
			break;
		case getAP:
			requestForServer = "GET_ALL_PAT;";
			break;
		default:
			break;
		}
		requestForServer += HelperFactory.getHelper().getPilotTourDAO().loadPilotTour(Option.Instance().getPilotTourID()).getTourID() + ";" + Option.Instance().getVersion();
		connectionStatus.setRequestForServer(requestForServer);
		connectionTask.execute();
	}
	
	private void sendExecuteRequest() {
		connectionStatus = new ConnectionStatus(syncHandler);
		connectionStatus.CurrentState = 0;
		connectionTask = new ConnectionAsyncTask(connectionStatus);
		String requestForServer = "";
		String tourID = HelperFactory.getHelper().getPilotTourDAO().loadPilotTour(Option.Instance().getPilotTourID()).getTourID() + ";";
		switch(additionalEmploymentsMode) {
		case getIP:
			requestForServer = "SEL_WIP;";
			break;
		case removeIP:
			requestForServer = "REMOVE_WIP;";
			break;
		case getCP:
			requestForServer = "SEL_CTP;";
			break;
		case removeCP: 
			requestForServer = "REMOVE_CTP;";
			break;
		default:
			return;
		}
		requestForServer += tourID + getPatientsStr() + ";";
		connectionStatus.setRequestForServer(requestForServer);
		connectionTask.execute();
	}
	
	
	private void nextStage() {
		switch(connectionStatus.CurrentState) {
		case 0:	connectionStatus.CurrentState = 1;
			break;
		case 1:	connectionStatus.CurrentState = 8;
			break;	
		case 8:	connectionStatus.CurrentState = 6;
			break;
		}
	}
	
	private void reloadData() {
		newEmployments = HelperFactory.getHelper().getEmploymentDAO().load(Employment.PILOT_TOUR_ID_FIELD, String.valueOf(Option.Instance().getPilotTourID()));
		for (Employment newEmployment : newEmployments)
			if (!newEmployment.isDone() && !newEmployment.isAdditionalWork() && (!newEmployment.isFromMobile() || additionalEmploymentsMode != eAdditionalPatientsMode.removeIP))
				addEmployments.add(new AdditionalEmployment(newEmployment.getId(), String.format("%s %s\n%s", newEmployment.getTime(), newEmployment.getName(), newEmployment.getDayPart())));
	}
	
	public void btOkClick(View view) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            if (additionalEmploymentsMode == eAdditionalPatientsMode.getAP)
            {
                createAdditionalEmployment();
                startCatalogsActivity();
                return;
            }
            pbSync.setVisibility(View.VISIBLE);
            btOK.setEnabled(false);
            sendExecuteRequest();
            return;
        }
        noConectionDialog = new BaseInfoDialog(getString(R.string.attention), getString(R.string.dialog_no_connection_synchronize));
        noConectionDialog.show(getSupportFragmentManager(), "noConectionDialog");
	}
	
	private String getPatientsStr() {
		String str = "";
		for (AdditionalEmployment additionalEmployment : employmentAdapter.getSelectedAdditionalEmployments())
			str += (str.equals("") ? "" : ",") + additionalEmployment.getID();
		return str;
	}
	
	private void getPatientStr() {
		boolean isNotEmpty = true;
		StringParser stringParser = new StringParser(connectionStatus.getAnswerFromServer());
		while (isNotEmpty)
			isNotEmpty = parseCommonTouremployments(stringParser.next("\0"));
		fillUp();
	}
	
	private boolean parseCommonTouremployments(String strElement) {
		if (strElement.equals("."))
			return false;
		if (additionalEmploymentsMode != eAdditionalPatientsMode.getAP)
			addEmployments.add(new AdditionalEmployment(strElement));
		else
			addPatients.add(new Patient(strElement));
		return true;
	}
	
	private void initSyncHandler(){
 		syncHandler = new SynchronizationHandler() {
 			
 			@Override
			public void onSynchronizedFinished(boolean isOK, String text) {
 			}
 			
 			@Override
 			public void onItemSynchronized(String text) {
 				nextStage();
				connectionTask = new ConnectionAsyncTask(connectionStatus);
				connectionTask.execute();
				if (isGetSyncEnded() || isRemoveSyncEnded())
				{
					startSyncActivity();
					pbSync.setVisibility(View.INVISIBLE);
					btOK.setEnabled(true);
				}
				else if (isFinishSync())
					getPatientStr();
 			}
 			
 			@Override
 			public void onProgressUpdate(String text, int progress){
 				
 			}

			@Override
			public void onProgressUpdate(String text) {
				
			}				
 		};
	}
	
	private void initControls() {
		pbSync = (ProgressBar) findViewById(R.id.pbSync);
		btOK = (Button) findViewById(R.id.btOK);
		etFilter = (EditText) findViewById(R.id.etAddEmploymentsFilter);
	}
	
	private void initDialogs() {
		noPatientsDialog = new BaseInfoDialog(getString(R.string.dialog_empty_tour), getString(R.string.dialog_no_patients));
		noPatientsDialog.setCancelable(false);
	}
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		startPatientsActivity();		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub		
	}
	
	private boolean isRemoveSyncEnded() {
		return connectionStatus.CurrentState == 6 && (additionalEmploymentsMode == eAdditionalPatientsMode.removeIP 
				|| additionalEmploymentsMode == eAdditionalPatientsMode.removeCP);
	}
	
	private boolean isGetSyncEnded() {
		return connectionStatus.getAnswerFromServer().startsWith("OK") && (additionalEmploymentsMode == eAdditionalPatientsMode.getIP 
				|| additionalEmploymentsMode == eAdditionalPatientsMode.getCP);
	}
	
	private boolean isFinishSync() {
		return connectionStatus.CurrentState == 6;
	}
	
	private boolean isNoPatients() {
		return (additionalEmploymentsMode != eAdditionalPatientsMode.getAP && addEmployments.size() == 0) 
				|| (additionalEmploymentsMode == eAdditionalPatientsMode.getAP && addPatients.size() == 0);
	}
	
	private boolean isRemovingPatients() {
		return additionalEmploymentsMode == eAdditionalPatientsMode.removeIP 
 				|| additionalEmploymentsMode == eAdditionalPatientsMode.removeCP;
	}
	
	private void createAdditionalEmployment() {
		Patient patient = HelperFactory.getHelper().getPatientDAO().load(patientAdapter.getSelectedAdditionalPatient().getId());
		if (patient == null)
		{
			patient = patientAdapter.getSelectedAdditionalPatient();
			patient.setIsAdditional(true);
			HelperFactory.getHelper().getPatientDAO().save(patient);
		}
		Employment employment = EmploymentDAO.createEmployment(patient);
		Option.Instance().setEmploymentID(employment.getId());
		Option.Instance().save();		
	}
	
	private void startCatalogsActivity() {
		Intent catalogsActivity = new Intent(getApplicationContext(), CatalogsActivity.class);
		catalogsActivity.putExtra("additionalEmployment", true);
		startActivity(catalogsActivity);
	}	
}
