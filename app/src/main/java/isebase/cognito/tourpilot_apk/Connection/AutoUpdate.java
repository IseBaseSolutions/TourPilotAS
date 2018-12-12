package isebase.cognito.tourpilot_apk.Connection;

import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

public class AutoUpdate extends AsyncTask<Void, Boolean, Void> {
	
	@Override
	protected Void doInBackground(Void... params) {
		platformRequest();
		return null;
	}
	
    public String platformRequest()
    {
    	if (Option.Instance().getVersionLink() == null)
    		return null;
        
        try {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(Option.Instance().getVersionLink());
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                     return "Server returned HTTP " + connection.getResponseCode() 
                         + " " + connection.getResponseMessage();

                input = connection.getInputStream();
                output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/download/" + "TourPilot.apk");

                byte data[] = new byte[4096];
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled())
                    {
                    	input.close();
                        return null;
                    }
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } 
                catch (IOException ignored) { }

                if (connection != null)
                    connection.disconnect();
            }
        } finally {
        	
        }
              	
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	File file = new File(Environment.getExternalStorageDirectory() + "/download/" + "TourPilot.apk");
    	intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
    	StaticResources.getBaseContext().startActivity(intent);
        return null;
    }

}
