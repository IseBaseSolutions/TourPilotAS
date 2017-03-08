package isebase.cognito.tourpilot.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class BaseInfoDialog extends DialogFragment {


	private String title;
	private String message;
	
	protected BaseDialogListener listener;
	
	public BaseInfoDialog(){
		this("");
	}
	
	public BaseInfoDialog(String title){
		this(title,"");
	}
		
	public BaseInfoDialog(String title, String message){
		this.title = title;
		this.message = message;
	}	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle(title);
		adb.setMessage(message);
		adb.setPositiveButton(isebase.cognito.tourpilot.R.string.ok,
				new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							listener.onDialogPositiveClick(BaseInfoDialog.this);
						}
					});
		return adb.create();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (BaseDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement BaseDialogListener");
		}
	}

}
