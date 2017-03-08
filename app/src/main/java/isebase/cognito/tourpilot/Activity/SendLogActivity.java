package isebase.cognito.tourpilot.Activity;

import isebase.cognito.tourpilot.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class SendLogActivity extends Activity implements OnClickListener {

	//Hello new user!
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // make a dialog without
														// a titlebar
		setFinishOnTouchOutside(false); // prevent users from dismissing the
										// dialog by tapping outside
		setContentView(R.layout.activity_send_log);
		TextView tv = (TextView) findViewById(R.id.tvText);
		sendLogFile();
	}

	private void sendLogFile() {
		String fullName = extractLogToFile();
		if (fullName == null)
			return;

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");
		intent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "hotline@cognito-ambulant.de" });
		intent.putExtra(Intent.EXTRA_SUBJECT, "MyApp log file");
		intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + fullName));
		intent.putExtra(Intent.EXTRA_TEXT, "Log file attached.");
		startActivity(intent);
	}

	private String extractLogToFile() {
		PackageManager manager = this.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e2) {
		}
		String model = Build.MODEL;
		if (!model.startsWith(Build.MANUFACTURER))
			model = Build.MANUFACTURER + " " + model;

		// Make file name - file must be saved to external storage or it wont be
		// readable by
		// the email app.
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/TourPilot");
		myDir.mkdirs();
		File file = new File(myDir, "sendLog.txt");
		// Extract to file.
		InputStreamReader reader = null;
		FileWriter writer = null;
		try {
			// For Android 4.0 and earlier, you will get all app's log output,
			// so filter it to
			// mostly limit it to your app's output. In later versions, the
			// filtering isn't needed.

			String cmd = (Build.VERSION.SDK_INT <= 15) ? "logcat -d -v time MyApp:v dalvikvm:v System.err:v *:s"
					: "logcat -d -v time";

			// get input stream
			Process process = Runtime.getRuntime().exec(cmd);
			reader = new InputStreamReader(process.getInputStream());

			// write output stream
			writer = new FileWriter(file);
			writer.write("Android version: " + Build.VERSION.SDK_INT + "\n");
			writer.write("Device: " + model + "\n");
			writer.write("App version: "
					+ (info == null ? "(null)" : info.versionCode) + "\n");

			char[] buffer = new char[10000];
			do {
				int n = reader.read(buffer, 0, buffer.length);
				if (n == -1)
					break;
				writer.write(buffer, 0, n);
			} while (true);

			reader.close();
			writer.close();
		} catch (IOException e) {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e1) {
				}
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e1) {
				}

			// You might want to write a failure message to the log here.
			return null;
		}

		return file.getPath();
	}

	@Override
	public void onClick(View v) {
		// respond to button clicks in your UI
	}
	
	public void btRestartApplicationClick(View v) {
		Intent i = getBaseContext().getPackageManager()
	             .getLaunchIntentForPackage( getBaseContext().getPackageName() );
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}
	
	public void btSendLogs(View v) {
		sendLogFile();
	}
	
}
