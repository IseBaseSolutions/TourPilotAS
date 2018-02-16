package isebase.cognito.tourpilot.Connection;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.DataBase.HelperFactory;
import isebase.cognito.tourpilot.StaticResources.StaticResources;
import isebase.cognito.tourpilot.Utils.StringParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

//import android.R.string;
import android.os.AsyncTask;

public class ConnectionAsyncTask extends AsyncTask<Void, Boolean, Void> {

	private ConnectionStatus conStatus;
	
	private boolean isTerminated;

	public ConnectionAsyncTask(ConnectionStatus cs) {
		isTerminated = false;
		conStatus = cs;
	}

	public void terminate(){
		isTerminated = true;
	}
	
	@Override
	protected void onProgressUpdate(Boolean... values) {
		super.onProgressUpdate(values);
		if(values.length > 0 && values[0]){
			conStatus.UISynchHandler.onProgressUpdate("Start");
		}
		else
			conStatus.UISynchHandler.onProgressUpdate(
				conStatus.getProgressMessage(), conStatus.getCurrentProgress());
	}
	
	@Override
	protected void onPostExecute(Void result) {
		if (!conStatus.lastExecuteOK 
				|| conStatus.isFinished
				|| isTerminated) {
			if (!conStatus.lastExecuteOK || isTerminated) {
				conStatus.UISynchHandler.onSynchronizedFinished(false,
						conStatus.getMessage());
				closeConnection();
			}
			if (conStatus.isFinished){
				conStatus.UISynchHandler.onSynchronizedFinished(
						conStatus.isFinished, conStatus.getMessage());	
			}				
			return;
		}
		conStatus.UISynchHandler.onItemSynchronized(conStatus.getMessage());
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected Void doInBackground(Void... params) {
		switch (conStatus.CurrentState) {
		case ConnectionStatus.INIT:
			conStatus.setMessage(String.format(
					"%1$s %2$s : %3$s ...",
					StaticResources.getBaseContext().getString(
							R.string.connection_try), 
							Option.Instance().getServerIP(),
							Option.Instance().getServerPort()));
			break;
		case ConnectionStatus.CONNECTION:
			conStatus.lastExecuteOK = initializeConnection();
			break;
		case ConnectionStatus.INVINTATION:
			conStatus.lastExecuteOK = recievingInvitation();
			break;
		case ConnectionStatus.DATE_SYNC:
			conStatus.lastExecuteOK = sendDateSycnhronizationRequest();
			break;
		case ConnectionStatus.SEND_DATA:
			conStatus.lastExecuteOK = sendHelloRequest();
			break;
		case ConnectionStatus.COMPARE_CHECKSUMS:
			conStatus.lastExecuteOK = compareCkeckSums();
			break;
		case ConnectionStatus.PARSE_DATA:
			conStatus.lastExecuteOK = parseRecievedData();
			break;
		case ConnectionStatus.CLOSE_CONNECTION:
			conStatus.isFinished = true;
			conStatus.lastExecuteOK = closeConnection();
			break;
		case ConnectionStatus.ADDITONAL_PATIENTS_SYNC:
			conStatus.lastExecuteOK = additionalPatientsSync();
			break;
		case ConnectionStatus.TIME_SYNC:
			conStatus.lastExecuteOK = getTimeSync();
			break;
		default:
			conStatus.isFinished = true;
			break;
		}
		return null;

	}

	private boolean initializeConnection() {
		try {
			conStatus.socket = new Socket(Option.Instance().getServerIP(),
					Option.Instance().getServerPort());

			conStatus.OS = conStatus.socket.getOutputStream();
			conStatus.IS = conStatus.socket.getInputStream();

		} catch (Exception ex) {
			ex.printStackTrace();
			conStatus.setMessage(String.format("%1$s ...", ex.getMessage()));
			return false;
		}
		conStatus.setMessage(StaticResources.getBaseContext().getString(
				R.string.connection_ok));
		return true;
	}

	private boolean recievingInvitation() {
		String strInvitation = "";
		boolean retVal = true;
		try {
			strInvitation = readFromStream(conStatus.IS);
			if (strInvitation.length() > 2
					&& strInvitation.substring(0, 2).compareTo("OK") == 0)
				retVal = true;
			else
				retVal = false;
		} catch (Exception ex) {
			ex.printStackTrace();
			retVal = false;
		} finally {
			if (retVal)
				conStatus.setMessage(StaticResources.getBaseContext()
						.getString(R.string.invitation_ok));
			else
				conStatus.setMessage(StaticResources.getBaseContext()
						.getString(R.string.invitation_fail));
		}
		return retVal;
	}

	private boolean sendDateSycnhronizationRequest() {
		boolean retVal = true;

		try {
			writeToStream(conStatus.OS, "GETTIME");
			conStatus.OS.flush();
			String recievedDate = readFromStream(conStatus.IS);
			conStatus.serverCommandParser.parseElement(recievedDate, true);
			RecievedObjectSaver.Instance().save();
			Option.Instance().setTimeSynchronised(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			retVal = false;
		} finally {
			if (retVal)
				conStatus.setMessage(StaticResources.getBaseContext()
						.getString(R.string.sycnhronizing_ok));
			else
				conStatus.setMessage(StaticResources.getBaseContext()
						.getString(R.string.sycnhronizing_fail));
		}

		return retVal;
	}

	private boolean sendHelloRequest() {
		boolean retVal = true;
		try {
			Option.Instance().setIsAuto(false);
			writePack(conStatus.OS, getDataToSend() + "\0.\0");
			String recievedStatus = readFromStream(conStatus.IS);
			if (recievedStatus.startsWith("OVER") 
					/*|| recievedStatus.equals("")*/) {
				conStatus.setAnswerFromServer("OVER");
				retVal = false;
			}
			else if (recievedStatus.equals("")) {
				conStatus.setAnswerFromServer("INTERRUPT");
				retVal = false;
			}
			else if(recievedStatus.startsWith("OK")) {
				SentObjectVerification.Instance().setWasSent();
				StringParser stringParser = new StringParser(recievedStatus);
				String msg = stringParser.next("\0");
				Option.Instance().setIsAuto(msg.contains("1"));
				Option.Instance().setIsSkippingPflegeOK(msg.contains("SkipPflegeOk"));
				Option.Instance().setWorkerPhones(msg.contains("WorkersInfo"));
				if (stringParser.contains(ServerCommandParser.SERVER_CURRENT_VERSION))
					conStatus.serverCommandParser.parseElement(stringParser.next("\0"), false);
				else
					Option.Instance().setPalmVersion(null);
				if (stringParser.contains(ServerCommandParser.SERVER_VERSION_LINK))
					conStatus.serverCommandParser.parseElement(stringParser.next("\0"), false);
				else
					Option.Instance().setVersionLink(null);
				RecievedObjectSaver.Instance().save();
			}
						
		} catch (Exception ex) {
			ex.printStackTrace();
			conStatus.setAnswerFromServer("INTERRUPT");
			retVal = false;
		} finally {
			Option.Instance().save();
			if (retVal)
				conStatus.setMessage(StaticResources.getBaseContext()
						.getString(R.string.hello_request_ok));
			else
				conStatus.setMessage(StaticResources.getBaseContext()
						.getString(R.string.hello_request_fail));
		}
		return retVal;
	}

	private boolean compareCkeckSums() {
		boolean retVal = true;
		try {
			writePack(conStatus.OS, getStrChecksums());
			String strChecksumRecieve = readFromStream(conStatus.IS);
			String strCheckItems = strChecksumRecieve;
			writePack(conStatus.OS, get_SRV_msgStoredData(strCheckItems));
			String dataFromServer = readPack(conStatus.IS);
			if(dataFromServer.equals(""))
				retVal = false;
			else
				conStatus.setDataFromServer(dataFromServer.split("\0"));
		} catch (Exception ex) {
			ex.printStackTrace();
			retVal = false;
		} finally {
			if (retVal)
				conStatus.setMessage(String.format("%1$s \n %2$s: %3$s",
						StaticResources.getBaseContext().getString(R.string.checksum_ok),
						StaticResources.getBaseContext().getString(R.string.data_to_download),
						conStatus.getTotalProgress()));
			else
				conStatus.setMessage(StaticResources.getBaseContext()
						.getString(R.string.checksum_fail));
		}
		return retVal;
	}

	private boolean parseRecievedData() {
		boolean retVal = true;
		try {
			publishProgress(true);
			for (String data : conStatus.getDataFromServer()){
				if(isTerminated)
				{
					conStatus.isFinished = true;
					break;
				}
				conStatus.serverCommandParser.parseElement(data, false);
				publishProgress();
			}
			RecievedObjectSaver.Instance().save();
		} catch (Exception ex) {
			ex.printStackTrace();
			retVal = false;
		} finally {
			if (retVal)
				conStatus.setMessage(StaticResources.getBaseContext()
						.getString(R.string.processing_data_ok));
			else
				conStatus.setMessage(StaticResources.getBaseContext()
						.getString(R.string.processing_data_fail));
		}
		return retVal;
	}

	private boolean closeConnection() {
		boolean retVal = true;
		try {
			writeToStream(conStatus.OS, "OK" + "\0");
		} catch (Exception e) {
			e.printStackTrace();
			retVal = false;
		} finally {
			conStatus.closeConnection();
			if (retVal)
				conStatus.setMessage(StaticResources.getBaseContext()
						.getString(R.string.connection_close_ok));
			else
				conStatus.setMessage(StaticResources.getBaseContext()
						.getString(R.string.connection_close_fail));
		}
		return true;
	}

	private String readFromStream(InputStream is) throws InterruptedException,
			IOException {
		String retVal = "";
		int timeoutCount = 120000;
		long startTime = new Date().getTime();
		long currentTime = startTime;
		
		while (currentTime - startTime < timeoutCount) {			
			if(isTerminated)
				return "";
			int av = 0;
			try {
				av = is.available();
			} finally {
				if (av == 0) {
					Thread.sleep(100);
					currentTime = new Date().getTime();
					continue;
				}
			}			
			while (av != 0) {
				byte[] charI = { (byte) is.read() };
				retVal += new String(charI, "cp1252");
				av = is.available();
			}
			return retVal;
		}
		return retVal;
	}

	private void writeToStream(OutputStream os, String text) {
		try {
			text = "[DEFLATER]" + text;
			os.write(text.getBytes());
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getDataToSend() {		
		return getHeaderStr() + getDoneStr(true);
	}
	
	private String getHeaderStr(){
		String strMsg = new String("U;");
		strMsg += Option.Instance().getWorkerID() +";";
		strMsg += Option.Instance().getPrevWorkerID() + ";:";
		strMsg += Option.Instance().getDeviceID() + ";";
		strMsg += Option.Instance().getPhoneNumber() + "@";
		strMsg += Option.Instance().getVersion() + "\0R;";
			strMsg += "0";
		strMsg += "\0x1\0";
		return strMsg;
	}
	
	public static String getDoneStr(Boolean checkWp){
		String strDone = "";
		strDone += HelperFactory.getHelper().getEmploymentDAO().getDone();
		strDone += HelperFactory.getHelper().getWorkDAO().getDone();
		strDone += HelperFactory.getHelper().getAnswerDAO().getDone();
		strDone += HelperFactory.getHelper().getUserRemarkDAO().getDone();
		strDone += HelperFactory.getHelper().getEmploymentVerificationDAO().getDone();
		if (checkWp)
			strDone += HelperFactory.getHelper().getWayPointDAO().getDone();
		return strDone;
	}

	private String getStrChecksums() {
		String strMsg = "";
		strMsg += "z" + HelperFactory.getHelper().getAdditionalTaskDAO().getCheckSumByRequest() + "\0.\0";
		strMsg += "a" + HelperFactory.getHelper().getWorkerDAO().getCheckSumByRequest() + "\0.\0";
		strMsg += "u" + HelperFactory.getHelper().getAdditionalWorkDAO().getCheckSumByRequest() + "\0.\0";		

		boolean userIsPresent = Option.Instance().getWorkerID() != -1;
		strMsg += "d" + (userIsPresent ? HelperFactory.getHelper().getDiagnoseDAO().getCheckSumByRequest() : 0) + "\0.\0";
		strMsg += "b" + (userIsPresent ? HelperFactory.getHelper().getPatientRemarkDAO().getCheckSumByRequest() : 0) + "\0.\0";
		strMsg += "v" + (userIsPresent ? HelperFactory.getHelper().getRelativeDAO().getCheckSumByRequest() : 0) + "\0.\0";
		strMsg += "m" + (userIsPresent ? HelperFactory.getHelper().getDoctorODA().getCheckSumByRequest() : 0) + "\0.\0";
		strMsg += "p" + (userIsPresent ? HelperFactory.getHelper().getPatientDAO().getCheckSumByRequest() : 0) + "\0.\0";
		strMsg += "i" + (userIsPresent ? HelperFactory.getHelper().getInformationDAO().getCheckSumByRequest() : 0) + "\0.\0";
		strMsg += "r" + (userIsPresent ? HelperFactory.getHelper().getTourDAO().getCheckSumByRequest() : 0) + "\0.\0";
		strMsg += "t" + (userIsPresent ? HelperFactory.getHelper().getTaskDAO().getCheckSumByRequest() : 0) + "\0.\0";
		if (Integer.parseInt(Option.Instance().getVersion()) > 1042)
			strMsg += "#" + HelperFactory.getHelper().getCustomRemarkDAO().getCheckSumByRequest() + "\0.\0";
		if (Integer.parseInt(Option.Instance().getVersion()) > 1042)
		{
			strMsg += "q" + HelperFactory.getHelper().getQuestionDAO().getCheckSumByRequest() + "\0.\0";
			strMsg += "y" + HelperFactory.getHelper().getCategoryDAO().getCheckSumByRequest() + "\0.\0";
			strMsg += "j" + HelperFactory.getHelper().getLinkDAO().getCheckSumByRequest() + "\0.\0";
			strMsg += "x" + (userIsPresent ? HelperFactory.getHelper().getQuestionSettingDAO().getCheckSumByRequest() : 0) + "\0.\0";
		}
		strMsg += "?" + HelperFactory.getHelper().getRelatedQuestionSettingDAO().getCheckSumByRequest() + "\0.\0";
		strMsg += "+" + (userIsPresent ? HelperFactory.getHelper().getTourOncomingInfoDAO().getCheckSumByRequest() : 0) + "\0.\0";
//		strMsg += ">" + CFreeQuestions.Instance().getCheckSums() + "\0.\0";
//		strMsg += "<" + CFreeTopics.Instance().getCheckSums() + "\0.\0";
//		strMsg += "*" + (userIsPresent ? CFreeQuestionSettings.Instance().getCheckSums() : 0) + "\0.\0";
//		strMsg += "^" + (userIsPresent ? CAutoQuestionSettings.Instance().getCheckSums() : 0) + "\0.\0";
		return strMsg;
	}

	private String get_SRV_msgStoredData(String strNeedSend) {
		String strMsg = "";
//		if (strNeedSend.length() > 18 && strNeedSend.charAt(18) == '1')
//			strMsg += CAutoQuestionSettings.Instance().forServer();
//		if (strNeedSend.length() > 17 && strNeedSend.charAt(17) == '1')
//			strMsg += CFreeQuestionSettings.Instance().forServer();
//		if (strNeedSend.length() > 16 && strNeedSend.charAt(16) == '1')
//			strMsg += CFreeTopics.Instance().forServer();
//		if (strNeedSend.length() > 15 && strNeedSend.charAt(15) == '1')
//			strMsg += CFreeQuestions.Instance().forServer();
		if (strNeedSend.length() > 17 && strNeedSend.charAt(18) == '1')
			strMsg += HelperFactory.getHelper().getPatientAdditionalAddressDAO().forServer();
		if (strNeedSend.length() > 16 && strNeedSend.charAt(17) == '1')
			strMsg += HelperFactory.getHelper().getTourOncomingInfoDAO().forServer();
		if (strNeedSend.length() > 16 && strNeedSend.charAt(16) == '1')
			strMsg += HelperFactory.getHelper().getRelatedQuestionSettingDAO().forServer();
		if (strNeedSend.length() > 15 && strNeedSend.charAt(15) == '1')
			strMsg += HelperFactory.getHelper().getQuestionSettingDAO().forServer();
		if (strNeedSend.length() > 14 && strNeedSend.charAt(14) == '1')
			strMsg += HelperFactory.getHelper().getLinkDAO().forServer();
		if (strNeedSend.length() > 13 && strNeedSend.charAt(13) == '1')
			strMsg += HelperFactory.getHelper().getCategoryDAO().forServer();
		if (strNeedSend.length() > 12 && strNeedSend.charAt(12) == '1')
			strMsg += HelperFactory.getHelper().getQuestionDAO().forServer();
		if (strNeedSend.length() > 11 && strNeedSend.charAt(11) == '1')
			strMsg += HelperFactory.getHelper().getCustomRemarkDAO().forServer();
		if (strNeedSend.length() > 10 && strNeedSend.charAt(10) == '1')
			strMsg += HelperFactory.getHelper().getTaskDAO().forServer();
		if (strNeedSend.length() > 9 && strNeedSend.charAt(9) == '1')
			strMsg += HelperFactory.getHelper().getAdditionalWorkDAO().forServer();
		if (strNeedSend.length() > 8 && strNeedSend.charAt(8) == '1')
			strMsg += HelperFactory.getHelper().getTourDAO().forServer();
		if (strNeedSend.length() > 7 && strNeedSend.charAt(7) == '1')
			strMsg += HelperFactory.getHelper().getInformationDAO().forServer();
		if (strNeedSend.length() > 6 && strNeedSend.charAt(6) == '1')
			strMsg += HelperFactory.getHelper().getPatientRemarkDAO().forServer();
		if (strNeedSend.length() > 5 && strNeedSend.charAt(5) == '1')
			strMsg += HelperFactory.getHelper().getPatientDAO().forServer();
		if (strNeedSend.length() > 4 && strNeedSend.charAt(4) == '1')
			strMsg += HelperFactory.getHelper().getDoctorODA().forServer();
		if (strNeedSend.length() > 3 && strNeedSend.charAt(3) == '1')
			strMsg += HelperFactory.getHelper().getRelativeDAO().forServer();
		if (strNeedSend.length() > 2 && strNeedSend.charAt(2) == '1')
			strMsg += HelperFactory.getHelper().getDiagnoseDAO().forServer();
		if (strNeedSend.length() > 1 && strNeedSend.charAt(1) == '1')
			strMsg += HelperFactory.getHelper().getWorkerDAO().forServer();
		if (strNeedSend.length() > 0 && strNeedSend.charAt(0) == '1')
			strMsg += HelperFactory.getHelper().getAdditionalTaskDAO().forServer();
		if (strNeedSend.indexOf("1") == -1)
			strMsg += ".";
		return strMsg;
	}

	public void writePack(OutputStream os, String strToWrite)
			throws IOException {		
		DeflaterOutputStream dos = null;
		try {
			dos = new DeflaterOutputStream(os);
			dos.write(strToWrite.getBytes());
			dos.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String readPack(InputStream is) throws IOException, InterruptedException {			
	
		int bytesRead = 0;
		InflaterInputStream in = null;
		byte[] contents = new byte[2*1024];
		byte[] buf = new byte[2*1024];
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    String result = "";
	    try {
	    	in = new InflaterInputStream(is, new Inflater(true));
	    	contents = new byte[in.available()];
	        while((bytesRead = in.read(buf, 0, contents.length)) != -1){
	        	if(isTerminated)
					return "";
	        	baos.write(buf, 0, bytesRead);
	        }
	        result = baos.toString("cp1252");
	    } catch (IOException e) {	       
	        e.printStackTrace();
	    }	    
	    return result;
	}
	
	private boolean additionalPatientsSync() {
		writeToStream(conStatus.OS, conStatus.getRequestMessage());
		try {
			conStatus.OS.flush();
		} catch(Exception e) {
			e.printStackTrace();
		}
		if (!conStatus.getRequestMessage().toLowerCase().startsWith("get") 
				&& !conStatus.getRequestMessage().toLowerCase().startsWith("sel"))
			return true;
		String answerFromServer = "";
		try {
			answerFromServer = readPack(conStatus.IS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		conStatus.setAnswerFromServer(answerFromServer);
		return true;
	}
	
	private boolean getTimeSync() {
		boolean retVal = true;

		try {
			writeToStream(conStatus.OS, "GETTIME_CLOSE");
			conStatus.OS.flush();
			String recievedDate = readFromStream(conStatus.IS);
			conStatus.serverCommandParser.parseElement(recievedDate, true);
			RecievedObjectSaver.Instance().save();
			Option.Instance().setTimeSynchronised(true);		
		} catch (Exception ex) {
			ex.printStackTrace();
			retVal = false;
		} finally {
			if (retVal)
				conStatus.setMessage(StaticResources.getBaseContext()
						.getString(R.string.sycnhronizing_ok));
			else
				conStatus.setMessage(StaticResources.getBaseContext()
						.getString(R.string.sycnhronizing_fail));
		}

		return retVal;
	}

}
