package isebase.cognito.tourpilot_apk.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class InfoBaseDialog extends DialogFragment implements OnClickListener {

	private String title;
	private String messageText;

	public InfoBaseDialog(){}
	
	public InfoBaseDialog(String title, String messageText) {
		this.title = title;
		this.messageText = messageText;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setNegativeButton(isebase.cognito.tourpilot_apk.R.string.close, this)
				.setMessage(messageText);
		return adb.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		dismiss();
	}

}
