package isebase.cognito.tourpilot_apk.Dialogs;

import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class BaseDialog extends DialogFragment{

	private String title;
	private String message;
	
	private String positiveName;
	private String negativeName;
	
	protected BaseDialogListener listener;
	
	public BaseDialog(){
		this("");
	}
	
	public BaseDialog(String title){
		this(title,"");
	}
		
	public BaseDialog(String title, String message){
		this.title = title;
		this.message = message;
		this.positiveName = StaticResources.getBaseContext().getString(isebase.cognito.tourpilot_apk.R.string.ok);
		this.negativeName = StaticResources.getBaseContext().getString(isebase.cognito.tourpilot_apk.R.string.cancel);
		
	}	
	public BaseDialog(String title, String message, String positiveName, String negativeName){
		this.title = title;
		this.message = message;
		this.positiveName = positiveName;
		this.negativeName = negativeName;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle(title);
		adb.setMessage(message);
		adb.setPositiveButton(positiveName,
				new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							listener.onDialogPositiveClick(BaseDialog.this);
						}
					});
		adb.setNegativeButton(negativeName,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogNegativeClick(BaseDialog.this);
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
