package isebase.cognito.tourpilot.Data.Option;

import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import isebase.cognito.tourpilot.Data.Worker.Worker;
import isebase.cognito.tourpilot.DataBase.HelperFactory;
import isebase.cognito.tourpilot.StaticResources.StaticResources;
import android.content.pm.PackageInfo;
import android.telephony.TelephonyManager;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Options")
public class Option {

	public static final String ID_FIELD = "_id";
	public static final String WORKER_ID_FIELD = "worker_id";
	public static final String PILOT_TOUR_ID_FIELD = "pilot_tour_id";
	public static final String EMPLOYMENT_ID_FIELD = "employment_id";
	public static final String PREVIOUS_WORKER_ID_FIELD = "prev_worker_id";
	public static final String WORK_ID_FIELD = "work_id";
	public static final String SERVER_IP_FIELD = "server_ip";
	public static final String SERVER_PORT_FIELD = "server_port";
	public static final String PHONE_NUMBER_FIELD = "phone_number";
	public static final String IS_AUTO_FIELD = "is_auto";
	public static final String IS_WORKER_ACTIVITY_FIELD = "is_worker_activity";
	public static final String IS_TOUR_ACTIVITY_FIELD = "is_tour_activity";
	public static final String PIN_FIELD = "pin";
	public static final String SERVER_TIME_DIFFERENCE_FIELD = "server_time_difference";
	public static final String IS_LOCK_OPTIONS_FIELD = "lock_options";
	public static final String IS_WORKER_PHONES_FIELD = "is_worker_phones";
	public static final String IS_SKIPPING_PFLEGE_OK = "is_skipping_pflege_ok";
	
	public static boolean testMode = false;
	
	public long gpsStartTime;
	private Worker worker;
	private TelephonyManager phoneManager = StaticResources.phoneManager;
	private static Option instance;
	
	public String testPin = "159753";

	@DatabaseField(generatedId = true, allowGeneratedIdInsert = true, columnName = ID_FIELD)
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	

	@DatabaseField(dataType = DataType.INTEGER, columnName = PREVIOUS_WORKER_ID_FIELD)
	private int prevWorkerID;
	
	public void setPrevWorkerID(int id){
		this.prevWorkerID = id;
	}

	public int getPrevWorkerID(){
		return prevWorkerID;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = WORKER_ID_FIELD)
	private int workerID;
	
	public int getWorkerID() {
		return workerID;
	}

	public void setWorkerID(int workerID) {
		this.workerID = workerID;
	}
	
	@DatabaseField(dataType = DataType.LONG, columnName = EMPLOYMENT_ID_FIELD)
	private long employmentID;
	
	public long getEmploymentID() {
		return employmentID;
	}

	public void setEmploymentID(long employmentID) {
		this.employmentID = employmentID;
	}
	
	@DatabaseField(dataType = DataType.LONG, columnName = PILOT_TOUR_ID_FIELD)
	private long pilotTourID;
	
	public void setPilotTourID(long id){
		this.pilotTourID = id;
	}

	public long getPilotTourID(){
		return pilotTourID;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = WORK_ID_FIELD)
	private int workID;
	
	public int getWorkID() {
		return workID;
	}

	public void setWorkID(int workID) {
		this.workID = workID;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = SERVER_IP_FIELD)
	private String serverIP;
	
	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = SERVER_PORT_FIELD)
	private int serverPort;
	
	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}	
	

	@DatabaseField(dataType = DataType.LONG, columnName = SERVER_TIME_DIFFERENCE_FIELD)
	private long serverTimeDifference;
	
	public long getServerTimeDifference() {
		return serverTimeDifference;
	}
	
	public void setServerTimeDifference(long serverTimeDifference) {
		this.serverTimeDifference = serverTimeDifference;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = PIN_FIELD)
	private String pin;
	
	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}
	
	private String palmVersion;
	private String versionLink;
	
	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_AUTO_FIELD)
	private boolean isAuto;
	
	public void setIsAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}

	public boolean getIsAuto() {
		return isAuto;
	}
	
	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_SKIPPING_PFLEGE_OK)
	private boolean isSkippingPflegeOK;
	
	public void setIsSkippingPflegeOK(boolean isSkippingPflegeOK) {
		this.isSkippingPflegeOK = isSkippingPflegeOK;
	}

	public boolean getIsSkippingPflegeOK() {
		return isSkippingPflegeOK;
	}
	
//	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_WORKER_ACTIVITY_FIELD)
//	private boolean isWorkerActivity;
//	
//	public boolean isWorkerActivity() {
//		return isWorkerActivity;
//	}
//
//	public void setWorkerActivity(boolean isWorkerActivity) {
//		this.isWorkerActivity = isWorkerActivity;
//	}
	
//	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_TOUR_ACTIVITY_FIELD)
//	private boolean isTourActivity;
//	
//	public boolean isTourActivity() {
//		return isTourActivity;
//	}
//
//	public void setTourActivity(boolean isTourActivity) {
//		this.isTourActivity = isTourActivity;
//	}
	
	private boolean isTimeSynchronised;
	
	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_LOCK_OPTIONS_FIELD)
	private boolean isLockOptions;
	
	public boolean isLockOptions() {
		return isLockOptions;
	}

	public void setLockOptions(boolean isLockOptions) {
		this.isLockOptions = isLockOptions;
	}
	
	@DatabaseField(dataType = DataType.BOOLEAN, columnName = IS_WORKER_PHONES_FIELD)
	private boolean isWorkerPhones;

	public boolean isWorkerPhones() {
		return isWorkerPhones;
	}

	public void setWorkerPhones(boolean isWorkerPhones) {
		this.isWorkerPhones = isWorkerPhones;
	}

	public String getPalmVersion() {
		return palmVersion;
	}

	public void setPalmVersion(String palmVersion) {
		this.palmVersion = palmVersion;
	}

	public String getVersionLink() {
		return versionLink;
	}

	public void setVersionLink(String versionLink) {
		this.versionLink = versionLink;
	}
	
	public boolean isTimeSynchronised() {
		return isTimeSynchronised;
	}

	public void setTimeSynchronised(boolean isTimeSynchronised) {
		this.isTimeSynchronised = isTimeSynchronised;
	}

	public Worker getWorker() {
		if (worker != null && worker.getId() == getWorkerID())
			return worker;
		worker = HelperFactory.getHelper().getWorkerDAO().load(workerID);
		return worker;
	}

	protected void clear() {
		setId(0);//NewBaseObject.EMPTY_ID);	
		setServerPort(4448);		
		setServerIP("");
		setPin("");
		setPhoneNumber("");
		clearSelected();
	}

	public void clearSelected() {
		prevWorkerID = BaseObject.EMPTY_ID;
		workerID = BaseObject.EMPTY_ID;
		workID = BaseObject.EMPTY_ID;
		pilotTourID = BaseObject.EMPTY_ID;
		employmentID = BaseObject.EMPTY_ID;
	}

	public String getVersion() {
		try {
			PackageInfo packageInfo = StaticResources.getBaseContext().getPackageManager().
			getPackageInfo(StaticResources.getBaseContext().getPackageName(), 0);
			return packageInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public int getVersionCode() {
		try {
			PackageInfo packageInfo = StaticResources.getBaseContext().getPackageManager().
			getPackageInfo(StaticResources.getBaseContext().getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	@DatabaseField(dataType = DataType.STRING, columnName = PHONE_NUMBER_FIELD)
	private String phoneNumber;

	public String getPhoneNumber() {
		return phoneNumber != null && phoneNumber != ""				
				? phoneNumber
				: phoneManager.getLine1Number() == null 
					? "" 
					: phoneManager.getLine1Number().toString();
	}
	
	public void setPhoneNumber(String number){
		this.phoneNumber = number;		
	}

	public String getDeviceID() {
		return phoneManager.getDeviceId() == null ? "" : phoneManager
				.getDeviceId().toString();
	}

	public static Option Instance() {
		if (instance == null) {
			instance = HelperFactory.getHelper().getOptionDAO().loadOption();
		}
		return instance;
	}

	public Option() {
		clear();
	}
	
	public Option(String[] arr) {
		clear();
		int counter = 0;
		setId(Integer.parseInt(arr[counter++]));
		setLockOptions(Integer.parseInt(arr[counter++]) == 1);
		setWorkerID(Integer.parseInt(arr[counter++]));
		setWorkID(Integer.parseInt(arr[counter++]));
		setPrevWorkerID(Integer.parseInt(arr[counter++]));
		setPilotTourID(Integer.parseInt(arr[counter++]));
		setEmploymentID(Integer.parseInt(arr[counter++]));
		setServerIP(arr[counter++]);
		setServerPort(Integer.parseInt(arr[counter++]));
		setIsAuto(Integer.parseInt(arr[counter++]) == 1);
//		setWorkerActivity(Integer.parseInt(arr[counter++]) == 1);
		setPin(arr[counter++]);
		setServerTimeDifference(Integer.parseInt(arr[counter++]));
		setWorkerPhones(Integer.parseInt(arr[counter++]) == 1);
//		setTourActivity(Integer.parseInt(arr[counter++]) == 1);
	}
	
	public void resetOptions() {
		instance = null;
	}

	public void save() {
		HelperFactory.getHelper().getOptionDAO().save(instance);
	}
	
	public boolean isTest() {
		return pin.equals(testPin);
	}
}
