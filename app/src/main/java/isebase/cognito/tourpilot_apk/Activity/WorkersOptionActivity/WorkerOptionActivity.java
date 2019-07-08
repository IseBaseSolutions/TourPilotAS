package isebase.cognito.tourpilot_apk.Activity.WorkersOptionActivity;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialogListener;
import isebase.cognito.tourpilot_apk.Dialogs.BaseInfoDialog;
import isebase.cognito.tourpilot_apk.Dialogs.MicurasPortDialog;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WorkerOptionActivity extends BaseActivity implements
		BaseDialogListener {

	private SectionsPagerAdapter mSectionsPagerAdapter;

	public static final int PICKFILE_RESULT_CODE = 0;

	private int READ_PHONE_PERMISSION_CODE = 1;

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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            requestReadPhonePermission();
        } else{
           init();
        }
	}

	private void init(){
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
            String name = getBaseContext().getPackageName();
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

    private void requestReadPhonePermission() {

        String[] permissions = new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        final List<String> noGrantedPermissions = new ArrayList<>();
        for (String permission: permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                noGrantedPermissions.add(permission);
        }

        if (noGrantedPermissions.size() > 0) {
            ActivityCompat.requestPermissions(WorkerOptionActivity.this,
                    noGrantedPermissions.toArray(new String[noGrantedPermissions.size()]),
                    READ_PHONE_PERMISSION_CODE);
        }else{
           init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_PHONE_PERMISSION_CODE) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                init();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
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
