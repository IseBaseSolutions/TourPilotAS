package isebase.cognito.tourpilot_apk.Activity.QuestionActivities;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot_apk.Data.Answer.Answer;
import isebase.cognito.tourpilot_apk.Data.AnsweredCategory.AnsweredCategory;
import isebase.cognito.tourpilot_apk.Data.Category.Category;
import isebase.cognito.tourpilot_apk.Data.Employment.Employment;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialog;
import isebase.cognito.tourpilot_apk.Dialogs.BaseDialogListener;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

public class FallenFactorSkalaActivity extends BaseActivity implements BaseDialogListener {

	Category category;
	List<Answer> answers;
	Employment newEmployment;
	ToggleButton toggleButton;
	LinearLayout llMain;
	BaseDialog changeAnswerDialog;
	boolean isCategoryAnswered;
	ChangedAnswer changedAnswer;
	
	public boolean isCategoryAnswered() {
		return isCategoryAnswered;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fallen_factor);
		setTitle();
		reloadData();
		initControls();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
	
    private void setTitle() {
    	setTitle(R.string.fallenFactor);
    }

	private void reloadData() {
		category = HelperFactory.getHelper().getCategoryDAO().loadByCategoryName(getString(R.string.fallenFactor));
		answers = HelperFactory.getHelper().getAnswerDAO().loadByCategoryIDAndType(category.getId(), Category.type.sturzrisiko);
		isCategoryAnswered = HelperFactory.getHelper().getAnsweredCategoryDAO().loadByCategoryID(category.getId()) != null;
		newEmployment = HelperFactory.getHelper().getEmploymentDAO().loadAll((int)Option.Instance().getEmploymentID());
	}
		
	private void initControls() {
		toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
		llMain = (LinearLayout) findViewById(R.id.linear_Layout_main);	
		if (isCategoryAnswered()) {
			toggleButton.setVisibility(View.GONE);
			showAnsweredQuestions();			
		}
		else {
			hideAnsweredQuestions();
		}
		
	}
	
	private void hideAnsweredQuestions() {
		visibilityAction(View.GONE);
	}
	
	private void showAnsweredQuestions() {
		visibilityAction(View.VISIBLE);
	}
	
	private void visibilityAction(int visibility) {
		int questionIndex = 0;
		for (int i = 0; i < llMain.getChildCount(); i++) {
			if (llMain.getChildAt(i) instanceof LinearLayout) {
				((LinearLayout)llMain.getChildAt(i)).setVisibility(isQuestionAnswered(questionIndex) 
						? visibility 
						: getOpositeVisibility(visibility));
				if (isQuestionAnswered(questionIndex))
					setAnswers((LinearLayout)llMain.getChildAt(i), questionIndex);
				questionIndex++;
			}
		}
	}
	
	private int getOpositeVisibility(int visibility) {
		return visibility == View.VISIBLE ? View.GONE : View.VISIBLE;
	}
	
	private void setAnswers(LinearLayout linearLayout, int questionIndex) {
		for (int i = 0; i < linearLayout.getChildCount(); i++) {
			if (linearLayout.getChildAt(i) instanceof RadioGroup)
				setCheckedAnsweredRadioButtons((RadioGroup)linearLayout.getChildAt(i), questionIndex);
		}			
	}
	
	private void setCheckedAnsweredRadioButtons(RadioGroup radioGroup, int questionIndex) {
		for (int i = 0; i < radioGroup.getChildCount(); i++) {
			if (i == findAnswerByQuestionId(questionIndex).getAnswerID() 
					|| (i == (radioGroup.getChildCount() - 1) && findAnswerByQuestionId(questionIndex).getAnswerID() == -1))
				((RadioButton)radioGroup.getChildAt(i)).setChecked(true);
			((RadioButton)radioGroup.getChildAt(i)).setEnabled(!newEmployment.isDone());
		}
	}
	
	private boolean isQuestionAnswered(int questionIndex) {
		return findAnswerByQuestionId(questionIndex) != null;
	}
	
	private Answer findAnswerByQuestionId(int questionIndex) {
		for (Answer answer : answers)
			if (answer.getQuestionID() == questionIndex)
				return answer;
		return null;
	}
	
	public void onRadioButtonClick(View view) {
		getAnswerFromMainLayout((LinearLayout)view.getParent().getParent().getParent(), (RadioButton)view);
		if (answers.size() < 8 || isCategoryAnswered)
			return;
		HelperFactory.getHelper().getAnsweredCategoryDAO().save(new AnsweredCategory(category.getId(), newEmployment.getId()));
		onBackPressed();
	}
	
	private void initChangeAnswerDialog(Answer answer) {
		String title = getString(R.string.answer_changing);
		String text = getString(R.string.do_you_want_change_answer);
		changeAnswerDialog = new BaseDialog(title, text);
		changeAnswerDialog.show(getSupportFragmentManager(), "changeAnswerDialog");
	}
	
	private void getAnswerFromMainLayout(LinearLayout linearLayout, RadioButton radioButton) {
		int questionIndex = 0;
		for (int i = 0; i < linearLayout.getChildCount(); i++)
			if (linearLayout.getChildAt(i) instanceof LinearLayout) {
				if (getAnswerFromRadioGroup((RadioGroup)((LinearLayout)linearLayout.getChildAt(i)).getChildAt(1), questionIndex, radioButton))
					return;
				questionIndex++;
			}
	}
	
	private boolean getAnswerFromRadioGroup(RadioGroup radioGroup, int questionIndex, RadioButton radioButton) {
		for (int i = 0; i < radioGroup.getChildCount(); i++)
			if (((RadioButton)radioGroup.getChildAt(i)).equals(radioButton)) {
				if (!isCategoryAnswered() && !isQuestionAnswered(questionIndex))
					((LinearLayout)radioGroup.getParent()).setVisibility(View.GONE);
				saveAnswer(questionIndex, getAnswerIndex(radioGroup.getChildCount(), i));
				return true;
			}
		return false;
	}
	
	private int getAnswerIndex(int answersCount, int  radioButtonIndex) {
		return (answersCount - 1) == radioButtonIndex ? -1 : radioButtonIndex;
	}
	
	private void saveAnswer(int questionIndex, int answerIndex) {
		if (isCategoryAnswered() || isQuestionAnswered(questionIndex)) {
			initChangeAnswerDialog(findAnswerByQuestionId(questionIndex));
			changedAnswer = new ChangedAnswer(answerIndex, findAnswerByQuestionId(questionIndex));
			return;
		}
		Answer answer = getAnswer(questionIndex, answerIndex);
		HelperFactory.getHelper().getAnswerDAO().save(answer);
	}
	
	private Answer getAnswer(int questionIndex, int answerIndex) {
		Answer answer = findAnswerByQuestionId(questionIndex);
		if (answer != null) {
			answer.setAnswerID(answerIndex);
			return answer;
		}
		answer = new Answer(newEmployment.getPatient().getId(), questionIndex, "", answerIndex, category.getId(), "", Category.type.sturzrisiko.ordinal());
		answers.add(answer);
		return answer;
	}
	
	public void onToggleButtonSwitched(View view) {
		if(toggleButton.isChecked()) {
			showAnsweredQuestions();
		}
		else {
			hideAnsweredQuestions();
		}
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		changedAnswer.answer.setAnswerID(changedAnswer.answerIndex);
		HelperFactory.getHelper().getAnswerDAO().save(changedAnswer.answer);
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		int count = 0;
		for (int i = 0; i < llMain.getChildCount(); i++)
			if (llMain.getChildAt(i) instanceof LinearLayout) {
				if (count == changedAnswer.answer.getQuestionID()) {
					setAnswers((LinearLayout)llMain.getChildAt(i), changedAnswer.answer.getQuestionID());
					return;
				}
				count++;
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
