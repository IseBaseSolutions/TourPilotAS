package isebase.cognito.tourpilot.Dialogs.Tasks;

import isebase.cognito.tourpilot.Data.Task.Task;
import isebase.cognito.tourpilot.Dialogs.BaseDialogListener;
import isebase.cognito.tourpilot.StaticResources.StaticResources;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StandardTaskDialog extends DialogFragment{

	private String title;
	private String hint;
	private boolean isViewMode;
	private int typeInput;
	private static EditText etValue;

	protected Task task;

	public Task getTask() {
		return task;
	}

	public String getValue() {
		return etValue.getText().toString();
	}

	private BaseDialogListener listener;

	public StandardTaskDialog(Task task, String title, String hint,
			int typeInput) {
		this();
		this.task = task;
		this.title = title;
		this.hint = hint;
		this.typeInput = typeInput;
		this.isViewMode = false;
	}

	public StandardTaskDialog(Task task, String title, String value,
			String hint, int typeInput) {
		this(task, title, hint, typeInput);
		this.isViewMode = true;
		etValue.setText(value);
	}

	protected StandardTaskDialog() {
		etValue = new EditText(StaticResources.getBaseContext());
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle(title);
		setCancelable(false);
		etValue.setTextColor(etValue.getHintTextColors().getDefaultColor());
		etValue.setHint(hint);
		etValue.setInputType(typeInput);
		etValue.setEnabled(!isViewMode);
		etValue.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
		etValue.addTextChangedListener(new TextWatcher(){
			Pattern pattern = Pattern.compile("[0-9,.]");
			
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0)
					return;
				Matcher matcher = pattern.matcher(String.valueOf(editable.toString().charAt(editable.length() - 1)));
				if(matcher.matches() && editable.toString().indexOf(".") == -1)
					return;
				if (editable.toString().indexOf(".") != -1)
					editable.replace(editable.length() - 1, editable.length(), ",");
				else
					editable.delete(editable.length() - 1, editable.length());

			}

			@Override
			public void beforeTextChanged(CharSequence charSequence, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
		});

		adb.setView(etValue);
		if (!isViewMode)
			adb.setPositiveButton(isebase.cognito.tourpilot.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							listener.onDialogPositiveClick(StandardTaskDialog.this);
						}
					});
		adb.setNegativeButton(isebase.cognito.tourpilot.R.string.cancel,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogNegativeClick(StandardTaskDialog.this);
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

	@Override
	public void onStart() {
		super.onStart();
		AlertDialog dialog = (AlertDialog) getDialog();
		if (dialog != null) {
			Button positiveButton = (Button) dialog
					.getButton(Dialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (etValue.getText().length() > 0 && !etValue.getText().toString().endsWith(",")) {
						dismiss();
						listener.onDialogPositiveClick(StandardTaskDialog.this);
					}
				}

			});
		}
	}


}
