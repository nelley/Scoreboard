package com.anklebreaker.basketball.tw.recordboard;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * manage the individual player's record
 * */
public class CompetitorObj extends Player implements Cloneable{

    private static final String TAG = "RecordBoard.CompetitorObj";
    
    public static CompetitorObj objInstance = null;
    public Context mContext = null;
    public static ArrayList<CompetitorObj> playerMap = new ArrayList<CompetitorObj>();
    
    /**
     * Constructor
     * */
    public CompetitorObj(String num, String name, boolean isS, boolean isB, boolean isO){
    	Log.i(TAG, "CompetitorObj is created: " + num);
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
    public CompetitorObj(Context c, Bitmap image_check, Bitmap image, String num, String name, boolean isS, boolean isB, boolean isO){
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
    public static CompetitorObj getInstance(Context c, int act, Bitmap image_check, Bitmap image, String num, String name, boolean isS, boolean isB, boolean isO, String time, int x, int y){
        objInstance = null;
        // check exist elements
        for (CompetitorObj existPlayer : playerMap) {
            if(existPlayer.playerNum == num){
                objInstance = existPlayer;
                break;
            }
        }

        if(objInstance == null){
            objInstance = new CompetitorObj(c, image_check, image, num, name, isS, isB, isO);
            playerMap.add(objInstance);
            Log.i(TAG, objInstance.playerNum + "Competitor player created");
        }

        objInstance.playerAct = act;
        objInstance.actTime = time;
        objInstance.xPos = x;
        objInstance.yPos = y;
        Log.i(TAG, objInstance.playerNum + "player act as" + objInstance.playerAct);
        return objInstance;
    }

}