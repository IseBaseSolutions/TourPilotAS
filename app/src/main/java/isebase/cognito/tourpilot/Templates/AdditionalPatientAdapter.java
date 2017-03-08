package isebase.cognito.tourpilot.Templates;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Data.Patient.Patient;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;

public class AdditionalPatientAdapter extends ArrayAdapter<Patient> implements Filterable{

	private List<AdditionalPatientHolder> patients;
	private List<AdditionalPatientHolder> filteredPatients;
	private AdditionalPatientHolder lastCheckedAdditionalPatientHolder;
	private int layoutResourceId;
	private Context context;
	private Button btOK;
	
	public AdditionalPatientAdapter(Context context, int layoutResourceId, List<Patient> additionalPatients) {
		super(context, layoutResourceId, additionalPatients);
		
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		patients = new ArrayList<AdditionalPatientHolder>();
		for (Patient additionalPatient : additionalPatients)
			patients.add(new AdditionalPatientHolder(additionalPatient));
		filteredPatients = patients;
		initControls();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);
		
		AdditionalPatientHolder additionalPatientHolder = filteredPatients.get(position);
		additionalPatientHolder.rdbAdditionalPatient = (RadioButton) row.findViewById(R.id.rdb);
		additionalPatientHolder.rdbAdditionalPatient.setTag(additionalPatientHolder);
		additionalPatientHolder.rdbAdditionalPatient.setOnCheckedChangeListener(onCheckboxCheckedListener);
		additionalPatientHolder.rdbAdditionalPatient.setText(additionalPatientHolder.additionalPatient.getFullName());
		additionalPatientHolder.rdbAdditionalPatient.setChecked(additionalPatientHolder == lastCheckedAdditionalPatientHolder);
		
		return row;
	}
	
	OnCheckedChangeListener onCheckboxCheckedListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (!isChecked)
				return;
			if (lastCheckedAdditionalPatientHolder == null) 
				btOK.setEnabled(true);
			else
				lastCheckedAdditionalPatientHolder.rdbAdditionalPatient.setChecked(false);
			lastCheckedAdditionalPatientHolder = (AdditionalPatientHolder) buttonView.getTag();
			lastCheckedAdditionalPatientHolder.rdbAdditionalPatient.setChecked(isChecked);
		}
	};	
	
	public class AdditionalPatientHolder {		
		public RadioButton rdbAdditionalPatient;
		
		public Patient additionalPatient;
		public AdditionalPatientHolder(Patient additionalPatient) {
			this.additionalPatient = additionalPatient;
		}
	}
		
	public Patient getSelectedAdditionalPatient() {
		return lastCheckedAdditionalPatientHolder.additionalPatient;
	}
	
	private void initControls() {
		btOK = (Button) ((Activity) context).findViewById(R.id.btOK);
	}
	
	@Override
    public Filter getFilter() {
        Filter filter = new Filter() {
	        @Override
	        @SuppressWarnings("unchecked")
	        protected void publishResults(CharSequence constraint, FilterResults results) {
	        	filteredPatients = (List<AdditionalPatientHolder>) results.values;
	            notifyDataSetChanged();
	        }
	
	        @Override
	        protected FilterResults performFiltering(CharSequence constraint) {
	            FilterResults results = new FilterResults();
	            List<AdditionalPatientHolder> filteredPatients = new ArrayList<AdditionalPatientHolder>();
	            constraint = constraint.toString().toLowerCase();
	            for (AdditionalPatientHolder patient : patients) 
	                if (patient.additionalPatient.getFullName().toLowerCase().startsWith(constraint.toString()))
	                	filteredPatients.add(patient);	
	            results.count = filteredPatients.size();
	            results.values = filteredPatients;	
	            return results;
        	}
        };
        return filter;
    }
	
	@Override
	public int getCount () {
	    return filteredPatients.size();
	}

}
