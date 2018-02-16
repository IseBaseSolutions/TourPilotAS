package isebase.cognito.tourpilot.Data.TourOncomingInfo;

import isebase.cognito.tourpilot.Connection.ServerCommandParser;
import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot.Data.Worker.Worker;
import isebase.cognito.tourpilot.DataBase.HelperFactory;
import isebase.cognito.tourpilot.Utils.DateUtils;
import isebase.cognito.tourpilot.Utils.NCryptor;
import isebase.cognito.tourpilot.Utils.StringParser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = TourOncomingInfo.TABLE_NAME)
public class TourOncomingInfo extends BaseObject {

	public static final String OWNER_ID_FIELD = "owner_id";
	public static final String INFO_TYPE = "info_type";
	public static final String HEADER_INFO = "header_info";
	public static final String WORKERS_INFO = "workers_info";
	public static final String TABLE_NAME = "TourOncomingInfo";

	@DatabaseField(dataType = DataType.INTEGER, columnName = OWNER_ID_FIELD)
	private int ownerID;

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	@DatabaseField(dataType = DataType.INTEGER, columnName = INFO_TYPE)
	private int infoType;

	public int getInfoType() {
		return infoType;
	}

	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}

	@DatabaseField(dataType = DataType.STRING, columnName = HEADER_INFO)
	private String headerInfo;

	public String getHeaderInfo() {
		return headerInfo;
	}

	public void setHeaderInfo(String headerInfo) {
		this.headerInfo = headerInfo;
	}

	@DatabaseField(dataType = DataType.STRING, columnName = WORKERS_INFO)
	private String workersInfo;

	public String getWorkersInfo() {
		return workersInfo;
	}

	public void setWorkersInfo(String workersInfo) {
		this.workersInfo = workersInfo;
	}

	List<TourOncomingInfoDayPart> items = new ArrayList<TourOncomingInfoDayPart>();

	public TourOncomingInfo() {
		clear();
	}

	public TourOncomingInfo(String initString) {
		clear();
		StringParser InitString = new StringParser(initString);
		InitString.next(";");
		setId(Integer.parseInt(InitString.next(";")));
		setOwnerID(Integer.parseInt(InitString.next(";")));
		setInfoType(Integer.parseInt(InitString.next(";")));
		setHeaderInfo(InitString.next(";"));
		setWorkersInfo(InitString.next("~"));
		setCheckSum(Integer.parseInt(InitString.next()));
	}

	public List<TourOncomingInfoDayPart> getDayPartsInfo() {
		if (items.size() > 0)
			return items;
		String[] infoDayPart = getWorkersInfo().split("%");
		for (int i = 0; i < infoDayPart.length; i++) {
			items.add(new TourOncomingInfoDayPart());
			String[] arr = infoDayPart[i].split("#");
			items.get(i).dayPart = arr[0];
			items.get(i).workers.addAll(getInfoWorkers(arr[1]));
		}
		return items;
	}

	public List<TourOncomingInfoWoker> getInfoWorkers(String str) {
		List<TourOncomingInfoWoker> list = new ArrayList<TourOncomingInfoWoker>();
		String[] dateWorkers = str.split("\\$");
		for (int i = 0; i < dateWorkers.length; i++) {
			list.add(new TourOncomingInfoWoker());
			String[] arr = dateWorkers[i].split("@");
			try {
				list.get(i).date = DateUtils.DateFormat.parse(arr[0]);
				list.get(i).worker = HelperFactory.getHelper().getWorkerDAO()
						.load(Integer.parseInt(arr[1]));

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public String getHeaderText() {
		switch (infoType) {
		case 0:
			return String.format("Die Tour %s wird an den kommenden Tagen wie folgt versorgt:",
							headerInfo);
		case 1:
			return getCarHeader();
		case 2:
			return String.format("%s wird an den kommenden Tagen von folgenden Mitarbeitern versorgt:", headerInfo);
		}
		return "";
	}
	
	public String getCarHeader() {
//		if (getName().equals("InService"))
//			return "Für weitere Information rufen Sie das Büro an.";
			return String.format("Der %s wird wie folgt weiter gegeben:",
					headerInfo);
	}
	
    public String forServer()
    {
    	NCryptor ncryptor = new NCryptor();
    	String strValue = new String(ServerCommandParser.TOUR_ONCOMING_INFO + ";");
        strValue += ncryptor.LToNcode(getId()) + ";";
        strValue += ncryptor.LToNcode(getCheckSum());
        return strValue;
    }

	@Override
	protected void clear() {
		super.clear();
		ownerID = EMPTY_ID;
		infoType = EMPTY_ID;
		headerInfo = "";
		workersInfo = "";
	}

	public class TourOncomingInfoDayPart {
		public String dayPart;
		public List<TourOncomingInfoWoker> workers = new ArrayList<TourOncomingInfoWoker>();
	}

	public class TourOncomingInfoWoker {
		public Date date;
		public Worker worker;
	}

}
