package isebase.cognito.tourpilot_apk.Data.AnsweredCategory;

import isebase.cognito.tourpilot_apk.Data.BaseObject.BaseObject;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "AnsweredCategories")
public class AnsweredCategory extends BaseObject {
	
	public static final String CATEGORY_ID_FIELD = "category_id";
	public static final String EMPLOYMENT_ID_FIELD = "employment_id";
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = CATEGORY_ID_FIELD)
	int categoryID;
	
	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}
	
	@DatabaseField(dataType = DataType.LONG, columnName = EMPLOYMENT_ID_FIELD)
	long employmentID;
	
	public long getEmploymentID() {
		return employmentID;
	}

	public void setEmploymentID(long employmentID) {
		this.employmentID = employmentID;
	}	
	
	boolean isAnswered;
	
	public AnsweredCategory() {
		clear();
	}
	
	public AnsweredCategory(int categoryID, long employmentID) {
		clear();
		setCategoryID(categoryID);
		setEmploymentID(employmentID);
	}

	@Override
	protected void clear() {
		super.clear();
		setCategoryID(BaseObject.EMPTY_ID);
		setEmploymentID(BaseObject.EMPTY_ID);
		setName(String.valueOf(employmentID));
	}

}
