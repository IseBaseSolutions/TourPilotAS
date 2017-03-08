package isebase.cognito.tourpilot.StaticResources;

import android.content.Context;
import android.telephony.TelephonyManager;

public class StaticResources {

	public static TelephonyManager phoneManager;
	
	private static Context baseContext;
	
	public static int width;
	public static int height;
	
	public static void setBaseContext(Context context) {
		baseContext = context;
		phoneManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
	}

	public static Context getBaseContext() {
		return baseContext;
	}
	
}
