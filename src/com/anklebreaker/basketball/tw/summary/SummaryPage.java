package com.anklebreaker.basketball.tw.summary;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.def.ActionDef;
import com.anklebreaker.basketball.tw.engine.RecordEngine;
import com.anklebreaker.basketball.tw.recordboard.CompetitorObj;
import com.anklebreaker.basketball.tw.recordboard.GameTimer;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.recordboard.TeamObj;
import com.anklebreaker.basketball.tw.util.Custom_alert_Dialog;
import com.anklebreaker.basketball.tw.util.MultiDevInit;

public class SummaryPage{

    private static final String TAG = "Scoreboard.SummaryPage";
    
    final int EXPAND_BANNER = 5;
    final String NUM_NAME_COMPETITOR = "對手";
    final String COMPETITOR = "COMPETITOR_PLAYER";
    final float CELL_TITLE_WIDTH_RATIO = 1.5f;
    final float CELL_WIDTH_RATIO = 1.0f;
    final int LEFT = 0;
    final int RIGHT = 1;
    // control flag for expand banner
    public static boolean IS_EXPAND = false;

    //default definition of img
    Integer[] imageSet = {R.drawable.basketicon, R.drawable.basketicon,R.drawable.basketicon};
    //img for icons except player icon
    Integer[] rebound = {R.drawable.r_press, R.drawable.r_d,R.drawable.r_o};
    Integer[] twoPoint = {R.drawable.two_press, R.drawable.in,R.drawable.miss};
    Integer[] threePoint = {R.drawable.three_press, R.drawable.in,R.drawable.miss};
    Integer[] freeThrow = {R.drawable.free_press, R.drawable.in,R.drawable.miss};
    Integer[] to_n_foul = {R.drawable.fail_press, R.drawable.fail_up,R.drawable.fail_down};

    Context mContext = null;
    FragmentActivity mActivity = null;
    
    //public static PlayerListAdapter select_list_adapter = null;
    private ListView mListView;
    private PlayerListAdapter def_list_adapter = null;
    //private ArrayList<PlayerObj> playerList = new ArrayList<PlayerObj>();
    private static HashMap<String, AlertDialog> dialogMap = new HashMap<String, AlertDialog>();
    
    ImageView bktCourt, summary, rival, mBall, mBallAnim, mBallAna, missIcon, testBtn;
    Button undo, settingBnt, competitor;
    private static TextView strTime, strTimeTitle, strScore, strFoul;
    private static NumberPicker qp;

    private TextView teama, teamb;
    
    ViewPager mViewPager;
    FragmentPagerAdapter mFragmentPagerAdapter;
    
    /**
     * constructor
     * */
    public SummaryPage(Context c, FragmentActivity a){
        mActivity = a;
        mContext = c;
    }

    /*
     * create the view
     * */
    public View createSummaryPage(LayoutInflater inflater){
        Log.i(TAG, "createSummaryPage S");

        final View mixedView = inflater.inflate(R.layout.summary_layout, null);
        strScore = (TextView) mixedView.findViewById(R.id.score);
        strTimeTitle = (TextView) mixedView.findViewById(R.id.timeTitle);
        strTime = (TextView)mixedView.findViewById(R.id.time);
        strFoul = (TextView)mixedView.findViewById(R.id.foul);
        mListView = (ListView) mixedView.findViewById(R.id.player_list);
        
        // set the timer
        GameTimer.getInstance(mActivity, strTime, 180000, 100);
        strTime.setOnLongClickListener(new OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                TextView title = new TextView(mActivity);
                title.setText("請輸入比賽時間");
                title.setBackgroundColor(Color.DKGRAY);
                title.setPadding(10, 10, 10, 10);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(20);

                final View rootView = LayoutInflater.from(mActivity).inflate(R.layout.time_setting, null);
                qp = (NumberPicker) rootView.findViewById(R.id.quarter);
                qp.setMaxValue(5);
                qp.setMinValue(0);
                qp.setDisplayedValues(TeamObj.qString);
                final NumberPicker npM = (NumberPicker) rootView.findViewById(R.id.npMinute);
                npM.setMinValue(0);
                npM.setMaxValue(48);
                npM.setValue(10);
                final NumberPicker npS = (NumberPicker) rootView.findViewById(R.id.npSecond);
                npS.setMinValue(0);
                npS.setMaxValue(59);
                npS.setValue(00);

                final AlertDialog defBuilder = new AlertDialog.Builder(mActivity)
                .setView(rootView)
                .setCustomTitle(title)
                .setPositiveButton(android.R.string.ok, null) //Set to null. then override the onclick
                .create();
                defBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button b = defBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!(npM.getValue() == 0 && npS.getValue() == 0)){
                                    String quarter = TeamObj.qString[qp.getValue()];
                                    strTimeTitle.setText(quarter);
                                    int min = npM.getValue();
                                    int sec = npS.getValue();
                                    //set text
                                    String tmp = min + ":" + sec;
                                    strTime.setText(tmp +":0");
                                    //set timer
                                    GameTimer.gtInstance.update(min*60*1000 + sec*1000, 100);
                                    GameTimer.gtInstance.create();
                                    defBuilder.dismiss();
                                }else{
                                    Toast.makeText(mActivity, "ok", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });
                defBuilder.show();

                return false;
            }
        });
        
        // start the timer
        strTime.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                String mTimer = strTime.getText().toString();
                if(mTimer.equals("00:00:0")){
                    CustomToast("請長按以設定比賽時間");
                }else{

                    if(GameTimer.gtInstance.isRunning()){
                        GameTimer.gtInstance.pause();
                    }else{
                        GameTimer.gtInstance.resume();
                    }
                }

            }
        });
        
        // set the listview
        mListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // change the listview by watching bench player's data or not
                if(position == EXPAND_BANNER){
                    if(IS_EXPAND){
                        // fold the listview
                        IS_EXPAND = false;
                        TeamObj.mPlayerListAdapter = new PlayerListAdapter(mActivity, PlayerObj.playerMap, IS_EXPAND);
                        mListView.setAdapter(TeamObj.mPlayerListAdapter);
                        TeamObj.mPlayerListAdapter.notifyDataSetChanged();
                    }else{
                        // expand the listview
                        IS_EXPAND = true;
                        TeamObj.mPlayerListAdapter = new PlayerListAdapter(mActivity, PlayerObj.playerMap, IS_EXPAND);
                        mListView.setAdapter(TeamObj.mPlayerListAdapter);
                        TeamObj.mPlayerListAdapter.notifyDataSetChanged();
                    }

                }else{
                    // init overlay's view param object
                    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                            PixelFormat.TRANSLUCENT);
                    
                    // get alertDialog's title
                    PlayerObj selectPlayer = PlayerObj.playerMap.get(position);
                    // create alertDialog to show the 9*9 panel
                    RecordEngine competitorDialog = new RecordEngine(mContext, mActivity, bktCourt, strTime, mListView, strScore, strFoul);
                    competitorDialog.customPlayerObjDialogInit(selectPlayer, R.layout.summarypage_record_board, mixedView, params);
                    competitorDialog = null;
                    // update the player_list's content
                    // end
                }
            }
        });

        // init when opening the app
        if(PlayerObj.playerMap.size() != 0){
            TeamObj.mPlayerListAdapter = new PlayerListAdapter(mActivity, PlayerObj.playerMap, IS_EXPAND);
            mListView.setAdapter(TeamObj.mPlayerListAdapter);
            TeamObj.mPlayerListAdapter.notifyDataSetChanged();
        }else{
            def_list_adapter = new PlayerListAdapter(mActivity, ActionDef.defaultTotalPlayer, false);
            mListView.setAdapter(def_list_adapter);	
            def_list_adapter.notifyDataSetChanged();
        }
        
        // init undo button
        initUndo(mixedView);
        
        // init competitor
        initCompetitor(mixedView);
        
        // init setting button
        initSettingBtn(mixedView);
        
        // set the header.xml layout param
        LinearLayout headerLayout = (LinearLayout) mixedView.findViewById(R.id.header);
        headerLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, MultiDevInit.headerH));
        
        Log.i(TAG, "createSummaryPage E");
        return mixedView;
    }

    
    /**
     * init undo button
     * */
    private void initUndo(View v) {
        final Animation animRotate = AnimationUtils.loadAnimation(mActivity, R.anim.anim_rotate);
        undo = (Button) v.findViewById(R.id.undo);
        // set to square shape
        LayoutParams undoP = (LayoutParams) undo.getLayoutParams();
        undoP.setMargins(10, 10, 10, 10);
        undoP.width = MultiDevInit.recordRowH;

        undo.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!TeamObj.undoStack.empty()){
                    // animation
                    v.startAnimation(animRotate);
                    RecordEngine tmp = new RecordEngine(mContext, mActivity, bktCourt, strTime, mListView, strScore, strFoul);
                    tmp.undo(v);
                    
                }else{
                    // toast
                    
                }
            }
        });
    }
    
    /**
     * init competitor button
     * */
    private void initCompetitor(View v){
        competitor = (Button) v.findViewById(R.id.competitor);
        // set to square shape
        LayoutParams competitorP = (LayoutParams) competitor.getLayoutParams();
        competitorP.setMargins(10, 10, 10, 10);
        competitorP.width = MultiDevInit.recordRowH;
        
        competitor.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // init overlay's view param object
                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                        PixelFormat.TRANSLUCENT);
                
                // get alertDialog's title
                CompetitorObj competitorPlayer = new CompetitorObj(NUM_NAME_COMPETITOR, COMPETITOR, false, false, false);
                // create alertDialog to show the 9*9 panelX
                RecordEngine competitorDialog = new RecordEngine(mContext, mActivity, bktCourt, strTime, mListView, strScore, strFoul);
                competitorDialog.CompetitorDialogInit(competitorPlayer, R.layout.summarypage_record_board, v, params);
                competitorDialog = null;
            }
        });
    }
    
    /**
     * init setting button
     * */
    private void initSettingBtn(View v){
        final Animation settingAnimRotate = AnimationUtils.loadAnimation(mActivity, R.anim.anim_rotate);
        settingBnt = (Button)v.findViewById(R.id.setting);
        // set to square shape
        LayoutParams settingP = (LayoutParams) settingBnt.getLayoutParams();
        settingP.setMargins(10, 10, 10, 10);
        settingP.width = MultiDevInit.recordRowH;
        
        settingBnt.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                v.startAnimation(settingAnimRotate);
                View dialogView = null;
                // set the 4quarter game or 2quarter game
                if(qp == null || qp.getValue() > 1){
                    dialogView = customDialogInit_Setting("設定選單", R.layout.setting_menu_def);
                    // set score of each quarter
                    TextView aFirst = (TextView) dialogView.findViewById(R.id.teama_1st);
                    TextView aSecond = (TextView) dialogView.findViewById(R.id.teama_2nd);
                    TextView aThird = (TextView) dialogView.findViewById(R.id.teama_3rd);
                    TextView aFour = (TextView) dialogView.findViewById(R.id.teama_4th);
                    
                    TextView bFirst = (TextView) dialogView.findViewById(R.id.teamb_1st);
                    TextView bSecond = (TextView) dialogView.findViewById(R.id.teamb_2nd);
                    TextView bThird = (TextView) dialogView.findViewById(R.id.teamb_3rd);
                    TextView bFour = (TextView) dialogView.findViewById(R.id.teamb_4th);
                    
                    TextView aTotal = (TextView) dialogView.findViewById(R.id.teama_total);
                    TextView bTotal = (TextView) dialogView.findViewById(R.id.teamb_total);
                    
                    // 
                    aFirst.setText(String.valueOf(TeamObj.scoreKeeper[0][0]));
                    aSecond.setText(String.valueOf(TeamObj.scoreKeeper[0][1]));
                    aThird.setText(String.valueOf(TeamObj.scoreKeeper[0][2]));
                    aFour.setText(String.valueOf(TeamObj.scoreKeeper[0][3]));
                    aTotal.setText(String.valueOf(TeamObj.scoreKeeper[0][0] + TeamObj.scoreKeeper[0][1] 
                            + TeamObj.scoreKeeper[0][2] + TeamObj.scoreKeeper[0][3]));
                    
                    bFirst.setText(String.valueOf(TeamObj.scoreKeeper[1][0]));
                    bSecond.setText(String.valueOf(TeamObj.scoreKeeper[1][1]));
                    bThird.setText(String.valueOf(TeamObj.scoreKeeper[1][2]));
                    bFour.setText(String.valueOf(TeamObj.scoreKeeper[1][3]));
                    bTotal.setText(String.valueOf(TeamObj.scoreKeeper[1][0] + TeamObj.scoreKeeper[1][1] 
                            + TeamObj.scoreKeeper[1][2] + TeamObj.scoreKeeper[1][3]));
                    
                }else{
                    dialogView = customDialogInit_Setting("設定選單", R.layout.setting_menu_half);
                    // set score of each quarter
                    TextView aFirst = (TextView) dialogView.findViewById(R.id.teama_1st);
                    TextView aSecond = (TextView) dialogView.findViewById(R.id.teama_2nd);
                    
                    TextView bFirst = (TextView) dialogView.findViewById(R.id.teamb_1st);
                    TextView bSecond = (TextView) dialogView.findViewById(R.id.teamb_2nd);
                    
                    TextView aTotal = (TextView) dialogView.findViewById(R.id.teama_total);
                    TextView bTotal = (TextView) dialogView.findViewById(R.id.teamb_total);
                    
                    aFirst.setText(String.valueOf(TeamObj.scoreKeeper[0][0]));
                    aSecond.setText(String.valueOf(TeamObj.scoreKeeper[0][1]));
                    aTotal.setText(String.valueOf(TeamObj.scoreKeeper[0][0] + TeamObj.scoreKeeper[0][1]));
                    
                    bFirst.setText(String.valueOf(TeamObj.scoreKeeper[1][0]));
                    bSecond.setText(String.valueOf(TeamObj.scoreKeeper[1][1]));
                    bTotal.setText(String.valueOf(TeamObj.scoreKeeper[1][0] + TeamObj.scoreKeeper[1][1]));
                }
                
                // set layout param for score_table
                TableLayout score_table = (TableLayout) dialogView.findViewById(R.id.score_table);
                score_table.getLayoutParams().width = MultiDevInit.xPIXEL/2;
                // set layout param for function buttons
                ImageView game_summary = (ImageView) dialogView.findViewById(R.id.game_summary);
                ImageView game_analysis = (ImageView) dialogView.findViewById(R.id.game_analysis);
                ImageView restart = (ImageView) dialogView.findViewById(R.id.restart);
                ImageView finish_upload = (ImageView) dialogView.findViewById(R.id.finish_upload);
                
                game_summary.getLayoutParams().width = MultiDevInit.yPIXEL/5;
                game_analysis.getLayoutParams().width = MultiDevInit.yPIXEL/5;
                restart.getLayoutParams().width = MultiDevInit.yPIXEL/5;
                finish_upload.getLayoutParams().width = MultiDevInit.yPIXEL/5;
                
                // set current score
                TextView myteam = (TextView)dialogView.findViewById(R.id.myteam);
                TextView competitor = (TextView)dialogView.findViewById(R.id.competitor);
                myteam.setText(((String)strScore.getText()).split(":")[0]);
                competitor.setText(((String)strScore.getText()).split(":")[1]);
                
                // set name of each team
                final TextView myteam_title = (TextView)dialogView.findViewById(R.id.myteam_title);
                final TextView competitor_title = (TextView)dialogView.findViewById(R.id.competitor_title);
                
                myteam_title.getLayoutParams().width = MultiDevInit.xPIXEL/6;
                competitor_title.getLayoutParams().width = MultiDevInit.xPIXEL/6;
                
                myteam_title.setText(TeamObj.teamName[0]);
                competitor_title.setText(TeamObj.teamName[1]);
                
                // set name of each team in table
                teama = (TextView)dialogView.findViewById(R.id.teama);
                teamb = (TextView)dialogView.findViewById(R.id.teamb);
                
                teama.setText(TeamObj.teamName[0]);
                teamb.setText(TeamObj.teamName[1]);
                
                myteam_title.setOnClickListener(new OnClickListener(){
                    public void onClick(View v) {
                        customDialogInit_teamname_change(v, "隊名設定", R.layout.modify_team_name, android.R.string.ok, LEFT);
                    }
                });
                
                competitor_title.setOnClickListener(new OnClickListener(){
                    public void onClick(View v) {
                        customDialogInit_teamname_change(v, "隊名設定", R.layout.modify_team_name, android.R.string.ok, RIGHT);
                    }
                });
                
                
                // set functions
                game_summary.setOnClickListener(new OnClickListener(){
                    public void onClick(View v) {
                        Intent myIntent = new Intent(mContext, GameSummaryActivity.class);
                        mContext.startActivity(myIntent);
                    }
                });
                
                game_analysis.setOnClickListener(new OnClickListener(){
                    public void onClick(View v) {
                        //Toast.makeText(mContext, "game_analysis", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(mContext, GameAnalysisActivity.class);
                        mContext.startActivity(myIntent);
                    }
                });
                
                restart.setOnClickListener(new OnClickListener(){
                    public void onClick(View v) {
                        Custom_alert_Dialog cdd= new Custom_alert_Dialog(mActivity, mListView);
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        cdd.show();
                    }
                });
                
                finish_upload.setOnClickListener(new OnClickListener(){
                    public void onClick(View v) {
                        //Toast.makeText(mContext, "finish_upload", Toast.LENGTH_SHORT).show();
                        UploadAsyncTask task = new UploadAsyncTask();
                        task.execute();
                    }
                });
                
            }
        });
    }
    
    
    
    /**
     * init customized dialog without button
     * */
    public View customDialogInit_Setting(String mTitle, int layoutId) {
        TextView title = new TextView(mActivity);
        title.setText(mTitle);
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        final View rootView = LayoutInflater.from(mActivity).inflate(layoutId, null);

        final AlertDialog defBuilder = new AlertDialog.Builder(mActivity)
        .setView(rootView)
        .setCustomTitle(title)
        .create();
        defBuilder.show();
        
        // put dialog object to the map for managing
        dialogMap.put("setting", defBuilder);
        
        return rootView;
    }

    /**
     * init customized dialog with button
     * */
    public View customDialogInit(String mTitle, int layoutId, int bntName) {
        TextView title = new TextView(mActivity);
        title.setText(mTitle);
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        final View rootView = LayoutInflater.from(mActivity).inflate(layoutId, null);

        final AlertDialog defBuilder = new AlertDialog.Builder(mActivity)
        .setView(rootView)
        .setCustomTitle(title)
        .setPositiveButton(bntName, null) //Set to null. then override the onclick
        .create();
        
        defBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = defBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        defBuilder.dismiss();
                    }
                });
            }
        });
        defBuilder.show();
        
        return rootView;
    }
    
    /**
     * init customized dialog with button
     * */
    public View customDialogInit_teamname_change(final View parentv, String mTitle, int layoutId, int bntName, final int position) {
        TextView title = new TextView(mActivity);
        title.setText(mTitle);
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        final View rootView = LayoutInflater.from(mActivity).inflate(layoutId, null);

        final AlertDialog defBuilder = new AlertDialog.Builder(mActivity)
        .setView(rootView)
        .setCustomTitle(title)
        .setPositiveButton(bntName, null) //Set to null. then override the onclick
        .create();
        
        defBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = defBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText newTeamName = (EditText) rootView.findViewById(R.id.changeTeamName);
                        ((TextView)parentv).setText(newTeamName.getText());
                        if(position == LEFT){
                            TeamObj.teamName[0] = newTeamName.getText().toString();
                            teama.setText(newTeamName.getText().toString());
                        }else{
                            TeamObj.teamName[1] = newTeamName.getText().toString();
                            teamb.setText(newTeamName.getText().toString());
                        }
                        defBuilder.dismiss();
                    }
                });
            }
        });
        defBuilder.show();
        
        return rootView;
    }
    
    
    
    /**
     * toast show in 0.5 second method
     * */
    private void CustomToast(String str) {
        final Toast toast = Toast.makeText(mActivity, str, Toast.LENGTH_SHORT);
        // set the position
        toast.setGravity(Gravity.TOP, 0, 80);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {toast.cancel();}
        }, 500);
    }
    
    /**
     * 
     * */
    public static void dismissDialog(String name){
        AlertDialog tmp = dialogMap.remove(name);
        tmp.dismiss();
    }
    
    /**
     * reset foul record
     * */
    public static void resetScore(){
        strScore.setText("0:0");
    }
    
    /**
     * reset score record
     * */
    public static void resetFoul(){
        strFoul.setText("0:0");
    }
    
    /**
     * reset score timer
     * */
    public static void resetTimer(){
        GameTimer.gtInstance.update(0*60*1000 + 0*1000, 100);
        GameTimer.gtInstance.create();
    }
    
    /**
     * return the qp
     * */
    public static NumberPicker getQp(){
        return qp;
    }
    
}
