package isebase.cognito.tourpilot.Activity;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectComparer;
import isebase.cognito.tourpilot.Data.Worker.Worker;
import isebase.cognito.tourpilot.DataBase.HelperFactory;
import isebase.cognito.tourpilot.Dialogs.WorkerPhoneNumbersDialog;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ActualWorkersActivity extends BaseActivity {

	/** Members **/
	
	private EditText etWorkerName;
	private ListView lvWorkers;
	private Worker selectedWorker;	
	private WorkerPhoneNumbersDialog workerPhoneNumbersDialog;
	private List<Worker> workers;

	/** Override **/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_actual_workers);
		reloadData();
		initControls();
		fillUp();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actual_workers_menu, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		startPatientsActivity();
	}
	
	/** Internal **/
	
	private void reloadData() {
		workers = HelperFactory.getHelper().getWorkerDAO().load();
		Collections.sort(workers, new BaseObjectComparer());
	}

	private void initControls() {
		etWorkerName = (EditText) findViewById(R.id.etWorkerName);
		lvWorkers = (ListView) findViewById(R.id.lvWorkers);
	}

	private void fillUp() {
		final ArrayAdapter<Worker> adapter = new ArrayAdapter<Worker>(
				getBaseContext(), android.R.layout.simple_list_item_1, workers);
		lvWorkers.setAdapter(adapter);
		lvWorkers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int selectedIndex,
					long arg3) {
				selectedWorker = adapter.getItem(selectedIndex);
				if (selectedWorker != null && selectedWorker.isSendingInfoAllowed())
					showWorkerPhoneNumberDialog();
			}
			
		});
		etWorkerName.addTextChangedListener(new TextWatcher() {

		    @Override
		    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
		        adapter.getFilter().filter(cs);
		    }

		    @Override
		    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

		    @Override
		    public void afterTextChanged(Editable arg0) { }
		    
		});
	}
	
	private void showWorkerPhoneNumberDialog() {
		workerPhoneNumbersDialog = new WorkerPhoneNumbersDialog(selectedWorker, getBaseContext());
		workerPhoneNumbersDialog.show(getSupportFragmentManager(), "workerPhoneNumbersDialog");
	}

}
