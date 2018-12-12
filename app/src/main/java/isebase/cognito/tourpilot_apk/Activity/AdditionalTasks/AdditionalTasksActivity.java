package isebase.cognito.tourpilot_apk.Activity.AdditionalTasks;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseTimeSyncActivity;
import isebase.cognito.tourpilot_apk.Data.AdditionalTask.AdditionalTask;
import isebase.cognito.tourpilot_apk.Data.AdditionalTask.Catalog;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectComparer;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.Patient.Patient;
import isebase.cognito.tourpilot_apk.Data.Task.Task;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Templates.AdditionalTaskAdapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class AdditionalTasksActivity extends BaseTimeSyncActivity {

	/** Members **/
	
	public Button btSaveAddTasks;
	
	private ListView lvAddTasks;
	private AdditionalTaskAdapter adapter;	
	private List<AdditionalTask> additionalTasks;
	private List<Task> tasks;
	private Catalog catalog;

	/** Override **/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_add_tasks);
			initControls();
			reloadData();
			deleteExistedAdditionalTasks();
			fillUp();
			fillUpTitle();
			setTimeSync(true);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			criticalClose();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	/** Internal **/
	
	private void initControls() {
		lvAddTasks = (ListView) findViewById(R.id.lvAddTasks);
		btSaveAddTasks = (Button) findViewById(R.id.btSaveAddTask);
		btSaveAddTasks.setEnabled(false);
		initFilter();
	}
	
	private void initFilter() {
		EditText etFilter = (EditText) findViewById(R.id.etAddTasksFilter);
		etFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable text) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				adapter.getFilter().filter(arg0);
			}
			
		});
	}
	
	private void reloadData() {
		int catalogType = getIntent().getIntExtra("catalog_type", BaseObject.EMPTY_ID);
		catalog = new Catalog(Catalog.eCatalogType.values()[catalogType]);	

		Patient patient = HelperFactory.getHelper().getEmploymentDAO().loadAll((int)Option.Instance().getEmploymentID()).getPatient();
		
		switch (catalog.getCatalogType()) {
			case btyp_kk:
				additionalTasks = HelperFactory.getHelper().getAdditionalTaskDAO().loadByCatalog(patient.getKK());
				break;
			case btyp_pk:		
				additionalTasks = HelperFactory.getHelper().getAdditionalTaskDAO().loadByCatalog(patient.getPK());	
				break;
			case btyp_pr:			
				additionalTasks = HelperFactory.getHelper().getAdditionalTaskDAO().loadByCatalog(patient.getPR());
				break;
			case btyp_sa:			
				additionalTasks = HelperFactory.getHelper().getAdditionalTaskDAO().loadByCatalog(patient.getSA());
				break;
		}
		sortAdditinalTasks();
	}
	
	private void deleteExistedAdditionalTasks() {
		tasks = HelperFactory.getHelper().getTaskDAO().load(Task.EMPLOYMENT_ID_FIELD, String.valueOf(Option.Instance().getEmploymentID()));
		
		HashMap<Integer, AdditionalTask> addTasks = new HashMap<Integer, AdditionalTask>();
		
		for (AdditionalTask additionalTask : additionalTasks)
			addTasks.put(additionalTask.getId(), additionalTask);
		
		for (Task task : tasks) {
			if (task.isFirstTask() || task.isLastTask())
				continue;
			if (addTasks.containsKey(task.getAditionalTaskID()))
				additionalTasks.remove(addTasks.get(task.getAditionalTaskID()));
		}
		
	}

	private void fillUp() {
		adapter = new AdditionalTaskAdapter(this, R.layout.row_additional_task_template, additionalTasks);
		lvAddTasks.setAdapter(adapter);
	}
	
	private void fillUpTitle() {
		setTitle(catalog.getName());
	}

	public void onSaveAddTasks(View view) {
		HelperFactory.getHelper().getTaskDAO().createTasks(adapter.getSelectedTasks());
		startTasksActivity();
	}

	private void sortAdditinalTasks() {
		Collections.sort(additionalTasks, new BaseObjectComparer());
	}
}
