package isebase.cognito.tourpilot.Activity.AdditionalTasks;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot.Data.AdditionalTask.Catalog;
import isebase.cognito.tourpilot.Data.AdditionalTask.Catalog.eCatalogType;
import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot.Data.Employment.Employment;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.Data.Patient.Patient;
import isebase.cognito.tourpilot.DataBase.HelperFactory;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CatalogsActivity extends BaseActivity {

	private List<Catalog> catalogs = new ArrayList<Catalog>();
	private Employment newEmployment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_add_tasks_category);
			reloadData();
			fillUpTitle();
			fillUp();
		}
		catch(Exception e){
			e.printStackTrace();
			criticalClose();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	private void reloadData(){
		newEmployment = HelperFactory.getHelper().getEmploymentDAO().load((int)Option.Instance().getEmploymentID());

		Patient patient = HelperFactory.getHelper().getPatientDAO().load(newEmployment.getPatientID());
		if(patient.getKK() != BaseObject.EMPTY_ID)
			catalogs.add(new Catalog(eCatalogType.btyp_kk));
		if(patient.getPK() != BaseObject.EMPTY_ID)
			catalogs.add(new Catalog(eCatalogType.btyp_pk));
		if(patient.getPR() != BaseObject.EMPTY_ID)
			catalogs.add(new Catalog(eCatalogType.btyp_pr));
		if(patient.getSA() != BaseObject.EMPTY_ID)
			catalogs.add(new Catalog(eCatalogType.btyp_sa));
	}
	
	@Override
	public void onBackPressed() {
		Intent intentFlege = getIntent();
		Bundle bundle = intentFlege.getExtras();
		if(bundle == null)
			super.onBackPressed();
		else {
			int employmentID = (int)Option.Instance().getEmploymentID();
			HelperFactory.getHelper().getEmploymentDAO().delete(employmentID);
			HelperFactory.getHelper().getTaskDAO().deleteByEmploymentID(employmentID);
			Option.Instance().setEmploymentID(BaseObject.EMPTY_ID);
			Option.Instance().save();
			super.onBackPressed();
		}
			
	}
		
	private void fillUpTitle(){
		setTitle(newEmployment.getName());
	}
	
	private void fillUp(){
		ListView lvAddTasksCategories = (ListView)findViewById(R.id.lvAddTasksCategory);
		ArrayAdapter<Catalog> adapter = new ArrayAdapter<Catalog>(this, android.R.layout.simple_list_item_1, catalogs);
		lvAddTasksCategories.setAdapter(adapter);		
		lvAddTasksCategories.setOnItemClickListener(catalogOnItemClickListener);
	}
	
	private AdapterView.OnItemClickListener catalogOnItemClickListener 
			= new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			Catalog catalog = catalogs.get(position);
			Intent addTasksActivity = new Intent(getApplicationContext(), AdditionalTasksActivity.class);
			addTasksActivity.putExtra("catalog_type", catalog.getCatalogType().ordinal());
			startActivity(addTasksActivity);
		}
	};
	
}
