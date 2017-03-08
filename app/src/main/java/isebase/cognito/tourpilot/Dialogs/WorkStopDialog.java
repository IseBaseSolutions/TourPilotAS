package isebase.cognito.tourpilot.Dialogs;

import isebase.cognito.tourpilot.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class WorkStopDialog extends BaseDialog {	
	
	private String title;
	private Date startTime;
	
	public WorkStopDialog(String title, Date startTime) {
		this.title = title;
		this.startTime = startTime;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMessage(String.format("%s %s", getString(R.string.started_at), format.format(startTime)))
				.setPositiveButton(
						isebase.cognito.tourpilot.R.string.stop, new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								listener.onDialogPositiveClick(WorkStopDialog.this);
							}

						});
		return adb.create();
	}

}