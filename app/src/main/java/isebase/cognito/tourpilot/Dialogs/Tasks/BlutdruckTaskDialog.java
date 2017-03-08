package isebase.cognito.tourpilot.Dialogs.Tasks;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Data.Task.Task;
import isebase.cognito.tourpilot.Dialogs.BaseDialogListener;
import isebase.cognito.tourpilot.StaticResources.StaticResources;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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
	
	public BlutdruckTaskDialog(Task task, String value){
		this();
		this.task = task;
		this.isViewMode = true;
		String[] parsedData = value.split("\\:");
		String minValue = parsedData.length > 0 ? parsedData[0] : "";
		String maxValue = parsedData.length > 1 ? parsedData[1] : "";
		etMaxValue.setText(maxValue);
		etMinValue.setText(minValue);
	}
	
	private BlutdruckTaskDialog(){
		etMinValue = new EditText(StaticResources.getBaseContext());
		etMaxValue = new EditText(StaticResources.getBaseContext());
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle(getString(R.string.blood_pressure));
		setCancelable(false);
		etMinValue.setTextColor(etMinValue.getHintTextColors().getDefaultColor());
		etMinValue.setHint(getString(R.string.min));
		etMinValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
		etMinValue.setEnabled(!isViewMode);
		etMinValue.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
		
		etMaxValue.setTextColor(etMaxValue.getHintTextColors().getDefaultColor());
		etMaxValue.setHint(getString(R.string.max));
		etMaxValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
		etMaxValue.setEnabled(!isViewMode);
		etMaxValue.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
		
		LinearLayout ll = new LinearLayout(StaticResources.getBaseContext());
		ll.addView(etMaxValue);
		ll.addView(etMinValue);
		
		adb.setView(ll);
		if(!isViewMode)
			adb.setPositiveButton(isebase.cognito.tourpilot.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							listener.onDialogPositiveClick(BlutdruckTaskDialog.this);
						}
					});
		adb.setNegativeButton(isebase.cognito.tourpilot.R.string.cancel,
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
