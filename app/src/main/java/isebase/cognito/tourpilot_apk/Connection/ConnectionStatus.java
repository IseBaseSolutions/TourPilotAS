package isebase.cognito.tourpilot_apk.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import isebase.cognito.tourpilot_apk.EventHandle.SynchronizationHandler;

public class ConnectionStatus {
	
	public static final int INIT = -1;
	public static final int CONNECTION = 0;
	public static final int INVINTATION = 1;
	public static final int DATE_SYNC = 2;
	public static final int SEND_DATA = 3;
	public static final int COMPARE_CHECKSUMS = 4;
	public static final int PARSE_DATA = 5;
	public static final int CLOSE_CONNECTION = 6;
	public static final int ADDITONAL_PATIENTS_SYNC = 8;
	public static final int TIME_SYNC = 9;
	
	public int CurrentState;

	public boolean isFinished;
	
	public Socket socket;
	
	public OutputStream OS;
	public InputStream IS;
	
	public ServerCommandParser serverCommandParser;
	
	public SynchronizationHandler localSynchHandler;
	public SynchronizationHandler UISynchHandler;
		
	private String currMessage = "" ;	
	private String currProgressMessage = "" ;	
	private String requestForServer = "";
	private String answerFromServer = "";

	private int currProgress;
	
	public String getProgressMessage(){
		return currProgressMessage;
	}
	
	public int getCurrentProgress(){
		return currProgress;
	}
	
	private int totalProgress = 1;

	public int getTotalProgress(){
		return totalProgress;
	}
	
	public void setTotalProgress(int val){
		totalProgress = val;
	}
	
	public String getMessage() {
		return currMessage;
	}

	public void setMessage(String currMessage) {
		this.currMessage = currMessage;
	}
	
	public String getRequestMessage() {
		return requestForServer;
	}

	public void setRequestForServer(String requestForServer) {
		this.requestForServer = requestForServer;
	}
	
	public String getAnswerFromServer() {
		return answerFromServer;
	}

	public void setAnswerFromServer(String answerFromServer) {
		this.answerFromServer = answerFromServer;
	}

	public boolean lastExecuteOK = true;
	
	private String[] dataFromServer;
	
	public void setDataFromServer(String[] data){
		dataFromServer = data;
		totalProgress = data.length;
	}
	
	public String[] getDataFromServer(){
		return dataFromServer;
	}
		
	public ConnectionStatus(SynchronizationHandler synchHandler){
		currProgress = 0;
		CurrentState = INIT;
		isFinished = false;
		dataFromServer = new String[0];
		UISynchHandler = synchHandler;
		localSynchHandler = new SynchronizationHandler() {			
			@Override
			public void onSynchronizedFinished(boolean isOK, String text) {
				
			}
			
			@Override
			public void onItemSynchronized(String text) {	
				
			}

			@Override
			public void onProgressUpdate(String text, int progress) {
				currProgressMessage = text;		
				currProgress = progress;
			}

			@Override
			public void onProgressUpdate(String text) {
				currProgressMessage = text;		
				currProgress++;
			}
			
		};
		serverCommandParser = new ServerCommandParser(localSynchHandler);
	}
	
	public void nextState(){
		CurrentState++;
		currMessage = "";
	}
	
	public void closeConnection(){
		try {
			if(OS != null)
				OS.close();
			if(IS != null)
				IS.close();	
			if(socket != null )
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
