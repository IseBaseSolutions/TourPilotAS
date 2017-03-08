package isebase.cognito.tourpilot.Data.RelatedQuestionSetting;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot.Utils.StringParser;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "RelatedQuestionSettings")
public class RelatedQuestionSetting extends BaseObject {

	public static final String YES_STR_FIELD = "yes_str";
	public static final String NO_STR_FIELD = "no_str";

	@DatabaseField(dataType = DataType.STRING, columnName = YES_STR_FIELD)
	String yesStr;

	public String getYesStr() {
		return yesStr;
	}

	public void setYesStr(String yesStr) {
		this.yesStr = yesStr;
	}

	@DatabaseField(dataType = DataType.STRING, columnName = NO_STR_FIELD)
	String noStr;

	public String getNoStr() {
		return noStr;
	}

	public void setNoStr(String noStr) {
		this.noStr = noStr;
	}

	private List<RelatedObject> relatedObjects = new ArrayList<RelatedObject>();

	public RelatedQuestionSetting() {
		clear();
	}

	public RelatedQuestionSetting(String initString) {
		clear();
		StringParser parsingString = new StringParser(initString);
		parsingString.next(";");
		setId(Integer.parseInt(parsingString.next(";")));
		setYesStr(parsingString.next("@"));
		setNoStr(parsingString.next("~"));
	}

	private void initRelatedObjects() {
		initRelatedObjectsStr(getYesStr());
		initRelatedObjectsStr(getNoStr());
	}
	
	private void initRelatedObjectsStr(String string) {
		if (string.equals(""))
			return;
		String[] stringArr = string.split(";");
		for (int i = 1; i < 3; i++) {
			if (stringArr.length > i && !stringArr[i].equals(""))
				for (String item : stringArr[i].split(",")) {
					if (stringArr[i].equals(""))
						continue;
					switch (i) {
					case 1:
						relatedObjects.add(new RelatedObject(Integer.parseInt(item), 
								-1, Integer.parseInt(stringArr[0])));
						break;
					case 2:
						relatedObjects
								.add(new RelatedObject(Integer.parseInt(item.split("#")[0]), 
										Integer.parseInt(item.split("#")[1]),
										Integer.parseInt(stringArr[0])));
						break;
					}
				}
		}
	}
	
	public List<RelatedObject> getRelatedObjects() {
		if (relatedObjects.size() == 0)
			initRelatedObjects();
		return relatedObjects;
	}

	public class RelatedObject {

		public int id;
		public int answer;
		public int ownerAnswer;
		
		public RelatedObject() {
			
		}

		public RelatedObject(int id, int answer, int ownerAnswer) {
			this.id = id;
			this.answer = answer;
			this.ownerAnswer = ownerAnswer;
		}
		
//		public Answer getAnswer(Patient patient) {
//			Question question = HelperFactory.getHelper().getQuestionDAO().load(id);
//			return question == null ? null : new Answer(patient, question, answer, -1, Category.type.normal.ordinal());
//		}

	}

}
