package com.anklebreaker.basketball.tw.util;

import java.io.File;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;

public class LogOutput {
	private static String TAG ="letsBasket.LogOutput";

	private static String filePath = null;					// 出力ファイルパス
	private static String logFileName = "letsBasket";		// ログファイル名
	private static int logFileSize = 1024;					// ログファイルサイズ
	private static int logFileRotationCount = 0;			// ログローテーション数
	
	private static long logSize = 0;							// n-1時点のファイルサイズ
	private static Activity mActivity;
	
	public LogOutput(Activity a){
		mActivity = a;
	}
	
	static public void run(){
		setFilePath("");
		File logpath = new File(getFilePath());
		try{
			if(!logpath.exists()){
	            //create new folder if there are null
	            logpath.mkdirs();
	            Log.i(TAG, "setLogCat() create logpath!");
	        }
			String cmd2 = "logcat -v time" + 
		            " -f " + filePath + "/" + logFileName + ".log";
	        Process proc2 = Runtime.getRuntime().exec(cmd2);
	        //save the pid of logcat
            Log.i(TAG, "setLogCat() mproc = " + proc2.toString());
		}catch(Exception e){
			Log.e(TAG, "ERROR in initializing Logcat" + e.toString());
		}
	}
	
	static public String getFilePath(){
		return filePath;
	}
	static public void setFilePath(String newFilePath){
        String filepathTemp = Environment.getExternalStorageDirectory().getAbsolutePath();
        filepathTemp = filepathTemp + "/log/"+ newFilePath;
        Log.i(TAG, "setLogCat() filepath = " + filepathTemp);

		filePath = filepathTemp;
	}

	public static long getLogSize() {
		return logSize;
	}

	public static void setLogSize(long logSize) {
		LogOutput.logSize = logSize;
	}
}
