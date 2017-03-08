package isebase.cognito.tourpilot.Templates;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Activity.TasksAssessmentsActivity.AssessmentsFragment.EmploymentCategory;
import isebase.cognito.tourpilot.StaticResources.StaticResources;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EmploymentCategoryAdapter extends ArrayAdapter<EmploymentCategory> {

	private List<EmploymentCategory> employmentCategories;
	private int layoutResourceId;
	private Context context;
	
	public EmploymentCategoryAdapter(Context context, int layoutResourceId, List<EmploymentCategory> employmentCategories) {
		super(context, layoutResourceId, employmentCategories);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.employmentCategories = employmentCategories;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);
		EmploymentCategorytHolder employmentCategorytHolder = new EmploymentCategorytHolder();
		employmentCategorytHolder.employmentCategory = employmentCategories.get(position);
		employmentCategorytHolder.tvCategoryName = (TextView) row.findViewById(R.id.tvCategoryName);
		employmentCategorytHolder.tvCategoryName.setText(employmentCategories.get(position).name);
		if (employmentCategorytHolder.employmentCategory.isAnswered)
			employmentCategorytHolder.tvCategoryName.setTextColor(StaticResources.getBaseContext().getResources().getColor(R.color.active));
		row.setTag(employmentCategorytHolder);
		return row;
	}
	
	public class EmploymentCategorytHolder {
		EmploymentCategory employmentCategory;
		TextView tvCategoryName;
	}
	
}
