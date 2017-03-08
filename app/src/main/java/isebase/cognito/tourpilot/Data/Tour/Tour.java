package isebase.cognito.tourpilot.Data.Tour;

import isebase.cognito.tourpilot.Connection.ServerCommandParser;
import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot.Utils.NCryptor;
import isebase.cognito.tourpilot.Utils.StringParser;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Tours")
public class Tour extends BaseObject {

	public static final String IS_COMMON_TOUR_FIELD = "is_common_tour";

	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_COMMON_TOUR_FIELD)
	private boolean isCommonTour;

	public boolean getIsCommonTour() {
		return isCommonTour;
	}

	public void setIsCommonTour(boolean isCommonTour) {
		this.isCommonTour = isCommonTour;
	}

	public Tour() {
		clear();
	}

	public Tour(String strInitString) {
		clear();
		StringParser InitString = new StringParser(strInitString);
		InitString.next(";");
		setId(Integer.parseInt(InitString.next(";")));
		setName(InitString.next(";"));
		setIsCommonTour(Integer.parseInt(InitString.next("~")) == 1 
				? true : false);
		setCheckSum(Long.parseLong(InitString.next()));
	}

	@Override
	public String toString() {
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("EE MM.dd");
		String dayOfTheWeek = simpleDateformat.format(new Date());
		return String.format("%s %s", getName(), dayOfTheWeek);
	}

	@Override
	public String forServer() {
		NCryptor ncryptor = new NCryptor();
		String strValue = new String(ServerCommandParser.TOUR + ";");
		strValue += getId() + ";";
		strValue += ncryptor.LToNcode(getCheckSum());
		return strValue;
	}

	@Override
	protected void clear() {
		super.clear();
		setIsCommonTour(false);
	}
	
}
