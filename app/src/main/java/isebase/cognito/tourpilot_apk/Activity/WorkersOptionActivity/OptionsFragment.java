package isebase.cognito.tourpilot_apk.Activity.WorkersOptionActivity;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Dialogs.InfoBaseDialog;
import isebase.cognito.tourpilot_apk.Dialogs.MicurasPortDialog;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;
import isebase.cognito.tourpilot_apk.Utils.DataBaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class OptionsFragment extends Fragment {

	public EditText etServerIP;
	public EditText etServerPort;
	public EditText etPhoneNumber;
	
	private ProgressBar pbBusy;
	
	private CheckBox cbLockOptions;
	
	private View rootView;
	
	public DialogFragment versionFragmentDialog;
	public DialogFragment noConnectionDialog;
	public DialogFragment noIPEnteredDialog;
	public boolean isMicuraVersion = false;
	public boolean isNHKVersion = false;
	public boolean isImpulseVersion = false;
	public boolean isHansenVersion = false;
	public boolean isTestVersion = false;
	public boolean isClient114 = false;
	public DialogFragment micurasPortDialog;
	
	public WorkerOptionActivity activity;
	
	public final int BACKUP_MODE = 0;
	public final int CLEAR_MODE = 1;
	public final int RESTORE_MODE = 2;
	
	public int getBackupMode() {
		return BACKUP_MODE;
	}

	public int getClearMode() {
		return CLEAR_MODE;
	}

	public int getRestoreMode() {
		return RESTORE_MODE;
	}
	
	public OptionsFragment() {
		
	}	
	
	public OptionsFragment(WorkerOptionActivity activity) {
		this.activity = activity;
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		rootView = inflater.inflate(
				R.layout.activity_options, container, false);
		initControls();
		fillUp();
		initDialogs();
		return rootView;
	}

	public void onLockOptionsChecked() {
		initControlsState();
		Option.Instance().setLockOptions(cbLockOptions.isChecked());
		Option.Instance().save();
	}
	
	
	private void initControls() {
		etServerIP = (EditText) rootView.findViewById(R.id.etServerIP);
		etServerPort = (EditText) rootView.findViewById(R.id.etServerPort);
		etPhoneNumber = (EditText) rootView.findViewById(R.id.etPhoneNumber);
		cbLockOptions = (CheckBox) rootView.findViewById(R.id.cb_LockOptions);
		pbBusy = (ProgressBar) rootView.findViewById(R.id.pbClearDB);
		cbLockOptions.setChecked(Option.Instance().isLockOptions());
	}
	
	private void fillUp() {
		etServerIP.setText(Option.Instance().getServerIP());
		etServerPort.setText(String.valueOf(Option.Instance().getServerPort()));
		if (isNHKVersion && Option.Instance().getServerIP().equals("")) {
			etServerIP.setText("mde.cognito-service.de");
			etServerPort.setText("4448");
		}
		if (isImpulseVersion && Option.Instance().getServerIP().equals("")) {
			etServerIP.setText("impulse.rz-24.de");
			etServerPort.setText("4448");
		}
		if (isHansenVersion && Option.Instance().getServerIP().equals("")) {
			etServerIP.setText("85.183.42.165");
			etServerPort.setText("4448");
		}
		if (isTestVersion && Option.Instance().getServerIP().equals("")) {
			etServerIP.setText("192.168.1.1");
			etServerPort.setText("4449");
		}
		if (isClient114 && Option.Instance().getServerIP().equals("")) {
			etServerIP.setText("85.16.72.214");
			etServerPort.setText("4449");
		}
		etPhoneNumber.setText(Option.Instance().getPhoneNumber());
		initControlsState();
	}
	
	private void initControlsState() {
		etServerIP.setEnabled(!cbLockOptions.isChecked());
		etServerPort.setEnabled(!cbLockOptions.isChecked());
		etPhoneNumber.setEnabled(!cbLockOptions.isChecked());
	}
	
	public void busy(final int mode, final String... data){
		pbBusy.setVisibility(View.VISIBLE);
		//syncButton.setEnabled(false);
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try{
					switch(mode){
						case BACKUP_MODE:
							DataBaseUtils.backup();
							break;
						case CLEAR_MODE:
							HelperFactory.getHelper().clearAllData();
							break;
						case RESTORE_MODE:
							DataBaseUtils.restore(data[0]);
							break;
					}							
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}
							
			@Override
			protected void onPostExecute(Void result) {
				int textID = 0;
				switch(mode){
					case BACKUP_MODE:
						textID = R.string.db_backup_created;
						break;
					case CLEAR_MODE:
						textID = R.string.db_cleared;
						break;
					case RESTORE_MODE:
						textID = R.string.db_backup_restored;
						Option.Instance().resetOptions();
						activity.switchToLastActivity();
					default:
						return;
				}
				pbBusy.setVisibility(View.INVISIBLE);
				Toast.makeText(StaticResources.getBaseContext()
						, textID
						, Toast.LENGTH_SHORT).show();
			}
		}.execute();
	}
	

	
	private void initDialogs() {
		versionFragmentDialog = new InfoBaseDialog(
			getString(R.string.menu_program_info), 
			String.format("%s %s - %d\n%s %s"
					, getString(R.string.program_version)
					, Option.Instance().getVersion()
					, Option.Instance().getVersionCode()
					, getString(R.string.data_base_version)
					, HelperFactory.getHelper().getReadableDatabase().getVersion())
			);
		noIPEnteredDialog = new InfoBaseDialog(
				getString(R.string.dialog_connection_problems),
				getString(R.string.dialog_no_ip_entered));
		noConnectionDialog = new InfoBaseDialog(
				getString(R.string.dialog_connection_problems),
				getString(R.string.dialog_no_connection));
		if (isMicuraVersion && Option.Instance().getServerIP().equals("")) {
			micurasPortDialog = new MicurasPortDialog();
			micurasPortDialog.show(getFragmentManager(), "micurasPortDialog");
			micurasPortDialog.setCancelable(false);
			etServerIP.setText("server.micura.de");
		}
	}
	
}
