package com.anklebreaker.basketball.tw.summary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.recordboard.TeamObj;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class UploadAsyncTask extends AsyncTask<Integer, Integer, String> {
    
    private final String TAG = "ScoreBoard.UploadAsyncTask";
    private final String msg = "資料上傳中...";
    private final String URL = "http://192.168.0.3/shop/uploadGameRecord/";
    private ProgressDialog progressDialog;				//與server連結處理時顯示進度的視窗
    private JSONArray uploadObj = new JSONArray();
    private Activity tmpActivity;
    
    public UploadAsyncTask(Activity a){
        this.tmpActivity = a;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(tmpActivity);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(msg);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.show();
        try{
            for(int i=0; i<PlayerObj.playerMap.size(); i++){
                if(PlayerObj.playerMap.get(i).getPlayerName() != TeamObj.DUMMY_PLAYER){
                    JSONObject playerObj = new JSONObject();
                    playerObj.put("number", PlayerObj.playerMap.get(i).getPlayerNum());
                    playerObj.put("name", PlayerObj.playerMap.get(i).getPlayerName());
                    playerObj.put("isStarter", PlayerObj.playerMap.get(i).getIsStarter());
                    playerObj.put("twoMade", PlayerObj.playerMap.get(i).recordsArray[2]);
                    playerObj.put("twoTried", PlayerObj.playerMap.get(i).recordsArray[3]);
                    playerObj.put("threeMade", PlayerObj.playerMap.get(i).recordsArray[4]);
                    playerObj.put("threeTried", PlayerObj.playerMap.get(i).recordsArray[5]);
                    playerObj.put("freeMade", PlayerObj.playerMap.get(i).recordsArray[6]);
                    playerObj.put("freeMiss", PlayerObj.playerMap.get(i).recordsArray[7]);
                    playerObj.put("DR", PlayerObj.playerMap.get(i).recordsArray[8]);
                    playerObj.put("OR", PlayerObj.playerMap.get(i).recordsArray[9]);
                    playerObj.put("assist", PlayerObj.playerMap.get(i).recordsArray[10]);
                    playerObj.put("block", PlayerObj.playerMap.get(i).recordsArray[11]);
                    playerObj.put("steal", PlayerObj.playerMap.get(i).recordsArray[12]);
                    playerObj.put("turnover", PlayerObj.playerMap.get(i).recordsArray[13]);
                    playerObj.put("foul", PlayerObj.playerMap.get(i).recordsArray[14]);
                    playerObj.put("totalPoint", PlayerObj.playerMap.get(i).recordsArray[15]);
                    uploadObj.put(playerObj);	
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        Log.i(TAG, uploadObj.toString());
    }

    @Override
    protected String doInBackground(Integer... params) {
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            // create JSON data to send to server
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,3000);
            HttpConnectionParams.setSoTimeout(httpParams, 3000);
            
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpPost request = new HttpPost(URL);
            request.setEntity(new ByteArrayEntity(uploadObj.toString().getBytes("UTF8")));
            request.setHeader("json", uploadObj.toString());
            HttpResponse response = client.execute(request);

            if(response != null){
                InputStream is = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                //retrieve the jason data from response
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (ClientProtocolException e) {
            Log.e(TAG, "ClientProtocolException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "IOException");
            e.printStackTrace();
        }
        
        //response body
        return sb.toString();
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Toast.makeText(tmpActivity, "上傳成功", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

}
