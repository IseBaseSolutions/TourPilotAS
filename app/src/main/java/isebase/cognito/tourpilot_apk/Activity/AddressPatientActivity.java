package isebase.cognito.tourpilot_apk.Activity;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot_apk.Data.Employment.Employment;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.Patient.Patient;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Templates.AddressAdapter;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

public class AddressPatientActivity extends BaseActivity {

	private List<Patient> patients;
	private Employment newEmployment;
	private AddressAdapter<Patient> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_address);
			reloadData();
			initForm();
			fillUpTitle();
		}
		catch(Exception ex){
			ex.printStackTrace();
			criticalClose();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.base_menu, menu);
		return true;
	}
	
	private void initForm(){
		adapter = new AddressAdapter<Patient>(this
				, R.layout.row_address_template, patients);
		ListView doctorsListView = (ListView) findViewById(R.id.lvListPatientAdrress);
		doctorsListView.setAdapter(adapter);
	}
	
	public void reloadData() {
		patients = new ArrayList<Patient>();
		newEmployment = HelperFactory.getHelper().getEmploymentDAO().loadAll((int)Option.Instance().getEmploymentID());
		patients.add(HelperFactory.getHelper().getPatientDAO().loadAll(newEmployment.getPatientID()));
	}
	
	private void fillUpTitle(){
		setTitle(getString(R.string.menu_address) + ". " + newEmployment.getName());
	}
	
}
