package com.anklebreaker.basketball.tw.recordboard;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.anklebreaker.basketball.tw.def.ActionDef;

public class RivalPlayerObj extends Player implements Cloneable{

    private static final String TAG = "RecordBoard.RivalPlayerObj";
    private static final int DIFF = 1;
    // HashMap for managing the players
    public static ArrayList<RivalPlayerObj> rivalPlayerMap = new ArrayList<RivalPlayerObj>();
    public static RivalPlayerObj objInstance = null;

    public Context mContext = null;

    /**
     * Constructor for default ScoreBoard View(Used in DUMMY PLAYER)
     * */
    
    public RivalPlayerObj(String num, String name, boolean isS, boolean isB, boolean isO){
        this.playerNum = num;
        this.playerName = name;
        this.isStarter = isS;
        this.isBench = isB;
        this.isOnPlay = isO;
    }

    /**
     * Constructor for GridView
     * @image_check starter arrow icon
     * @image player jordan icon
     * @isStarter starter or not
     * @isBench bench player or not
     * @isOnPlay is onplayer or not
     * */
    
    public RivalPlayerObj(Context c, Bitmap image_check, Bitmap image, String num, String name, boolean isS, boolean isB, boolean isO){
        this.image_check = image_check;
        this.image = image;
        this.mContext = c;
        this.playerNum = num;
        this.playerName = name;
        this.isStarter = isS;
        this.isBench = isB;
        this.isOnPlay = isO;
    }
    
    /**
     * create player's object by singleton
     * @c
     * @act
     * @image_check
     * @image
     * @num player number
     * @name player's name
     * @isS Starter or not
     * @isB Bench or not
     * @isO onplay or not
     * @time timestamp for action
     * @x x position for player's action
     * @y y position for player's action
     * */
    public static RivalPlayerObj getInstance(Context c, int act, Bitmap image_check, Bitmap image, String num, String name, boolean isS, boolean isB, boolean isO, String time, int quarter, int x, int y){
        objInstance = null;
        // check exist elements
        for (RivalPlayerObj existPlayer : rivalPlayerMap) {
            if(existPlayer.playerNum == num){
                objInstance = existPlayer;
                break;
            }
        }

        if(objInstance == null){
            objInstance = new RivalPlayerObj(c, image_check, image, num, name, isS, isB, isO);
            rivalPlayerMap.add(objInstance);
            Log.i(TAG, objInstance.playerNum + "player created");
        }

        objInstance.playerAct = act;
        objInstance.actTime = time;
        objInstance.quarter = quarter;
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
    public Boolean setSummary(RivalPlayerObj mPlayerObj, int n){
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
    
    /**
     * change the isOnplay, isBench value 
     * */
    public static void playerReplace(int position){
        // reverse the values
        if(RivalPlayerObj.rivalPlayerMap.get(position).isBench){
            RivalPlayerObj.rivalPlayerMap.get(position).isBench = false;
        }else{
            RivalPlayerObj.rivalPlayerMap.get(position).isBench = true;
        }
        if(RivalPlayerObj.rivalPlayerMap.get(position).isOnPlay){
            RivalPlayerObj.rivalPlayerMap.get(position).isOnPlay = false;
        }else{
            RivalPlayerObj.rivalPlayerMap.get(position).isOnPlay = true;
        }
    }
    
    
    /**
     * reset the playerobj
     * */
    public static void reset(RivalPlayerObj i){
        i.playerName = null;
        i.playerNum = null;
        i.actTime = null;
        i.playerAct = 0;
        i.xPos = 0;
        i.yPos = 0;
    }
    
    /**
     * clone for adding to the timeline
     * */
    @Override
    protected RivalPlayerObj clone() throws CloneNotSupportedException {
        return (RivalPlayerObj) super.clone();
    }


}
