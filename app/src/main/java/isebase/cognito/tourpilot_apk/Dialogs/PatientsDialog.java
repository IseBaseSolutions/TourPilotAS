package isebase.cognito.tourpilot_apk.Dialogs;

import isebase.cognito.tourpilot_apk.Data.Patient.Patient;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;

public class PatientsDialog extends BaseDialog {
	
	private List<Patient> patients;
	private String[] patientNames;
	private boolean[] selectedPatients;

	private String title;
	
	public PatientsDialog(List<Patient> patients, String title) {
		this.patients = patients;
		this.title = title;
		initPatientNames();
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMultiChoiceItems(patientNames, selectedPatients, new OnMultiChoiceClickListener() {
			
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						selectedPatients[which] = isChecked;
					}
			
				})
				.setPositiveButton(isebase.cognito.tourpilot_apk.R.string.ok, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogPositiveClick(PatientsDialog.this);
					}
					
				});
		return adb.create();
	}
		
	private void initPatientNames() {
		patientNames = new String[patients.size()];
		int counter = 0;
		for (Patient patient : patients)
			patientNames[counter++] = patient.getFullName(); 
		selectedPatients = new boolean[patientNames.length];
	}
	
	public String getSelectedPatientIDs() {
		String patientIDs = "";
		for (int i = 0; i < selectedPatients.length; i++)
			if (selectedPatients[i])
				patientIDs += (patientIDs.equals("") ? "" : ",") + patients.get(i).getId();
		return patientIDs;
	}
	
}
