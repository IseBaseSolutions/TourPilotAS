package isebase.cognito.tourpilot_apk.Activity;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot_apk.Data.CustomRemark.CustomRemark;
import isebase.cognito.tourpilot_apk.Data.Employment.Employment;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.UserRemark.RemarksComparer;
import isebase.cognito.tourpilot_apk.Data.UserRemark.UserRemark;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserRemarksActivity extends BaseActivity {

    private UserRemark userRemark;
	private List<CustomRemark> customRemarks;
	
	private LinearLayout llCustomRemarks;
	private boolean viewMode;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_user_remarks);
			reloadData();
			initComponents();
			fillUp();
		} catch (Exception e) {
			e.printStackTrace();
			criticalClose();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	private void initComponents() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		Intent intentSave = getIntent();
		Bundle bundle = intentSave.getExtras();
		viewMode = false;
		if(bundle != null){
			viewMode = bundle.getBoolean("ViewMode");
		}
		llCustomRemarks = (LinearLayout) findViewById(R.id.llCustomRemarks);
	}

	private void reloadData() {
        Employment newEmployment = HelperFactory.getHelper().getEmploymentDAO().load((int) Option.Instance().getEmploymentID());

		customRemarks = HelperFactory.getHelper().getCustomRemarkDAO().load();
		userRemark = HelperFactory.getHelper().getUserRemarkDAO().load((int)Option.Instance().getEmploymentID());
		if (userRemark == null)
				userRemark = new UserRemark(newEmployment.getId(), Option.Instance().getWorkerID(), newEmployment.getPatientID(), 
						false, false, false, false, "");
		Collections.sort(customRemarks, new RemarksComparer());
	}

	private void fillUp() {
		for (CustomRemark customRemark : customRemarks)
			if (customRemark.getId() != 1)
				addCheckBox(customRemark);
		TextView textView = new TextView(this);
		textView.setText(getString(R.string.other));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		llCustomRemarks.addView(textView);
		EditText editText = new EditText(this);
		editText.setText(userRemark.getName());
		editText.setEnabled(!viewMode);
		
		llCustomRemarks.addView(editText);
	}
	
	private void addCheckBox(CustomRemark customRemark) {
		CheckBox checkBox = new CheckBox(this);
		checkBox.setText(customRemark.getName());
		checkBox.setTag(customRemark.getId());
		if (!userRemark.getCheckedIDs().equals(""))
		{
			List<String> s = Arrays.asList(userRemark.getCheckedIDsArr());
			checkBox.setChecked(s.contains(String.valueOf(customRemark.getId())));
		}
		checkBox.setEnabled(!viewMode);
		llCustomRemarks.addView(checkBox);
	}

	private void pickUp() {
		String checkedIDs = "";
		for (int i = 0; i < llCustomRemarks.getChildCount(); i++) {
			if (llCustomRemarks.getChildAt(i) instanceof CheckBox) {
				CheckBox checkBox = (CheckBox) llCustomRemarks.getChildAt(i);
				checkedIDs += (checkBox.isChecked() ? ((checkedIDs.equals("") ? "" : ",") + String.valueOf(checkBox.getTag())) : "");
			}
			else if (llCustomRemarks.getChildAt(i) instanceof EditText) {
				EditText editText = (EditText) llCustomRemarks.getChildAt(i);
				userRemark.setName(editText.getText().toString());
			}
		}
		String chid = userRemark.getName().equals("") ? checkedIDs : (checkedIDs.equals("") ? "1" : (checkedIDs + ",1"));
		userRemark.setCheckedIDs(chid);
	}

	private void save() {
		try {
			HelperFactory.getHelper().getUserRemarkDAO().save(userRemark);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void btSaveClick(View view) {
		pickUp();
		save();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle == null)
			return;
		
		setResult(UserRemarksActivity.RESULT_OK, intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		setResult(UserRemarksActivity.RESULT_CANCELED);
		finish();
	}

}
