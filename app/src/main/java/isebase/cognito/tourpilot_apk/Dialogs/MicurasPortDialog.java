package isebase.cognito.tourpilot_apk.Dialogs;

import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class MicurasPortDialog extends BaseDialog {
	
	public String port;
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final List<String> list = Arrays.asList(
				"Berlin - Port  4440", 
				"Bremen - Port  4441", 
				"Duesseldorf - Port  4442",
				"Hamburg - Port  4443",
				"Koeln - Port  4444",
				"Krefeld - Port  4445",
				"MuenchenDachau - Port  4446",
				"MuenchenOst - Port  4447",
				"Muenster - Port  4448",
				"Nuernberg - Port  4449");
		CharSequence[] cs = list.toArray(new CharSequence[list.size()]);
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
				.setTitle("Wählen Sie bitte den Ort Ihres Pflegedienstes aus:")
				.setSingleChoiceItems(cs, -1, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						port = list.get(which).split("  ")[1];
					}
				})
				.setPositiveButton(isebase.cognito.tourpilot_apk.R.string.ok, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogPositiveClick(MicurasPortDialog.this);
					}
					
				});
		return adb.create();
	}

}
