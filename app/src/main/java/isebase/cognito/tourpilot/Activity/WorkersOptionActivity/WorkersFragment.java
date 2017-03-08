package isebase.cognito.tourpilot.Activity.WorkersOptionActivity;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Activity.SynchronizationActivity;
import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectComparer;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.Data.Worker.Worker;
import isebase.cognito.tourpilot.DataBase.HelperFactory;
import isebase.cognito.tourpilot.Dialogs.BaseDialogListener;
import isebase.cognito.tourpilot.Dialogs.PinDialog;
import isebase.cognito.tourpilot.StaticResources.StaticResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
//import isebase.cognito.tourpilot.Data.Worker.Worker;

public class WorkersFragment extends Fragment implements BaseDialogListener {

	private List<Worker> workers = new ArrayList<Worker>();
	private PinDialog pinDialog;
	private Worker selectedWorker;
	
	private View rootView;
	
	private ListView listView;
	private EditText etWorkerName;
	
	WorkerOptionActivity activity;
	
	public WorkersFragment() {

	}
	
	public WorkersFragment(WorkerOptionActivity activity) {
		this.activity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(
				R.layout.activity_workers, container, false);
		reloadData();
		initControls();
		initDialogs();
		fillUp();		
		return rootView;
	}
	
	private void initControls() {	
		listView = (ListView) rootView.findViewById(R.id.lvWorkers);
		etWorkerName = (EditText) rootView.findViewById(R.id.etWorkerName);
	}
	
	public void fillUp() {	
		final ArrayAdapter<Worker> adapter = new ArrayAdapter<Worker>(StaticResources.getBaseContext(),
				android.R.layout.simple_list_item_1, workers);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				selectedWorker = (Worker) listView.getItemAtPosition(position);
				if (selectedWorker.checkPIN(String.valueOf(Option.Instance().getPin())))
					logIn();
				else if (pinDialog.getFragmentManager() == null)
					showPinDialog();
			}
		});
		etWorkerName.addTextChangedListener(new TextWatcher() {

		    @Override
		    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
		        // When user changed the Text
		    	adapter.getFilter().filter(cs);
		    }

		    @Override
		    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

		    @Override
		    public void afterTextChanged(Editable arg0) {}
		});
	}
	
	public void reloadData() {
		workers = Option.Instance().isWorkerPhones() ? HelperFactory.getHelper().getWorkerDAO().loadActive() : HelperFactory.getHelper().getWorkerDAO().load();
		Collections.sort(workers, new BaseObjectComparer());
//		Option.Instance().setWorkerActivity(true);
		Option.Instance().save();
	}
	
	private void initDialogs() {
		pinDialog = new PinDialog();
	}
	
	private void showPinDialog() {
		pinDialog.setWorker(selectedWorker);
		pinDialog.show(getFragmentManager(), "dialogPin");
		getFragmentManager().executePendingTransactions();
		pinDialog.getDialog().setTitle(selectedWorker.getName());
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		if (dialog == pinDialog)
			logIn();		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		return;
	}
	
	public void logIn() {
		HelperFactory.getHelper().clearWorkerData();
		saveSelectedWorkerID();
		startSyncActivity();
	}
	
	private void saveSelectedWorkerID() {
		Option.Instance().setPrevWorkerID(Option.Instance().getWorkerID());
		Option.Instance().setWorkerID(selectedWorker.getId());
		Option.Instance().save();
	}
	
	private void startSyncActivity() {
		Intent synchActivity = new Intent(getActivity().getApplicationContext(),
				SynchronizationActivity.class);
		startActivity(synchActivity);
	}

}
