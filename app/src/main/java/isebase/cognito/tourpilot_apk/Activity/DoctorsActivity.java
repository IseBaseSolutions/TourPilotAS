package isebase.cognito.tourpilot_apk.Activity;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot_apk.Data.Doctor.Doctor;
import isebase.cognito.tourpilot_apk.Data.Employment.Employment;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Templates.AddressAdapter;

import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

public class DoctorsActivity extends BaseActivity {

	private List<Doctor> addressable;
	private Employment newEmployment;
	AddressAdapter<Doctor> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_doctors);
			reloadData();
			fillUp();
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

	private void fillUp(){
		adapter = new AddressAdapter<Doctor>(this
				, R.layout.row_address_template, addressable);
		ListView doctorsListView = (ListView) findViewById(R.id.lvListDoctors);
		doctorsListView.setAdapter(adapter);
	}
	
	public void reloadData() {
		newEmployment = HelperFactory.getHelper().getEmploymentDAO().loadAll((int)Option.Instance().getEmploymentID());
		addressable = HelperFactory.getHelper().getDoctorODA().sortByStrIDs(HelperFactory.getHelper()
                .getDoctorODA().loadAllByIDs(newEmployment.getPatient().getStrDoctorsIDs()),
                newEmployment.getPatient().getStrDoctorsIDs());
	}

	private void fillUpTitle(){
		setTitle(getString(R.string.menu_doctors) + ", " + newEmployment.getName());
	}
}
