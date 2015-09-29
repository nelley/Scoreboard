package com.anklebreaker.basketball.tw.summary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.recordboard.TeamObj;

import android.os.AsyncTask;
import android.util.Log;

public class UploadAsyncTask extends AsyncTask<Integer, Integer, Integer> {
    
    private final String TAG = "ScoreBoard.UploadAsyncTask";
    private JSONArray uploadObj = new JSONArray();
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try{
            for(int i=0; i<PlayerObj.playerMap.size(); i++){
                if(PlayerObj.playerMap.get(i).getPlayerName() != TeamObj.DUMMY_PLAYER){
                    JSONObject playerObj = new JSONObject();
                    playerObj.put("number", PlayerObj.playerMap.get(i).getPlayerNum());
                    playerObj.put("name", PlayerObj.playerMap.get(i).getPlayerName());
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
        
        
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        try {
            Thread.sleep(5000);
            Log.i(TAG, uploadObj.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        
        return null;
    }


    @Override
    protected void onPostExecute(Integer result) {
        
        super.onPostExecute(result);
    }

}
