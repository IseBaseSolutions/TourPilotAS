package isebase.cognito.tourpilot.Activity.TasksAssessmentsActivity;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Activity.QuestionActivities.BradenSkalaActivity;
import isebase.cognito.tourpilot.Activity.QuestionActivities.FallenFactorSkalaActivity;
import isebase.cognito.tourpilot.Activity.QuestionActivities.NortonSkalaActivity;
import isebase.cognito.tourpilot.Activity.QuestionActivities.PainAnalyseSkalaActivity;
import isebase.cognito.tourpilot.Activity.QuestionActivities.QuestionsActivity;
import isebase.cognito.tourpilot.Data.Answer.Answer;
import isebase.cognito.tourpilot.Data.AnsweredCategory.AnsweredCategory;
import isebase.cognito.tourpilot.Data.Category.Category;
import isebase.cognito.tourpilot.Data.Category.Category.type;
import isebase.cognito.tourpilot.Data.ExtraCategory.ExtraCategory;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.Data.Question.Question;
import isebase.cognito.tourpilot.Data.QuestionSetting.QuestionSetting;
import isebase.cognito.tourpilot.Data.RelatedQuestionSetting.RelatedQuestionSetting;
import isebase.cognito.tourpilot.DataBase.HelperFactory;
import isebase.cognito.tourpilot.Dialogs.BaseDialog;
import isebase.cognito.tourpilot.Dialogs.ExtraCategoriesDialog;
import isebase.cognito.tourpilot.Templates.EmploymentCategoryAdapter;
import isebase.cognito.tourpilot.Utils.DateUtils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class AssessmentsFragment extends Fragment {

	TasksAssessementsActivity activity;
	
	View rootView;
	public QuestionSetting questionSetting;
	ListView lvAssessments;
	int allCategoriesCount;
	List<Category> categories = new ArrayList<Category>();
	public List<AnsweredCategory> answeredCategories = new ArrayList<AnsweredCategory>();
	List<EmploymentCategory> employmentCategories = new ArrayList<EmploymentCategory>();
	EmploymentCategoryAdapter adapter;
	
	BaseDialog relatedQuestionsDialog;
	
	List<RelatedQuestionSetting> relatedQuestionSettings;
	List<Question> relatedQuestions;
	List<Answer> relatedAnswers;
	
	public AssessmentsFragment() {
		
	}
	
	public AssessmentsFragment(TasksAssessementsActivity instance) {
		activity = instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(
				R.layout.activity_assessments, container, false);
		try{
			super.onCreate(savedInstanceState);
			initControls();
			//reloadData();	
			fillUpAssessments();	
		} catch(Exception e){
			e.printStackTrace();
			//criticalClose();
		}	
		return rootView;
	}
	
	@Override
	public void onResume() {
		reloadData();
		adapter.notifyDataSetChanged();
		super.onResume();
	}
	
	private void initControls() {
		lvAssessments = (ListView) rootView.findViewById(R.id.lvAssessments);
	}
	
	public void reloadData() {
		questionSetting = HelperFactory.getHelper().getQuestionSettingDAO().loadAll((int)Option.Instance().getEmploymentID());
		reloadCategories();
	}
	
	public void reloadCategories() {
		if (questionSetting == null)
			return;
		allCategoriesCount = HelperFactory.getHelper().getCategoryDAO().load().size();
		categories = HelperFactory.getHelper().getCategoryDAO().loadByQuestionSettings(questionSetting);		
		answeredCategories = HelperFactory.getHelper().getAnsweredCategoryDAO().LoadByEmploymentID(activity.tasksFragment.employment.getId());
		employmentCategories.clear();
		relatedQuestionSettings = HelperFactory.getHelper().getRelatedQuestionSettingDAO().load();
		relatedQuestions = HelperFactory.getHelper().getQuestionDAO().loadByRelatedQuestionSettings(relatedQuestionSettings);
		relatedAnswers = HelperFactory.getHelper().getAnswerDAO().loadByRelatedQuestionSettings(relatedQuestionSettings);	
		
		
		List<Integer> ids = new ArrayList<Integer>();
		for (Answer relatedAnswer : relatedAnswers) {
			for (RelatedQuestionSetting relatedQuestionSetting : relatedQuestionSettings) {
				if (relatedAnswer.getQuestionID() != relatedQuestionSetting.getId())
					continue;
				for (RelatedQuestionSetting.RelatedObject relatedObject : relatedQuestionSetting.getRelatedObjects()) {
					if (relatedObject.ownerAnswer != relatedAnswer.getAnswerID())
						continue;
					if (relatedObject.answer != -1)
						continue;
					ids.add(relatedObject.id);
					for(int i = 0; i < answeredCategories.size(); i++)
						if (answeredCategories.get(i).getCategoryID() == relatedObject.id) {
							answeredCategories.remove(i);
							break;
						}
				}
			}
		}
		for(Category category : categories) {
			if (ids.contains(category.getId()))
				continue;
			employmentCategories.add(new EmploymentCategory(category.getName(), category.getId(), category.getCategoryType(), isAnswered(category)));
		}
		Collections.sort(employmentCategories, new EmploymentCategoryComparer());
		
	}
	
	private boolean isAnswered(Category category) {
		for(AnsweredCategory answeredCategory : answeredCategories)
			if (answeredCategory.getCategoryID() == category.getId())
				return true;
		if (category.getCategoryType() == type.normal) {
			List<Question> questions = HelperFactory.getHelper().getQuestionDAO().loadActualsByCategory(category.getId());
			if (questions.size() > 0)
				return false;
			if (HelperFactory.getHelper().getAnsweredCategoryDAO().loadByCategoryID(category.getId()) != null)
				return true;
			AnsweredCategory newAnsweredCategory = new AnsweredCategory(category.getId(), Option.Instance().getEmploymentID());
			HelperFactory.getHelper().getAnsweredCategoryDAO().save(newAnsweredCategory);
			answeredCategories.add(newAnsweredCategory);
			return true;
		}
		return false;
	}
	
	private void fillUpAssessments() {
		adapter = new EmploymentCategoryAdapter(activity, 
				R.layout.row_employment_category_template, employmentCategories);
		lvAssessments.setAdapter(adapter);
		lvAssessments.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				if (activity.tasksFragment.getStartTask().getRealDate().getTime() == DateUtils.EmptyDate.getTime())
				{
					if (activity.tasksFragment.startEmploymentDialog.getFragmentManager() == null)
						activity.tasksFragment.startEmploymentDialog.show(getFragmentManager(), "");
					return;
				}
				if (relatedQuestions.size() > 0) {
					relatedQuestionsDialog = new BaseDialog(getString(R.string.attention), getString(R.string.dialog_related_questions));
					relatedQuestionsDialog.show(getFragmentManager(), "relatedQuestionsDialog");
					return;
				}
				EmploymentCategory employmentCategory = (EmploymentCategory) lvAssessments.getItemAtPosition(position);
				switch(employmentCategory.type)
				{
					case normal:
						startQuestionActivity(employmentCategory.categoryID);
						break;
					case braden:
						startBradenSkalaActivity();
						break;
					case norton:
						startNortonSkalaActivity();
						break;
					case schmerzermittlung:
						startPainAnalyseSkalaActivity();
						break;
					case sturzrisiko:
						startFallenFactorSkalaActivity();
						break;
				}
				
			}
		});
	}
	
	public void showExtraAssessmentsDialog() {
		List<Category> ExtraCategories = HelperFactory.getHelper().getCategoryDAO().loadExtraCategoriesByQuestionSettings(questionSetting);
		ExtraCategoriesDialog extraCategoriesDialog = new ExtraCategoriesDialog(ExtraCategories, getString(R.string.extra_assessments));
		extraCategoriesDialog.show(getFragmentManager(), "extraAssessmentsDialog");
	}
	
	public void saveExtraAssessments(DialogFragment dialog) {
		questionSetting.setExtraCategoriesID(questionSetting.getExtraCategoryIDsString() + (questionSetting.getExtraCategoryIDsString().equals("") ? "" : ",") +((ExtraCategoriesDialog)dialog).getSelectedCategoriesID());
		ExtraCategory extraCategory = HelperFactory.getHelper().getExtraCategoryDAO().load((int)Option.Instance().getEmploymentID());
		if (extraCategory == null)
			extraCategory = new ExtraCategory((int)Option.Instance().getEmploymentID()); 
		extraCategory.setExtraCategoryIDsString(questionSetting.getExtraCategoryIDsString());
		HelperFactory.getHelper().getExtraCategoryDAO().save(extraCategory);
		reloadCategories();
		adapter.notifyDataSetChanged();
	}
	
	private void startQuestionActivity(int categoryID) {
		Intent questionsActivity = new Intent(activity.getApplicationContext(), QuestionsActivity.class);
		questionsActivity.putExtra("categoryID", categoryID);
		startActivity(questionsActivity);
	}
	
	private void startBradenSkalaActivity() {
		Intent bradenSkalaActivity = new Intent(activity.getApplicationContext(), BradenSkalaActivity.class);
		startActivity(bradenSkalaActivity);
	}
	
	private void startPainAnalyseSkalaActivity() {
		Intent painAnalyseSkalaActivity = new Intent(activity.getApplicationContext(), PainAnalyseSkalaActivity.class);
		startActivity(painAnalyseSkalaActivity);
	}
	
	private void startFallenFactorSkalaActivity() {
		Intent fallenFactorSkalaActivity = new Intent(activity.getApplicationContext(), FallenFactorSkalaActivity.class);
		startActivity(fallenFactorSkalaActivity);
	}
	
	private void startNortonSkalaActivity() {
		Intent nortonSkalaActivity = new Intent(activity.getApplicationContext(), NortonSkalaActivity.class);
		startActivity(nortonSkalaActivity);
	}
	
	public class EmploymentCategory {
		public String name;
		public int categoryID;
		public boolean isAnswered;
		public Category.type type;
		
		public EmploymentCategory(String name, int categoryID, Category.type type, boolean isAnswered) {
			this.name = name;
			this.categoryID = categoryID;
			this.type = type;
			this.isAnswered = isAnswered;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public class EmploymentCategoryComparer implements Comparator<EmploymentCategory> {

		@Override
		public int compare(EmploymentCategory lhs, EmploymentCategory rhs) {
			
			Collator deCollator = Collator.getInstance(Locale.GERMANY);
			// TODO Auto-generated method stub
			if (lhs.isAnswered == rhs.isAnswered)
				return deCollator.compare(lhs.name, rhs.name);
			return lhs.isAnswered ? 1 : -1;
		}
		
	}

}

