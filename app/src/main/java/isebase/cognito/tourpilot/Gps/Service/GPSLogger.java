package isebase.cognito.tourpilot.Gps.Service;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Activity.PatientsActivity;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.Data.WayPoint.WayPoint;
import isebase.cognito.tourpilot.DataBase.HelperFactory;
import isebase.cognito.tourpilot.StaticResources.StaticResources;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class GPSLogger extends Service implements LocationListener {

	// private DataHelper dataHelper;

	/**
	 * Are we currently tracking ?
	 */
	private boolean isTracking = false;

	/**
	 * Is GPS enabled ?
	 */
	private boolean isGpsEnabled = false;

	/**
	 * System notification id.
	 */
	private static final int NOTIFICATION_ID = 1;

	/**
	 * Last known location
	 */
	private Location lastLocation;

	/**
	 * Last number of satellites used in fix.
	 */
	private int lastNbSatellites;

	/**
	 * LocationManager
	 */
	private LocationManager lmgr;

	/**
	 * Current Track ID
	 */
	private long currentTrackId = -1;

	/**
	 * the timestamp of the last GPS fix we used
	 */
	private long lastGPSTimestamp = 0;
	
	private long previousTime = 0;

	/**
	 * the interval (in ms) to log GPS fixes defined in the preferences
	 */
	private long gpsLoggingInterval;
	
	private double prevLat;
	
	private double prevLon;
	
	private WayPoint currentWayPoint;
	
	ScheduledExecutorService scheduleTaskExecutor;
	
	NotificationManager mNotificationManager;
	
	int notification = -1;

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

		}
	};

	/**
	 * Binder for service interaction
	 */
	private final IBinder binder = new GPSLoggerBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public boolean onUnbind(Intent intent) {

		if (!isTracking) {
			stopSelf();
		}
		return false;
	}

	public class GPSLoggerBinder extends Binder {

		/**
		 * Called by the activity when binding. Returns itself.
		 * 
		 * @return the GPS Logger service
		 */
		public GPSLogger getService() {
			return GPSLogger.this;
		}
	}

	@Override
	public void onCreate() {
		try {
			StaticResources.setBaseContext(this);
			gpsLoggingInterval = Long.parseLong(PreferenceManager
					.getDefaultSharedPreferences(this.getApplicationContext())
					.getString(OSMTracker.Preferences.KEY_GPS_LOGGING_INTERVAL,
							OSMTracker.Preferences.VAL_GPS_LOGGING_INTERVAL)) * 1000;
	
			// Register our broadcast receiver
			IntentFilter filter = new IntentFilter();
			filter.addAction(OSMTracker.INTENT_TRACK_WP);
			filter.addAction(OSMTracker.INTENT_UPDATE_WP);
			filter.addAction(OSMTracker.INTENT_DELETE_WP);
			filter.addAction(OSMTracker.INTENT_START_TRACKING);
			filter.addAction(OSMTracker.INTENT_STOP_TRACKING);
			registerReceiver(receiver, filter);
	
			// Register ourselves for location updates
			lmgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			
			scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

			// This schedule a runnable task every 2 minutes
			scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
			  public void run() {
			    saveCoordinates();
			  }
			}, 0, 10, TimeUnit.SECONDS); // CHECK
			
			super.onCreate();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//		Toast toast = Toast.makeText(StaticResources.getBaseContext(), "Service onStartCommand(-,"+flags+","+startId+")", Toast.LENGTH_SHORT);
//		toast.show();
        //startForeground(NOTIFICATION_ID, getNotification());
        return Service.START_STICKY;
    }
    
    @Override
    public void onDestroy() {
    	try {

	        if (isTracking) {
	            // If we're currently tracking, save user data.
	            stopTrackingAndSave();
	        }
	
	        // Unregister listener
	        lmgr.removeUpdates(this);
	        
	        // Unregister broadcast receiver
	        unregisterReceiver(receiver);
	        
	        // Cancel any existing notification

	        mNotificationManager.cancel(19901213);
	        mNotificationManager.cancelAll();
	        stopNotifyBackgroundService();
	        scheduleTaskExecutor.shutdownNow();
	        super.onDestroy();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

	/**
	 * Stops GPS Logging
	 */
	private void stopTrackingAndSave() {
        isTracking = false;
        currentTrackId = -1;
        this.stopSelf();
	}
	
	@Override
	public void onLocationChanged(Location location) {
		try {
			lastLocation = location;
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
    /**
     * Counts number of satellites used in last fix.
     * @return The number of satellites
     */
    private int countSatellites() {
        int count = 0;
        GpsStatus status = lmgr.getGpsStatus(null);
        for(GpsSatellite sat:status.getSatellites()) {
            if (sat.usedInFix()) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Stops notifying the user that we're tracking in the background
     */
    private void stopNotifyBackgroundService() {
    	try {
		    NotificationManager nmgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		    nmgr.cancel(NOTIFICATION_ID);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

	@Override
	public void onProviderDisabled(String provider) {
		isGpsEnabled = false;
	}

	@Override
	public void onProviderEnabled(String provider) {
		isGpsEnabled = true;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
	

    /**
     * Getter for gpsEnabled
     * @return true if GPS is enabled, otherwise false.
     */
    public boolean isGpsEnabled() {
            return isGpsEnabled;
    }
    
    /**
     * Setter for isTracking
     * @return true if we're currently tracking, otherwise false.
     */
    public boolean isTracking() {
            return isTracking;
    }
    
    private void saveCoordinates() {
        if (Option.Instance().gpsStartTime - System.nanoTime() > (2 * 60 * 60 * 1000000000)) {
        	stopSelf();        	
        }
        if(((lastGPSTimestamp + gpsLoggingInterval) < System.currentTimeMillis())){
            lastGPSTimestamp = System.currentTimeMillis(); // save the time of this fix
//            lastNbSatellites = countSatellites();
            if (!lmgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				HelperFactory.getHelper().getWayPointDAO().save(new WayPoint(Option.Instance().getWorkerID(), Option.Instance().getPilotTourID(), 900));
				if (notification != 0) {
					notification = 0;
					drawGPSNotification();
				}
            	return;
            }
            if (lastLocation == null || (lastLocation.getProvider().equals(LocationManager.GPS_PROVIDER) && lastLocation.getAccuracy() >= 25)) {
				HelperFactory.getHelper().getWayPointDAO().save(new WayPoint(Option.Instance().getWorkerID(), Option.Instance().getPilotTourID(), 901));
				if (notification != 0) {
					notification = 0;
					drawGPSNotification();
				}
            	return;
            }
			if ((lastLocation.getLatitude() != prevLat || lastLocation.getLongitude() != prevLon) && lastLocation.getProvider().equals(LocationManager.GPS_PROVIDER))
			{
				if (notification != 1) {
					notification = 1;
					drawGPSNotification();
				}
				StaticResources.setBaseContext(this);
    			currentWayPoint = new WayPoint(Option.Instance().getWorkerID(), Option.Instance().getPilotTourID(), lastLocation, lastNbSatellites);
				HelperFactory.getHelper().getWayPointDAO().save(currentWayPoint);
				prevLat = lastLocation.getLatitude();
				prevLon = lastLocation.getLongitude();
				previousTime = System.currentTimeMillis();
			}
        }
    }
    
    private void drawGPSNotification() {

		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(notification == 1 ? R.drawable.gg : R.drawable.gr)
		        .setContentTitle("TourPilot")
		        .setContentText("GPS is not getting coordinates");
		
		Intent resultIntent = new Intent(this, PatientsActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(PatientsActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(19901213, mBuilder.build());
    }

}
