package isebase.cognito.tourpilot.Data.WayPoint;

import java.util.Date;

import isebase.cognito.tourpilot.Connection.SentObjectVerification;
import isebase.cognito.tourpilot.Connection.ServerCommandParser;
import isebase.cognito.tourpilot.Data.BaseObject.BaseObject;
import android.location.Location;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "WayPoints")
public class WayPoint extends BaseObject {

	public static final String LATITUDE_FIELD = "latitude"; 
	public static final String LONGITUDE_FIELD = "longitude"; 
	public static final String NB_SATELLITES_FIELD = "nbSatellites"; 
	public static final String ALTITUDE_FIELD = "altitude"; 
	public static final String ACCURACY_FIELD = "accuracy";
	public static final String TIME_FIELD = "time"; 
	public static final String WORKER_ID_FIELD = "worker_id"; 
	public static final String PILOT_TOUR_ID_FIELD = "pilot_tour_id"; 
	
	@DatabaseField(dataType = DataType.DOUBLE, columnName = LATITUDE_FIELD)
	private double latitude;
	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	@DatabaseField(dataType = DataType.DOUBLE, columnName = LONGITUDE_FIELD)
	private double longitude;
	
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	@DatabaseField(dataType = DataType.DOUBLE, columnName = ALTITUDE_FIELD)
	private double altitude;
	
	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	
	@DatabaseField(dataType = DataType.FLOAT, columnName = ACCURACY_FIELD)
	private float accuracy;
	
	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = NB_SATELLITES_FIELD)
	private int nbSatellites;
	
	public int getNbSatellites() {
		return nbSatellites;
	}

	public void setNbSatellites(int nbSatellites) {
		this.nbSatellites = nbSatellites;
	}	
	
	@DatabaseField(dataType = DataType.LONG, columnName = TIME_FIELD)
	private long time;
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	@DatabaseField(dataType = DataType.INTEGER, columnName = WORKER_ID_FIELD)
	private int workerID;
	
	public int getWorkerID() {
		return workerID;
	}

	public void setWorkerID(int workerID) {
		this.workerID = workerID;
	}
	
	@DatabaseField(dataType = DataType.LONG, columnName = PILOT_TOUR_ID_FIELD)
	private long pilotTourID;

	public long getPilotTourID() {
		return pilotTourID;
	}

	public void setPilotTourID(long pilotTourID) {
		this.pilotTourID = pilotTourID;
	}
	
	public WayPoint() {
		clear();
	}
	
	public WayPoint(int workerID, long pilotTourID, Location location, int nbSatellites) {
		try {
			setWorkerID(workerID);
			setPilotTourID(pilotTourID);
//			setLatitude((location.getProvider() == LocationManager.GPS_PROVIDER && location.getAccuracy() < 25) 
//					? location.getLatitude() 
//							: 1001);
			setLatitude(location.getLatitude());
	    	setLongitude(location.getLongitude());
	    	setNbSatellites(nbSatellites);
	        if (location.hasAltitude()) {
	        	setAltitude(location.getAltitude());
	        }
	        
	        if (location.hasAccuracy()) {
	        	setAccuracy(location.getAccuracy());
	        }
	        setTime(location.getTime());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public WayPoint(int workerID, long pilotTourID, int x) {
		try {
			setWorkerID(workerID);
			setPilotTourID(pilotTourID);
	    	setLatitude(x);
	    	setLongitude(x);
	    	setNbSatellites(nbSatellites);
	        setTime(new Date().getTime());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
    public String getDone()
    {
		String strValue = new String(ServerCommandParser.WAY_POINT + ";");
		strValue += getWorkerID() + ";";
		strValue += getPilotTourID() + ";";
		strValue += getTime() + ";";
		strValue += getLatitude() + ";";
		strValue += getLongitude() + ";";
		SentObjectVerification.Instance().sentWayPoints.add(this);
		return strValue;
    }

	
}
