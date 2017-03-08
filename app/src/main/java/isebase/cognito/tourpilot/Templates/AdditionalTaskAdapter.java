package isebase.cognito.tourpilot.Templates;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Data.AdditionalTask.AdditionalTask;

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
import android.widget.Filter;
import android.widget.Filterable;

public class AdditionalTaskAdapter extends ArrayAdapter<AdditionalTask> implements Filterable {

	private List<AdditionalTasktHolder> tasks;
	public List<AdditionalTask> getSelectedTasks(){
		List<AdditionalTask> selectedTasks = new ArrayList<AdditionalTask>();
		for(AdditionalTasktHolder taskHolder : tasks)
			if(taskHolder.isChecked)
				selectedTasks.add(taskHolder.additionalTask);
		return selectedTasks;
	}
	
	private List<AdditionalTasktHolder> filteredTasks;
	private int layoutResourceId;
	private Context context;
	private Button btSaveAddTasks;
	int selectedCount;
	
	public boolean isVisible = false;
	
	public AdditionalTaskAdapter(Context context, int layoutResourceId, List<AdditionalTask> tasks) {
		super(context, layoutResourceId, tasks);
		
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.tasks = new ArrayList<AdditionalTasktHolder>();
		for(AdditionalTask task : tasks)
			this.tasks.add(new AdditionalTasktHolder(task));
		this.filteredTasks = this.tasks;
		initControls();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);
		
		AdditionalTasktHolderView additionalTaskHolderView = new AdditionalTasktHolderView();		
		AdditionalTasktHolder additionalTaskHolder = filteredTasks.get(position);
		additionalTaskHolderView.chbAdditionalTask = (CheckBox) row.findViewById(R.id.chbAddTask);	
		additionalTaskHolderView.chbAdditionalTask.setOnCheckedChangeListener(onCheckboxCheckedListener);
		additionalTaskHolderView.chbAdditionalTask.setTag(additionalTaskHolder);	
		additionalTaskHolderView.chbAdditionalTask.setText(additionalTaskHolder.additionalTask.getName());
		additionalTaskHolderView.chbAdditionalTask.setChecked(additionalTaskHolder.isChecked);		
		return row;
	}
	
	OnCheckedChangeListener onCheckboxCheckedListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			AdditionalTasktHolder taskHolder = (AdditionalTasktHolder) buttonView.getTag();
			taskHolder.isChecked = isChecked;
			selectedCount += isChecked ? 1 : -1;
			btSaveAddTasks.setEnabled(selectedCount != 0);
		}
	};
	
	@Override
    public Filter getFilter() {
        Filter filter = new Filter() {
	        @Override
	        @SuppressWarnings("unchecked")
	        protected void publishResults(CharSequence constraint, FilterResults results) {
	        	filteredTasks = (List<AdditionalTasktHolder>) results.values;
	            notifyDataSetChanged();
	        }
	
	        @Override
	        protected FilterResults performFiltering(CharSequence constraint) {
	            FilterResults results = new FilterResults();
	            ArrayList<AdditionalTasktHolder> filteredTasks = new ArrayList<AdditionalTasktHolder>();
	            constraint = constraint.toString().toLowerCase();
	            for (AdditionalTasktHolder task : tasks) 
	                if (task.additionalTask.getName().toLowerCase().startsWith(constraint.toString()))
	                    filteredTasks.add(task);	
	            results.count = filteredTasks.size();
	            results.values = filteredTasks;	
	            return results;
        	}
        };
        return filter;
    }
	
	@Override
	public int getCount () {
	    return filteredTasks.size();
	}
	
	public class AdditionalTasktHolderView{
		public CheckBox chbAdditionalTask;
	}
	
	public class AdditionalTasktHolder {
		public AdditionalTask additionalTask;
		public boolean isChecked;
		
		public AdditionalTasktHolder(AdditionalTask task){
			this.additionalTask = task;
		}		
	}
	
	private void initControls() {
		btSaveAddTasks = (Button) ((Activity) context).findViewById(R.id.btSaveAddTask);
	}
	
}
