package isebase.cognito.tourpilot.Activity.WorkersOptionActivity;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.DataBase.HelperFactory;
import isebase.cognito.tourpilot.Dialogs.BaseDialogListener;
import isebase.cognito.tourpilot.Dialogs.BaseInfoDialog;
import isebase.cognito.tourpilot.Dialogs.MicurasPortDialog;
import isebase.cognito.tourpilot.StaticResources.StaticResources;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class WorkerOptionActivity extends BaseActivity implements
		BaseDialogListener {

	private SectionsPagerAdapter mSectionsPagerAdapter;

	public static final int PICKFILE_RESULT_CODE = 0;

	private ViewPager mViewPager;

	WorkerOptionActivity instance;

	OptionsFragment optionsFragment;
	WorkersFragment workersFragment;

	BaseInfoDialog noConectionDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_worker_option);
		instance = this;
		StaticResources.setBaseContext(getBaseContext());
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		PackageManager pm = getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo("org.microemu.android",
					PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		if (app_installed) {
			Uri uri = Uri.fromParts("package", "org.microemu.android", null);
			Intent it = new Intent(Intent.ACTION_DELETE, uri);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			StaticResources.getBaseContext().startActivity(it);
		}
		initDialogs();
		setDeviceSettings();
		switchToLastActivity();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_show_program_info:
			optionsFragment.versionFragmentDialog.show(
					getSupportFragmentManager(), "dialogVersion");
			return true;
		case R.id.action_clear_database:
			optionsFragment.busy(optionsFragment.getClearMode());
			return true;
		case R.id.action_db_backup:
			optionsFragment.busy(optionsFragment.getBackupMode());
			Toast.makeText(
					StaticResources.getBaseContext(),
					StaticResources.getBaseContext().getString(
							R.string.db_backup_created), Toast.LENGTH_SHORT)
					.show();
			return true;
		case R.id.action_db_restore:
			chooseFile();
			return true;
		case R.id.action_close_program:
			close();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void chooseFile() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("file/*");
		try {
			startActivityForResult(intent,
					WorkerOptionActivity.PICKFILE_RESULT_CODE);
		} catch (Exception e) {
			Toast.makeText(StaticResources.getBaseContext(),
					R.string.err_no_filemanager, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null || data.getData() == null)
			return;
		String path = data.getData().getPath();
		if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK)
			if (path.endsWith(".db")) {
				optionsFragment.busy(optionsFragment.RESTORE_MODE, path);
			}
			else
				Toast.makeText(StaticResources.getBaseContext(),
						R.string.err_not_db_file, Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onBackPressed() {

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
			if (position == 0)
				return optionsFragment = new OptionsFragment(instance);
			else
				return workersFragment = new WorkersFragment(instance);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Optionen";
			case 1:
				return "Mitarbeiter";
			}
			return null;
		}
	}

	public void onLockOptions(View view) {
		optionsFragment.onLockOptionsChecked();
	}

	public void btSyncClick(View view) {
		saveSettings();
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null)
			startSyncActivity();
		else
			noConectionDialog.show(getSupportFragmentManager(),
					"noConectionDialog");
	}

	private void initDialogs() {
		noConectionDialog = new BaseInfoDialog(getString(R.string.attention),
				getString(R.string.dialog_no_connection_sync));
	}

	public void saveSettings() {
		Option.Instance().setServerIP(
				optionsFragment.etServerIP.getText().toString());
		Option.Instance().setServerPort(
				Integer.parseInt(optionsFragment.etServerPort.getText()
						.toString()));
		Option.Instance().setPhoneNumber(optionsFragment.etPhoneNumber.getText()
				.toString().trim());
		Option.Instance().setPrevWorkerID(BaseObject.EMPTY_ID);
		Option.Instance().setWorkerID(BaseObject.EMPTY_ID);
	}

	public void switchToLastActivity() {
		if (Option.Instance().getWorkID() != -1)
			startAdditionalWorksActivity();
		else if (Option.Instance().getEmploymentID() != -1)
			startTasksActivity();
		else if (Option.Instance().getPilotTourID() != -1)
			startPatientsActivity();
		else if (Option.Instance().getWorkerID() != -1
				&& HelperFactory.getHelper().getPilotTourDAO().loadPilotTours()
						.size() > 0)
			startToursActivity();
		else if (HelperFactory.getHelper().getWorkerDAO().load().size() > 0)
			mViewPager.setCurrentItem(1);
		else
			mViewPager.setCurrentItem(0);
		return;
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		if (dialog.getTag().equals("dialogPin")) {
			saveSettings();
			workersFragment.logIn();
		}
		if (dialog.getTag().equals("micurasPortDialog")) {
			Option.Instance().setServerPort(
					Integer.parseInt(((MicurasPortDialog) dialog).port));
			optionsFragment.etServerPort
					.setText(((MicurasPortDialog) dialog).port);
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		return;
	}

	private void setDeviceSettings() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		StaticResources.height = displaymetrics.heightPixels;
		StaticResources.width = displaymetrics.widthPixels;
	}

}
