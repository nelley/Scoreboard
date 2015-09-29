package com.anklebreaker.basketball.tw.recordboard;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;

public class Player {

	private static final String TAG = "RecordBoard.Player";
    private static final int DIFF = 1;

    //HashMap for managing the players
    public static ArrayList<PlayerObj> playerMap = new ArrayList<PlayerObj>();
    public static PlayerObj objInstance = null;

    public Context mContext = null;
    protected String playerNum =null;
    protected String playerName = null;
    public String actTime = null;
    protected int quarter = -1;
	protected int xPos = 0;
    protected int yPos = 0;
    public int playerAct = -9;


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
     * clone for adding to the timeline
     * */
    @Override
    protected Player clone() throws CloneNotSupportedException {
        return (Player) super.clone();
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

    public int getQuarter() {
		return quarter;
	}
}
