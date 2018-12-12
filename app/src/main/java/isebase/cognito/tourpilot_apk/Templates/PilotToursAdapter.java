package isebase.cognito.tourpilot_apk.Templates;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Data.PilotTour.PilotTour;
import isebase.cognito.tourpilot_apk.Utils.DateUtils;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PilotToursAdapter extends ArrayAdapter<PilotTour>{
	
	private static final String todayStr = "heute";
	
	private List<PilotTour> tours;
	private int layoutResourceId;
	private Context context;
	
	public PilotToursAdapter(Context context, int layoutResourceId, List<PilotTour> tours) {
		super(context, layoutResourceId, tours);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.tours = tours;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		PilotTourHolder employmentHolder = new PilotTourHolder();
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);
		
		PilotTour pilotTour = tours.get(position);
		employmentHolder.tvTourDate = (TextView) row.findViewById(R.id.tvTourDate);
		employmentHolder.tvTourDate.setText(DateUtils.WeekDateFormat.format(pilotTour.getPlanDate()));
		
		employmentHolder.tvTourName = (TextView) row.findViewById(R.id.tvTourName);
		employmentHolder.tvTourName.setText(pilotTour.getName());
		
		employmentHolder.tvTourStatus = (TextView) row.findViewById(R.id.tvTourStatus);
		if(DateUtils.isToday(pilotTour.getPlanDate()))
			employmentHolder.tvTourStatus.setText(todayStr);
	
		row.setTag(employmentHolder);
		return row;
	}

	class PilotTourHolder {
		TextView tvTourDate;
		TextView tvTourName;
		TextView tvTourStatus;
	}
}
