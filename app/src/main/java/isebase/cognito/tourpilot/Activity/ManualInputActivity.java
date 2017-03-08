package isebase.cognito.tourpilot.Activity;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot.Data.AdditionalWork.AdditionalWork;
import isebase.cognito.tourpilot.Data.Employment.Employment;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.Data.Patient.Patient;
import isebase.cognito.tourpilot.Data.SelectionPeriod.SelectionPeriod;
import isebase.cognito.tourpilot.Data.Work.Work;
import isebase.cognito.tourpilot.DataBase.HelperFactory;
import isebase.cognito.tourpilot.DataInterfaces.Job.IJob;
import isebase.cognito.tourpilot.DataInterfaces.Job.JobComparer;
import isebase.cognito.tourpilot.DataInterfaces.Job.JobComparer.eJobComparerType;
import isebase.cognito.tourpilot.Dialogs.BaseDialogListener;
import isebase.cognito.tourpilot.Dialogs.IntervalInputDialog;
import isebase.cognito.tourpilot.Dialogs.PatientsDialog;
import isebase.cognito.tourpilot.Templates.ManualInputAdapter;
import isebase.cognito.tourpilot.Utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ManualInputActivity extends BaseActivity implements BaseDialogListener {

    private List<IJob> jobs;
    private List<Patient> patients;
	  
    private IntervalInputDialog intervalInputDialog;
    private PatientsDialog patientsDialog;
    
    private AdditionalWork additionalWork;
    
    private Employment newEmployment;
    
    SelectionPeriod selectedPeriod;
    
    enum eManualInputType {employment, work};
    
    private eManualInputType manualInputType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_input);
        reloadData();
        fillUp();
        initDialogs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manual_input, menu);
        return true;
    }

    private void fillUp() {
        ManualInputAdapter adapter = new ManualInputAdapter(this,R.layout.row_job_template, jobs);
        ListView lvJobs = (ListView) findViewById(R.id.lvJobs);
        lvJobs.setAdapter(adapter);
        lvJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
                if ((jobs.get(position) instanceof Employment) || (jobs.get(position) instanceof Work))
                    return;
	            intervalInputDialog.setSelectedPeriod((SelectionPeriod) jobs.get(position));
	            intervalInputDialog.show(getSupportFragmentManager(), "intervalDialog");		
			}
        });
    }

    private void reloadData() {
    	jobs = new ArrayList<IJob>();
		jobs.addAll(HelperFactory.getHelper().getEmploymentDAO().loadDoneByPilotTourID((int)Option.Instance().getPilotTourID()));
        jobs.addAll(HelperFactory.getHelper().getWorkDAO().loadDoneByPilotTourID((int)Option.Instance().getPilotTourID()));
        Collections.sort(jobs, new JobComparer(eJobComparerType.ONLY_TIME));
        insertSelectionPeriods();
        patients = HelperFactory.getHelper().getPatientDAO().loadPatientsByPilotTourID(Option.Instance().getPilotTourID());
        
        manualInputType = Option.Instance().getEmploymentID() != -1 ? eManualInputType.employment : eManualInputType.work;                
        if (manualInputType == eManualInputType.employment) 
    		newEmployment = HelperFactory.getHelper().getEmploymentDAO().load((int)Option.Instance().getEmploymentID());
      	if (getIntent().getExtras() == null)
       		return;
   		additionalWork = HelperFactory.getHelper().getAdditionalWorkDAO().load(getIntent().getExtras().getInt("addWorkID"));
    }
    
    private void insertSelectionPeriods() {                
        int location = 0;
        int size = jobs.size();
        for (int i = 0; i < size + 1; i++) {
            SelectionPeriod selPeriod = new SelectionPeriod();
            if (location > jobs.size() - 1)
                jobs.add(jobs.size(), selPeriod);
            else
                jobs.add(location, selPeriod);
            location += 2;
        }
        fillPeriods();
    }
    
    private void fillPeriods() {
        Date today = new Date();
        for (int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i) instanceof Employment || jobs.get(i) instanceof Work)
            	continue;
            else if (jobs.size() == 1) {
                ((SelectionPeriod)jobs.get(i)).setStartTime(DateUtils.getStartOfDay(today));
                ((SelectionPeriod)jobs.get(i)).setStopTime(DateUtils.getEndOfDay(today));
            }
            else if (i == 0) {
                ((SelectionPeriod)jobs.get(i)).setStartTime(DateUtils.getStartOfDay(today));
                ((SelectionPeriod)jobs.get(i)).setStopTime(jobs.get(i+1).startTime());
            }
            else if (i == jobs.size() - 1) {
                ((SelectionPeriod)jobs.get(i)).setStartTime(jobs.get(i-1).stopTime());
                ((SelectionPeriod)jobs.get(i)).setStopTime(DateUtils.getEndOfDay(today));
            }
            else {
                ((SelectionPeriod)jobs.get(i)).setStartTime(jobs.get(i-1).stopTime());
                ((SelectionPeriod)jobs.get(i)).setStopTime(jobs.get(i+1).startTime());
            }                        
        }
    }
    
    private void initDialogs() {
    	String title = "";
    		title = (manualInputType == eManualInputType.employment) ? newEmployment.getName() : additionalWork.getName();
        intervalInputDialog = new IntervalInputDialog(title);
        patientsDialog = new PatientsDialog(patients, title);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
    	if (dialog == intervalInputDialog)
            if (manualInputType == eManualInputType.work)
                patientsDialog.show(getSupportFragmentManager(), "patientsDialog");
            else
            {
                    newEmployment = HelperFactory.getHelper().getEmploymentDAO().loadAll(newEmployment.getId());
                    newEmployment.getFirstTask().setManualDate(intervalInputDialog.getStartDate());
                    newEmployment.getLastTask().setManualDate(intervalInputDialog.getStopDate());
                    HelperFactory.getHelper().getTaskDAO().save(newEmployment.getTasks());
                    startTasksActivity();
            }
        if (dialog == patientsDialog) {
            saveWork();
            startPatientsActivity();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
            
    }
    
    private void saveWork() {    	Work work = new Work(intervalInputDialog.getStartDate(),
        		intervalInputDialog.getStopDate(), 
        		new Date(),
        		additionalWork.getId(),
        		Option.Instance().getPilotTourID(),
        		additionalWork.getName(), Option.Instance().getWorkerID());
    	work.setPatientIDs(patientsDialog.getSelectedPatientIDs());
        work.setIsDone(true);
        HelperFactory.getHelper().getWorkDAO().save(work);
    }
}