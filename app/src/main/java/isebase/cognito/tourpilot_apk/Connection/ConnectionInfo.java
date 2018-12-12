package isebase.cognito.tourpilot_apk.Connection;

import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionInfo {

	private ConnectivityManager connMgr;
	
	private static ConnectionInfo instance;
	
	public static ConnectionInfo Instance() {
		if (instance != null)
			return instance;
		instance = new ConnectionInfo();
		return instance;
	}

	public NetworkInfo getNetworkInfo() {
		return connMgr.getActiveNetworkInfo();
	}

	public ConnectionInfo() {
		connMgr = (ConnectivityManager) StaticResources
				.getBaseContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

}