package isebase.cognito.tourpilot_apk.Activity.TasksAssessmentsActivity;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.TourOncomingInfoActivity;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseTimeSyncActivity;
import isebase.cognito.tourpilot_apk.Activity.QuestionActivities.RelatedQuestionsActivity;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialog;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialogListener;
import isebase.cognito.tourpilot_apk.Dialogs.Tasks.StandardTaskDialog;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

public class TasksAssessementsActivity extends BaseTimeSyncActivity implements BaseDialogListener{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	protected ViewPager mViewPager;
	
	TasksFragment tasksFragment;
	AssessmentsFragment assessmentsFragment;
	
	TasksAssessementsActivity instance;
	
	boolean hasQuestions;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_worker_option);
		StaticResources.setBaseContext(getBaseContext());
		instance = this;
		reloadData();
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setTimeSync(true);
		//switchToLastActivity();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tasks, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem manualInputMenu = menu.findItem(R.id.manualInput);
		MenuItem undoneTasksMenu = menu.findItem(R.id.cancelAllTasks);
		MenuItem removeManualTasksMenu = menu.findItem(R.id.removeManualTasks);
		MenuItem catalogsMenu = menu.findItem(R.id.catalogs);
		MenuItem infoMenu = menu.findItem(R.id.info);
		MenuItem commentsMenu = menu.findItem(R.id.comments);
		MenuItem diagnoseMenu = menu.findItem(R.id.diagnose);
		MenuItem addresseMenu = menu.findItem(R.id.address);
		MenuItem additionalAddresseMenu = menu.findItem(R.id.additional_address);
		MenuItem doctorsMenu = menu.findItem(R.id.doctors);
		MenuItem relativesMenu = menu.findItem(R.id.relatives);
		MenuItem notesMenu = menu.findItem(R.id.notes);
		MenuItem extraAssessments = menu.findItem(R.id.extraAssessments);
		MenuItem workersInfo = menu.findItem(R.id.tasks_workers_info);
		infoMenu.setVisible(tasksFragment.getInfos().size() != 0);
		commentsMenu.setVisible(!(tasksFragment.getPatientRemark() == null || tasksFragment.getPatientRemark().getName().length() == 0));
		diagnoseMenu.setVisible(!(tasksFragment.getDiagnose() == null || tasksFragment.getDiagnose().getName().length() == 0));
		notesMenu.setVisible(!tasksFragment.getStartTask().getRealDate().equals(DateUtils.EmptyDate));
		catalogsMenu.setVisible(tasksFragment.isClickable());
		undoneTasksMenu.setVisible(DateUtils.isToday(tasksFragment.employment.getDate()) && !tasksFragment.employment.isFromMobile());
		manualInputMenu.setVisible(!tasksFragment.isEmploymentDone() && !tasksFragment.isClickable() && DateUtils.isToday(tasksFragment.employment.getDate()));
		removeManualTasksMenu.setVisible(!tasksFragment.isEmploymentDone() && tasksFragment.employment.isFromMobile() && !tasksFragment.isClickable());
		extraAssessments.setVisible(false);
		if (assessmentsFragment != null) {
			extraAssessments.setVisible(!tasksFragment.getStartTask().getRealDate().equals(DateUtils.EmptyDate) && !tasksFragment.isEmploymentDone()
					&& assessmentsFragment.allCategoriesCount != assessmentsFragment.categories.size());
			//extraAssessments.setVisible(true);
		}

		if(tasksFragment.isEmploymentDone()) {
			undoneTasksMenu.setVisible(false);
			catalogsMenu.setVisible(false);
		}
		if(tasksFragment.employment.isAdditionalWork()) {
			catalogsMenu.setVisible(false);
			diagnoseMenu.setVisible(false);
			addresseMenu.setVisible(false);
			additionalAddresseMenu.setVisible(false);
			doctorsMenu.setVisible(false);
			relativesMenu.setVisible(false);
		}

		if (!HelperFactory.getHelper().getPatientAdditionalAddressDAO().isAdditionalAddressesAssigned(tasksFragment.employment.getPatientID())) {
			additionalAddresseMenu.setVisible(false);
		}

		workersInfo.setVisible(tasksFragment.workersInfo != null);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.catalogs:
			if(tasksFragment.isEmploymentDone())
				return false;
			tasksFragment.startCatalogsActivity();
			return true;
		case R.id.cancelAllTasks:
			if(tasksFragment.isEmploymentDone())
				return false;
			tasksFragment.showUndoneDialog();
			return true;
        case R.id.removeManualTasks:
            if(tasksFragment.isEmploymentDone())
                    return false;
            tasksFragment.showRemoveManualTasksDialog();
                return true;
		case R.id.notes:
			tasksFragment.startUserRemarksActivity(tasksFragment.SIMPLE_MODE, tasksFragment.ACTIVITY_USERREMARKS_CODE);
			return true;
		case R.id.info:
			tasksFragment.showPatientInfo(true);
			return true;
		case R.id.extraAssessments:
			assessmentsFragment.showExtraAssessmentsDialog();
			return true;
		case R.id.manualInput:
			startManualInputActivity();
			return true;
		case R.id.address:
			tasksFragment.startAddressActivity();
			return true;
		case R.id.additional_address:
			tasksFragment.startAdditionalAddressActivity();
			return true;
		case R.id.doctors:
			tasksFragment.startDoctorsActivity();
			return true;
		case R.id.relatives:
			tasksFragment.startRelativesActivity();
			return true;
		case R.id.comments:
			tasksFragment.showComments();
			return true;
		case R.id.diagnose:
			tasksFragment.showDiagnose();
			return true;
		case R.id.tasks_workers_info:
			startTourOncomingInfo(tasksFragment.workersInfo.getId());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed() {
		if(!tasksFragment.employment.isDone() && tasksFragment.isClickable()) {
			BaseDialog dialog = new BaseDialog(
					getString(R.string.attention),
					getString(R.string.dialog_task_proof_back));
			dialog.show(getSupportFragmentManager(), "dialogBack");
			getSupportFragmentManager().executePendingTransactions();
		}
		else
		{
			tasksFragment.clearEmployment();
			startPatientsActivity();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		if (assessmentsFragment != null)
//			assessmentsFragment.reloadCategories();
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			if(position == 0)
				return tasksFragment = new TasksFragment(instance);
			else
				return assessmentsFragment = new AssessmentsFragment(instance);
		}

		@Override
		public int getCount() {
			if (hasQuestions)
				return 2;
			return 1;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
				case 0:
					return "Tätigkeiten";
				case 1:
					return "Assessments";
			}
			return null;
		}
		
	}
	
//	public void btSyncClick(View view) {
//		//saveSettings();
//		startSyncActivity();
//	}
	
	public void btStartTaskTimerClick(View view) {
		tasksFragment.btStartTaskTimerClick(view);
	}
	
	public void btEndTaskTimerClick(View view) {
		tasksFragment.btEndTaskTimerClick(view);		
	}
	
	public void onChangeState(View view) {
		tasksFragment.onChangeState(view);
	}
	
	public void onTaskClick(View view) {
		tasksFragment.onTaskClick(view);
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		if (dialog.getTag().equals("dialogBack"))
		{
			tasksFragment.clearTasks();
		}
		else if (dialog.getTag().equals("dialogUndone")) {
			tasksFragment.undoneTasks();
		}
        else if(dialog.getTag().equals("removeManualTasksDialog")) {
            tasksFragment.removeManualTasks();
        }
		else if (dialog.getTag().equals("dialogCheckLeavingState")) {
			tasksFragment.startVerification();
		}
		else if (dialog.getTag().equals("dialogTasks")) {
			tasksFragment.setTaskValue(dialog);
		}
		else if (dialog.getTag().equals("clearAllTasksDialog")) {
			tasksFragment.updateStartTime();
		}
		else if (dialog.getTag().equals("extraAssessmentsDialog")) {
			assessmentsFragment.saveExtraAssessments(dialog);
		}
		else if (dialog.getTag().equals("relatedQuestionsDialog")) {
			Intent relatedQuestionsActivity = new Intent(getBaseContext(), RelatedQuestionsActivity.class);
			startActivity(relatedQuestionsActivity);
		}
		else if(dialog.getTag().equals("dialogDateCompare")){
			tasksFragment.startTask();
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		if(dialog.getTag().equals("dialogCheckLeavingState")){
			if (Option.Instance().getIsAuto())
				tasksFragment.startUserRemarksActivity(tasksFragment.SYNC_MODE, tasksFragment.ACTIVITY_USERREMARKS_CODE);
			else
				tasksFragment.startUserRemarksActivity(tasksFragment.NO_SYNC_MODE, tasksFragment.ACTIVITY_USERREMARKS_CODE);
		}
		if(dialog.getTag().equals("dialogTasks"))
		{			
			tasksFragment.setTaskState(((StandardTaskDialog)dialog).getTask());
		}
		return;
	}
	
	private void reloadData() {
		hasQuestions = HelperFactory.getHelper().getQuestionSettingDAO().loadAll((int)Option.Instance().getEmploymentID()) != null;	
	}
	
	public boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	private void startTourOncomingInfo(int id) {
		Intent tourOncomingInfoActivity = new Intent(
				getApplicationContext(), TourOncomingInfoActivity.class);
		tourOncomingInfoActivity.putExtra("id", id);
		startActivity(tourOncomingInfoActivity);
	}

}
