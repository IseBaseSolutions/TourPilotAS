package isebase.cognito.tourpilot_apk.Activity;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot_apk.Data.CustomRemark.CustomRemark;
import isebase.cognito.tourpilot_apk.Data.Employment.Employment;
import isebase.cognito.tourpilot_apk.Data.EmploymentVerification.EmploymentVerification;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.Task.Task;
import isebase.cognito.tourpilot_apk.Data.Task.Task.eTaskState;
import isebase.cognito.tourpilot_apk.Data.UserRemark.UserRemark;
import isebase.cognito.tourpilot_apk.Data.Worker.Worker;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class VerificationActivity extends BaseActivity {

	private List<Task> tasks;
	
	private TextView tvVerification;
	
	private Button btOK;	
	
	private CheckBox chbCheckVerification;	
	
	private Employment employment;
	
	private UserRemark userRemark;		
	
	private Date dateBegin;
	private Date dateEnd;
	
	private boolean isFlegeOK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);
		initComponents();
		reloadData();
		fillUpVerification();
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.verification, menu);
		return true;
	}
	
	private void initComponents() { 
		chbCheckVerification = (CheckBox)findViewById(R.id.chbCheckVerification);
		btOK = (Button) findViewById(R.id.btOK);
		btOK.setEnabled(chbCheckVerification.isChecked());
		tvVerification = (TextView) findViewById(R.id.tvVerificationText);
	}
	
	private void fillUpVerification() {
		Task startTask = new Task();
		Task endTask = new Task();
		List<Task> tasks = HelperFactory.getHelper().getTaskDAO().load(Task.EMPLOYMENT_ID_FIELD, String.valueOf(Option.Instance().getEmploymentID()));
		for (Task task : tasks) {
			if (task.isFirstTask())
				startTask = task;
			else if (task.isLastTask())
				endTask = task;
		}
		dateBegin = startTask.getManualDate().equals(DateUtils.EmptyDate) ? startTask.getRealDate() : startTask.getManualDate();
		dateEnd = endTask.getManualDate().equals(DateUtils.EmptyDate) ? endTask.getRealDate() : endTask.getManualDate();

		String taskVerification = "";
		taskVerification += "<b>" + getWorker(Option.Instance().getWorker()) + " </b>";
		taskVerification += "hat am ";
		
		taskVerification += "<b>" + DateUtils.DateFormat.format(employment.getDate()) + " </b> ";
		taskVerification += "bei Patient ";
		taskVerification += "<b>" + getPatientNameWithoutKey(employment.getName()) + " </b>";
		taskVerification += "in der Zeit von: ";
		taskVerification += "<b>" + DateUtils.HourMinutesFormat.format(dateBegin) + " </b>";
		taskVerification += "bis ";
		taskVerification += "<b>" + DateUtils.HourMinutesFormat.format(dateEnd) + " </b> ";
		int interval = DateUtils.getInterval(dateBegin, dateEnd); 
		if(interval > 0) {
			taskVerification += "bei einer Dauer von ";
			taskVerification += "<b>" + interval + "</b> ";
			taskVerification += getString(R.string.minuten_einen) + " ";
			taskVerification += getString(R.string.data_to_send);
		} else {
			taskVerification += getString(R.string.work_was_done);
			taskVerification += " Die Einsatzdauer <b>ist weniger als 1 Minute</b>. ";
			taskVerification += getString(R.string.data_to_send);
		}
		taskVerification += "<br /><br />";
		
		String[] arrayResultTask = getTasks();
		int doneTasks = 0;
		int undoneTasks = 1;
		taskVerification += "<b>" + getString(R.string.done_tasks) + ":</b> <br />" + ((arrayResultTask[doneTasks].equals("")) 
				? getString(R.string.there_are_no_done_tasks) + "<br />" 
						: arrayResultTask[doneTasks]) + "<br />";
		taskVerification += "<b>" + getString(R.string.undone_tasks) + ":</b> <br />" + ((arrayResultTask[undoneTasks].equals("")) 
				? getString(R.string.there_are_no_undone_tasks) + "<br />" 
						: arrayResultTask[undoneTasks]) + "<br />";
		taskVerification += getFlege();
		tvVerification.setText(Html.fromHtml(taskVerification));
	}

	public void onClickButtonOk(View view) {		
		saveVerification();
		setResult(VerificationActivity.RESULT_OK);
		finish();
	}

	public void onClickButtonCancel(View view) {
		setResult(VerificationActivity.RESULT_CANCELED);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		setResult(VerificationActivity.RESULT_CANCELED);
		finish();
	}

	public void onClickCheckVerification(View view) {
		btOK.setEnabled(chbCheckVerification.isChecked());
	}
	
	public void onBtOkClick(View view) {
		saveVerification();
		setResult(VerificationActivity.RESULT_OK);
		finish();		
	}
	
	public void onBtCancelClick(View view) {
		setResult(VerificationActivity.RESULT_CANCELED);
		finish();	
	}
	
	private String getWorker(Worker worker) {
		String[] workerName = worker.getName().split(" ");
		return String.format("%s %s", workerName[0], workerName[1]);
	}
	
	private String getPatientNameWithoutKey(String patient) {
		String[] patientName = patient.split(" ") ;
		return String.format("%s %s", patientName[0], patientName[1]); 
	}


		
	private String[] getTasks() {
		String[] sTasks = new String[]{ "", "" };
		for(Task task : tasks) {
			if(task.isFirstTask() || task.isLastTask())
				continue;
			if(task.getState() == eTaskState.Done) {
				sTasks[0] +=  " - " + task.getName() + (task.getQualityResult().equals("") ? "" : (" (" + task.getQualityResult()) + ")") + "<br />";
			}
			else
				sTasks[1] += " - " + task.getName() + "<br />";
		}
		return sTasks;
	}
	
	private String getFlege() {
		String flege = "";
		Intent intentFlege = getIntent();
		Bundle bundle = intentFlege.getExtras();
		isFlegeOK = false;
		if(bundle != null){
			isFlegeOK = bundle.getBoolean("isAllOK");
			if(isFlegeOK)
				flege += getString(R.string.all_ok) + "<br />";
			userRemark = HelperFactory.getHelper().getUserRemarkDAO().load(employment.getId());
			if(userRemark == null)
				return flege;	
			flege += "<b>" + getString(R.string.visit_notes) + ":</b> <br />";
			if (Integer.parseInt(Option.Instance().getVersion()) > 1042)
			{
				List<CustomRemark> cutomRemarks = HelperFactory.getHelper().getCustomRemarkDAO().load();
				List<String> s = Arrays.asList(userRemark.getCheckedIDsArr());
				for (CustomRemark customRemark : cutomRemarks) {
					flege += customRemark.getName() + ": " + "<b>";
					flege += s.contains(String.valueOf(customRemark.getId())) ? getString(R.string.yes) : getString(R.string.no);
					flege += "</b>" + " <br />";
				}			
				if(!userRemark.getName().equals(""))
					flege += "<b>" + getString(R.string.other) + "</b> " + userRemark.getName() + "<br />";
			} else {
				if((userRemark.getCheckboxes() & 1) == 1)
					flege += getString(R.string.connect_with) + ": " + "<b>" + getString(R.string.yes) + "</b>" + " <br />";
				else
					flege += getString(R.string.connect_with) + ": " + "<b>" + getString(R.string.no) + "</b>" + " <br />";
				if((userRemark.getCheckboxes() & 2) == 2)
					flege += getString(R.string.medications_changed) + ": " + "<b>" + getString(R.string.yes) + "</b>" + " <br />";
				else
					flege += getString(R.string.medications_changed) + ": " + "<b>" + getString(R.string.no) + "</b>" + " <br />";
				if((userRemark.getCheckboxes() & 4) == 4)
					flege += getString(R.string.aubrplanmabige_pflege) + ": " + "<b>" + getString(R.string.yes) + "</b>" + " <br />";
				else
					flege += getString(R.string.aubrplanmabige_pflege) + ": " + "<b>" + getString(R.string.no) + "</b>" + " <br />";
				if(!userRemark.getName().equals(""))
					flege += "<b>" + getString(R.string.other) + "</b> " + userRemark.getName() + "<br />";
			}
		}
		return flege + "<br />";
	}
	
	private void saveVerification() {
		long employmentID = Option.Instance().getEmploymentID();
		
		int workerID = Option.Instance().getWorkerID();
		
		int patientID = HelperFactory.getHelper().getPatientDAO().load(employment.getPatientID()).getId();

		String doneTasksIDs = "", undoneTasksIDs = "";
		for(Task task : tasks) {
			if(task.getName().contains(getString(R.string.start_task)) || task.getName().contains(getString(R.string.end_task)))
				continue;
			if(task.getState().equals(eTaskState.Done))
				doneTasksIDs += (doneTasksIDs.equals("") ? "" : ",") + task.getAditionalTaskID();
			else
				undoneTasksIDs += (undoneTasksIDs.equals("") ? "" : ",") + task.getAditionalTaskID();
		}
		String userRemarks;
		if (Integer.parseInt(Option.Instance().getVersion()) > 1042) {
			userRemarks = userRemark != null ? (userRemark.getCheckedIDs() + ";" + userRemark.getName()) : "";
		}
		else {
			userRemarks = getFlegeMarks();
		}		
		
		EmploymentVerification verification = new EmploymentVerification(employmentID, workerID,
				patientID, dateBegin, dateEnd, doneTasksIDs, undoneTasksIDs, userRemarks, isFlegeOK);
		HelperFactory.getHelper().getEmploymentVerificationDAO().save(verification);
	}

	private String getFlegeMarks() {
		if(userRemark == null)
			return "";
		String flegeMarks = "";
		flegeMarks += ((userRemark.getCheckboxes() & 1) == 1) ? "+," : "-,";
		flegeMarks += ((userRemark.getCheckboxes() & 2) == 2) ? "+," : "-,";
		flegeMarks += ((userRemark.getCheckboxes() & 1) == 1) ? "+," : "-,";
		flegeMarks += userRemark.getName();
		return flegeMarks;
	}
	
	private void reloadData() {
		tasks = HelperFactory.getHelper().getTaskDAO().load(Task.EMPLOYMENT_ID_FIELD, String.valueOf(Option.Instance().getEmploymentID()));
		employment = HelperFactory.getHelper().getEmploymentDAO().loadAll((int)Option.Instance().getEmploymentID());
	}
	
}
