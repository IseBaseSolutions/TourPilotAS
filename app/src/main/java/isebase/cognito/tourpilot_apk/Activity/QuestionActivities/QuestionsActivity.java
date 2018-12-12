package isebase.cognito.tourpilot_apk.Activity.QuestionActivities;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot_apk.Activity.TasksAssessmentsActivity.TasksAssessementsActivity;
import isebase.cognito.tourpilot_apk.Data.Answer.Answer;
import isebase.cognito.tourpilot_apk.Data.AnsweredCategory.AnsweredCategory;
import isebase.cognito.tourpilot_apk.Data.Category.Category;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.Question.IQuestionable;
import isebase.cognito.tourpilot_apk.Data.Question.Question;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialog;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialogListener;
import isebase.cognito.tourpilot_apk.Templates.QuestionAdapter;
import isebase.cognito.tourpilot_apk.Templates.QuestionAdapter.QuestionHolder;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

public class QuestionsActivity extends BaseActivity implements BaseDialogListener {

	ListView lvQuestions;
	ListView lvAnswers;
	ToggleButton tbSwitch;
	List<IQuestionable> questions = new ArrayList<IQuestionable>();
	List<IQuestionable> answers = new ArrayList<IQuestionable>();
	List<Question> subQuestions = new ArrayList<Question>();
	Category category;
	QuestionAdapter questionAdapter;
	QuestionAdapter answerAdapter;
	BaseDialog changeAnswerDialog;
	ChangedAnswer changedAnswer;
	
	BaseDialog leavingDialog;
	
	boolean isCategoryAnswered;
	
	public boolean isCategoryAnswered() {
		return isCategoryAnswered;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions);
		initControls();
		reloadData();
		fillUpTitle();
		fillUp();
		setVisibility();
	}
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		if (!category.getName().equals("Kontrakturrisikoerfassung"))
			return false;
		
    	getMenuInflater().inflate(R.menu.kontraktur_picture_info, menu);
        return true;
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_set_kontraktur_points:
			Intent intent = new Intent(getBaseContext(), KontrakturPictureInfoActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void startTasksActivity() {
		Intent tasksActivity = new Intent(getApplicationContext(), TasksAssessementsActivity.class);
		startActivity(tasksActivity);
	}
	
	private void initControls() {
		lvQuestions = (ListView) findViewById(R.id.lvQuestions);
		lvAnswers = (ListView) findViewById(R.id.lvAnswers);
		tbSwitch = (ToggleButton) findViewById(R.id.tgSwitch);
	}
	
	private void reloadData() {
		int categoryID = -1;
		Intent intentFlege = getIntent();
		Bundle bundle = intentFlege.getExtras();
		if(bundle != null)
			categoryID = bundle.getInt("categoryID");		
		category = HelperFactory.getHelper().getCategoryDAO().load(categoryID);
//		answers.addAll(HelperFactory.getHelper().getAnswerDAO().loadByCategoryID(categoryID));
		answers.addAll(HelperFactory.getHelper().getAnswerDAO().loadByCategoryIDAndType(category.getId(), Category.type.normal));
		isCategoryAnswered = HelperFactory.getHelper().getAnsweredCategoryDAO().loadByCategoryID(category.getId()) != null;
		initQuestions(HelperFactory.getHelper().getQuestionDAO().loadActualsByCategory(categoryID));
	}
	
	private void fillUpTitle() {
		setTitle(category.getName());
	}
	
	private void fillUp() {		
		questionAdapter = new QuestionAdapter(this,
				R.layout.row_question, questions);
		lvQuestions.setAdapter(questionAdapter);
		lvQuestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

			}
		});
		answerAdapter = new QuestionAdapter(this,
				R.layout.row_question, answers);
		lvAnswers.setAdapter(answerAdapter);
		lvAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

			}
		});		
	}
	
	public void onRadioButtonClicked(View view) {
		QuestionHolder questionHolder = (QuestionHolder) ((RadioGroup) view.getParent()).getTag();
		int answerIndex = questionHolder.rgAnswers.indexOfChild((RadioButton) view);
		if (questionHolder.item instanceof Question)
			questionRadioButtonClicked(questionHolder, answerIndex);
		else
			answerRadioButtonClicked(questionHolder, answerIndex);
		if (questions.size() > 0 || isCategoryAnswered())
			return;
		if (HelperFactory.getHelper().getAnsweredCategoryDAO().loadByCategoryID(category.getId()) == null)
			HelperFactory.getHelper().getAnsweredCategoryDAO().save(new AnsweredCategory(category.getId(), Option.Instance().getEmploymentID()));
		if (!category.getName().equals("Kontrakturrisikoerfassung"))
			onBackPressed();
		else {
			Answer pointAnswer = HelperFactory.getHelper().getAnswerDAO().loadByQuestionIDAndType(-1, Category.type.normal.ordinal());
			if (pointAnswer != null) {
				onBackPressed();
				return;
			}
			leavingDialog = new BaseDialog(getString(R.string.attention), getString(R.string.dialog_kontraktur_point_marking));
			leavingDialog.show(getSupportFragmentManager(), "leavingDialog");
		}
	}
	
	private void questionRadioButtonClicked(QuestionHolder questionHolder, int answerIndex) {
		Question question = (Question) questionHolder.item;
		saveAnswer(new Answer(questionAdapter.patient, question, answerIndex, category.getId(), Category.type.normal.ordinal()));
		if (!question.isSubQuestion())
			addSubQuestions(question);
		removeQuestionFromList(questionHolder);			
		questionAdapter.notifyDataSetChanged();
	}
	
	private void answerRadioButtonClicked(QuestionHolder questionHolder, int answerIndex) {
		changedAnswer = new ChangedAnswer(answerIndex, (Answer) questionHolder.item);
		initChangeAnswerDialog(changedAnswer.answer);
		answerAdapter.notifyDataSetChanged();
	}
	
	private void removeQuestionFromList(QuestionHolder questionHolder) {
		questionHolder.tvQuestionName.setVisibility(View.GONE);
		questionHolder.rgAnswers.setVisibility(View.GONE);
		questions.remove((Question) questionHolder.item);
	}
	
	private void addSubQuestions(Question question) {
		for (Question subQuestionItem : subQuestions)
			if (isOwnerAnswered(subQuestionItem))
				questions.add(questions.indexOf(question), subQuestionItem);
	}
	
	private void saveAnswer(Answer answer) {
		answers.add(answer);
		HelperFactory.getHelper().getAnswerDAO().save(answer);
	}
	
	private void initChangeAnswerDialog(Answer answer) {
		String title = getString(R.string.answer_changing);
		String text = getString(R.string.do_you_want_change_answer);
		changeAnswerDialog = new BaseDialog(title, text);
		changeAnswerDialog.show(getSupportFragmentManager(), "changeAnswerDialog");
	}
	
	public void onTgSwitchClicked(View view) {
		lvQuestions.setVisibility(lvQuestions.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
		lvAnswers.setVisibility(lvQuestions.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
	}
	
	private void setVisibility() {
		tbSwitch.setVisibility(questions.size() > 0 ? View.VISIBLE : View.GONE);
		lvQuestions.setVisibility(questions.size() > 0 ? View.VISIBLE : View.INVISIBLE);
		lvAnswers.setVisibility(lvQuestions.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);		
	}
	
	private void initQuestions(List<Question> allQuestions) {
		for (Question question : allQuestions)
		{
			if (question.isSubQuestion() && !isOwnerAnswered(question))
			{
				if (isSubQuestionIsAnswered(question))
					continue;
				else
					subQuestions.add(question);
			}
			else
				questions.add(question);
		}
	}
	
	private boolean isOwnerAnswered(Question question) {
		for (IQuestionable answer : answers)
			for (int i = 0; i < question.getOwnerIDs().length; i++)
				if (isOwner(question.getOwnerIDs()[i], (Answer)answer) && isRightAnswer(question, (Answer)answer) && !isSubQuestionIsAnswered(question))
					return true;
		return false;
	}
	
	private boolean isOwner(String ownerID, Answer answer) {
		return Integer.parseInt(ownerID) == answer.getQuestionID();
	}
	
	private boolean isRightAnswer(Question question, Answer answer) {
		return question.getKeyAnswer() == answer.getAnswerID();
	}
	
	private boolean isSubQuestionIsAnswered(Question subQuestion) {		
		return HelperFactory.getHelper().getAnswerDAO().isSubQuestionAnswered(subQuestion);
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		if (dialog.getTag() == "changeAnswerDialog") {
			changedAnswer.answer.setAnswerID(changedAnswer.answerIndex);
			HelperFactory.getHelper().getAnswerDAO().save(changedAnswer.answer);
			answerAdapter.notifyDataSetChanged();
		}
		if (dialog.getTag() == "leavingDialog") {
			Intent intent = new Intent(getBaseContext(), KontrakturPictureInfoActivity.class);
			startActivity(intent);
		}	
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		if (dialog.getTag() == "leavingDialog") {
			onBackPressed();
		}			
	}
	
	public class ChangedAnswer {
		int answerIndex;
		Answer answer;
		
		public ChangedAnswer(int answerIndex, Answer answer) {
			this.answerIndex = answerIndex;
			this.answer = answer;
		}
		
	}

}
