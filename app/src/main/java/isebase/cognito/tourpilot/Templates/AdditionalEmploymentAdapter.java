package isebase.cognito.tourpilot.Templates;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Data.AdditionalEmployment.AdditionalEmployment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AdditionalEmploymentAdapter extends ArrayAdapter<AdditionalEmployment> {

	private List<AdditionalEmploymentHolder> additionalEmployments;
	private int layoutResourceId;
	private Context context;
	int selectedCount;
	private Button btOK;
	
	public AdditionalEmploymentAdapter(Context context, int layoutResourceId, List<AdditionalEmployment> additionalEmployments) {
		super(context, layoutResourceId, additionalEmployments);
		
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.additionalEmployments = new ArrayList<AdditionalEmploymentHolder>();
		for(AdditionalEmployment additionalEmployment : additionalEmployments)
			this.additionalEmployments.add(new AdditionalEmploymentHolder(additionalEmployment));
		initControls();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);
		
		AdditionalEmploymentHolderView additionalEmploymentHolderView = new AdditionalEmploymentHolderView();	
		AdditionalEmploymentHolder additionalEmploymentHolder = additionalEmployments.get(position);
		additionalEmploymentHolderView.chbAdditionalEmployment = (CheckBox) row.findViewById(R.id.chb);
		additionalEmploymentHolderView.chbAdditionalEmployment.setTag(additionalEmploymentHolder);	
		additionalEmploymentHolderView.chbAdditionalEmployment.setOnCheckedChangeListener(onCheckboxCheckedListener);
		additionalEmploymentHolderView.chbAdditionalEmployment.setText(additionalEmploymentHolder.additionalEmployment.getName());
		additionalEmploymentHolderView.chbAdditionalEmployment.setChecked(additionalEmploymentHolder.isChecked);		
		return row;
	}
	
	OnCheckedChangeListener onCheckboxCheckedListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			AdditionalEmploymentHolder employmentHolder = (AdditionalEmploymentHolder) buttonView.getTag();
			employmentHolder.isChecked = isChecked;	
			selectedCount += isChecked ? 1 : -1;
			btOK.setEnabled(selectedCount != 0);
		}
	};
		
	public class AdditionalEmploymentHolderView{
		public CheckBox chbAdditionalEmployment;
	}
	
	public class AdditionalEmploymentHolder {
		public AdditionalEmployment additionalEmployment;
		public boolean isChecked;
		
		public AdditionalEmploymentHolder(AdditionalEmployment additionalEmployment){
			this.additionalEmployment = additionalEmployment;
		}		
	}
	
	public List<AdditionalEmployment> getSelectedAdditionalEmployments(){
		List<AdditionalEmployment> selectedAdditionalEmployments = new ArrayList<AdditionalEmployment>();
		for(AdditionalEmploymentHolder additionalEmploymentHolder : additionalEmployments)
			if(additionalEmploymentHolder.isChecked)
				selectedAdditionalEmployments.add(additionalEmploymentHolder.additionalEmployment);
		return selectedAdditionalEmployments;
	}

	private void initControls() {
		btOK = (Button) ((Activity) context).findViewById(R.id.btOK);
		btOK.setEnabled(false);
	}
	
}
