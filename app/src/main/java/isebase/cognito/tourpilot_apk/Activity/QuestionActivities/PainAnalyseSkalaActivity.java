package isebase.cognito.tourpilot_apk.Activity.QuestionActivities;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot_apk.Data.Answer.Answer;
import isebase.cognito.tourpilot_apk.Data.AnsweredCategory.AnsweredCategory;
import isebase.cognito.tourpilot_apk.Data.Category.Category;
import isebase.cognito.tourpilot_apk.Data.Employment.Employment;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.Patient.Patient;
import isebase.cognito.tourpilot_apk.Data.Relative.Relative;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class PainAnalyseSkalaActivity extends BaseActivity {

	Category category;
	Patient patient;
	Employment newEmployment;
	
	LinearLayout llAbleGiveData;	
	LinearLayout llHasAnyPain;
	
	LinearLayout llTest;
	
	LinearLayout llCanSomeoneAnswer;
	LinearLayout llRelativeOrNurse;
	
	LinearLayout llRelativeSelection;
	
	LinearLayout llGrade;
	
	TextView tvGrade;
	
	ArrayAdapter<String> relativesAdapter;	
	ArrayAdapter<String> gradesAdapter;
	
	Spinner spRelatives;
	Spinner spGrades;
	
	String additionalInfo;
	String answerKey = "";
	int grade = -1;
	
	Button btOK;
	
	Answer answer;
	
	/** Stages
	 * 1. В состоянии дать данные о боли
	 * 2. Тест
	 * 3. Может ли родственник или медсестра дать оценку
	 * 
	 * 
	 * **/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pain_analyse);
		setTitle();
		reloadData();
		initControls();
		if (answer != null)
			fillUpIfAnswered();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
    private void setTitle() {
    	setTitle(R.string.pain_analyse);
    }

	private void reloadData() {
		category = HelperFactory.getHelper().getCategoryDAO().loadByCategoryName(getString(R.string.pain_analyse));

		newEmployment = HelperFactory.getHelper().getEmploymentDAO().load((int)Option.Instance().getEmploymentID());
		patient = HelperFactory.getHelper().getPatientDAO().load(newEmployment.getPatientID());

		List<Answer> answers = HelperFactory.getHelper().getAnswerDAO().loadPainAnaliseAnswers();
		if (answers.size() > 0)
			answer = answers.get(0);
	}
	
	private void initControls() {
		llAbleGiveData = (LinearLayout) findViewById(R.id.linear_layout_able_give_data);
		llHasAnyPain = (LinearLayout) findViewById(R.id.linear_layout_has_any_pain);
		
		llTest = (LinearLayout) findViewById(R.id.linear_layout_test);		
		
		llCanSomeoneAnswer = (LinearLayout) findViewById(R.id.linear_layout_can_someone_answer);
		llRelativeOrNurse = (LinearLayout) findViewById(R.id.linear_layout_relative_or_nurse);
		llRelativeSelection = (LinearLayout) findViewById(R.id.linear_layout_relative_selection);
		llGrade = (LinearLayout) findViewById(R.id.linear_layout_grade);
		
		spRelatives = (Spinner) findViewById(R.id.sp_relative);
		List<Relative> relatives = HelperFactory.getHelper().getRelativeDAO().loadByIds(patient.getStrRelativeIDs());
		String[] relativesArr = new String[relatives.size()];
		for (int i = 0; i < relatives.size(); i++)
			relativesArr[i] = relatives.get(i).getFullName();
			
		relativesAdapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_spinner_item, relativesArr);
		// Specify the layout to use when the list of choices appears
		relativesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spRelatives.setAdapter(relativesAdapter);
		spRelatives.setOnItemSelectedListener(new SpinnerActivity());
		
		spGrades = (Spinner) findViewById(R.id.sp_grades);
		
		gradesAdapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_spinner_item, getGrades());
		// Specify the layout to use when the list of choices appears
		gradesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spGrades.setAdapter(gradesAdapter);
		spGrades.setOnItemSelectedListener(new SpinnerActivity());
		tvGrade = (TextView) findViewById(R.id.tv_grade);
		
		btOK = (Button) findViewById(R.id.bt_ok_grades);
	}
	
	private void fillUpIfAnswered() {
		if (answer.getAnswerKey().equals("")) {
			((RadioButton)((RadioGroup)llAbleGiveData.getChildAt(1)).getChildAt(0)).setChecked(true);
			llHasAnyPain.setVisibility(View.VISIBLE);
			if (answer.getAnswerID() == 0) {
				((RadioButton)((RadioGroup)llHasAnyPain.getChildAt(1)).getChildAt(1)).setChecked(true);
				if (!isEmploymentDone())
					showBtOk(R.id.linear_layout_has_any_pain);
			}
			else {
				showPatientGradeLayout();
				((RadioButton)((RadioGroup)llHasAnyPain.getChildAt(1)).getChildAt(0)).setChecked(true);
				((Spinner)(llGrade.getChildAt(1))).setSelection(answer.getAnswerID()-1);
				if (!isEmploymentDone())
					showBtOk(R.id.linear_layout_grade);
			}
		}
		else {
			((RadioButton)((RadioGroup)llAbleGiveData.getChildAt(1)).getChildAt(1)).setChecked(true);
			((RadioButton)((RadioGroup)llAbleGiveData.getChildAt(1)).getChildAt(1)).setEnabled(!isEmploymentDone());
			setTestAnswers();
			llTest.setVisibility(View.VISIBLE);
			llCanSomeoneAnswer.setVisibility(View.VISIBLE);
			if (answer.getAnswerID() == -1) {
				((RadioButton)((RadioGroup)llCanSomeoneAnswer.getChildAt(1)).getChildAt(1)).setChecked(true);
				if (!isEmploymentDone())
					showBtOk(R.id.linear_layout_can_someone_answer);
			}
			else {
				((RadioButton)((RadioGroup)llCanSomeoneAnswer.getChildAt(1)).getChildAt(0)).setChecked(true);
				llRelativeOrNurse.setVisibility(View.VISIBLE);
				if (answer.getAddInfo().equals("Nurse")) {
					showNurseGradeLayout();
					((RadioButton)((RadioGroup)llRelativeOrNurse.getChildAt(1)).getChildAt(0)).setChecked(true);
					((Spinner)(llGrade.getChildAt(1))).setSelection(answer.getAnswerID()-1);
				}
				else {
					showRelativeGradeLayout();
					llRelativeSelection.setVisibility(View.VISIBLE);
					((RadioButton)((RadioGroup)llRelativeOrNurse.getChildAt(1)).getChildAt(1)).setChecked(true);
					((Spinner)(llRelativeSelection.getChildAt(1))).setSelection(getSelectedRelativeIndex());
					((Spinner)(llGrade.getChildAt(1))).setSelection(answer.getAnswerID()-1);
				}
				if (!isEmploymentDone())
					showBtOk(R.id.linear_layout_grade);
			}
		}	
		((RadioButton)((RadioGroup)llAbleGiveData.getChildAt(1)).getChildAt(0)).setEnabled(!isEmploymentDone());
		((RadioButton)((RadioGroup)llAbleGiveData.getChildAt(1)).getChildAt(1)).setEnabled(!isEmploymentDone());
		((RadioButton)((RadioGroup)llHasAnyPain.getChildAt(1)).getChildAt(0)).setEnabled(!isEmploymentDone());
		((RadioButton)((RadioGroup)llHasAnyPain.getChildAt(1)).getChildAt(1)).setEnabled(!isEmploymentDone());
		((RadioButton)((RadioGroup)llCanSomeoneAnswer.getChildAt(1)).getChildAt(0)).setEnabled(!isEmploymentDone());
		((RadioButton)((RadioGroup)llCanSomeoneAnswer.getChildAt(1)).getChildAt(1)).setEnabled(!isEmploymentDone());
		((RadioButton)((RadioGroup)llRelativeOrNurse.getChildAt(1)).getChildAt(0)).setEnabled(!isEmploymentDone());
		((RadioButton)((RadioGroup)llRelativeOrNurse.getChildAt(1)).getChildAt(1)).setEnabled(!isEmploymentDone());
		((Spinner)(llRelativeSelection.getChildAt(1))).setEnabled(!isEmploymentDone());
		((Spinner)(llGrade.getChildAt(1))).setEnabled(!isEmploymentDone());
		for (int i = 0; i < llTest.getChildCount(); i++)
			for (int j = 0; j < ((LinearLayout)llTest.getChildAt(i)).getChildCount(); j++)
				if (((LinearLayout)llTest.getChildAt(i)).getChildAt(j) instanceof RadioGroup)
					for (int k = 0; k < ((RadioGroup)((LinearLayout)llTest.getChildAt(i)).getChildAt(j)).getChildCount(); k++)
						((CheckBox)((RadioGroup)((LinearLayout)llTest.getChildAt(i)).getChildAt(j)).getChildAt(k)).setEnabled(!isEmploymentDone());
	}
	
	private int getSelectedRelativeIndex() {
		for (int i = 0; i < relativesAdapter.getCount(); i++)
			if (relativesAdapter.getItem(i).equals(answer.getAddInfo()))
				return i;
		return -1;
	}
	
	private void setTestAnswers() {
		String[] answers = answer.getAnswerKey().split("=");
		for (int i = 0; i < llTest.getChildCount(); i++)
			setRadioGroupAnswers(((LinearLayout)llTest.getChildAt(i)), answers[i]);
	}
	
	private void setRadioGroupAnswers(LinearLayout linearLayout, String answer) {
		int radioGroupIndex = 0;
		for (int i = 0; i < linearLayout.getChildCount(); i++)
			if (linearLayout.getChildAt(i) instanceof RadioGroup) {
				if (radioGroupIndex == Integer.parseInt(answer.split("#")[1]))
					setAnswerCheckBoxes((RadioGroup)linearLayout.getChildAt(i), answer.split("#")[0]);
				radioGroupIndex++;
			}
	}
	
	private void setAnswerCheckBoxes(RadioGroup radioGroup, String answer) {
		String[] answers = answer.split("/");
		for (int i = 0; i < answers.length; i++)
			((CheckBox)radioGroup.getChildAt(Integer.parseInt(answers[i]))).setChecked(true);
	}
	
	private String[] getGrades() {
		String[] grades = new String[10];
		for (int i = 0; i < 10; i++) {
			grades[i] = "Angabe der Schmerzstärke:  " + (i + 1);
		}
		return grades;
	}
	
	public void onRadioButtonClicked(View view) {
		nextStage(view);
	}
	
	public void onCheckBoxChecked(View view) {
		if (isStageAnswered())
			nextStage(view);
		else
			hideAndClearUnnecessary();
		if (!((CheckBox)view).isChecked())
			return;
		clearAnotherLevelCheckBoxes((LinearLayout) view.getParent().getParent(), view);
	}
	
	public void onBtOkClicked(View view) {
		saveAnswer();
	}
	
	private void getAnswerKey() {
		for (int i = 0; i < llTest.getChildCount(); i++) {
			getAnswerKeyFromLayout((LinearLayout)llTest.getChildAt(i));
		}
	}
	
	private void getAnswerKeyFromLayout(LinearLayout linearLayout) {
		int radioGroupIndex = 0;
		for (int i = 0; i < linearLayout.getChildCount(); i++) {
			if (linearLayout.getChildAt(i) instanceof RadioGroup)
			{
				if (getAnswerKeyFromRadioGroup((RadioGroup)linearLayout.getChildAt(i), radioGroupIndex))
					return;
				radioGroupIndex++;
			}
		}
	}
	
	private boolean getAnswerKeyFromRadioGroup(RadioGroup radioGroup, int radioGroupIndex) {
		String answer = "";
		for (int i = 0; i < radioGroup.getChildCount(); i++) {
			if (((CheckBox)radioGroup.getChildAt(i)).isChecked())
				answer += (answer.equals("") ? "" : "/") + i ;
		}
		if (answer.equals(""))
			return false;
		answer += "#" + radioGroupIndex;
		answerKey += (answerKey.equals("") ? "" : "=") + answer;
		return true;
	}
	
	private boolean isStageAnswered() {
		int answeredQuestionsCount = 0;
		for (int i = 0; i < llTest.getChildCount(); i++)
			if (isQuestionAnswered((LinearLayout)llTest.getChildAt(i)))
				answeredQuestionsCount++;
		return answeredQuestionsCount < 5 ? false : true;
	}
	
	private void hideHasAnyPainLayout() {
		additionalInfo = "";
		llHasAnyPain.setVisibility(View.GONE);
		((RadioButton)((RadioGroup)llHasAnyPain.getChildAt(1)).getChildAt(0)).setChecked(false);
		((RadioButton)((RadioGroup)llHasAnyPain.getChildAt(1)).getChildAt(1)).setChecked(false);
	}
	
	private void hideAndClearUnnecessary() {
		hideCanSomeoneAnswerLayout();
		hideRelativeOrNurseLayot();
		hideRelativeSelectionLayout();
		hideGradeLayout();
	}	
	
	private void hideCanSomeoneAnswerLayout() {
		llCanSomeoneAnswer.setVisibility(View.GONE);
		((RadioButton)((RadioGroup)llCanSomeoneAnswer.getChildAt(1)).getChildAt(0)).setChecked(false);
		((RadioButton)((RadioGroup)llCanSomeoneAnswer.getChildAt(1)).getChildAt(1)).setChecked(false);
	}
	
	private void hideTestLayout() {
		llTest.setVisibility(View.GONE);
		for (int i = 0; i < llTest.getChildCount(); i++)
			clearTestLayout((LinearLayout)llTest.getChildAt(i));
	}
	
	private void clearTestLayout(LinearLayout linearLayout){
		for (int i = 0; i < linearLayout.getChildCount(); i++)
			if (linearLayout.getChildAt(i) instanceof RadioGroup)
				clearRadioGroup((RadioGroup)linearLayout.getChildAt(i));
	}
	
	private void clearRadioGroup(RadioGroup radioGroup) {
		for (int i = 0; i < radioGroup.getChildCount(); i++)
			((CheckBox)radioGroup.getChildAt(i)).setChecked(false);
	}
	
	private void hideRelativeSelectionLayout() {
		llRelativeSelection.setVisibility(View.GONE);
		((Spinner)llRelativeSelection.getChildAt(1)).setSelection(0);
	}
	
	private void hideRelativeOrNurseLayot() {
		llRelativeOrNurse.setVisibility(View.GONE);
		((RadioButton)((RadioGroup)llRelativeOrNurse.getChildAt(1)).getChildAt(0)).setChecked(false);
		((RadioButton)((RadioGroup)llRelativeOrNurse.getChildAt(1)).getChildAt(1)).setChecked(false);
		additionalInfo = "";
	}
	
	private void hideGradeLayout() {
		llGrade.setVisibility(View.GONE);
		((Spinner)llGrade.getChildAt(1)).setSelection(0);
		grade = 0;
	}
	
	private void hideBtOk() {
		btOK.setVisibility(View.GONE);
	}
	
	private void showTest() {
		llTest.setVisibility(View.VISIBLE);
	}
	
	private boolean isQuestionAnswered(LinearLayout linearLayout) {
		for(int i = 0; i < linearLayout.getChildCount(); i++)
			if (linearLayout.getChildAt(i) instanceof RadioGroup)
				if (isRadioGroupchecked((RadioGroup)linearLayout.getChildAt(i)))
					return true;
		return false;
	}
	
	private boolean isRadioGroupchecked(RadioGroup radioGroup) {
		for(int i = 0; i < radioGroup.getChildCount(); i++)
			if (((CheckBox)radioGroup.getChildAt(i)).isChecked())
				return true;
		return false;
	}
	
	private void clearAnotherLevelCheckBoxes(LinearLayout linearLayout, View view) {
		for (int i = 0; i < linearLayout.getChildCount(); i++)
			if (linearLayout.getChildAt(i) instanceof RadioGroup && linearLayout.getChildAt(i) != view.getParent())
				clearRadioGroupCheckBoxes((RadioGroup)linearLayout.getChildAt(i));
	}
	
	private void clearRadioGroupCheckBoxes(RadioGroup radioGroup) {
		for (int j = 0; j < radioGroup.getChildCount(); j++)
			((CheckBox)radioGroup.getChildAt(j)).setChecked(false);
	}
	
	private void nextStage(View view) {
		switch(getCurrentStage(view)) {
			case 0: ableGiveDataStage(view);
			break;
			case 1: testStage();
			break;
			case 2: canSomeoneAnswer(view);
			break;
			case 3: relativeOrNurse(view);
			break;
			case 4: hasAnyPain(view);
			break;
		}		
	}
	
	private int getCurrentStage(View view) {
		if (view instanceof CheckBox)
			return 1;
		else if (((LinearLayout)view.getParent().getParent()).equals(llAbleGiveData))
			return 0;
		else if (((LinearLayout)view.getParent().getParent()).equals(llCanSomeoneAnswer))
			return 2;
		else if (((LinearLayout)view.getParent().getParent()).equals(llRelativeOrNurse))
			return 3;
		else if (((LinearLayout)view.getParent().getParent()).equals(llHasAnyPain))
			return 4;
		else
			return 5;		
	}
	
	private void ableGiveDataStage(View view) {
		if (((RadioButton)view).getText() == getText(R.string.yes)) {
			llHasAnyPain.setVisibility(View.VISIBLE);
			hideTestLayout();
			hideAndClearUnnecessary();
			hideBtOk();
		}
		else {
			showTest();
			hideHasAnyPainLayout();
			hideBtOk();
			hideGradeLayout();
		}
	}
	
	private void testStage() {
		llCanSomeoneAnswer.setVisibility(View.VISIBLE);
	}
	
	private void canSomeoneAnswer(View view) {
		if (((RadioButton)view).getText() == getText(R.string.yes)) {
			hideBtOk();
			llRelativeOrNurse.setVisibility(View.VISIBLE);
		}
		else {
			showBtOk(R.id.linear_layout_can_someone_answer);
			hideRelativeOrNurseLayot();
			hideRelativeSelectionLayout();
			hideGradeLayout();
			additionalInfo = "Nurse";
			grade = -1;
		}
	}
	
	private void hasAnyPain(View view) {
		if (((RadioButton)view).getText() == getText(R.string.yes)) {
			additionalInfo = "Patient";
			showPatientGradeLayout();
			showBtOk(R.id.linear_layout_grade);
		}
		else {			
			hideGradeLayout();
			additionalInfo = "Patient";
			grade = 0;
			showBtOk(R.id.linear_layout_has_any_pain);
		}

	}
	
	private void showPatientGradeLayout() {
		llGrade.setVisibility(View.VISIBLE);
		tvGrade.setText("Der Patient gibt an, Schmerzen in einer Stärke von 1- 10 zu haben. 1 = "
				+ "kaum Schmerzen, 10 = stärkster vorstellbarer Schmerz. Bitte geben Sie die entsprechende "
				+ "Nummer in das Feld ein.");
		llGrade.setLayoutParams(getLayoutParamsBelow(R.id.linear_layout_has_any_pain));
		
	}
	
	private void showBtOk(int objectID) {
		btOK.setVisibility(View.VISIBLE);
		btOK.setLayoutParams(getLayoutParamsBelow(objectID));
	}
	
	private void relativeOrNurse(View view) {
		if (((RadioButton)view).getText() == getText(R.string.relative)) {
			llRelativeSelection.setVisibility(View.VISIBLE);
			showRelativeGradeLayout();
			showBtOk(R.id.linear_layout_grade);
		}
		else {
			additionalInfo = "Nurse";
			llRelativeSelection.setVisibility(View.GONE);
			showNurseGradeLayout();
		}
	}
	
	public void saveAnswer() {
		getAnswerKey();
		if (answer == null)
			answer = new Answer(patient.getId(), -1, "painAnalyse", grade, category.getId(), answerKey, Category.type.schmerzermittlung.ordinal());
		answer.setAnswerID(grade);		
		answer.setAddInfo(additionalInfo);
		answer.setAnswerKey(answerKey);
		HelperFactory.getHelper().getAnswerDAO().save(answer);
		if (HelperFactory.getHelper().getAnsweredCategoryDAO().loadByCategoryID(category.getId()) == null)
			HelperFactory.getHelper().getAnsweredCategoryDAO().save(new AnsweredCategory(category.getId(), Option.Instance().getEmploymentID()));
		onBackPressed();
	}
	
	private void showNurseGradeLayout() {
		llGrade.setVisibility(View.VISIBLE);
		tvGrade.setText("Bitte bewerten Sie bei "
				+ patient.getSurname() + " " + patient.FullClearName()
				+ " vorhandene Schmerzen in einer Stärke von 1-10 "
				+ "hat. 1 = kaum Schmerzen, 10 = stärkster vorstellbarer Schmerz. Bitte geben Sie die entsprechende Nummer "
				+ "in das Feld ein.");
		llGrade.setLayoutParams(getLayoutParamsBelow(R.id.linear_layout_relative_or_nurse));
		showBtOk(R.id.linear_layout_grade);
	}
	
	private RelativeLayout.LayoutParams getLayoutParamsBelow(int objectID) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
		        ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, objectID);
		return params;
	}
	
	private void showRelativeGradeLayout() {
		llGrade.setVisibility(View.VISIBLE);
		tvGrade.setText("Der Angehörige gibt an, dass "
				+ patient.getSurname() + " " + patient.FullClearName()
				+ " Schmerzen in einer Stärke von 1- 10 hat. 1 = "
				+ "kaum Schmerzen, 10 = stärkster vorstellbarer Schmerz. Bitte geben Sie die entsprechende Nummer in das "
				+ "Feld ein.");
		llGrade.setLayoutParams(getLayoutParamsBelow(R.id.linear_layout_relative_selection));
	}
	
	public class SpinnerActivity extends Activity implements OnItemSelectedListener {


		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg0.equals(spRelatives)) {
				additionalInfo = relativesAdapter.getItem(arg2);
				showRelativeGradeLayout();
			}
			else
				grade = Integer.parseInt(gradesAdapter.getItem(arg2).split("  ")[1]);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	}
	
	private boolean isEmploymentDone() {
		return newEmployment.isDone();
	}

}
