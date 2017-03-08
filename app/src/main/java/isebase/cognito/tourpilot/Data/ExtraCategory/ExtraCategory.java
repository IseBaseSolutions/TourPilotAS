package isebase.cognito.tourpilot.Data.ExtraCategory;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ExtraCategories")
public class ExtraCategory extends BaseObject {

	public static final String EXTRA_CATEGORIES_ID = "extra_category_ids_string";	
	
	@DatabaseField(dataType = DataType.STRING, columnName = EXTRA_CATEGORIES_ID)
	private String extraCategoryIDsString;
	
	public String getExtraCategoryIDsString() {
		return extraCategoryIDsString;
	}

	public void setExtraCategoryIDsString(String extraCategoryIDsString) {
		this.extraCategoryIDsString = extraCategoryIDsString;
	}
	
	public ExtraCategory() {
		clear();
	}
	
	public ExtraCategory(int id) {
		clear();
		setId(id);
	}
	
	@Override
	protected void clear() {
		super.clear();
		setExtraCategoryIDsString("");
	}

}
