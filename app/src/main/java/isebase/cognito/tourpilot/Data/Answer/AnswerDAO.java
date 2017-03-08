package isebase.cognito.tourpilot.Data.Answer;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObjectDAO;
import isebase.cognito.tourpilot.Data.Category.Category;
import isebase.cognito.tourpilot.Data.Employment.Employment;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.Data.Question.Question;
import isebase.cognito.tourpilot.Data.RelatedQuestionSetting.RelatedQuestionSetting;
import isebase.cognito.tourpilot.DataBase.HelperFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class AnswerDAO extends BaseObjectDAO<Answer> {

	public AnswerDAO(ConnectionSource connectionSource, Class<Answer> dataClass)
			throws SQLException {
		super(connectionSource, dataClass);
	}

	protected static String strEmpls;

	public String getDone() {
		strEmpls = "";
		final List<Employment> newEmployments = HelperFactory.getHelper()
				.getEmploymentDAO().LoadDone();
		try {
			callBatchTasks(new Callable<Void>() {
				public Void call() {
					for (Employment newEmployment : newEmployments)
						for (Answer answer : loadByEmploymentID(newEmployment
								.getId()))
							strEmpls += answer.forServer() + "\0";
					return null;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strEmpls;
	}

//	public List<Answer> loadByCategoryID(int categoryID) {
//		QueryBuilder<Answer, Integer> queryBuilder = queryBuilder();
//		try {
//			return queryBuilder
//					.where()
//					.eq(Answer.EMPLOYMENT_ID_FIELD,
//							Option.Instance().getEmploymentID()).and()
//					.eq(Answer.CATEGORY_ID_FIELD, categoryID).and().not()
//					.eq(Answer.QUESTION_ID_FIELD, -1).query();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return new ArrayList<Answer>();
//	}

	public List<Answer> loadByCategoryIDAndType(int categoryID,
			Category.type type) {
		if (type == type.normal) {
			String strSQL = String
					.format("SELECT t1.* FROM Answers AS t1 "
							+ " LEFT JOIN Links AS t2 ON t1.question_id = t2.question_id"
							+ " WHERE t1.type = 0 AND t2._id NOT NULL AND t2.category_id = %d AND t1.employment_id = %d"
							+ " GROUP BY t1._id "
							, categoryID
							, Option.Instance().getEmploymentID());
			GenericRawResults<String[]> rawResults = null;
			List<Answer> answers = new ArrayList<Answer>();
			try {
				rawResults = queryRaw(strSQL);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			for (String[] resultArray : rawResults)
				answers.add(new Answer(resultArray, rawResults.getColumnNames()));
			return answers;
		}
		QueryBuilder<Answer, Integer> queryBuilder = queryBuilder();
		try {
			return queryBuilder
					.where()
					.eq(Answer.EMPLOYMENT_ID_FIELD,
							Option.Instance().getEmploymentID()).and()
					.eq(Answer.CATEGORY_ID_FIELD, categoryID).and().not()
					.eq(Answer.QUESTION_ID_FIELD, -1).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<Answer>();
	}
	
	public List<Answer> loadPainAnaliseAnswers() {
		QueryBuilder<Answer, Integer> queryBuilder = queryBuilder();
		try {
			return queryBuilder
					.where()
					.eq(Answer.EMPLOYMENT_ID_FIELD,
							Option.Instance().getEmploymentID()).and()
					.eq(Answer.CATEGORY_ID_FIELD, 13).and()
					.eq(Answer.QUESTION_ID_FIELD, -1).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<Answer>();
	}

	// public List<Answer> loadByType(Category.type type) {
	// if (type == type.normal) {
	// String strSQL = String.format("SELECT * FROM Answers AS t1 " +
	// " LEFT JOIN Links AS t2 ON t1.question_id = t2.question_id " +
	// " WHERE t2._id NOT NULL AND t1.employment_id = %d" +
	// " GROUP BY t1._id ", Option.Instance().getEmploymentID());
	// GenericRawResults<String[]> rawResults = null;
	// List<Answer> answers = new ArrayList<Answer>();
	// try {
	// rawResults = queryRaw(strSQL);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// for (String[] resultArray : rawResults)
	// answers.add(new Answer(resultArray));
	// return answers;
	// }
	// QueryBuilder<Answer, Integer> queryBuilder = queryBuilder();
	// try {
	// return queryBuilder
	// .where()
	// .eq(Answer.EMPLOYMENT_ID_FIELD,
	// Option.Instance().getEmploymentID()).and()
	// .eq(Answer.TYPE_FIELD, type.ordinal()).and().not()
	// .eq(Answer.QUESTION_ID_FIELD, -1).query();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return new ArrayList<Answer>();
	// }

	public List<Answer> loadByEmploymentID(long employmentID) {
		return load(Answer.EMPLOYMENT_ID_FIELD, employmentID);
	}
	
	public Answer loadByQuestionID(int questionID) {
		List<Answer> answers = new ArrayList<Answer>();
		QueryBuilder<Answer, Integer> queryBuilder = queryBuilder();
		try {
			answers = queryBuilder
					.where()
					.eq(Answer.EMPLOYMENT_ID_FIELD,
							Option.Instance().getEmploymentID()).and()
					.eq(Answer.QUESTION_ID_FIELD, questionID).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (answers.size() > 0)
			return answers.get(0);
		return null;
	}

	public Answer loadByQuestionIDAndType(int questionID, int type) {
		List<Answer> answers = new ArrayList<Answer>();
		QueryBuilder<Answer, Integer> queryBuilder = queryBuilder();
		try {
			answers = queryBuilder
					.where()
					.eq(Answer.EMPLOYMENT_ID_FIELD,
							Option.Instance().getEmploymentID()).and()
					.eq(Answer.QUESTION_ID_FIELD, questionID).and()
					.eq(Answer.TYPE_FIELD, type).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (answers.size() > 0)
			return answers.get(0);
		return null;
	}

	public boolean isSubQuestionAnswered(Question subQuestion) {
		QueryBuilder<Answer, Integer> queryBuilder = queryBuilder();
		try {
			return queryBuilder
					.where()
					.eq(Answer.EMPLOYMENT_ID_FIELD,
							Option.Instance().getEmploymentID()).and()
					.eq(Answer.QUESTION_ID_FIELD, subQuestion.getId()).query()
					.size() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void deleteByEmploymentID(int employmentID) {
		DeleteBuilder<Answer, Integer> deleteBuilder = deleteBuilder();
		try {
			deleteBuilder.where().eq(Answer.EMPLOYMENT_ID_FIELD, employmentID);
			deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Answer loadKontrakturenAnswer() {
		List<Answer> answers = new ArrayList<Answer>();
		QueryBuilder<Answer, Integer> queryBuilder = queryBuilder();
		try {
			answers = queryBuilder
					.where()
					.eq(Answer.EMPLOYMENT_ID_FIELD,
							Option.Instance().getEmploymentID())
					.and()
					.eq(Answer.QUESTION_ID_FIELD, -1)
					.and()
					.eq(Answer.CATEGORY_ID_FIELD,
							HelperFactory
									.getHelper()
									.getCategoryDAO()
									.loadByCategoryName(
											"Kontrakturrisikoerfassung")
									.getId()).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (answers.size() > 0)
			return answers.get(0);
		return null;
	}
	
	public List<Answer> loadByRelatedQuestionSettings(List<RelatedQuestionSetting> list) {
		if (list.size() == 0)
			return new ArrayList<Answer>();
		String idsStr = "";
		for (RelatedQuestionSetting item : list) {
			idsStr += (idsStr.equals("") ? "" : ",") + item.getId();
		}
		
//		String strSQL = String.format(" SELECT t1.* FROM Questions as t1 " +
//			" LEFT JOIN Answers AS t2 ON t1._id = t2.question_id AND t2.employment_id = %d " +
//			" WHERE t1._id IN (%s) AND t2._id IS NULL " +
//			" ORDER BY _id", Option.Instance().getEmploymentID(), idsStr);
		
		String strSQL = String.format(" SELECT * FROM Answers " +
				" WHERE employment_id = %d AND question_id IN (%s)" +
				" ORDER BY _id", Option.Instance().getEmploymentID(), idsStr);
		
		
		GenericRawResults<String[]> rawResults = null;
		List<Answer> answers = new ArrayList<Answer>();
		try {
			rawResults = queryRaw(strSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		for (String[] resultArray : rawResults) 
			answers.add(new Answer(resultArray, rawResults.getColumnNames()));	
		return answers;
	}

}
