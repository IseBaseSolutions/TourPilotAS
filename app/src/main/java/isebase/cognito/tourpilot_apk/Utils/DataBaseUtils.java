package isebase.cognito.tourpilot_apk.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class DataBaseUtils {

	public static final String DB_PATH = "/data/data/isebase.cognito.tourpilot/databases/TourPilot.db";
	public static final String DB_BACKUP_PATH = "/mnt/sdcard/backup/";
	
	/**
	 * Require sdcard.
	 * Copy data base file from application root to sdcard/backup/tourpilot_{today}.db</p>
	 * This function will replace file if file already exist.
	 * */
	public static void backup(){
		File dbFile = new File(DB_PATH);
		String dbBackupPath = DB_BACKUP_PATH + "tourpilot_" + DateUtils.BackupDateFormat.format(new Date()) + ".db";
		File dbBackupFile = new File(dbBackupPath);
		File backupDirPath = new File(DB_BACKUP_PATH);
		try {
			backupDirPath.mkdirs();
			dbBackupFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileUtils.copyFile(new FileInputStream(dbFile), new FileOutputStream(dbBackupFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Copy file from filePath to application root
	 * @param filePath - path to *.db file
	 * */
	public static void restore(String filePath){
		try {
			FileUtils.copyFile(new FileInputStream(filePath), new FileOutputStream(DB_PATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
