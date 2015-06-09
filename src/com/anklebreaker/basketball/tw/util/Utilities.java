package com.anklebreaker.basketball.tw.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

public class Utilities {
	
	private static final String TAG = "letsBasket.Utilities";
	// コネクションタイマアウト時間10秒（ms単位）
	public static final int HTTP_CONNECTION_TIMEOUT = 10000;
	// 通信読み込みタイマアウト設定時間10秒（ms単位）
	public static final int HTTP_DATAREAD_TIMEOUT = 10000;
	
	/************ 処理結果定義 ******************************************************/
	// 0:成功
	// 1:通信エラー
	// 2:プロセスエラー
	// 3:処理中断エラー
	// 4:送信データ作成エラー
	// 5:送信回答エラー
	public static final int UPLOAD_RET_SUCCESS = 0;
	public static final int UPLOAD_RET_COMMUNICATIONERROR = 1;
	public static final int UPLOAD_RET_PROCESS = 2;
	public static final int UPLOAD_RET_CANCELLED = 3;
	public static final int UPLOAD_RET_DATAERROR = 4;
	public static final int UPLOAD_RET_RESPONSEERROR = 5;

	static public double getScreenDpiX(Context mcontext){
		//get the screen size
		DisplayMetrics metrics = mcontext.getResources().getDisplayMetrics();
		int scale = (int)(metrics.density);
		return (metrics.widthPixels/scale);
	}
	static public double getScreenDpiY(Context mcontext){
		DisplayMetrics metrics = mcontext.getResources().getDisplayMetrics();
		int scale = (int)(metrics.density);
		return (metrics.heightPixels/scale);
	}

	/**
	 * "yyyyMMddhhmmss"のフォーマットで現在時間を取得
	 * */
	public static String getSystemTime() {
		//SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date curDate = new Date(System.currentTimeMillis());
		String str = formatter.format(curDate);
		Log.i(TAG, "getSystemTime() systime = " + str);
	
		return str;
	}


}
