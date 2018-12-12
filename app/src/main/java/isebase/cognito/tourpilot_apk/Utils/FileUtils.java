package isebase.cognito.tourpilot_apk.Utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils {

	public static void copyFile(FileInputStream sourceFile, FileOutputStream outFile){
		FileChannel sourceChannel = null;
        FileChannel outChannel = null;
        try {
            sourceChannel = sourceFile.getChannel();
            outChannel = outFile.getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), outChannel);
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
                if (sourceChannel != null) {
                    try {
						sourceChannel.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
                }
            } finally {
                if (outChannel != null) {
                    try {
						outChannel.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
                }
            }
        }
	}
	
}
