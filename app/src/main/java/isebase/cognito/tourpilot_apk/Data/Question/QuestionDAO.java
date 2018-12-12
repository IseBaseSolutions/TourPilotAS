package isebase.cognito.tourpilot_apk.Data.Question;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObjectDAO;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.Data.QuestionSetting.QuestionSetting;
import isebase.cognito.tourpilot_apk.Data.RelatedQuestionSetting.RelatedQuestionSetting;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.support.ConnectionSource;

public class QuestionDAO extends BaseObjectDAO<Question> {
	
	public QuestionDAO(ConnectionSource connectionSource,
			Class<Question> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
	
	public List<Question> loadActualsByCategory(int categoryID) {
		QuestionSetting questionSettings = HelperFactory.getHelper().getQuestionSettingDAO().loadAll((int)Option.Instance().getEmploymentID());
		if (questionSettings == null)
			return new ArrayList<Question>();
		String strSQL = String.format("SELECT %7$s " +
				" FROM %1$s AS t1 " + 
				" INNER JOIN %2$s as t2 ON t1._id = t2.category_id " +
				" INNER JOIN %3$s t3 ON t2.question_id = t3._id " +
				" INNER JOIN %8$s t4 ON t4._id = %4$d " +
				" LEFT JOIN answers t5 ON t3._id = t5.question_id AND t5.employment_id = %4$d" +  
				" WHERE t1._id = %5$s AND t5._id IS NULL %6$s GROUP BY t3._id "
				, HelperFactory.getHelper().getCategoryDAO().getTableInfo().getTableName()
				, HelperFactory.getHelper().getLinkDAO().getTableInfo().getTableName()
				, HelperFactory.getHelper().getQuestionDAO().getTableInfo().getTableName()
				, Option.Instance().getEmploymentID()
				, categoryID
				, isExtraCategory(categoryID, questionSettings) ? "" : getCassualCategoryStr(questionSettings)
				, getSelectString()
				, HelperFactory.getHelper().getQuestionSettingDAO().getTableInfo().getTableName());
		GenericRawResults<String[]> rawResults = null;
		List<Question> questions = new ArrayList<Question>();
		try {
			rawResults = queryRaw(strSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		for (String[] resultArray : rawResults) 
			questions.add(new Question(resultArray, rawResults.getColumnNames()));	
		return questions;
	}
	
	private String getCassualCategoryStr(QuestionSetting questionSettings) {
		return String.format("AND t3._id in (%1$s)", questionSettings.getQuestionIDsString());
	}
	
	private boolean isExtraCategory(int categoryID, QuestionSetting questionSettings) {
		if (questionSettings.getExtraCategoryIDsString().equals(""))
			return false;		
		String[] extracategoriesIDArr = questionSettings.getExtraCategoryIDsString().split(",");
		for (int i = 0; i < extracategoriesIDArr.length; i++)
			if (Integer.parseInt(extracategoriesIDArr[i]) == categoryID)
				return true;
		return false;
	}
	
	private String getSelectString() {
		String strSelect = "";
		strSelect += "t3." + Question.OWNER_IDS_FIELD + ", ";
		strSelect += "t3." + Question.KEY_ANSWER_FIELD + ", ";
		strSelect += "t3." + Question.NAME_FIELD + ", ";
		strSelect += "t3." + Question.ID_FIELD + ", ";
		strSelect += "t3." + Question.CHECK_SUM_FIELD + ", ";
		strSelect += "t3." + Question.IS_SERVER_TIME_FIELD + ", ";
		strSelect += "t3." + Question.WAS_SENT_FIELD;
		return strSelect;
	}
	
	public List<Question> loadByRelatedQuestionSettings(List<RelatedQuestionSetting> list) {
		if (list.size() == 0)
			return new ArrayList<Question>();
		String idsStr = "";
		QuestionSetting questionSetting = HelperFactory.getHelper().getQuestionSettingDAO().loadAll((int)Option.Instance().getEmploymentID());
		List<Integer> ids = getAssignedQuestionIds(questionSetting);
		for (RelatedQuestionSetting item : list) {
			if (ids.contains(item.getId()))
				idsStr += (idsStr.equals("") ? "" : ",") + item.getId();
		}
		
		String strSQL = String.format(" SELECT t1.* FROM Questions as t1 " +
			" LEFT JOIN Answers AS t2 ON t1._id = t2.question_id AND t2.employment_id = %d " +
			" WHERE t1._id IN (%s) AND t2._id IS NULL " +
			" ORDER BY _id", Option.Instance().getEmploymentID(), idsStr);
		GenericRawResults<String[]> rawResults = null;
		List<Question> questions = new ArrayList<Question>();
		try {
			rawResults = queryRaw(strSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		for (String[] resultArray : rawResults) 
			questions.add(new Question(resultArray, rawResults.getColumnNames()));	
		return questions;
	}
	
	private List<Integer> getAssignedQuestionIds(QuestionSetting questionSettings) {
		if (questionSettings.getQuestionIDsString().equals(""))
			return new ArrayList<Integer>();
		List<Integer> ids = new ArrayList<Integer>();
		for (String strId : questionSettings.getQuestionIDsString().split(","))
			if (!strId.equals(""))
				ids.add(Integer.parseInt(strId));
		return ids;
	}
	
}
