package isebase.cognito.tourpilot_apk.Activity.TasksAssessmentsActivity;

import isebase.cognito.tourpilot_apk.Activity.AdditionalAddressActivity;
import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.AddressPatientActivity;
import isebase.cognito.tourpilot_apk.Activity.DoctorsActivity;
import isebase.cognito.tourpilot_apk.Activity.ManualInputActivity;
import isebase.cognito.tourpilot_apk.Activity.PatientsActivity;
import isebase.cognito.tourpilot_apk.Activity.RelativesActivity;
import isebase.cognito.tourpilot_apk.Activity.SynchronizationActivity;
import isebase.cognito.tourpilot_apk.Activity.UserRemarksActivity;
import isebase.cognito.tourpilot_apk.Activity.VerificationActivity;
import isebase.cognito.tourpilot_apk.Activity.AdditionalTasks.CatalogsActivity;
import isebase.cognito.tourpilot_apk.Data.AdditionalTask.AdditionalTask;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Data.Diagnose.Diagnose;
import isebase.cognito.tourpilot_apk.Data.Employment.Employment;
import isebase.cognito.tourpilot_apk.Data.EmploymentInterval.EmploymentInterval;
import isebase.cognito.tourpilot_apk.Data.Information.Information;
import isebase.cognito.tourpilot_apk.Data.Information.InformationDAO;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.PatientRemark.PatientRemark;
import isebase.cognito.tourpilot_apk.Data.PilotTour.PilotTour;
import isebase.cognito.tourpilot_apk.Data.QuestionSetting.QuestionSetting;
import isebase.cognito.tourpilot_apk.Data.Task.Task;
import isebase.cognito.tourpilot_apk.Data.Task.Task.eTaskState;
import isebase.cognito.tourpilot_apk.Data.TourOncomingInfo.TourOncomingInfo;
import isebase.cognito.tourpilot_apk.Data.Work.Work;
import isebase.cognito.tourpilot_apk.Data.Worker.Worker;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.DataInterfaces.Job.IJob;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialog;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialogListener;
import isebase.cognito.tourpilot_apk.Dialogs.BaseInfoDialog;
import isebase.cognito.tourpilot_apk.Dialogs.InfoBaseDialog;
import isebase.cognito.tourpilot_apk.Dialogs.Tasks.BlutdruckTaskDialog;
import isebase.cognito.tourpilot_apk.Dialogs.Tasks.StandardTaskDialog;
import isebase.cognito.tourpilot_apk.Dialogs.Tasks.TaskTypes;
import isebase.cognito.tourpilot_apk.Gps.Service.GPSLogger;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;
import isebase.cognito.tourpilot_apk.Templates.TaskAdapter;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TasksFragment extends Fragment implements BaseDialogListener {

	private TaskAdapter taskAdapter;
	public Employment employment;
	private PilotTour pilotTour;
	private Worker worker;
	private Diagnose diagnose;

	private List<Task> tasks;
	private Task startTask;
	private Task endTask;
	
	private View clickedCheckBox;
	
	private ListView lvTasks;
	
	private TextView tvStartTaskTime;
	private TextView tvEndTaskTime;
	private TextView tvStartTaskDate;
	private TextView tvEndTaskDate;
		
	private Button btEndTask;
	private Button btStartTask;
	
	public final static int ACTIVITY_USERREMARKS_CODE = 0;
	public final static int ACTIVITY_VERIFICATION_CODE = 1;
	
	public final static String EMPLOYMENTS_ALLOWED_TIME = "04:00"; 
	
	public final static int SIMPLE_MODE = 0;
	public final static int SYNC_MODE = 1;
	public final static int NO_SYNC_MODE = 2;
	
	public static String timeEndTasks;
	
	public static boolean IS_FLEGE_OK = true;
	
	private List<Information> infos;

	private PatientRemark patientRemark;
	
	public TourOncomingInfo workersInfo;

	public DialogFragment startEmploymentDialog;
	private BaseDialog clearAllTasksDialog;
	
	private View rootView;
	
	TasksAssessementsActivity activity;

	public Context context;

	public Task getStartTask() {
		return startTask;
	}
	
	public Diagnose getDiagnose() {
		return diagnose;
	}
	
	public List<Information> getInfos() {
		return infos;
	}
	
	public PatientRemark getPatientRemark() {
		return patientRemark;
	}
	
	public TasksFragment() {
		
	}
	
	public TasksFragment(TasksAssessementsActivity instance) {
		activity = instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(
				R.layout.activity_tasks, container, false);
		try{
			super.onCreate(savedInstanceState);
			initControls();
			initDialogs();	
			reloadData();	
			fillUpTasks();
			changeButtonsState();
			showPatientInfo(false);
		} catch(Exception e){
			e.printStackTrace();
		}	
		return rootView;
	}
	
	@Override
	public void onActivityResult(int activityCode, int resultCode, Intent data) {
		super.onActivityResult(activityCode, resultCode, data);

		switch(activityCode) {
		case ACTIVITY_USERREMARKS_CODE:
			if(resultCode == activity.RESULT_OK) {
				startVerificationActivity(ACTIVITY_VERIFICATION_CODE,!IS_FLEGE_OK);
			} else {
				clearEndTask();
				fillUpTasks();
			}
			
			break;
		case ACTIVITY_VERIFICATION_CODE:
			if(resultCode == activity.RESULT_OK) {
				saveData(true);
				saveQuestionSetting();
				ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo ni = cm.getActiveNetworkInfo();
				
				if(Option.Instance().getIsAuto() && ni != null)
					startSyncActivity();
				else
					startPatientsActivity();
			} else {
				clearEndTask();
				fillUpTasks();
			}
			break;
		}		
		List<IJob> jobs = new ArrayList<IJob>();
		jobs.addAll(HelperFactory.getHelper().getEmploymentDAO().load(Employment.PILOT_TOUR_ID_FIELD, Option.Instance().getPilotTourID()));
		jobs.addAll(HelperFactory.getHelper().getWorkDAO().loadAll(Work.PILOT_TOUR_ID_FIELD, String.valueOf(Option.Instance().getPilotTourID()), null));
		boolean allIsDone = true;
		for (IJob job : jobs)
			if(!job.isDone())
			{
				allIsDone = false;
				break;
			}
		if (allIsDone && activity.isMyServiceRunning(GPSLogger.class))
			activity.stopService(new Intent(activity, GPSLogger.class));
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	private void fillUpEndButtonEnabling(){
        if(!canEditTasks()){
            disableButtons();
            return;
        }
		btEndTask.setEnabled(false);
		for(int i=1; i < tasks.size(); i++){
			Task task = tasks.get(i);
			if(task.getState() == eTaskState.Empty 
					&& task != startTask && task != endTask)
				return;
		}
		if (employment.isFromMobile() && startTask.getRealDate().equals(DateUtils.EmptyDate))
			return;
		btEndTask.setEnabled(true);
	}
	
	private void fillUpStartTask() {
		fillUpDate(tvStartTaskTime, tvStartTaskDate, startTask);
	}
	
	private void fillUpEndTask() {
		fillUpDate(tvEndTaskTime, tvEndTaskDate, endTask);
	}
	
	private void fillUpDate(TextView tvTime, TextView tvDate, Task task){
		tvTime.setText(task.getRealDate().equals(DateUtils.EmptyDate)
				? getString(R.string.def_empty_time)
				: DateUtils.HourMinutesFormat.format(task.getManualDate().equals(DateUtils.EmptyDate) 
						? task.getRealDate() 
						: task.getManualDate()));
		tvDate.setText(task.getRealDate().equals(DateUtils.EmptyDate)
				? getString(R.string.def_empty_date)
				: DateUtils.DateFormat.format(task.getManualDate().equals(DateUtils.EmptyDate) 
						? task.getRealDate() 
						: task.getManualDate()));
	}
	
	private void fillUpTitle() {
		activity.setTitle(employment.isWork()
				? employment.text() 
				: (employment.text() + (startTask.getDayPart().equals("") 
						? "" 
						: (", " + startTask.getDayPart()))));
	}
	
	private void fillUpTasks() {		
		List<Task> tasksExceptFirstAndLast = new ArrayList<Task>(tasks);
		tasksExceptFirstAndLast.remove(startTask);
		tasksExceptFirstAndLast.remove(endTask);
		taskAdapter = new TaskAdapter(activity, R.layout.row_task_template, tasksExceptFirstAndLast);
		lvTasks.setAdapter(taskAdapter);
		fillUpTitle();
		fillUpEndButtonEnabling();
		fillUpStartTask();
		fillUpEndTask();
	}	

	public void reloadData() {
		employment = HelperFactory.getHelper().getEmploymentDAO().load((int)Option.Instance().getEmploymentID());
		patientRemark = HelperFactory.getHelper().getPatientRemarkDAO().load( employment.getPatientID());
		tasks = HelperFactory.getHelper().getTaskDAO().load(Task.EMPLOYMENT_ID_FIELD, String.valueOf(Option.Instance().getEmploymentID()));
		diagnose = HelperFactory.getHelper().getDiagnoseDAO().load(employment.getPatientID());
		pilotTour = HelperFactory.getHelper().getPilotTourDAO().loadPilotTour(Option.Instance().getPilotTourID());
		worker = HelperFactory.getHelper().getWorkerDAO().load(Option.Instance().getWorkerID());
		workersInfo = HelperFactory.getHelper().getTourOncomingInfoDAO().LoadByOwnerID(employment.getId(), 2);
		initHeadTasks();		
	}
	
	public void btStartTaskTimerClick(View view) {
		Date currentTime = DateUtils.parseTimeOnly(DateUtils.GetServerDateTime());
		Date watchedTime = DateUtils.parseTimeOnly(DateUtils.parseTime(EMPLOYMENTS_ALLOWED_TIME));
		Date currentDate = DateUtils.getTodayDateOnly();
		Date planDate = DateUtils.getDateOnly(pilotTour.getPlanDate());
		if (startTask.getRealDate().equals(DateUtils.EmptyDate) 
				&& currentDate.getTime() > planDate.getTime() 
				&& currentTime.getTime() > watchedTime.getTime()){		
			String message = "Sie wollen den Einsatz vom " + DateUtils.DateFormat.format(planDate) 
							+ " beginnen, das aktuelle Datum ist " + DateUtils.DateFormat.format(currentDate) 
							+ ". Möchten Sie den Vorgang fortsetzen?";
			BaseDialog dialog = new BaseDialog(getString(R.string.attention), message);
			dialog.show(getFragmentManager(), "dialogDateCompare");
			getFragmentManager().executePendingTransactions();
		} else
			startTask();		
	}
	
	public void startTask(){
		if (!startTask.getRealDate().equals(DateUtils.EmptyDate))
			clearAllTasksDialog.show(getFragmentManager(), "clearAllTasksDialog");
		else
			updateStartTime();
	}
	
	public void updateStartTime() {
		checkAllTasksAndFillUp(eTaskState.Empty);
		startTask.setRealDate(DateUtils.getSynchronizedTime());
		startTask.setState(eTaskState.Done);
		startTask.setServerTime(Option.Instance().isTimeSynchronised());
		endTask.setRealDate(DateUtils.EmptyDate);
		HelperFactory.getHelper().getTaskDAO().save(startTask);
		HelperFactory.getHelper().getTaskDAO().save(endTask);
		fillUpStartTask();
		fillUpEndTask();		
		fillUpEndButtonEnabling();
		activity.supportInvalidateOptionsMenu();
		if(activity.isMyServiceRunning(GPSLogger.class) || !worker.isUseGPS())
			return;
		Option.Instance().gpsStartTime = System.nanoTime();
		Intent gpsLoggerServiceIntent = new Intent(activity, GPSLogger.class);
		gpsLoggerServiceIntent.putExtra("track_id", 0);
		activity.startService(gpsLoggerServiceIntent);
	}
	

	
	public void btEndTaskTimerClick(View view) {
		if (activity.hasQuestions && activity.assessmentsFragment.answeredCategories.size() != activity.assessmentsFragment.employmentCategories.size()){
			activity.mViewPager.setCurrentItem(1);
			return;
		}			
		if (!employment.isWork())
		{
			if (!Option.Instance().getIsSkippingPflegeOK()) {
				saveData(false);
				checkLeavingState();
			}
			else {
				saveData(false);
				startVerificationActivity(ACTIVITY_VERIFICATION_CODE, IS_FLEGE_OK);
			}
		}
		else
		{
			saveData(true);
			if(Option.Instance().getIsAuto())
			{
				startSyncActivity();
			}
			else
			{
				startPatientsActivity();
			}
		}
	}
	
	public void onTaskClick(View view) {
		Task task = (Task) view.getTag();
		if(task.getQualityResult().equalsIgnoreCase(""))
			return;
		DialogFragment dialog = null;
		switch(task.getQuality()) {
			case AdditionalTask.WEIGHT:
				dialog = new BaseInfoDialog(getString(R.string.weight), task.getQualityResult());
				break;
			case AdditionalTask.DETECT_RESPIRATION:
				dialog = new BaseInfoDialog(getString(R.string.detect_respiration), task.getQualityResult());
				break;
			case AdditionalTask.BALANCE:
				dialog = new BaseInfoDialog(getString(R.string.balance), task.getQualityResult());
				break;
			case AdditionalTask.BLUTZUCKER:
				dialog = new BaseInfoDialog(getString(R.string.blood_sugar), task.getQualityResult());
				break;
			case AdditionalTask.TEMPERATURE:
				dialog = new BaseInfoDialog(getString(R.string.temperature), task.getQualityResult());
				break;
			case AdditionalTask.BLUTDRUCK:
				dialog = new BaseInfoDialog(getString(R.string.blood_pressure), task.getQualityResult());
				break;
			case AdditionalTask.PULS:
				dialog = new BaseInfoDialog(getString(R.string.pulse), task.getQualityResult());
				break;
			default:
				return;
		}
		dialog.show(getFragmentManager(), "dialogTaskResult");
		getFragmentManager().executePendingTransactions();
	}
		
	public void onChangeState(View view) {
		if(!isClickable())
		{
			if (employment.isDone() || startEmploymentDialog.getFragmentManager() != null)
				return;
			startEmploymentDialog.show(getFragmentManager(), "");
			return;
		}
		Task task = (Task) view.getTag();
		clickedCheckBox = view;
		openDialogForAdditionalTask(task);
	}
	
	private void openDialogForAdditionalTask(Task task) {
		DialogFragment dialog = null;
		if (task.getState() == eTaskState.Done) {
			task.setQualityResult("");
			setTaskState(task);
			return;
		}
		switch(task.getQuality()) {
			case AdditionalTask.WEIGHT:
				dialog = new StandardTaskDialog(task, getString(R.string.weight), getString(R.string.gewicht_value), TaskTypes.weightTypeInput);
				break;
			case AdditionalTask.DETECT_RESPIRATION:
				dialog = new StandardTaskDialog(task, getString(R.string.detect_respiration), getString(R.string.atemzuge_value), TaskTypes.respirationTypeInput);
				break;
			case AdditionalTask.BALANCE:
				dialog = new StandardTaskDialog(task, getString(R.string.balance), getString(R.string.ml), TaskTypes.balanceTypeInput);
				break;
			case AdditionalTask.BLUTZUCKER:
				dialog = new StandardTaskDialog(task, getString(R.string.blood_sugar), getString(R.string.blutzucker_value), TaskTypes.blutzuckerTypeInput);
				break;
			case AdditionalTask.TEMPERATURE:
				dialog = new StandardTaskDialog(task, getString(R.string.temperature), getString(R.string.temperature_value), TaskTypes.temperatureTypeInput);
				break;
			case AdditionalTask.BLUTDRUCK:
				dialog = new BlutdruckTaskDialog(task);
				break;
			case AdditionalTask.PULS:
				dialog = new StandardTaskDialog(task, getString(R.string.pulse), getString(R.string.puls_value), TaskTypes.pulsTypeInput);
				break;
			default:
				setTaskState(task);
				return;
		}
		dialog.show(getFragmentManager(), "dialogTasks");
		dialog.setCancelable(false);
		getFragmentManager().executePendingTransactions();
		
	}
	
	public void setTaskState(Task task) {
		task.setRealDate(DateUtils.getSynchronizedTime());
		task.setServerTime(Option.Instance().isTimeSynchronised());
		if (!startTask.getManualDate().equals(DateUtils.EmptyDate))
			task.setManualDate(DateUtils.getAverageDate(startTask.getManualDate(), endTask.getManualDate()));
		if(!(task.getQuality() < 1 || task.getQuality() > 7)) {
			if (task.getQualityResult().equals(""))
				task.setState(eTaskState.UnDone);
			else
				task.setState(eTaskState.Done);
		}
		else
			task.setState((task.getState() != eTaskState.Done) 
					? eTaskState.Done
					: eTaskState.UnDone);
		try {
			((ImageView) clickedCheckBox).setImageDrawable(StaticResources.getBaseContext()
				.getResources().getDrawable((task.getState() == eTaskState.UnDone) 
						? R.drawable.ic_action_cancel
						: R.drawable.ic_action_accept));
			HelperFactory.getHelper().getTaskDAO().save(task);
			fillUpEndButtonEnabling();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initControls() {
		lvTasks = (ListView) rootView.findViewById(R.id.lvTasksList);
		
		btEndTask = (Button) rootView.findViewById(R.id.btEndTask);
		btStartTask = (Button) rootView.findViewById(R.id.btStartTask);
		
		tvStartTaskTime = (TextView) rootView.findViewById(R.id.tvStartTaskTime);
		tvStartTaskDate = (TextView) rootView.findViewById(R.id.tvStartTaskDate);
		
		tvEndTaskTime = (TextView) rootView.findViewById(R.id.tvEndTaskTime);
		tvEndTaskDate = (TextView) rootView.findViewById(R.id.tvEndTaskDate);
	}
	
	private void initDialogs() {
		startEmploymentDialog = new BaseInfoDialog(getString(R.string.attention), getString(R.string.dialog_start_employment));
		startEmploymentDialog.setCancelable(false);
		clearAllTasksDialog = new BaseDialog(getString(R.string.attention), getString(R.string.dialog_clear_tasks));
	}

	private void checkAllTasks(eTaskState state){
		checkAllTasks(state, DateUtils.getSynchronizedTime());
	}
	
	private void checkAllTasks(eTaskState state, Date date){
		for(Task task : tasks) {
			task.setState(state);
			task.setRealDate(date);
			task.setServerTime(Option.Instance().isTimeSynchronised());
			task.setQualityResult("");
		}
		HelperFactory.getHelper().getTaskDAO().save(tasks);	
	}
	
	private void checkAllTasksAndFillUp(eTaskState state){
		checkAllTasks(state);
		fillUpTasks();
	}
	
	private void saveEmployment() {
		if (Option.Instance().getEmploymentID() == BaseObject.EMPTY_ID)
			return;

		Employment empl = HelperFactory.getHelper().getEmploymentDAO().load((int)Option.Instance().getEmploymentID());
		EmploymentInterval emplInterval = new EmploymentInterval(empl.getId(), 
				(startTask.getManualDate().equals(DateUtils.EmptyDate) 
						? startTask.getRealDate()
						: startTask.getManualDate()), 
							(endTask.getManualDate().equals(DateUtils.EmptyDate) 
								? endTask.getRealDate()
								: endTask.getManualDate()));
		HelperFactory.getHelper().getEmploymentIntervalDAO().save(emplInterval);
		empl.setStartTime(emplInterval.getStartTime());
		empl.setStopTime(emplInterval.getStopTime());
		empl.setIsDone(true);
//		HelperFactory.getHelper().getEmploymentIntervalDAO().save(new EmploymentInterval(empl.getId(), empl.getStartTime(), empl.getStopTime()));
		HelperFactory.getHelper().getEmploymentDAO().save(empl);
	}
	
	public void clearEmployment() {
		Option.Instance().setEmploymentID(BaseObject.EMPTY_ID);
		Option.Instance().save();
	}
	
	public void clearAnswers() {
		HelperFactory.getHelper().getAnswerDAO().delete(HelperFactory.getHelper().getAnswerDAO().loadByEmploymentID(Option.Instance().getEmploymentID()));
		HelperFactory.getHelper().getAnsweredCategoryDAO().delete(HelperFactory.getHelper().getAnsweredCategoryDAO().LoadByEmploymentID((int)Option.Instance().getEmploymentID()));
		QuestionSetting questionSetting = HelperFactory.getHelper().getQuestionSettingDAO().load(employment.getId());
		questionSetting.setExtraCategoriesID("");
		HelperFactory.getHelper().getExtraCategoryDAO().delete(employment.getId());
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.catalogs:
			if(isEmploymentDone())
				return false;
			startCatalogsActivity();
			return true;
		case R.id.cancelAllTasks:
			if(isEmploymentDone())
				return false;
			showUndoneDialog();
			return true;
        case R.id.removeManualTasks:
            if(isEmploymentDone())
                return false;
            showRemoveManualTasksDialog();
            return true;
		case R.id.notes:
			startUserRemarksActivity(SIMPLE_MODE, ACTIVITY_USERREMARKS_CODE);
			return true;
		case R.id.info:
			showPatientInfo(true);
			return true;
		case R.id.manualInput:
			startManualInputActivity();
			return true;
		case R.id.address:
			startAddressActivity();
			return true;
		case R.id.additional_address:
			startAdditionalAddressActivity();
			return true;
		case R.id.doctors:
			startDoctorsActivity();
			return true;
		case R.id.relatives:
			startRelativesActivity();
			return true;
		case R.id.comments:
			showComments();
			return true;
		case R.id.diagnose:
			showDiagnose();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
    public static String DateToDDMMYYYYHHMMSS(Date date)
    {
        String retVal = date.toString();
        return retVal.substring(0, 2) + retVal.substring(3, 5) + retVal.substring(6, 10)
                + retVal.substring(11, 13) + retVal.substring(14, 16) + retVal.substring(17, 19);
    }
	
    protected void showDiagnose() {
		InfoBaseDialog dialog = new InfoBaseDialog(getString(R.string.menu_diagnose), diagnose.getName());
		dialog.show(getFragmentManager(), "");
		getFragmentManager().executePendingTransactions();
	}
	
	private void removeAdditionalTasks() {
		if((employment != null && employment.isFromMobile()) || isAllTasksAdditional()) {
			return;
		}

		for(Task task : tasks)
			if(task.getIsAdditionalTask())
				try {
					HelperFactory.getHelper().getTaskDAO().deleteById(task.getId());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		if(dialog.getTag().equals("dialogBack")){
		}
		else if (dialog.getTag().equals("dialogUndone")) {
		}
		else if(dialog.getTag().equals("removeManualTasksDialog")) {

        }
		else if (dialog.getTag().equals("dialogCheckLeavingState")) {
			
		}
		else if (dialog.getTag().equals("dialogTasks")) {

		}
		else if (dialog.getTag().equals("clearAllTasksDialog")) {
			updateStartTime();
		}
		else if(dialog.getTag().equals("dialogDateCompare")){
			startTask();
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		if(dialog.getTag().equals("dialogCheckLeavingState")){
			if (Option.Instance().getIsAuto())
				startUserRemarksActivity(SYNC_MODE, ACTIVITY_USERREMARKS_CODE);
			else
				startUserRemarksActivity(NO_SYNC_MODE, ACTIVITY_USERREMARKS_CODE);
		}
		if(dialog.getTag().equals("dialogTasks"))
		{			
			setTaskState(((StandardTaskDialog)dialog).getTask());
			fillUpEndButtonEnabling();
		}
		return;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.context = context;
	}

	protected void showPatientInfo(boolean isFromMenu){
		infos = HelperFactory.getHelper().getInformationDAO().load(Information.EMPLOYMENT_ID_FIELD, String.valueOf(employment.getId()));
		final String strInfos = InformationDAO.getInfoStr(infos, DateUtils.getSynchronizedTime(), isFromMenu);
		if (strInfos.equals(""))
			return;

		boolean isReadToday = InformationDAO.getInfoIsRead(infos, pilotTour.getPlanDate(),isFromMenu);

		if (isReadToday)
			return;

		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(getString(R.string.menu_patient_info));
		alert.setMessage(strInfos);
		alert.setPositiveButton(R.string.ok, new
				DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						for(Information info : infos) {
							if(info.getName().equals(strInfos)){
									info.setReadTime(DateUtils.getSynchronizedTime());
									HelperFactory.getHelper().getInformationDAO().save(infos);
								}
						}
					}
				});
		alert.setNegativeButton(isebase.cognito.tourpilot_apk.R.string.cancel, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.dismiss();
			}
		});
		alert.setCancelable(false);
		alert.create();
		alert.show();
	}

	protected void showComments(){
		InfoBaseDialog dialog = new InfoBaseDialog(getString(R.string.dialog_comments), 
				patientRemark.getName());
		dialog.show(getFragmentManager(), "");
	}

	private void checkLeavingState() {		
		BaseDialog dialogLeavingState = new BaseDialog(employment.getName(), getString(R.string.dialog_leaving_patient), getString(R.string.yes), getString(R.string.no));
		dialogLeavingState.show(getFragmentManager(), "dialogCheckLeavingState");
		dialogLeavingState.setCancelable(false);
		getFragmentManager().executePendingTransactions();
	}

	private void saveData(Boolean clearEmployment){
		if(endTask.UnDone() && !isEmploymentDone()) {
			endTask.setRealDate(DateUtils.getSynchronizedTime());
			endTask.setState(eTaskState.Done);
			endTask.setServerTime(Option.Instance().isTimeSynchronised());
			if(isAnyDone()){
				startTask.setState(eTaskState.Done);
				HelperFactory.getHelper().getTaskDAO().save(startTask);
			}				
			HelperFactory.getHelper().getTaskDAO().save(endTask);
			fillUpEndTask();			
		}
		if(clearEmployment)
		{
			saveEmployment();
			clearEmployment();
		}
	}
	
	private void initHeadTasks() {
		for (Task task : tasks) {
			if (task.isFirstTask())
				startTask = task;
			else if (task.isLastTask())
				endTask = task;
		}
		startTask.setState(eTaskState.Done);
//		endTask.setState(eTaskState.Done);
		endTask.setState(eTaskState.Empty);
	}

	private boolean isAllTasksAdditional() {
		for (Task task : tasks) {
			if (task.isFirstTask() || task.isLastTask())
				continue;
			if (!task.getIsAdditionalTask())
				return false;
		}
		return true;
	}
	
	public void clearTasks() {
		checkAllTasks(eTaskState.Empty, DateUtils.EmptyDate);
		startTask.setManualDate(DateUtils.EmptyDate);
		HelperFactory.getHelper().getTaskDAO().save(startTask);
		endTask.setManualDate(DateUtils.EmptyDate);
		HelperFactory.getHelper().getTaskDAO().save(endTask);
		removeAdditionalTasks();
		HelperFactory.getHelper().getUserRemarkDAO().delete((int)Option.Instance().getEmploymentID());
		if (activity.hasQuestions)
			clearAnswers();
		clearEmployment();
		startPatientsActivity();
	}
	
	public void undoneTasks() {
		checkAllTasksAndFillUp(eTaskState.UnDone);
		checkLeavingState();
	}
	
	public void startVerification() {
		startVerificationActivity(ACTIVITY_VERIFICATION_CODE, IS_FLEGE_OK);
	}
	
	public void setTaskValue(DialogFragment dialog) {
		Task task  = ((StandardTaskDialog)dialog).getTask();
		task.setQualityResult(((StandardTaskDialog)dialog).getValue());
		setTaskState(((StandardTaskDialog)dialog).getTask());
		taskAdapter.notifyDataSetChanged();
	}

	public void startUserRemarksActivity(Integer mode, int activityCode) {
		Intent userRemarksActivity = new Intent(activity.getApplicationContext(), UserRemarksActivity.class);
		userRemarksActivity.putExtra("Mode", mode);
		userRemarksActivity.putExtra("ViewMode", isEmploymentDone());
		if (mode == SIMPLE_MODE)
			startActivity(userRemarksActivity);
		else
			startActivityForResult(userRemarksActivity, activityCode);
	}
	
	protected void startCatalogsActivity() {
		Intent catalogsActivity = new Intent(activity.getApplicationContext(), CatalogsActivity.class);
		startActivity(catalogsActivity);
	}
	
	protected void startAddressActivity() {
		Intent addressActivity = new Intent(activity.getApplicationContext(), AddressPatientActivity.class);
		startActivity(addressActivity);
	}

	protected void startAdditionalAddressActivity() {
		Intent addressActivity = new Intent(activity.getApplicationContext(), AdditionalAddressActivity.class);
		startActivity(addressActivity);
	}
	
	protected void startDoctorsActivity() {
		Intent doctorsActivity = new Intent(activity.getApplicationContext(), DoctorsActivity.class);
		startActivity(doctorsActivity);
	}
	
	protected void startRelativesActivity() {
		Intent relativesActivity = new Intent(activity.getApplicationContext(), RelativesActivity.class);
		startActivity(relativesActivity);
	}
	
	protected void startSyncActivity() {
		Intent synchActivity = new Intent(activity.getApplicationContext(),
				SynchronizationActivity.class);
		startActivity(synchActivity);
	}

    protected void startPatientsActivity() {
		Intent patientsActivity = new Intent(activity.getApplicationContext(), PatientsActivity.class);
		startActivity(patientsActivity);
	}
	
	private void startManualInputActivity() {
		Intent manualInputActivity = new Intent(activity.getApplicationContext(), ManualInputActivity.class);
		startActivity(manualInputActivity);
	}
	
	private void startVerificationActivity(Integer requestCode,boolean isAllOK) {
		Intent verificationActivity = new Intent(activity.getApplicationContext(), VerificationActivity.class);
		verificationActivity.putExtra("isAllOK", isAllOK);
		startActivityForResult(verificationActivity, requestCode);
	}
		
	protected void showUndoneDialog() {
		BaseDialog dialog = new BaseDialog(getString(R.string.attention),
				getString(R.string.dialog_task_proof_undone));
		dialog.show(getFragmentManager(), "dialogUndone");
		getFragmentManager().executePendingTransactions();
	}

    protected void showRemoveManualTasksDialog() {
        BaseDialog dialog = new BaseDialog(getString(R.string.attention),
                getString(R.string.dialog_task_remove_manual));
        dialog.show(getFragmentManager(), "removeManualTasksDialog");
        getFragmentManager().executePendingTransactions();
    }

    protected void removeManualTasks() {
        int employmentID = (int)Option.Instance().getEmploymentID();
        HelperFactory.getHelper().getEmploymentDAO().delete(employmentID);
        HelperFactory.getHelper().getTaskDAO().deleteByEmploymentID(employmentID);
        HelperFactory.getHelper().getUserRemarkDAO().delete(employmentID);
        if (activity.hasQuestions)
            clearAnswers();
        clearEmployment();
        startPatientsActivity();
    }
	
	public boolean isClickable(){
		return !startTask.getRealDate().equals(DateUtils.EmptyDate) 
				&& endTask.getRealDate().equals(DateUtils.EmptyDate);
	}
	
	protected boolean isEmploymentDone(){
		return employment.isDone();
	}

	private boolean isAllDone(){
		  return (!startTask.getRealDate().equals(DateUtils.EmptyDate) 
		    && !endTask.getRealDate().equals(DateUtils.EmptyDate) && isEmploymentDone())
		    || (DateUtils.getTodayDateOnly().getTime() < DateUtils.getDateOnly(pilotTour.getPlanDate()).getTime());
	}

	private boolean canEditTasks(){
        return !employment.isFromMobile() ||
                DateUtils.getDateOnly(pilotTour.getPlanDate()).getTime()
                        >= DateUtils.getDateOnly(DateUtils.getSynchronizedTime()).getTime();
    }

	private void changeButtonsState(){
		if(isAllDone() || isEmploymentDone() || !canEditTasks()){
			disableButtons();
		}
	}

	private void disableButtons(){
        btStartTask.setEnabled(false);
        btEndTask.setEnabled(false);
    }
	
	private boolean isAnyDone(){
		List<Task> tasksExceptFirstAndLast = new ArrayList<Task>(tasks);
		tasksExceptFirstAndLast.remove(startTask);
		tasksExceptFirstAndLast.remove(endTask);
		for(Task task : tasksExceptFirstAndLast)
			if(task.IsDone())
				return true;
		return false;
	}
	
	private void clearEndTask() {
		endTask.setRealDate(DateUtils.EmptyDate);
		endTask.setState(eTaskState.UnDone);
		HelperFactory.getHelper().getTaskDAO().save(endTask);
	}
	
	private void saveQuestionSetting() {
		QuestionSetting questionSetting = HelperFactory.getHelper().getQuestionSettingDAO().load(employment.getId());
		if (questionSetting == null)
			return;
		questionSetting.setWasSent(true);
		HelperFactory.getHelper().getQuestionSettingDAO().save(questionSetting);
	}
	
}
