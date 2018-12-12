package isebase.cognito.tourpilot_apk.Templates;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Data.Answer.Answer;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private List<String> questionNames;
    private Map<String, List<String>> answerNames;
    public List<Answer> answers;
    TextView defaultTextView;
 
    public ExpandableListAdapter(Activity context, List<String> questionNames,
    		Map<String, List<String>> answerNames, List<Answer> answers) {
        this.context = context;
        this.questionNames = questionNames;
        this.answerNames = answerNames;
        this.answers = answers;
        defaultTextView = new TextView(context);
    }
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return answerNames.get(questionNames.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        final String answerName = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(isebase.cognito.tourpilot_apk.R.layout.row_braden_answer_template, null);
        } 
        
        TextView item = (TextView) convertView.findViewById(isebase.cognito.tourpilot_apk.R.id.tvBradenAnswer);

        item.setText(answerName);
        if (isChildAnswered(groupPosition, childPosition))
        	item.setTextColor(StaticResources.getBaseContext().getResources().getColor(R.color.active));
        else
        	item.setTextColor(defaultTextView.getTextColors().getDefaultColor());
        return convertView;
    }

	@Override
	public int getChildrenCount(int groupPosition) {
		return answerNames.get(questionNames.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return questionNames.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return questionNames.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String questionName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(isebase.cognito.tourpilot_apk.R.layout.row_braden_question_template,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(isebase.cognito.tourpilot_apk.R.id.tvBradenQuestion);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(questionName);
        if (isGroupAnswered(groupPosition))
        	item.setTextColor(StaticResources.getBaseContext().getResources().getColor(R.color.active));
        else
        	item.setTextColor(defaultTextView.getTextColors().getDefaultColor());
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
	
	public boolean isGroupAnswered(int groupPosition) {
		for (Answer answer : answers)
			if (answer.getQuestionID() == groupPosition)
				return true;
		return false;
	}
	
	public boolean isChildAnswered(int groupPosition, int childPosition) {
		for (Answer answer : answers)
			if (answer.getQuestionID() == groupPosition && answer.getBradenAnswer() == childPosition)
				return true;
		return false;
	}

}
