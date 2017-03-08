package isebase.cognito.tourpilot.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class WorkTypeDialog extends BaseDialog {
	
	private String title;
	
	public WorkTypeDialog(String title) {
		this.title = title;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
			.setTitle(title)
			.setPositiveButton(
					isebase.cognito.tourpilot.R.string.start, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,	int which) {
							listener.onDialogPositiveClick(WorkTypeDialog.this);
						}

					})					
			.setNegativeButton(
					isebase.cognito.tourpilot.R.string.menu_manual_input, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							listener.onDialogNegativeClick(WorkTypeDialog.this);
						}

					});
		return adb.create();
	}

}
