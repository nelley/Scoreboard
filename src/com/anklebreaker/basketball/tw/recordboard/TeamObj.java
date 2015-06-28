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
    private Context mContext = null;
    //undo stack
    public static Stack<PlayerObj> undoStack = new Stack<PlayerObj>();
    //3*3 record board
    public static ArrayList<RecordBoardBtn> gridArray = new ArrayList<RecordBoardBtn>();
    //adapters:0~4 is GridViewAdapter, 5 is benchGridAdapter
    public static RecordGridViewAdapter RecordGVAdapter = null;

    // playerList adapter
    public static PlayerListAdapter mPlayerListAdapter = null;

    // playerObj arraylist
    public static ArrayList<PlayerObj> selectedBenches = new ArrayList<PlayerObj>();
    public static ArrayList<PlayerObj> selectedStarters = new ArrayList<PlayerObj>();
    public static ArrayList<PlayerObj> totalPlayerList= new ArrayList<PlayerObj>();

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
    public static int[] gridViewID = {R.id.gridPlayer1,R.id.gridPlayer2,
                                      R.id.gridPlayer3,R.id.gridPlayer4, R.id.gridPlayer5};
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

        RecordGVAdapter = new RecordGridViewAdapter(mContext, R.layout.row_grid, gridArray);

    }

    /**
     * set starter and bench player dialog's method
     * */
    static public String setByUser(View v, Context mContext){
        String results = null;
        // loop all players in the setting panel
        for(int i = 0; i< BasketFragment.player_settingGrid.size(); i++){
            PlayerObj mPlayer = BasketFragment.player_settingGrid.get(i);
            if(mPlayer.getIsBench()){
                if(mPlayer.getIsStarter()){
                    //starters
                    selectedStarters.add(mPlayer);
                    // add to PlayerObj's playermap
                    PlayerObj.getInstance(mContext, 9999, null, null, 
                                          mPlayer.playerNum, mPlayer.playerName, 
                                          true, false, true, null, -999, -999);
                }else{
                    //bench players
                    selectedBenches.add(mPlayer);
                    // add to PlayerObj's playermap
                    PlayerObj.getInstance(mContext, 9999, null, null, 
                            mPlayer.playerNum, mPlayer.playerName, 
                            false, true, false, null, -999, -999);
                }
            }
        }

        if((selectedBenches.size() + selectedStarters.size())< 5){
            results = "請選擇足夠的球員參加比賽";
            selectedBenches.clear();
            selectedStarters.clear();
        }else if((selectedBenches.size() + selectedStarters.size()) > 12){
            results = "可登錄球員上限為十二人";
            selectedBenches.clear();
            selectedStarters.clear();
        }else if(selectedStarters.size() < 5){
            results = "請選五位球員為先發";
            selectedBenches.clear();
            selectedStarters.clear();
        }else if(selectedStarters.size() > 5){
            results = "先發球員不得超過五位";
            selectedBenches.clear();
            selectedStarters.clear();
        }else{
            //-----------------------------
            //update info to bench/starter players
            //-----------------------------
            // combine two starterlist and player list
            totalPlayerList.addAll(selectedStarters);
            totalPlayerList.addAll(selectedBenches);
            // add expand banner
            totalPlayerList.add(new PlayerObj("999", "DUMMY_PLAYER", false, false, false));
            selectedStarters.add(new PlayerObj("999", "DUMMY_PLAYER", false, false, false));
            // sort by isStarter flag & isBench(Starters + DUMMY_PLAYER + Benches)
            Collections.sort(totalPlayerList, new StarterComparator());
            Collections.sort(selectedStarters, new StarterComparator());
            // set starters into listview(init)
            mPlayerListAdapter = new PlayerListAdapter((Activity) mContext, selectedStarters);

            //check selected player's number!!!
            View rootView = ((Activity) mContext).getWindow().getDecorView().findViewById(android.R.id.content);

            //for starters
            ListView startList = (ListView)rootView.findViewById(R.id.player_list);

            //notify data changed
            startList.setAdapter(mPlayerListAdapter);
            mPlayerListAdapter.notifyDataSetChanged();

            //-----------------------------
            //store info of players to local storage with no duplicate number
            //-----------------------------
            SharedPreferences pref = mContext.getSharedPreferences(PLAYER_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            int i = 0;
            for(; i<selectedStarters.size(); i++){
                editor.putString(PLAYER_POS[i], selectedStarters.get(i).getPlayerNum());
            }
            for(int k=0; k<selectedBenches.size(); k++){
                editor.putString(PLAYER_POS[i+k], selectedBenches.get(k).getPlayerNum());
            }
            editor.commit();
            results = "ok";
        }
        return results;
    }
    /**
     *
     * */
    static public void addTimeLine(PlayerObj tmpObj) {
        //update undolist by clone obj
        try {
            undoStack.push(tmpObj.clone());
            Log.i(TAG, tmpObj.toString() + "is pushed to the top of stack");
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
