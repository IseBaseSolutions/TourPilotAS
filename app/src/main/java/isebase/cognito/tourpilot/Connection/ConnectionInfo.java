package isebase.cognito.tourpilot.Connection;

import isebase.cognito.tourpilot.StaticResources.StaticResources;
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