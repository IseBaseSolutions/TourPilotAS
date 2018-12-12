package isebase.cognito.tourpilot_apk.Templates;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.DataInterfaces.Job.IJob;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ManualInputAdapter extends ArrayAdapter<IJob> {
	
	private List<IJob> jobs;
	private int layoutResourceId;
	private Context context;

	public ManualInputAdapter(Context context, int layoutResourceId, List<IJob> jobs) {
		super(context, layoutResourceId, jobs);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.jobs = jobs;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		WorkerHolder workerHolder = new WorkerHolder();
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		workerHolder.job = jobs.get(position);
		workerHolder.tvJob = (TextView) row.findViewById(R.id.tvJob);
		workerHolder.tvJob.setText(workerHolder.job.timeInterval() + " " + workerHolder.job.text());
	
		row.setTag(workerHolder);
		return row;
	}

	class WorkerHolder {
		IJob job;
		TextView tvJob;
	}
}
