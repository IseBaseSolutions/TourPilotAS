package isebase.cognito.tourpilot.Templates;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.DataInterfaces.Job.IJob;
import isebase.cognito.tourpilot.StaticResources.StaticResources;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WorkEmploymentAdapter extends ArrayAdapter<IJob>{
		
		private List<IJob> jobs;
		private int layoutResourceId;
		private Context context;

		public WorkEmploymentAdapter(Context context, int layoutResourceId, List<IJob> items) {
			super(context, layoutResourceId, items);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.jobs = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			EmploymentHolder employmentHolder = new EmploymentHolder();
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			employmentHolder.tvWorkEmploymentName = (TextView) row.findViewById(R.id.tvWorkEmploymentName);
			employmentHolder.tvWorkEmploymentName.setText(jobs.get(position).text());
			
			
			employmentHolder.tvWorkEmploymentTime = (TextView) row.findViewById(R.id.tvWorkEmploymentTime);
			employmentHolder.tvWorkEmploymentTime.setText(jobs.get(position).time());
			
			
			if(jobs.get(position).isDone())
				employmentHolder.tvWorkEmploymentName.setTextColor(StaticResources.getBaseContext().getResources().getColor(R.color.yellow));
			if(jobs.get(position).wasSent())
				employmentHolder.tvWorkEmploymentName.setTextColor(StaticResources.getBaseContext().getResources().getColor(R.color.green));
		
			row.setTag(employmentHolder);
			return row;
		}

		class EmploymentHolder {
			TextView tvWorkEmploymentName;
			TextView tvWorkEmploymentTime;
		}
}
