package isebase.cognito.tourpilot_apk.Connection;

import isebase.cognito.tourpilot_apk.Activity.WorkersOptionActivity.WorkerOptionActivity;
import isebase.cognito.tourpilot_apk.BuildConfig;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;

public class AutoUpdate extends AsyncTask<Void, Boolean, Void> {

    private Context context;

    public AutoUpdate(Context context) {
        this.context = context;
    }

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

        if (context != null) {

            File toInstall = new File(Environment.getExternalStorageDirectory() + "/download/" + "TourPilot.apk");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", toInstall);
                Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent.setData(apkUri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            } else {
                Uri apkUri = Uri.fromFile(toInstall);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
        return null;
    }
}
