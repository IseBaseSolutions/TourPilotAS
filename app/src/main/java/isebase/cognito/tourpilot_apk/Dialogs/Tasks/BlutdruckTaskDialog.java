package isebase.cognito.tourpilot_apk.Dialogs.Tasks;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Data.Task.Task;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialogListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BlutdruckTaskDialog extends StandardTaskDialog{
	
	private boolean isViewMode;
		
	@Override
	public String getValue(){
		return etMaxValue.getText().toString() + "/" + etMinValue.getText().toString();
	}
	
	private EditText etMinValue;
	private EditText etMaxValue;
	private BaseDialogListener listener;
		
	public BlutdruckTaskDialog(Task task){
		this();
		this.task = task;
		this.isViewMode = false;
	}
	
	private BlutdruckTaskDialog(){
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle(getString(R.string.blood_pressure));
		setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_blutdruck_task, null);
        adb.setView(view);

        etMinValue = (EditText) view.findViewById(R.id.etMinValue);
		etMinValue.setHint(getString(R.string.min));
		etMinValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
		etMinValue.setEnabled(!isViewMode);
		etMinValue.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});

        etMaxValue = (EditText) view.findViewById(R.id.etMaxValue);
		etMaxValue.setHint(getString(R.string.max));
		etMaxValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
		etMaxValue.setEnabled(!isViewMode);
		etMaxValue.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});

		if(!isViewMode)
			adb.setPositiveButton(isebase.cognito.tourpilot_apk.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							listener.onDialogPositiveClick(BlutdruckTaskDialog.this);
						}
					});
		adb.setNegativeButton(isebase.cognito.tourpilot_apk.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogNegativeClick(BlutdruckTaskDialog.this);
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
			throw new ClassCastException(activity.toString() + " must implement BaseDialogListener");
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		AlertDialog dialog = (AlertDialog) getDialog();
		if (dialog != null) {
			Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(etMaxValue.getText().length() > 0 && etMinValue.getText().length() > 0){
						dismiss();
						listener.onDialogPositiveClick(BlutdruckTaskDialog.this);
					}
				}
				
			});
		}
	}
}
