package isebase.cognito.tourpilot_apk.Gps;

import isebase.cognito.tourpilot_apk.Data.Address.Address;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;
import android.content.Intent;
import android.net.Uri;

public class GpsNavigator {

	public static void startGpsNavigation(Address address) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("google.navigation:q=%s+%s+%s", address.getCity().replace(" ", "+"), address.getStreet().replace(" ", "+"), address.getZip())));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		StaticResources.getBaseContext().startActivity(intent);
	}
	
	public static void startGpsNavigation(String address) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("google.navigation:q=%s", address)));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		StaticResources.getBaseContext().startActivity(intent);
	}

}
