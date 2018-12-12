package isebase.cognito.tourpilot_apk.Activity.BaseActivities;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.AdditionalWorksActivity;
import isebase.cognito.tourpilot_apk.Activity.ManualInputActivity;
import isebase.cognito.tourpilot_apk.Activity.PatientsActivity;
import isebase.cognito.tourpilot_apk.Activity.SendLogActivity;
import isebase.cognito.tourpilot_apk.Activity.SynchronizationActivity;
import isebase.cognito.tourpilot_apk.Activity.ToursActivity;
import isebase.cognito.tourpilot_apk.Activity.UserRemarksActivity;
import isebase.cognito.tourpilot_apk.Activity.VerificationActivity;
import isebase.cognito.tourpilot_apk.Activity.TasksAssessmentsActivity.TasksAssessementsActivity;
import isebase.cognito.tourpilot_apk.Activity.WorkersOptionActivity.WorkerOptionActivity;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialog;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {

	protected BaseDialog closeDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StaticResources.setBaseContext(getBaseContext());
		HelperFactory.setHelper(getBaseContext());
		if (!isMainActivity())
			closeDialog = new BaseDialog(getString(R.string.dialog_close),
					getString(R.string.dialog_closing));
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, Throwable e) {
				handleUncaughtException(thread, e);
			}

		});
	}

	protected boolean isMainActivity() {
		return false;
	}

	protected void criticalClose() {
		Option.Instance().clearSelected();
		Option.Instance().save();
		Intent optionActivity = new Intent(getApplicationContext(),
				WorkerOptionActivity.class);
		startActivity(optionActivity);
	}

	protected void criticalPatientsClose() {
		Option.Instance().setWorkID(BaseObject.EMPTY_ID);
		Option.Instance().setPilotTourID(BaseObject.EMPTY_ID);
		Option.Instance().setEmploymentID(BaseObject.EMPTY_ID);
		Option.Instance().save();
		Intent intent = new Intent(getApplicationContext(), ToursActivity.class);
		startActivity(intent);
	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		final int keycode = event.getKeyCode();
		final int action = event.getAction();
		if (keycode == KeyEvent.KEYCODE_MENU && action == KeyEvent.ACTION_UP) {
			return true; // consume the key press
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.base_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_close_program:
			showCloseDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showCloseDialog() {
		closeDialog.show(getSupportFragmentManager(), "closeDialog");
	}

	protected void startWorkersActivity() {
		Intent workersActivity = new Intent(getApplicationContext(),
				WorkerOptionActivity.class);
		startActivity(workersActivity);
	}

	protected void startPatientsActivity() {
		Intent patientsActivity = new Intent(getApplicationContext(),
				PatientsActivity.class);
		startActivity(patientsActivity);
	}

	protected void startSyncActivity() {
		Intent synchActivity = new Intent(getApplicationContext(),
				SynchronizationActivity.class);
		startActivity(synchActivity);
	}

	protected void startToursActivity() {
		Intent toursActivity = new Intent(getApplicationContext(),
				ToursActivity.class);
		startActivity(toursActivity);
	}

	protected void startTasksActivity() {
		Intent tasksActivity = new Intent(getApplicationContext(),
				TasksAssessementsActivity.class);
		startActivity(tasksActivity);
		return;
	}

	protected void startAdditionalWorksActivity() {
		Intent additionalWorksActivity = new Intent(getApplicationContext(),
				AdditionalWorksActivity.class);
		startActivity(additionalWorksActivity);
	}

	protected void startOptionsActivity() {
		Intent optionsActivity = new Intent(getApplicationContext(),
				WorkerOptionActivity.class);
		startActivity(optionsActivity);
	}

	protected void startManualInputActivity() {
		Intent manualInputActivity = new Intent(getApplicationContext(),
				ManualInputActivity.class);
		startActivity(manualInputActivity);
	}

	protected void startUserRemarksActivity() {
		Intent userRemarksActivity = new Intent(getApplicationContext(),
				UserRemarksActivity.class);
		startActivity(userRemarksActivity);
	}

	protected void startVerificationActivity(Integer requestCode,
			boolean isAllOK) {
		Intent VerificationActivity = new Intent(getApplicationContext(),
				VerificationActivity.class);
		VerificationActivity.putExtra("isAllOK", isAllOK);
		startActivityForResult(VerificationActivity, requestCode);
	}

	public void close() {
		Intent intent = new Intent(this, WorkerOptionActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
		moveTaskToBack(true);
	}

	protected boolean isPilotTourPresent() {
		return Option.Instance().getPilotTourID() != BaseObject.EMPTY_ID
				&& HelperFactory.getHelper().getPilotTourDAO()
						.loadPilotTour(Option.Instance().getPilotTourID()) != null;
	}

	protected boolean isWorkerPresent() {
		return Option.Instance().getWorkerID() != BaseObject.EMPTY_ID;
	}

	protected void switchToNextActivity() {
		Intent nextActivity = isPilotTourPresent() ? new Intent(
				getApplicationContext(), PatientsActivity.class)
				: isWorkerPresent() ? new Intent(getApplicationContext(),
						ToursActivity.class) : new Intent(
						getApplicationContext(), WorkerOptionActivity.class);
		startActivity(nextActivity);
	}

	public void handleUncaughtException(Thread thread, Throwable e) {
		e.printStackTrace();
		Intent intent = new Intent(getBaseContext(), SendLogActivity.class);
		intent.setAction("com.mydomain.SEND_LOG");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
		startActivity(intent);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		System.exit(1);
	}

}
