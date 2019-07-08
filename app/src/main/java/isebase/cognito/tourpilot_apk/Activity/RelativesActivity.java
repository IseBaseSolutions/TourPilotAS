package isebase.cognito.tourpilot_apk.Activity;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot_apk.Data.Employment.Employment;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.Relative.Relative;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Templates.AddressAdapter;

import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

public class RelativesActivity extends BaseActivity {
	
	private List<Relative> relatives;
	private Employment employment;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_relatives);
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
	
	private void fillUpTitle(){
		setTitle(getString(R.string.menu_relatives) + ", " + employment.getName());
	}
	
	private void fillUp() {
        AddressAdapter<Relative> adapter = new AddressAdapter<Relative>(this
                , R.layout.row_address_template, relatives);
		ListView relativesListView = (ListView) findViewById(R.id.lvListRelatives);
		relativesListView.setAdapter(adapter);
	}
	
	private void reloadData() {	
		employment = HelperFactory.getHelper().getEmploymentDAO().loadAll((int)Option.Instance().getEmploymentID());
		relatives =  HelperFactory.getHelper().getRelativeDAO().sortByStrIDs(HelperFactory.getHelper().getRelativeDAO()
                .loadAllByIDs(employment.getPatient().getStrRelativeIDs()), employment.getPatient().getStrRelativeIDs());
	}

}