package isebase.cognito.tourpilot_apk.Activity;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot_apk.Data.TourOncomingInfo.TourOncomingInfo;
import isebase.cognito.tourpilot_apk.Data.Worker.Worker;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Dialogs.WorkerPhoneNumbersDialog;
import isebase.cognito.tourpilot_apk.Templates.TourOncomingInfoAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

public class TourOncomingInfoActivity extends BaseActivity {

	private int id;
	private TourOncomingInfo info;
    private WorkerPhoneNumbersDialog workerPhoneNumbersDialog;
	 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tour_oncoming_info);
		initComponents();
		reloadData();
		fillUpTitle();
		fillUpList();
		fillUpHeader();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	private void initComponents() {
		Intent intentSave = getIntent();
		Bundle bundle = intentSave.getExtras();
		if(bundle != null) {
			id = bundle.getInt("id");
		}
	}
	
	private void reloadData() {
		info = HelperFactory.getHelper().getTourOncomingInfoDAO().load(id);
	}
	
	private void fillUpTitle() {
		setTitle(info.getInfoType() == 0 ? "Übergabe Tour" : info.getInfoType() == 1 ? "PKW Tausch" : "Wer versorgt wann?");
	}
	
    private void fillUpList() {
        ExpandableListView expListView = (ExpandableListView) findViewById(R.id.elvTourOncomingInfo);
        TourOncomingInfoAdapter expListAdapter = new TourOncomingInfoAdapter(this, info.getDayPartsInfo());
        expListView.setAdapter(expListAdapter);
        if (info.getInfoType() == 0 || info.getInfoType() == 1)
        	expListView.expandGroup(0);
        expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if ((workerPhoneNumbersDialog != null && workerPhoneNumbersDialog.getFragmentManager() != null) 
						|| !info.getDayPartsInfo().get(groupPosition).workers.get(childPosition).worker.isSendingInfoAllowed())
					return false;
				showWorkerPhoneNumberDialog(info.getDayPartsInfo().get(groupPosition).workers.get(childPosition).worker);
                return true;
			}
        });
    }
    
    private void fillUpHeader() {
    	TextView tvHeader = (TextView) findViewById(R.id.tvHeader);
    	tvHeader.setText(info.getHeaderText());
    }
    
	private void showWorkerPhoneNumberDialog(Worker selectedWorker) {
		workerPhoneNumbersDialog = new WorkerPhoneNumbersDialog(selectedWorker, getBaseContext());
		workerPhoneNumbersDialog.show(getSupportFragmentManager(), "workerPhoneNumbersDialog");
	}
	
}
