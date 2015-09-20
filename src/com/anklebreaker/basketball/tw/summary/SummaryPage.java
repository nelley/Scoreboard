package com.anklebreaker.basketball.tw.summary;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.def.ActionDef;
import com.anklebreaker.basketball.tw.engine.RecordEngine;
import com.anklebreaker.basketball.tw.recordboard.CompetitorObj;
import com.anklebreaker.basketball.tw.recordboard.GameTimer;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.recordboard.TeamObj;
import com.anklebreaker.basketball.tw.util.MultiDevInit;

public class SummaryPage{

    private static final String TAG = "Scoreboard.SummaryPage";
    
    final int EXPAND_BANNER = 5;
    final String NUM_NAME_COMPETITOR = "對手";
    final String COMPETITOR = "COMPETITOR_PLAYER";

    // control flag for expand banner
    static public boolean IS_EXPAND = false;

    //default definition of img
    Integer[] imageSet = {R.drawable.basketicon, R.drawable.basketicon,R.drawable.basketicon};
    //img for icons except player icon
    Integer[] rebound = {R.drawable.r_press, R.drawable.r_d,R.drawable.r_o};
    Integer[] twoPoint = {R.drawable.two_press, R.drawable.in,R.drawable.miss};
    Integer[] threePoint = {R.drawable.three_press, R.drawable.in,R.drawable.miss};
    Integer[] freeThrow = {R.drawable.free_press, R.drawable.in,R.drawable.miss};
    Integer[] to_n_foul = {R.drawable.fail_press, R.drawable.fail_up,R.drawable.fail_down};

    Context mContext = null;
    Activity mActivity = null;
    
    public static PlayerListAdapter select_list_adapter = null;
    private ListView mListView;
    private PlayerListAdapter def_list_adapter = null;
    ArrayList<PlayerObj> playerList = new ArrayList<PlayerObj>();
    
    ImageView bktCourt, summary, rival, mBall, mBallAnim, mBallAna, missIcon, testBtn;
    Button undo, settingBnt, competitor;
    private TextView strTime, strTimeTitle, strScore, strFoul;
    final String[] qString = new String[]{"上半場", "下半場", "第一節", "第二節", "第三節", "第四節"};

    /**
     * constructor
     * */
    public SummaryPage(Context c, Activity a){
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
                final NumberPicker qp = (NumberPicker) rootView.findViewById(R.id.quarter);
                qp.setMaxValue(5);
                qp.setMinValue(0);
                qp.setDisplayedValues(qString);
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
                                    String quarter = qString[qp.getValue()];
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
                        select_list_adapter = new PlayerListAdapter(mActivity, PlayerObj.playerMap, IS_EXPAND);
                        mListView.setAdapter(select_list_adapter);
                        select_list_adapter.notifyDataSetChanged();
                    }else{
                        // expand the listview
                        IS_EXPAND = true;
                        select_list_adapter = new PlayerListAdapter(mActivity, PlayerObj.playerMap, IS_EXPAND);
                        mListView.setAdapter(select_list_adapter);
                        select_list_adapter.notifyDataSetChanged();
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
            select_list_adapter = new PlayerListAdapter(mActivity, PlayerObj.playerMap, IS_EXPAND);
            mListView.setAdapter(select_list_adapter);
            select_list_adapter.notifyDataSetChanged();
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
        
        // set setting and undo icon
        //Button undoBut = (Button)mixedView.findViewById(R.id.undo);
        //Button settingBut = (Button)mixedView.findViewById(R.id.setting);
        
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
                
            }
        });
    }
    
    
    
    /**
     * init customized dialog
     * */
    public void customDialogInit(String mTitle, int layoutId, int bntName) {
        TextView title = new TextView(mActivity);
        title.setText(mTitle);
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(0, 0, 0, 0);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        final View rootView = LayoutInflater.from(mActivity).inflate(layoutId, null);
        //ImageView bktCourt = (ImageView) rootView.findViewById(R.id.bktCourt);
        //bktCourt.getLayoutParams().height = MultiDevInit.bktCourtH;
        //bktCourt.getLayoutParams().width = MultiDevInit.bktCourtW;

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
}
