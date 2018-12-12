package isebase.cognito.tourpilot_apk.Dialogs;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Data.Worker.Worker;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class WorkerPhoneNumbersDialog extends DialogFragment {
	private Worker worker;
	
	public WorkerPhoneNumbersDialog(Worker worker, Context context) {
		this.worker = worker;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
		.setTitle(worker.getName())
		.setItems(getPhoneNumbers(), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
				{
					Intent dialIntent = new Intent(Intent.ACTION_DIAL);
					String s = getPhoneNumbers()[which].split(".: ")[1];
					dialIntent.setData(Uri.parse("tel:" + s));
					startActivity(dialIntent);
				}else {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					String s = getPhoneNumbers()[which].split(".: ")[1];
					callIntent.setData(Uri.parse("tel:" + s));
					startActivity(callIntent);
				}
			}
		})
		.setNegativeButton(isebase.cognito.tourpilot_apk.R.string.back, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int id) {
				
			}
			
		});
		return adb.create();
	}
	
	private String[] getPhoneNumbers() {
		List<String> phoneNumbersList = new ArrayList<String>();
		if (!worker.getWorkPhone().equals(""))
			phoneNumbersList.add(StaticResources.getBaseContext().getString(R.string.phone) + " " + worker.getWorkPhone());
		if (!worker.getPrivatePhone().equals(""))
			phoneNumbersList.add(StaticResources.getBaseContext().getString(R.string.phone_private) + " " + worker.getPrivatePhone());
		if (!worker.getMobilePhone().equals(""))
			phoneNumbersList.add(StaticResources.getBaseContext().getString(R.string.phone_mobile) + " " + worker.getMobilePhone());
		String[] phoneNumbers = new String[phoneNumbersList.size()];
		int counter = 0;
		for (String phoneNumber : phoneNumbersList)
			phoneNumbers[counter++] = phoneNumber;
		return phoneNumbers;
	}
	
}
