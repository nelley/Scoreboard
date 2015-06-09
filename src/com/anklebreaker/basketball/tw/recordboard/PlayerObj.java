package com.anklebreaker.basketball.tw.recordboard;

import java.util.HashMap;
import com.anklebreaker.basketball.tw.def.ActionDef;
import android.content.Context;
import android.util.Log;

/**
 * manage the individual player's record
 * */
public class PlayerObj implements Cloneable{

    private static final String TAG = "PlayerObj";
    private static final int DIFF = 1;
    
    //HashMap for managing the players
    public static HashMap<String, PlayerObj> playerMap = new HashMap<String, PlayerObj>();
    public static PlayerObj objInstance = null;
    
    public Context mContext = null;
    protected String playerNum =null;
    protected String playerName = null;
    protected String actTime =null;
    protected int xPos = 0;
    protected int yPos = 0;
    protected int playerAct = -9;
    
    public String[] recordsArray = 
        {"number"/*號碼*/,"name"/*球員姓名*/,"0"/*兩分命中*/,"0"/*兩分出手*/,
         "0"/*三分命中*/,"0"/*三分出手*/,"0"/*罰球命中*/,"0"/*罰球出手*/,
         "0"/*防守籃板*/,"0"/*進攻籃板*/,"0"/*助攻*/,"0"/*火鍋*/,
         "0"/*抄截*/,"0"/*失誤*/,"0"/*犯規*/,"0"/*總得分*/};
    /**
     * constructor
     * */
    public PlayerObj(Context c, String num, String name){
        this.mContext = c;
        this.playerNum = num;
        this.playerName = name;
    }

    public static PlayerObj getInstance(Context c, int act, String num, String name, String time, int x, int y){
        objInstance = playerMap.get(num);
        
        if(objInstance == null){
            objInstance = new PlayerObj(c, num, name);
            playerMap.put(num, objInstance);
            Log.i(TAG, objInstance.playerNum + "player created");
        }
        
        objInstance.playerAct = act;
        objInstance.actTime = time;
        objInstance.xPos = x;
        objInstance.yPos = y;
        Log.i(TAG, objInstance.playerNum + "player act as" + objInstance.playerAct);
        return objInstance;
    }
    /**
     * update record's for showing in summary page
     * PlayerObj: player's Object
     * n: increment for recording
     * @return: true when success, false otherwise
     **/
    public Boolean setSummary(PlayerObj mPlayerObj, int n){
        Log.i(TAG, "playerActed");
        try{
            this.recordsArray[0] = mPlayerObj.playerNum;
            this.recordsArray[1] = mPlayerObj.playerName;
            //update the record
            int tmp = Integer.valueOf(recordsArray[mPlayerObj.playerAct]);
            recordsArray[mPlayerObj.playerAct] = String.valueOf((tmp + n));
            if(mPlayerObj.playerAct == ActionDef.ACT_TWOP_MA || 
               mPlayerObj.playerAct == ActionDef.ACT_THREEP_MA || 
               mPlayerObj.playerAct == ActionDef.ACT_FTMA){
               //increment both shoot and made
                int addition = Integer.valueOf(recordsArray[mPlayerObj.playerAct + DIFF]);
                recordsArray[mPlayerObj.playerAct + 1] = String.valueOf((addition + n));
                //update the total_score
                    int total_score = Integer.valueOf(recordsArray[2])*2+
                                      Integer.valueOf(recordsArray[4])*3+
                                      Integer.valueOf(recordsArray[6]);
                    recordsArray[15] = String.valueOf(total_score);
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }
    public static void reset(PlayerObj i){
        i.playerName = null;
        i.playerNum = null;
        i.actTime = null;
        i.playerAct = 0;
        i.xPos = 0;
        i.yPos = 0;
    }
    @Override
    protected PlayerObj clone() throws CloneNotSupportedException {
        return (PlayerObj) super.clone();
    }
}