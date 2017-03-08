package isebase.cognito.tourpilot.Activity.QuestionActivities;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot.Data.Answer.Answer;
import isebase.cognito.tourpilot.Data.AnsweredCategory.AnsweredCategory;
import isebase.cognito.tourpilot.Data.Category.Category;
import isebase.cognito.tourpilot.Data.Employment.Employment;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.Data.Question.IQuestionable;
import isebase.cognito.tourpilot.Data.Question.Question;
import isebase.cognito.tourpilot.Data.RelatedQuestionSetting.RelatedQuestionSetting;
import isebase.cognito.tourpilot.DataBase.HelperFactory;
import isebase.cognito.tourpilot.Templates.QuestionAdapter;
import isebase.cognito.tourpilot.Templates.QuestionAdapter.QuestionHolder;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RelatedQuestionsActivity extends BaseActivity {

	QuestionAdapter questionAdapter;
	ListView lvRelatedQuestions;
	List<IQuestionable> questions = new ArrayList<IQuestionable>();
	List<RelatedQuestionSetting> relatedQuestionSettings = new ArrayList<RelatedQuestionSetting>();
	Employment employment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_related_questions);
		initControls();
		reloadData();
		fillUp();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	private void initControls() {
		lvRelatedQuestions = (ListView) findViewById(R.id.lvRelatedQuestions);

	}

	private void reloadData() {
		relatedQuestionSettings = HelperFactory.getHelper().getRelatedQuestionSettingDAO().load();
		questions.clear();
		questions.addAll(HelperFactory.getHelper().getQuestionDAO()
				.loadByRelatedQuestionSettings(relatedQuestionSettings));
		employment = HelperFactory.getHelper().getEmploymentDAO()
				.loadAll((int) Option.Instance().getEmploymentID());

	}

	private void fillUp() {
		questionAdapter = new QuestionAdapter(this, R.layout.row_question,
				questions);
		lvRelatedQuestions.setAdapter(questionAdapter);
		lvRelatedQuestions
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {

					}
				});
	}

	public void onRadioButtonClicked(View view) {
		QuestionHolder questionHolder = (QuestionHolder) ((RadioGroup) view
				.getParent()).getTag();
		int answerIndex = questionHolder.rgAnswers
				.indexOfChild((RadioButton) view);
		Question question = (Question) questionHolder.item;
		answer(question, answerIndex);
		reloadData();
		questionAdapter.notifyDataSetChanged();
		if (questions.size() > 0)
			return;
		onBackPressed();
	}
	
	private void answer(Question question, int answerIndex) {
		if (HelperFactory.getHelper().getAnswerDAO().loadByQuestionID(question.getId()) != null)
			return;
		
		HelperFactory.getHelper().getAnswerDAO().save(new Answer(employment.getPatient(), question, answerIndex,
				-1, Category.type.normal.ordinal()));		
		
		List<Answer> answers = new ArrayList<Answer>();
		for (RelatedQuestionSetting item : relatedQuestionSettings) {
			if (item.getId() != question.getId())
				continue;
			for (RelatedQuestionSetting.RelatedObject relatedObject : item.getRelatedObjects()) {
				if (relatedObject.ownerAnswer != answerIndex)
					continue;
				if (relatedObject.answer == -1) {
					AnsweredCategory answeredCategory = new AnsweredCategory(relatedObject.id, Option.Instance().getEmploymentID());
					HelperFactory.getHelper().getAnsweredCategoryDAO().save(answeredCategory);
					continue;
				}					
				Question relatedQuestion = HelperFactory.getHelper().getQuestionDAO().load(relatedObject.id);
				if (relatedQuestion == null)
					continue;
				answer(relatedQuestion, relatedObject.answer);
			}
		}

		HelperFactory.getHelper().getAnswerDAO().save(answers);
	}

}
