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
import isebase.cognito.tourpilot_apk.Dialogs.BradenDialog;
import isebase.cognito.tourpilot_apk.Templates.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BradenSkalaActivity extends BaseActivity implements BaseDialogListener  {

	List<String> questionsName = new ArrayList<String>();
	Map<String, List<String>> answersNameMap = new LinkedHashMap<String, List<String>>();
	List<Answer> answers;
	List<String> answersName = new ArrayList<String>();
	Map<String, List<String>> subAnswersNameMap = new LinkedHashMap<String, List<String>>();
	ExpandableListView expListView;
    BradenDialog bradenDialog;
    int questionNumber;
    int answerNumber;
    Employment employment;
    Category category;
    ExpandableListAdapter expListAdapter;
    BaseDialog leavingDialog;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braden_skala);
        setTitle();
        reloadData();
        setQuestions();
        setAnswers();
        setSubAnswers();
        fillUpList();
    }
    
/*    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.action_set_braden:
			Intent intent  = new Intent(getBaseContext(), BradenSkalaPictureInfoActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
    }*/
    
    
    private void setTitle() {
    	setTitle(R.string.braden);
    }
    
    private void fillUpList() {
        expListView = (ExpandableListView) findViewById(R.id.elvBraden);
        expListAdapter = new ExpandableListAdapter(
                this, questionsName, answersNameMap, answers);
        expListView.setAdapter(expListAdapter);
 
        expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if (bradenDialog != null && bradenDialog.getFragmentManager() != null)
					return false;
				createBradenDialog(groupPosition, childPosition);
                return true;
			}
        });
    }
    
    private void createBradenDialog(int groupPosition, int childPosition) {
		bradenDialog = new BradenDialog(answersNameMap.get(questionsName.get(groupPosition)).get(childPosition), 
				subAnswersNameMap, HelperFactory.getHelper().getAnswerDAO().loadByQuestionIDAndType(groupPosition, 
						Category.type.braden.ordinal()), childPosition);
		bradenDialog.setCancelable(false);
		bradenDialog.show(getSupportFragmentManager(), "bradenDialog");
		questionNumber = groupPosition;
		answerNumber = childPosition;
		leavingDialog = new BaseDialog(getString(R.string.attention), getString(R.string.dialog_braden_point_marking));
    }
    public void setQuestions() {
        questionsName.add(getString(R.string.braden_question_0));
        questionsName.add(getString(R.string.braden_question_1));
        questionsName.add(getString(R.string.braden_question_2));
        questionsName.add(getString(R.string.braden_question_3));
        questionsName.add(getString(R.string.braden_question_4));
        questionsName.add(getString(R.string.braden_question_5));
    }
    
    public void setAnswers() {
    	List<String> list;
    	for (String question : questionsName) {
    		list = new ArrayList<String>();
    		if (question.equals(getString(R.string.braden_question_0)))	{    			
    			list.add(getString(R.string.braden_answer_0_0));
    			list.add(getString(R.string.braden_answer_0_1));
    			list.add(getString(R.string.braden_answer_0_2));
    			list.add(getString(R.string.braden_answer_0_3));
    			answersNameMap.put(question, list);
    		}
    		else if (question.equals(getString(R.string.braden_question_1))) {
    			list.add(getString(R.string.braden_answer_1_0));
    			list.add(getString(R.string.braden_answer_1_1));
    			list.add(getString(R.string.braden_answer_1_2));
    			list.add(getString(R.string.braden_answer_1_3));
    			answersNameMap.put(question, list);
			}
    		else if (question.equals(getString(R.string.braden_question_2))) {
    			list.add(getString(R.string.braden_answer_2_0));
    			list.add(getString(R.string.braden_answer_2_1));
    			list.add(getString(R.string.braden_answer_2_2));
    			list.add(getString(R.string.braden_answer_2_3));
    			answersNameMap.put(question, list);
			}
    		else if (question.equals(getString(R.string.braden_question_3))) {
    			list.add(getString(R.string.braden_answer_3_0));
    			list.add(getString(R.string.braden_answer_3_1));
    			list.add(getString(R.string.braden_answer_3_2));
    			list.add(getString(R.string.braden_answer_3_3));
    			answersNameMap.put(question, list);
			}
    		else if (question.equals(getString(R.string.braden_question_4))) {
    			list.add(getString(R.string.braden_answer_4_0));
    			list.add(getString(R.string.braden_answer_4_1));
    			list.add(getString(R.string.braden_answer_4_2));
    			list.add(getString(R.string.braden_answer_4_3));
    			answersNameMap.put(question, list);
			}
    		else {
    			list.add(getString(R.string.braden_answer_5_0));
    			list.add(getString(R.string.braden_answer_5_1));
    			list.add(getString(R.string.braden_answer_5_2));
    			answersNameMap.put(question, list);
    		}    		
    	}
    	answersName.add(getString(R.string.braden_answer_0_0));
		answersName.add(getString(R.string.braden_answer_0_1));
		answersName.add(getString(R.string.braden_answer_0_2));
		answersName.add(getString(R.string.braden_answer_0_3));
		answersName.add(getString(R.string.braden_answer_1_0));
		answersName.add(getString(R.string.braden_answer_1_1));
		answersName.add(getString(R.string.braden_answer_1_2));
		answersName.add(getString(R.string.braden_answer_1_3));
		answersName.add(getString(R.string.braden_answer_2_0));
		answersName.add(getString(R.string.braden_answer_2_1));
		answersName.add(getString(R.string.braden_answer_2_2));
		answersName.add(getString(R.string.braden_answer_2_3));
		answersName.add(getString(R.string.braden_answer_3_0));
		answersName.add(getString(R.string.braden_answer_3_1));
		answersName.add(getString(R.string.braden_answer_3_2));
		answersName.add(getString(R.string.braden_answer_3_3));
		answersName.add(getString(R.string.braden_answer_4_0));
		answersName.add(getString(R.string.braden_answer_4_1));
		answersName.add(getString(R.string.braden_answer_4_2));
		answersName.add(getString(R.string.braden_answer_4_3));
		answersName.add(getString(R.string.braden_answer_5_0));
		answersName.add(getString(R.string.braden_answer_5_1));
		answersName.add(getString(R.string.braden_answer_5_2));
    }
    
    private void setSubAnswers() {	
    	List<String> list;
    	for (String answer : answersName) {
    		list = new ArrayList<String>();
            if (answer.equals(getString(R.string.braden_answer_0_0))) {
            	list.add(getString(R.string.braden_subanswer_0_0_0));
            	list.add(getString(R.string.braden_subanswer_0_0_1));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_0_1))) {
            	list.add(getString(R.string.braden_subanswer_0_1_0));
            	list.add(getString(R.string.braden_subanswer_0_1_1));
            	list.add(getString(R.string.braden_subanswer_0_1_2));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_0_2))) {
            	list.add(getString(R.string.braden_subanswer_0_2_0));
            	list.add(getString(R.string.braden_subanswer_0_2_1));
            	list.add(getString(R.string.braden_subanswer_0_2_2));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_0_3))) {
            	list.add(getString(R.string.braden_subanswer_0_3_0));
            	list.add(getString(R.string.braden_subanswer_0_3_1));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_1_0))) {
            	list.add(getString(R.string.braden_subanswer_1_0_0));
            	list.add(getString(R.string.braden_subanswer_1_0_1));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_1_1))) {
            	list.add(getString(R.string.braden_subanswer_1_1_0));
            	list.add(getString(R.string.braden_subanswer_1_1_1));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_1_2))) {
            	list.add(getString(R.string.braden_subanswer_1_2_0));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_1_3))) {
            	list.add(getString(R.string.braden_subanswer_1_3_0));
            	list.add(getString(R.string.braden_subanswer_1_3_1));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_2_0))) {
            	list.add(getString(R.string.braden_subanswer_2_0_0));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_2_1))) {
            	list.add(getString(R.string.braden_subanswer_2_1_0));
            	list.add(getString(R.string.braden_subanswer_2_1_1));
            	list.add(getString(R.string.braden_subanswer_2_1_2));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_2_2))) {
            	list.add(getString(R.string.braden_subanswer_2_2_0));
            	list.add(getString(R.string.braden_subanswer_2_2_1));
            	list.add(getString(R.string.braden_subanswer_2_2_2));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_2_3))) {
            	list.add(getString(R.string.braden_subanswer_2_3_0));
            	list.add(getString(R.string.braden_subanswer_2_3_1));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_3_0))) {
            	list.add(getString(R.string.braden_subanswer_3_0_0));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_3_1))) {
            	list.add(getString(R.string.braden_subanswer_3_1_0));
            	list.add(getString(R.string.braden_subanswer_3_1_1));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_3_2))) {
            	list.add(getString(R.string.braden_subanswer_3_2_0));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_3_3))) {
            	list.add(getString(R.string.braden_subanswer_3_3_0));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_4_0))) {
            	list.add(getString(R.string.braden_subanswer_4_0_0));
            	list.add(getString(R.string.braden_subanswer_4_0_1));
            	list.add(getString(R.string.braden_subanswer_4_0_2));
            	list.add(getString(R.string.braden_subanswer_4_0_3));
            	list.add(getString(R.string.braden_subanswer_4_0_4));
            	list.add(getString(R.string.braden_subanswer_4_0_5));
            	list.add(getString(R.string.braden_subanswer_4_0_6));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_4_1))) {
             	list.add(getString(R.string.braden_subanswer_4_1_0));
            	list.add(getString(R.string.braden_subanswer_4_1_1));
            	list.add(getString(R.string.braden_subanswer_4_1_2));
            	list.add(getString(R.string.braden_subanswer_4_1_3));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_4_2))) {
             	list.add(getString(R.string.braden_subanswer_4_2_0));
            	list.add(getString(R.string.braden_subanswer_4_2_1));
            	list.add(getString(R.string.braden_subanswer_4_2_2));
            	list.add(getString(R.string.braden_subanswer_4_2_3));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_4_3))) {
             	list.add(getString(R.string.braden_subanswer_4_3_0));
            	list.add(getString(R.string.braden_subanswer_4_3_1));
            	list.add(getString(R.string.braden_subanswer_4_3_2));
            	list.add(getString(R.string.braden_subanswer_4_3_3));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_5_0))) {
             	list.add(getString(R.string.braden_subanswer_5_0_0));
            	list.add(getString(R.string.braden_subanswer_5_0_1));
            	list.add(getString(R.string.braden_subanswer_5_0_2));
            	list.add(getString(R.string.braden_subanswer_5_0_3));
            	list.add(getString(R.string.braden_subanswer_5_0_4));
            	subAnswersNameMap.put(answer, list);
            }
            else if (answer.equals(getString(R.string.braden_answer_5_1))) {
             	list.add(getString(R.string.braden_subanswer_5_1_0));
            	list.add(getString(R.string.braden_subanswer_5_1_1));
            	list.add(getString(R.string.braden_subanswer_5_1_2));
            	list.add(getString(R.string.braden_subanswer_5_1_3));
            	subAnswersNameMap.put(answer, list);
            }
            else {
             	list.add(getString(R.string.braden_subanswer_5_2_0));
            	list.add(getString(R.string.braden_subanswer_5_2_1));
            	list.add(getString(R.string.braden_subanswer_5_2_2));
            	subAnswersNameMap.put(answer, list);
            }
    	}
    }
 
    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
 
        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }
    
    private void reloadData() {
		employment = HelperFactory.getHelper().getEmploymentDAO().loadAll((int)Option.Instance().getEmploymentID());
    	category = HelperFactory.getHelper().getCategoryDAO().loadByCategoryName(getString(R.string.braden));
//    	answers = HelperFactory.getHelper().getAnswerDAO().loadByCategoryID(category.getId());
    	answers = HelperFactory.getHelper().getAnswerDAO().loadByCategoryIDAndType(category.getId(), Category.type.braden);
    	for (int i = 0; i < answers.size(); i++)
    		if (answers.get(i).getQuestionID() == 6) {
    			answers.remove(i);
    			break;
    		}
    }
 
    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.braden_skala, menu);
        return true;
    }

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		if (dialog.getTag() == "bradenDialog" && ((BradenDialog)dialog).getCurrentLayout() != null)	{
			String selectedCheckBoxes = getSelectedCheckBoxes(((BradenDialog)dialog).getCurrentLayout());
			if (selectedCheckBoxes.equals(""))
				return;
			String answerKey = getAnswerKey(selectedCheckBoxes, 
					getCurrentLevel(((BradenDialog)dialog).getCurrentLayout()));
			saveAnswer(HelperFactory.getHelper().getAnswerDAO().loadByQuestionIDAndType(questionNumber, Category.type.braden.ordinal()), answerKey);
			updateList();
			lastQuestionCheck();
		}
		
		if (dialog.getTag() == "leavingDialog") {
			Intent intent  = new Intent(getBaseContext(), BradenSkalaPictureInfoActivity.class);
			startActivity(intent);
		}
		
	}
	
	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		if (dialog.getTag() == "leavingDialog") {
			onBackPressed();
		}	
	}
	
	private String getSelectedCheckBoxes(LinearLayout linearLayout) {
		int checkBoxIndex = 0;
		String selectedCheckBoxes = "";
		for (int i = 0; i < linearLayout.getChildCount(); i++)
			if (linearLayout.getChildAt(i) instanceof CheckBox){
				if (((CheckBox)linearLayout.getChildAt(i)).isChecked())
					selectedCheckBoxes += checkBoxIndex + "/";
				checkBoxIndex++;
			}
		return selectedCheckBoxes;
	}
	
	private int getCurrentLevel(LinearLayout linearLayout) {
		for (int i = 0; i < ((RelativeLayout)(linearLayout).getParent()).getChildCount(); i++)
			if (((RelativeLayout)(linearLayout).getParent()).getChildAt(i).equals(linearLayout))
				return i;
		return -1;
	}
	
	private String getAnswerKey(String selectedCheckBoxes, int level) {
		return String.format("%d#%s%%%d", answerNumber, selectedCheckBoxes, level);
	}
	
	private void saveAnswer(Answer answer, String answerKey) {
		if (answer == null)
			answer = new Answer(employment.getPatient().getId(), questionNumber, 
					questionsName.get(questionNumber), answerNumber, category.getId(), answerKey, Category.type.braden.ordinal());
		else
			answer.setAnswerKey(answerKey);
		HelperFactory.getHelper().getAnswerDAO().save(answer);
	}
	
	private void updateList() {
//		expListAdapter.answers = answers = HelperFactory.getHelper().getAnswerDAO().loadByCategoryID(category.getId());
		expListAdapter.answers = answers = HelperFactory.getHelper().getAnswerDAO().loadByCategoryIDAndType(category.getId(), Category.type.braden);
		expListAdapter.notifyDataSetChanged();
	}
	
	private void lastQuestionCheck() {
		if (HelperFactory.getHelper().getAnsweredCategoryDAO().loadByCategoryID(category.getId()) == null && (answers.size() == 7 || allQuestionsAnswered()))
		{
			HelperFactory.getHelper().getAnsweredCategoryDAO().save(new AnsweredCategory(category.getId(), employment.getId()));
			Answer bradenSkalaPointAnswer = HelperFactory.getHelper().getAnswerDAO().loadByQuestionIDAndType(6, Category.type.braden.ordinal());
			if (bradenSkalaPointAnswer != null)
				onBackPressed();
			else
				leavingDialog.show(getSupportFragmentManager(), "leavingDialog");
		}
	}

	
	private boolean allQuestionsAnswered() {
		if (answers.size() != 6)
				return false;
		for (Answer answer : answers) {
			if (answers.size() == 6 && answer.getQuestionID() == 6)
				return false;
		}
		return true;
	}

}
