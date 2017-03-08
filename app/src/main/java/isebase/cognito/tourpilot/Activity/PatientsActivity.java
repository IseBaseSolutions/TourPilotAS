package isebase.cognito.tourpilot.Activity;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot.Data.Address.Address;
import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot.Data.Employment.Employment;
import isebase.cognito.tourpilot.Data.Information.Information;
import isebase.cognito.tourpilot.Data.Information.InformationDAO;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.Data.Patient.Patient;
import isebase.cognito.tourpilot.Data.PilotTour.PilotTour;
import isebase.cognito.tourpilot.Data.TourOncomingInfo.TourOncomingInfo;
import isebase.cognito.tourpilot.Data.Work.Work;
import isebase.cognito.tourpilot.Data.Worker.Worker;
import isebase.cognito.tourpilot.DataBase.HelperFactory;
import isebase.cognito.tourpilot.DataInterfaces.Job.IJob;
import isebase.cognito.tourpilot.DataInterfaces.Job.JobComparer;
import isebase.cognito.tourpilot.DataInterfaces.Job.JobComparer.eJobComparerType;
import isebase.cognito.tourpilot.Dialogs.BaseDialog;
import isebase.cognito.tourpilot.Dialogs.BaseDialogListener;
import isebase.cognito.tourpilot.Dialogs.BaseInfoDialog;
import isebase.cognito.tourpilot.Dialogs.InfoBaseDialog;
import isebase.cognito.tourpilot.Gps.GpsNavigator;
import isebase.cognito.tourpilot.Templates.WorkEmploymentAdapter;
import isebase.cognito.tourpilot.Utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class PatientsActivity extends BaseActivity implements
		BaseDialogListener {

	private Worker worker;
	private List<IJob> jobs;
	private List<Information> infos = new ArrayList<Information>();
	private Work work;
	private DialogFragment selectedPatientsDialog;
	private String[] patientsArr;
	private PilotTour pilotTour;

	private Button btTourEnd;
	private InfoBaseDialog infoDialog;
	private BaseDialog dialogStartNavigation;
	private BaseDialog dialogGPSEnabling;
	private BaseInfoDialog noConnectionDialog;
	
	private LocationManager locationManager;
	private TourOncomingInfo workersInfo;
	private TourOncomingInfo carsInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_patients);
			reloadData();
			initControls();
			fillUpTitle();
			fillUp();
			initDialogs();
			showTourInfos(false);
		} catch (Exception e) {
			e.printStackTrace();
			criticalPatientsClose();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.patients_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem commonTourMenu = menu.findItem(R.id.action_common_tours);
		MenuItem tourInfoMenu = menu.findItem(R.id.tour_info);
		MenuItem additionalWork = menu
				.findItem(R.id.action_add_additional_work);
		MenuItem illnessTourMenu = menu.findItem(R.id.action_illness_tours);
		MenuItem allPatientsMenu = menu.findItem(R.id.action_show_all_patients);
		MenuItem actualWorkers = menu.findItem(R.id.action_actual_workers);
		MenuItem workersInfo = menu.findItem(R.id.workers_info);
		MenuItem carsInfo = menu.findItem(R.id.cars_info);
		commonTourMenu.setEnabled(false);
		tourInfoMenu.setEnabled(false);
		additionalWork.setEnabled(false);
		illnessTourMenu.setEnabled(false);
		allPatientsMenu.setEnabled(false);
		if (pilotTour != null && DateUtils.isToday(pilotTour.getPlanDate())) {
			commonTourMenu.setEnabled(pilotTour.getIsCommonTour());
			additionalWork.setEnabled(true);
			illnessTourMenu.setEnabled(true);
			allPatientsMenu.setEnabled(true);
		}
		tourInfoMenu.setEnabled(infos.size() != 0);
		workersInfo.setEnabled(this.workersInfo != null);
		carsInfo.setEnabled(this.carsInfo != null);
		actualWorkers.setEnabled(Option.Instance().isWorkerPhones());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.tour_info:
			showTourInfos(true);
			return true;
		case R.id.action_add_additional_work:
			startAdditionalWorksActivity();
			return true;
		case R.id.action_add_patient_to_illness:
			startAdditionalPatientsActivity(0);
			return true;
		case R.id.action_remove_patient_from_illness:
			startAdditionalPatientsActivity(1);
			return true;
		case R.id.action_add_patient_to_common:
			startAdditionalPatientsActivity(2);
			return true;
		case R.id.action_remove_patient_from_common:
			startAdditionalPatientsActivity(3);
			return true;
		case R.id.action_show_all_patients:
			startAdditionalPatientsActivity(4);
			return true;
		case R.id.action_actual_workers:
			startWorkersAdditionalInfoActivity();
			return true;
		case R.id.workers_info:
			startTourOncomingInfo(workersInfo.getId());
			return true;
		case R.id.cars_info:
			startTourOncomingInfo(carsInfo.getId());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		startToursActivity();
	}

	public void btEndTourClick(View view) {
		for (IJob job : jobs)
			if (!job.isDone()) {
				infoDialog.show(getSupportFragmentManager(), "");
				getSupportFragmentManager().executePendingTransactions();
				return;
			}
		Option.Instance().setPilotTourID(BaseObject.EMPTY_ID);
		Option.Instance().save();
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null) {
			startSyncActivity();
			return;
		}
		noConnectionDialog = new BaseInfoDialog(getString(R.string.attention),
				getString(R.string.dialog_no_connection_sync));
		noConnectionDialog.show(getSupportFragmentManager(),
				"noConectionDialog");
		// startSyncActivity();
	}

	private void initControls() {
		btTourEnd = (Button) findViewById(R.id.btEndTour);
		checkTourEndButton();
	}

	private void fillUp() {
		WorkEmploymentAdapter adapter = new WorkEmploymentAdapter(this,
				R.layout.row_work_employment_template, jobs);
		ListView lvEmployments = (ListView) findViewById(R.id.lvEmployments);
		lvEmployments.setAdapter(adapter);
		lvEmployments.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (jobs.get(position) instanceof Employment) {
					Employment employment = (Employment) jobs.get(position);
					saveSelectedEmploymentID(employment.getId());
					if (worker.isUseGPS() && !employment.isDone()) {
						if (isGPSEnabled())
							showStartNavigationDialog();
						else
							showGPSEnablingDialog();
					} else
						startTasksActivity();
				} else {
					initSelectedPatientsDialog();
					showPatientsDialog(position);
				}
			}
		});
	}

	private void reloadData() {
		loadJobs();
		worker = HelperFactory.getHelper().getWorkerDAO()
				.load(Option.Instance().getWorkerID());
		workersInfo = HelperFactory.getHelper().getTourOncomingInfoDAO().LoadByOwnerID(pilotTour.getId(), 0);
		carsInfo = HelperFactory.getHelper().getTourOncomingInfoDAO().LoadByOwnerID(pilotTour.getId(), 1);
	}

	private void loadJobs() {
		jobs = new ArrayList<IJob>();
		jobs.addAll(HelperFactory
				.getHelper()
				.getEmploymentDAO()
				.loadAll(Employment.PILOT_TOUR_ID_FIELD,
						Option.Instance().getPilotTourID()));
		List<Work> works = HelperFactory
				.getHelper()
				.getWorkDAO()
				.loadAll(Work.PILOT_TOUR_ID_FIELD,
						Option.Instance().getPilotTourID());
		pilotTour = HelperFactory.getHelper().getPilotTourDAO()
				.loadPilotTour(Option.Instance().getPilotTourID());

		jobs.addAll(works);
		Collections.sort(jobs, new JobComparer(
				eJobComparerType.NORMAL_DONE_SENT));
	}

	private void checkTourEndButton() {
		int taskCount = jobs.size();
		int syncTaskCount = 0;
		for (IJob job : jobs)
			if (job.wasSent())
				syncTaskCount++;
		if (syncTaskCount == taskCount
				|| !DateUtils.isToday(pilotTour.getPlanDate())) {
			btTourEnd.setVisibility(ImageButton.GONE);
		} else
			btTourEnd.setVisibility(ImageButton.VISIBLE);
	}

	private void fillUpTitle() {
		Worker worker = Option.Instance().getWorker();
		setTitle(String.format("%1$s, %2$s - %3$s", worker.getName(),
				pilotTour.getName(),
				DateUtils.WeekDateFormat.format(pilotTour.getPlanDate())));
	}

	private void saveSelectedEmploymentID(int emplID) {
		Option.Instance().setEmploymentID(emplID);
		Option.Instance().save();
	}

	private void initDialogs() {
		infoDialog = new InfoBaseDialog(getString(R.string.attention),
				getString(R.string.dialog_complete_all_tasks));
		dialogStartNavigation = new BaseDialog(getString(R.string.start_navigator),
				getString(R.string.dialog_start_gps), getString(R.string.yes),
				getString(R.string.no));
		dialogGPSEnabling = new BaseDialog("GPS ist ausgeschaltet",
				"Möchten sie die Einstellungen öffnen und GPS einschalten?");
	}

	private void initSelectedPatientsDialog() {
		selectedPatientsDialog = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
						.setTitle(
								work.getName()
										+ " "
										+ DateUtils.HourMinutesFormat
												.format(work.getStartTime())
										+ " - "
										+ DateUtils.HourMinutesFormat
												.format(work.getStopTime()))
						.setItems(patientsArr, null)
						.setPositiveButton(
								isebase.cognito.tourpilot.R.string.ok,
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}

								});
				return adb.create();
			}
		};
	}

	private void showPatientsDialog(int position) {
		work = (Work) jobs.get(position);
		List<Patient> patients = HelperFactory.getHelper().getPatientDAO()
				.loadByIds(work.getPatientIDs());
		if (patients.size() > 0) {
			patientsArr = new String[patients.size()];
			int counter = 0;
			for (Patient patient : patients)
				patientsArr[counter++] = patient.getFullName();
		} else
			patientsArr = new String[] { getString(R.string.no_any_patient) };
		selectedPatientsDialog.show(getSupportFragmentManager(),
				"patientsDialog");
	}

	private void startAdditionalPatientsActivity(int mode) {
		Intent additionalPatientsActivity = new Intent(getApplicationContext(),
				AdditionalEmploymentsActivity.class);
		additionalPatientsActivity.putExtra("Mode", mode);
		startActivity(additionalPatientsActivity);
	}

	private void startWorkersAdditionalInfoActivity() {
		Intent workersAdditionalInfoActivity = new Intent(
				getApplicationContext(), ActualWorkersActivity.class);
		startActivity(workersAdditionalInfoActivity);
	}
	
	private void startTourOncomingInfo(int id) {
		Intent tourOncomingInfoActivity = new Intent(
				getApplicationContext(), TourOncomingInfoActivity.class);
		tourOncomingInfoActivity.putExtra("id", id);
		startActivity(tourOncomingInfoActivity);
	}

	private void showTourInfos(boolean isFromMenu) {
		infos = HelperFactory.getHelper().getInformationDAO()
				.load(Information.EMPLOYMENT_ID_FIELD, BaseObject.EMPTY_ID);
		String strInfos = InformationDAO.getInfoStr(infos,
				pilotTour.getPlanDate(), isFromMenu);
		if (strInfos.equals(""))
			return;
		InfoBaseDialog dialog = new InfoBaseDialog(
				getString(R.string.menu_info), strInfos);
		dialog.show(getSupportFragmentManager(), "informationDialog");
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		if (dialog.getTag() == "dialogStartNavigation") {
			startTasksActivity();
			GpsNavigator.startGpsNavigation(getPatientAdress());			
		} else if (dialog.getTag() == "dialogGPSEnabling") {
			Intent myIntent = new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(myIntent);
		}
	}

	private Address getPatientAdress() {
		Address adr = HelperFactory
				.getHelper()
				.getPatientDAO()
				.loadAll(
						HelperFactory
								.getHelper()
								.getEmploymentDAO()
								.load((int) Option.Instance().getEmploymentID())
								.getPatientID()).getAddress();

		return adr;
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		if (dialog.getTag() == "dialogStartNavigation") {
			startTasksActivity();
		}
	}

	private boolean isGPSEnabled() {
		if (locationManager == null)
			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	private void showStartNavigationDialog() {
		if (dialogStartNavigation.getFragmentManager() != null)
			return;
		dialogStartNavigation.show(getSupportFragmentManager(),
				"dialogStartNavigation");
	}
	
	private void showGPSEnablingDialog() {
		if (dialogGPSEnabling.getFragmentManager() != null)
			return;
		dialogGPSEnabling.show(getSupportFragmentManager(),
				"dialogGPSEnabling");
	}

}
