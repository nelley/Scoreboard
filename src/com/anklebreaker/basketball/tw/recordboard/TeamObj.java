package com.anklebreaker.basketball.tw.recordboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.summary.PlayerListAdapter;
import com.anklebreaker.basketball.tw.summary.RecordBoardBtn;
import com.anklebreaker.basketball.tw.tab.BasketFragment;
import com.anklebreaker.basketball.tw.util.StarterComparator;

/**
 * manage the bench player and starters
 * */
public class TeamObj {

	private static final String TAG = "ScoreBoard.TeamObj";

    static TeamObj ObjInstance = null;
    static final int CT_PANEL = 9;
    static final int CT_PANEL_STARTER = 5;
    static final int CT_PANEL_BENCH = 7;
    
    public static final String DUMMY_PLAYER = "DUMMY_PLAYER";
    public static final String NUM_DUMMY_PLAYER = "-1";
    
    private Context mContext = null;
    //undo stack
    public static Stack<Player> undoStack = new Stack<Player>();
    //3*3 record board
    public static ArrayList<RecordBoardBtn> gridArray = new ArrayList<RecordBoardBtn>();

    // playerList adapter
    public static PlayerListAdapter mPlayerListAdapter = null;

    // array for keeping score
    public static int[][] scoreKeeper = new int[][]{
        {0,0,0,0},// home team
        {0,0,0,0}// away team
    };
    
    public static final String[] qString = new String[]{"上半場", "下半場", "第一節", "第二節", "第三節", "第四節"};
    public static String[] teamName= new String[]{"己隊", "敵隊"};
    
    //pref
    public static final String PLAYER_FILE_NAME = "players";
    public static final String[] PLAYER_POS = new String[21];
    //
    int[] icons = {R.drawable.r, R.drawable.two, R.drawable.three,
                   R.drawable.free, R.drawable.nonplayer, R.drawable.fail,
                   R.drawable.block, R.drawable.steal, R.drawable.a};
    int[] lightIcons = {R.drawable.rr, R.drawable.lighttwo, R.drawable.lightthree,
                        R.drawable.freef, R.drawable.nonplayer, R.drawable.failf,
                        R.drawable.blockb, R.drawable.steals, R.drawable.aa};

    //
    Bitmap[] BitmapArray = new Bitmap[9];
    Bitmap[] LBitmapArray = new Bitmap[9];
    //text
    String[] textArray = {"","","",
                          "","","",
                          "","",""};
    //view
    //public static int[] gridViewID = {R.id.gridPlayer1,R.id.gridPlayer2,
    //                                  R.id.gridPlayer3,R.id.gridPlayer4, R.id.gridPlayer5};
    /**
     * constructor
     * */
    public TeamObj(Context c){
        mContext = c;
    }
    //singleton
    static public TeamObj getInstance(Context c){
        if(ObjInstance == null){
            ObjInstance = new TeamObj(c);
            ObjInstance.initialize();
        }
        return ObjInstance;
    }
    /**
     * Initialization
     * */
    public void initialize(){
        //initialize the icons
        Resources res = mContext.getResources();
        for(int i=0; i< CT_PANEL; i++){
            BitmapArray[i] = BitmapFactory.decodeResource(res, icons[i]);
            LBitmapArray[i] = BitmapFactory.decodeResource(res, lightIcons[i]);
        }

        //set the 3*3 gridview's icon and text
        for(int k=0; k< CT_PANEL; k++){
            gridArray.add(new RecordBoardBtn(BitmapArray[k], textArray[k]));
        }
    }

    /**
     * set starter and bench player dialog's method
     * */
    static public String setByUser(View v, Context mContext){
        int startCnt = 0;
        int benchCnt = 0;
        String results = null;
        // loop all players in the setting panel
        for(int i = 0; i< BasketFragment.player_settingGrid.size(); i++){
            PlayerObj mPlayer = BasketFragment.player_settingGrid.get(i);
            if(mPlayer.getIsBench()){
                if(mPlayer.getIsStarter()){
                    // add to PlayerObj's playermap(starters)
                    PlayerObj.getInstance(mContext, 9999, null, null, 
                                          mPlayer.playerNum, mPlayer.playerName, 
                                          true, false, true, null, -999, -999, -999);
                    startCnt = startCnt + 1;
                    
                }else{
                    // add to PlayerObj's playermap(bench players)
                    PlayerObj.getInstance(mContext, 9999, null, null, 
                            mPlayer.playerNum, mPlayer.playerName, 
                            false, true, false, null, -999, -999, -999);
                    benchCnt = benchCnt + 1;
                }
            }
        }
        
        if((startCnt + benchCnt) > 12){
            results = "可登錄球員上限為十二人";
            PlayerObj.playerMap.clear();
        }else if(startCnt < 5){
            results = "請選五位球員為先發";
            PlayerObj.playerMap.clear();
        }else if(startCnt > 5){
            results = "先發球員不得超過五位";
            PlayerObj.playerMap.clear();
        }else{
            //-----------------------------X
            //update info to bench/starter players
            //-----------------------------
            // add expand banner
            PlayerObj.playerMap.add(new PlayerObj(NUM_DUMMY_PLAYER, DUMMY_PLAYER, false, false, false));
            // sort by isStarter flag & isBench(Starters + DUMMY_PLAYER + Benches)
            Collections.sort(PlayerObj.playerMap, new StarterComparator());
            // set starters into listview(init)
            mPlayerListAdapter = new PlayerListAdapter((Activity) mContext, PlayerObj.playerMap, false);

            //check selected player's number!!!
            View rootView = ((Activity) mContext).getWindow().getDecorView().findViewById(android.R.id.content);

            //for starters
            ListView startList = (ListView)rootView.findViewById(R.id.player_list);

            //notify data changed
            startList.setAdapter(mPlayerListAdapter);
            mPlayerListAdapter.notifyDataSetChanged();

            //-----------------------------
            // store info of players to local storage with no duplicate number
            //-----------------------------
            SharedPreferences pref = mContext.getSharedPreferences(PLAYER_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            for(int i = 0; i < PlayerObj.playerMap.size(); i++){
                if(PlayerObj.playerMap.get(i).getPlayerNum() != "-1"){
                    editor.putString(PlayerObj.playerMap.get(i).getPlayerNum(), PlayerObj.playerMap.get(i).getPlayerNum());
                }
            }
            editor.commit();
            
            results = "ok";
        }
        return results;
    }
    /**
     * 
     * */
    static public void addTimeLine(Player tmpObj) {
        //update undolist by clone obj
        try {
            undoStack.push(tmpObj.clone());
            Log.i(TAG, tmpObj.toString() + "is pushed to the top of stack");
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * */
    static public void resetScoreKeeper(){
        scoreKeeper = new int[][]{
                {0,0,0,0},// home team
                {0,0,0,0}// away team
            };
    }
    
    /**
     * 
     * */
    static public void resetTeamName(){
        teamName= new String[]{"主隊", "客隊"};
    }
}
