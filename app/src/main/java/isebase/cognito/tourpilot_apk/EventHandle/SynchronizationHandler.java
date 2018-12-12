package isebase.cognito.tourpilot_apk.EventHandle;

public interface SynchronizationHandler {

	public void onItemSynchronized(String text);
	public void onSynchronizedFinished(boolean isOK, String text);
	public void onProgressUpdate(String text, int progress);
	public void onProgressUpdate(String text);
	
}
