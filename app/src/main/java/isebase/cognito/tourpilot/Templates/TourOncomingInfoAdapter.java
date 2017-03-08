package isebase.cognito.tourpilot.Templates;

import isebase.cognito.tourpilot.Data.Answer.Answer;
import isebase.cognito.tourpilot.Data.TourOncomingInfo.TourOncomingInfo.TourOncomingInfoDayPart;
import isebase.cognito.tourpilot.Data.TourOncomingInfo.TourOncomingInfo.TourOncomingInfoWoker;
import isebase.cognito.tourpilot.Utils.DateUtils;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class TourOncomingInfoAdapter extends BaseExpandableListAdapter {

	private Activity context;
	private List<TourOncomingInfoDayPart> Items;
	public List<Answer> answers;
	TextView defaultTextView;

	public TourOncomingInfoAdapter(Activity context,
			List<TourOncomingInfoDayPart> items) {
		this.context = context;
		Items = items;
		defaultTextView = new TextView(context);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return Items.get(groupPosition).workers.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		TourOncomingInfoWoker child = (TourOncomingInfoWoker) getChild(
				groupPosition, childPosition);
		final String childName = DateUtils.DateFormat.format(child.date) + " "
				+ child.worker.getName();
		LayoutInflater inflater = context.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater
					.inflate(
							isebase.cognito.tourpilot.R.layout.row_braden_answer_template,
							null);
		}

		TextView item = (TextView) convertView
				.findViewById(isebase.cognito.tourpilot.R.id.tvBradenAnswer);

		item.setText(childName);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return Items.get(groupPosition).workers.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return Items.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return Items.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String groupName = ((TourOncomingInfoDayPart) getGroup(groupPosition)).dayPart;
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater
					.inflate(
							isebase.cognito.tourpilot.R.layout.row_braden_question_template,
							null);
		}
		TextView item = (TextView) convertView
				.findViewById(isebase.cognito.tourpilot.R.id.tvBradenQuestion);
		item.setTypeface(null, Typeface.BOLD);
		item.setText(groupName);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
