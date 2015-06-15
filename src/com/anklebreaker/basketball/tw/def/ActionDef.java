package com.anklebreaker.basketball.tw.def;

import java.util.ArrayList;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;

public class ActionDef {

    //------------------------
    //init defaultStarters
    //------------------------
    @SuppressWarnings("serial")
    public static final ArrayList<PlayerObj> defaultStarters = new ArrayList<PlayerObj>(){{
        add(new PlayerObj("11", "Micheal Jordan", true, false, true));
        add(new PlayerObj("22", "Larry Bird", true, false, true));
        add(new PlayerObj("33", "Hardaway", true, false, true));
        add(new PlayerObj("44", "Morning", true, false, true));
        add(new PlayerObj("55", "Payton", true, false, true));
        // dummy player for banner
        add(new PlayerObj("999", "DUMMY_PLAYER", false, false, false));
    }};

    //------------------------
    //init defaultTotalPlayer
    //------------------------
    @SuppressWarnings("serial")
    public static final ArrayList<PlayerObj> defaultTotalPlayer = new ArrayList<PlayerObj>(){{
        add(new PlayerObj("11", "Micheal Jordan", true, false, true));
        add(new PlayerObj("22", "Larry Bird", true, false, true));
        add(new PlayerObj("33", "Hardaway", true, false, true));
        add(new PlayerObj("44", "Morning", true, false, true));
        add(new PlayerObj("55", "Payton", true, false, true));
        // dummy player for banner
        add(new PlayerObj("999", "DUMMY_PLAYER", false, false, false));
        // bench player
        add(new PlayerObj("66", "BENCH_UNKNOWN", false, true, false));
        add(new PlayerObj("77", "BENCH_UNKNOWN", false, true, false));
        add(new PlayerObj("88", "BENCH_UNKNOWN", false, true, false));
        add(new PlayerObj("99", "BENCH_UNKNOWN", false, true, false));
        add(new PlayerObj("10", "BENCH_UNKNOWN", false, true, false));
        add(new PlayerObj("11", "BENCH_UNKNOWN", false, true, false));
        add(new PlayerObj("12", "BENCH_UNKNOWN", false, true, false));
    }};

	//player info
	public static final int PLAYER_NUMBER = 0;
	public static final int PLAYER_NAME = 1;
	//2 point
	public static final int ACT_TWOP_MA = 2;
	public static final int ACT_TWOP_MI = 3;
	//3 point
	public static final int ACT_THREEP_MA = 4;
	public static final int ACT_THREEP_MI = 5;
	//free throw
	public static final int ACT_FTMA = 6;
	public static final int ACT_FTMI = 7;
	//rebound
	public static final int ACT_DR = 8;
	public static final int ACT_OR = 9;
	//assist
	public static final int ACT_AS = 10;
	//block
	public static final int ACT_BS = 11;
	//steal
	public static final int ACT_ST = 12;
	//turnover
	public static final int ACT_TO = 13;
	//foul
	public static final int ACT_FOUL = 14;
	//total score
	public static final int TOTAL_SCORE = 15;
	//efficient
	public static final int EFF = 16;
	//actions except 2 points, 3 points and free throw using by UNDO function
	public static final int UNDO_OTHERS = 17;

	//-------------------------------------
	//2 point
	public static final String ACT_strTWOP_MA = "兩分進帳";
	public static final String ACT_strTWOP_MI = "兩分沒進";
	//3 point
	public static final String ACT_strTHREEP_MA = "三分進帳";
	public static final String ACT_strTHREEP_MI = "三分沒進";
	//free throw
	public static final String ACT_strFTMA = "穩罰中一";
	public static final String ACT_strFTMI = "罰球不進";
	//rebound
	public static final String ACT_strDR = "怒拉一防守籃板";
	public static final String ACT_strOR = "怒拉一進攻籃板";
	//assist
	public static final String ACT_strAS = "妙傳助攻";
	//block
	public static final String ACT_strBS = "送出火鍋一次";
	//steal
	public static final String ACT_strST = "抄截加一";
	//turnover
	public static final String ACT_strTO = "失誤一次";
	//foul
	public static final String ACT_strFOUL = "犯規一次";

}
