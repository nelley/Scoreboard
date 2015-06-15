package com.anklebreaker.basketball.tw.recordboard;

import java.util.HashMap;

import com.anklebreaker.basketball.tw.def.ActionDef;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * manage the individual player's record
 * */
public class PlayerObj implements Cloneable{

    private static final String TAG = "RecordBoard.PlayerObj";
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


    protected Bitmap image_check;
    protected Bitmap image;
    //protected String title;
    protected Boolean isStarter = false;
    protected Boolean isBench = false;
    protected Boolean isOnPlay = false;


    public String[] recordsArray =
        {"number"/*號碼*/,"name"/*球員姓名*/,"0"/*兩分命中*/,"0"/*兩分出手*/,
         "0"/*三分命中*/,"0"/*三分出手*/,"0"/*罰球命中*/,"0"/*罰球出手*/,
         "0"/*防守籃板*/,"0"/*進攻籃板*/,"0"/*助攻*/,"0"/*火鍋*/,
         "0"/*抄截*/,"0"/*失誤*/,"0"/*犯規*/,"0"/*總得分*/};

    /**
     * Constructor for default ScoreBoard View
     * */
    public PlayerObj(String num, String name, boolean isS, boolean isB, boolean isO){
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
    public PlayerObj(Context c, Bitmap image_check, Bitmap image, String num, String name, boolean isS, boolean isB, boolean isO){
        this.image_check = image_check;
        this.image = image;
        this.mContext = c;
        this.playerNum = num;
        this.playerName = name;
        this.isStarter = isS;
        this.isBench = isB;
        this.isOnPlay = isO;
    }

    public static PlayerObj getInstance(Context c, int act, Bitmap image_check, Bitmap image, String num, String name, boolean isS, boolean isB, boolean isO, String time, int x, int y){
        objInstance = playerMap.get(num);

        if(objInstance == null){
            objInstance = new PlayerObj(c, image_check, image, num, name, isS, isB, isO);
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

    /**
     * setter and getter
     * */
	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public Boolean getIsStarter() {
		return isStarter;
	}

	public void setIsStarter(Boolean isStarter) {
		this.isStarter = isStarter;
	}

	public Boolean getIsBench() {
		return isBench;
	}
	public void setIsBench(Boolean isBench) {
		this.isBench = isBench;
	}

	public Bitmap getImage_check() {
		return image_check;
	}
	public void setImage_check(Bitmap image_check) {
		this.image_check = image_check;
	}

	public Boolean getIsOnPlay() {
		return isOnPlay;
	}

	public void setIsOnPlay(Boolean isOnPlay) {
		this.isOnPlay = isOnPlay;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerNum() {
		return playerNum;
	}

	public void setPlayerNum(String playerNum) {
		this.playerNum = playerNum;
	}

}
