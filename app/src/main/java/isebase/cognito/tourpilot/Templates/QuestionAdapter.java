package isebase.cognito.tourpilot.Templates;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Data.Answer.Answer;
import isebase.cognito.tourpilot.Data.Employment.Employment;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.Data.Patient.Patient;
import isebase.cognito.tourpilot.Data.Question.IQuestionable;
import isebase.cognito.tourpilot.Data.Question.Question;
import isebase.cognito.tourpilot.DataBase.HelperFactory;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class QuestionAdapter extends ArrayAdapter<IQuestionable> {
		
		private List<IQuestionable> items;
		private int layoutResourceId;
		private Context context;
		public Patient patient;
		private Employment newEmployment;

		public QuestionAdapter(Context context, int layoutResourceId, List<IQuestionable> items) {
			super(context, layoutResourceId, items);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.items = items;
			newEmployment = HelperFactory.getHelper().getEmploymentDAO().loadAll((int)Option.Instance().getEmploymentID());
			patient = newEmployment.getPatient();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			QuestionHolder questionHolder = new QuestionHolder();
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			questionHolder.tvQuestionName = (TextView) row.findViewById(R.id.tvQuestion);
			questionHolder.tvQuestionName.setText(items.get(position) instanceof Question 
					? ((Question)items.get(position)).getNameWithKeyWords(patient) 
							: items.get(position).name());						
			questionHolder.rgAnswers = (RadioGroup) row.findViewById(R.id.rg_1);
			if (newEmployment.isDone())
				disableRadioButtons(questionHolder.rgAnswers);
			if (items.get(position) instanceof Answer)
			{
				Answer answer = (Answer) items.get(position);
				((RadioButton) questionHolder.rgAnswers.getChildAt(answer.getAnswerID())).setChecked(true);
			}			
			questionHolder.item = items.get(position);
			
			questionHolder.rgAnswers.setTag(questionHolder);
			return row;
		}
		
		private void disableRadioButtons(RadioGroup radioGroup) {
			for (int i = 0; i < radioGroup.getChildCount(); i++)
				((RadioButton)radioGroup.getChildAt(i)).setEnabled(false);
		}

		public class QuestionHolder {
			public TextView tvQuestionName;
			public RadioGroup rgAnswers;
			public IQuestionable item;
		}
}

