package com.anklebreaker.basketball.tw.engine;

import java.util.ArrayList;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.animation.AnimatorPath;
import com.anklebreaker.basketball.tw.animation.PathEvaluator;
import com.anklebreaker.basketball.tw.animation.PathPoint;
import com.anklebreaker.basketball.tw.def.ActionDef;
import com.anklebreaker.basketball.tw.recordboard.CompetitorObj;
import com.anklebreaker.basketball.tw.recordboard.Player;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.recordboard.TeamObj;
import com.anklebreaker.basketball.tw.summary.PlayerListAdapter;
import com.anklebreaker.basketball.tw.summary.SummaryPage;
import com.anklebreaker.basketball.tw.util.MultiDevInit;
import com.anklebreaker.basketball.tw.util.Utilities;

public class RecordEngine {

    private Activity mActivity = null;
    private Context mContext = null;
    
    private ImageView bktCourt, mBall;
    private ViewGroup mRelativeBkt = null;
    
    final int DEFAULT_ACTION = 999;
    final int TOUCH_OK = 1;
    final int DISTANCE = 40;
    final int RIVAL_ACTION = 17;
    final int DEFAULT_X = 0;
    final int DEFAULT_Y = 0;
    final int EXPAND_BANNER = 5;
    
    final String NUM_NAME_COMPETITOR = "對手";
    final String COMPETITOR = "COMPETITOR_PLAYER";

    // control flag for expand banner
    static public boolean IS_EXPAND = false;

    final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    final int MP = ViewGroup.LayoutParams.MATCH_PARENT;

    private ImageView mFloat = null ;
    private boolean touch_flg = false;

    //default definition of img
    Integer[] imageSet = {R.drawable.basketicon, R.drawable.basketicon,R.drawable.basketicon};
    //img for icons except player icon
    Integer[] rebound = {R.drawable.r_press, R.drawable.r_d,R.drawable.r_o};
    Integer[] twoPoint = {R.drawable.two_press, R.drawable.in,R.drawable.miss};
    Integer[] threePoint = {R.drawable.three_press, R.drawable.in,R.drawable.miss};
    Integer[] freeThrow = {R.drawable.free_press, R.drawable.in,R.drawable.miss};
    Integer[] to_n_foul = {R.drawable.fail_press, R.drawable.fail_up,R.drawable.fail_down};

    //using in onTouchListener
    private float lastTouchX;
    private float lastTouchY;
    private float currentX;
    private float currentY;
    /*get the position relative to its parent*/
    private int xInterval;
    private int yInterval;
    
    private int tmpQuarter = -1;
    /**/
    int lastPos = -1;
    
    private int actionCode = 999;
    private String ActText = "720度轉身扣籃";

    public static PlayerListAdapter select_list_adapter = null;
    private ListView mListView;
    //private PlayerListAdapter def_list_adapter = null;
    ArrayList<PlayerObj> playerList = new ArrayList<PlayerObj>();
    
    // new add
    final GridView[] pPanel = new GridView[5];

    ImageView summary, rival, mBallAnim, mBallAna, missIcon, testBtn;
    Button undo, settingBnt, competitor;
    private String actTime;
    private TextView strTime, strScore, strFoul;
    final String[] qString = new String[]{"上半場", "下半場", "第一節", "第二節", "第三節", "第四節"};
    private float midX, midY, disX, disY;
    //for animation
    AnimatorPath path = null;

    WindowManager wm;
    View floatView = null;
    
    public RecordEngine(Context c, Activity act, ImageView bktC, 
            TextView st, ListView mlv, TextView sS, TextView sF){
        this.mContext = c;
        this.mActivity = act;
        this.bktCourt = bktC;
        this.strTime = st;
        this.mListView = mlv;
        this.strScore = sS;
        this.strFoul = sF;
    }
    
    /**
     * 
     * */
    /**
     * initialize engine of record board
     * */
    public void customPlayerObjDialogInit(final PlayerObj sPlayer, int layoutId, final View mixedV, 
            final WindowManager.LayoutParams xParams){
        
        TextView title = new TextView(mActivity);
        title.setText(sPlayer.getPlayerNum());
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        final View rootView = LayoutInflater.from(mActivity).inflate(layoutId, null);
        ImageView twoBtn = (ImageView)rootView.findViewById(R.id.two);
        ImageView freeBtn = (ImageView)rootView.findViewById(R.id.free);
        ImageView blockBtn = (ImageView)rootView.findViewById(R.id.block);
        ImageView failBtn = (ImageView)rootView.findViewById(R.id.fail);
        
        ImageView threeBtn = (ImageView)rootView.findViewById(R.id.three);
        ImageView reboundBtn = (ImageView)rootView.findViewById(R.id.rebound);
        ImageView assistBtn = (ImageView)rootView.findViewById(R.id.assist);
        ImageView stealBtn = (ImageView)rootView.findViewById(R.id.steal);
        
        bktCourt = (ImageView) rootView.findViewById(R.id.bktCourt);
        bktCourt.getLayoutParams().height = MultiDevInit.bktCourtH;
        bktCourt.getLayoutParams().width = MultiDevInit.bktCourtW;
        
        // get the bktCourt's position relative to its parent 
        bktCourt.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                xInterval = bktCourt.getLeft();
                yInterval = bktCourt.getTop();
            }
        });
        
        mBall = imageViewFactory(mBall, R.layout.ball, R.id.ballIV);
        
        bktCourt.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                mRelativeBkt = (ViewGroup) v.getParent();
                ImageView AddedView = (ImageView) mRelativeBkt.findViewById(R.id.ballIV);
                int isExist = mRelativeBkt.indexOfChild(AddedView);
                if(isExist != -1){
                    mRelativeBkt.removeView(AddedView);
                }
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(WC, WC);
                // event.getX is get the x position from bktCourt
                params.leftMargin = (int) event.getX() + xInterval;
                params.topMargin = (int) event.getY() + yInterval;
                mRelativeBkt.addView(mBall, params);
                //view added flg
                touch_flg = true;
                
                animFactory(params.leftMargin, params.topMargin);
                return false;
            }
            /**
             * set the destination and middle path of the animation
             * */
            private void animFactory(float tx, float ty) {
                
                //ring's xy minus touched xy
                disX = (xInterval + (MultiDevInit.bktCourtW/2)) - tx;
                disY = yInterval - ty;
                
                //cal for middle diff
                midX = (float) (disX * 0.5);
                midY = (float) (disY * 1.2);
                
                if(midX > 0){//left side
                    midX = midX - 100;
                    midY = midY - 100;
                }else{//right side
                    midX = midX + 100;
                    midY = midY + 100;
                }
            }
        });
        
        // set the touch event
        twoBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecord(mixedV, event, 1, xParams, sPlayer);
                return true;
            }
        });
        freeBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecord(mixedV, event, 3, xParams, sPlayer);
                return true;
            }
        	
        });
        blockBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecord(mixedV, event, 6, xParams, sPlayer);
                return true;
            }
        	
        });
        failBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecord(mixedV, event, 5, xParams, sPlayer);
                return true;
            }
        	
        });
        
        threeBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecord(mixedV, event, 2, xParams, sPlayer);
                return true;
            }
        	
        });
        reboundBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecord(mixedV, event, 0, xParams, sPlayer);
                return true;
            }
        	
        });
        assistBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecord(mixedV, event, 8, xParams, sPlayer);
                return true;
            }
        	
        });
        stealBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecord(mixedV, event, 7, xParams, sPlayer);
                return true;
            }
        	
        });

        final AlertDialog recDialogBuilder = new AlertDialog.Builder(mActivity)
        .setView(rootView)
        .setCustomTitle(title)
        .create();
        
        recDialogBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = recDialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recDialogBuilder.dismiss();
                    }
                });
            }
        });

        recDialogBuilder.show();
        //defBuilder.getWindow().setLayout(800, 600);
    }
    
    /**
     * init record board for Competitor
     * */
    public void CompetitorDialogInit(final CompetitorObj sPlayer, int layoutId, final View mixedV, 
            final WindowManager.LayoutParams xParams){
        TextView title = new TextView(mActivity);
        title.setText(sPlayer.getPlayerNum());
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        final View rootView = LayoutInflater.from(mActivity).inflate(layoutId, null);
        ImageView twoBtn = (ImageView)rootView.findViewById(R.id.two);
        ImageView freeBtn = (ImageView)rootView.findViewById(R.id.free);
        ImageView blockBtn = (ImageView)rootView.findViewById(R.id.block);
        ImageView failBtn = (ImageView)rootView.findViewById(R.id.fail);
        
        ImageView threeBtn = (ImageView)rootView.findViewById(R.id.three);
        ImageView reboundBtn = (ImageView)rootView.findViewById(R.id.rebound);
        ImageView assistBtn = (ImageView)rootView.findViewById(R.id.assist);
        ImageView stealBtn = (ImageView)rootView.findViewById(R.id.steal);
        
        bktCourt = (ImageView) rootView.findViewById(R.id.bktCourt);
        bktCourt.getLayoutParams().height = MultiDevInit.bktCourtH;
        bktCourt.getLayoutParams().width = MultiDevInit.bktCourtW;
        
        // get the bktCourt's position relative to its parent 
        bktCourt.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                xInterval = bktCourt.getLeft();
                yInterval = bktCourt.getTop();
            }
        });
        
        mBall = imageViewFactory(mBall, R.layout.ball, R.id.ballIV);
        
        bktCourt.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                mRelativeBkt = (ViewGroup) v.getParent();
                ImageView AddedView = (ImageView) mRelativeBkt.findViewById(R.id.ballIV);
                int isExist = mRelativeBkt.indexOfChild(AddedView);
                if(isExist != -1){
                    mRelativeBkt.removeView(AddedView);
                }
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(WC, WC);
                // event.getX is get the x position from bktCourt
                params.leftMargin = (int) event.getX() + xInterval;
                params.topMargin = (int) event.getY() + yInterval;
                mRelativeBkt.addView(mBall, params);
                //view added flg
                touch_flg = true;
                
                animFactory(params.leftMargin, params.topMargin);
                return false;
            }
            /**
             * set the destination and middle path of the animation
             * */
            private void animFactory(float tx, float ty) {
                
                //ring's xy minus touched xy
                disX = (xInterval + (MultiDevInit.bktCourtW/2)) - tx;
                disY = yInterval - ty;
                
                //cal for middle diff
                midX = (float) (disX * 0.5);
                midY = (float) (disY * 1.2);
                
                if(midX > 0){//left side
                    midX = midX - 100;
                    midY = midY - 100;
                }else{//right side
                    midX = midX + 100;
                    midY = midY + 100;
                }
            }
        });
        
        // set the touch event
        twoBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecordC(mixedV, event, 1, xParams, sPlayer);
                return true;
            }
        });
        freeBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecordC(mixedV, event, 3, xParams, sPlayer);
                return true;
            }
        
        });
        blockBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecordC(mixedV, event, 6, xParams, sPlayer);
                return true;
            }
        
        });
        failBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecordC(mixedV, event, 5, xParams, sPlayer);
                return true;
            }
        
        });
        
        threeBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecordC(mixedV, event, 2, xParams, sPlayer);
                return true;
            }
        
        });
        reboundBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecordC(mixedV, event, 0, xParams, sPlayer);
                return true;
            }
        
        });
        assistBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecordC(mixedV, event, 8, xParams, sPlayer);
                return true;
            }
        
        });
        stealBtn.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecordC(mixedV, event, 7, xParams, sPlayer);
                return true;
            }
        
        });

        final AlertDialog recDialogBuilder = new AlertDialog.Builder(mActivity)
        .setView(rootView)
        .setCustomTitle(title)
        .create();
        
        recDialogBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = recDialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recDialogBuilder.dismiss();
                    }
                });
            }
        });

        recDialogBuilder.show();
        //defBuilder.getWindow().setLayout(800, 600);
    }
    
    /**
     * init the imageview for adding new view
     * */
    public ImageView imageViewFactory(ImageView IV, int layoutId, int iconId){
        View tmpLayout = LayoutInflater.from(mActivity).inflate(layoutId, null);
        IV = (ImageView) tmpLayout.findViewById(iconId);
        ViewGroup tmpViewGroup = (ViewGroup)tmpLayout;
        // de-correlation between newView and its parent
        tmpViewGroup.removeView(IV);
        return IV;
    }
    
    /**
     * record engine for players
     * */
    public void doRecord(View v, MotionEvent event, int touched, 
            WindowManager.LayoutParams params, PlayerObj msPlayer){

        //no response to player item + ACTION_DOWN/ACTION_MOVE
        if(!((event.getAction() == 0 || event.getAction() == 2) && touched == 4)){
            switch (event.getAction()){
                //--------touch the screen--------//
                case MotionEvent.ACTION_DOWN://0
                    lastPos = touched;
                    lastTouchX = event.getX();
                    lastTouchY = event.getY();

                    // if rebound, 2p, 3p, free three, player, TO_n_foul is clicked
                    if(touched < 6){
                        //init the img by touched position
                        imageSetter(touched);
                        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                        // レイアウトファイルから重ね合わせするViewを作成する
                        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                        floatView = layoutInflater.inflate(R.layout.overlay, null);
                        // set the imageview in the overlay
                        mFloat = (ImageView) floatView.findViewById(R.id.float_image);
                        
                        mFloat.getLayoutParams().width = 300;
                        mFloat.getLayoutParams().height = 200;
                        
                        mFloat.setImageResource(imageSet[0]);
                        // attach overlay to the view
                        wm.addView(floatView, params);
                    }
                    break;
                    
                //--------finger moved--------//
                case MotionEvent.ACTION_MOVE://2
                    currentX = event.getX();
                    currentY = event.getY();
                    // identified moving direction(RIGHT or LEFT)
                    /*
                    if(currentX < lastTouchX){
                        // UP
                        if(movingCheck_RF(currentX)){
                            mFloat.setImageResource(imageSet[1]);
                        }
                    }else{
                        // DOWN
                        if(movingCheck_RF(currentX)){
                            mFloat.setImageResource(imageSet[2]);
                        }
                    }
                    */
                    // identified moving direction(UP or DOWN)
                    if(currentY < lastTouchY){
                        // UP
                        if(movingCheck(currentY)){
                            mFloat.setImageResource(imageSet[1]);
                        }
                    }else{
                        // DOWN
                        if(movingCheck(currentY)){
                            mFloat.setImageResource(imageSet[2]);
                        }
                    }
                    break;
                    
                //--------leave the screen--------//
                case MotionEvent.ACTION_UP://1
                    // get the player's number
                    currentY = event.getY();
                    actTime = strTime.getText().toString();
                    if(SummaryPage.getQp() == null){
                        tmpQuarter = 0;
                    }else{
                    	tmpQuarter = SummaryPage.getQp().getValue();
                    }
                    // if touched icon is block/steal/assist
                    if(lastPos > 5){
                        // update score & actionCode
                        Made(lastPos);
                        PlayerObj tmpPlayer = PlayerObj.getInstance(mContext, actionCode, null,null, 
                                                                    msPlayer.getPlayerNum(), msPlayer.getPlayerName(), 
                                                                    true, false,true, actTime, tmpQuarter, DEFAULT_X, DEFAULT_Y);
                        tmpPlayer.setSummary(tmpPlayer, 1);
                        updateSingleRow(tmpPlayer);
                        TeamObj.addTimeLine(tmpPlayer);
                        Utilities.CustomToast(mActivity, tmpPlayer.getPlayerName(), ActText);

                    // if touched icon are others
                    }else{
                        // identified moving direction(MOVING UP)
                        if(currentY < lastTouchY){
                            if(movingCheck(currentY)){
                                // update score & actionCode
                                Made(lastPos);
                                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBall.getLayoutParams();
                                PlayerObj tmpPlayer = PlayerObj.getInstance(mContext, actionCode, null, null,
                                                                            msPlayer.getPlayerNum(), msPlayer.getPlayerName(), 
                                                                            true, false,true, actTime, tmpQuarter, DEFAULT_X, DEFAULT_Y);
                                tmpPlayer.setSummary(tmpPlayer, 1);
                                updateSingleRow(tmpPlayer);
                                TeamObj.addTimeLine(tmpPlayer);
                                Utilities. CustomToast(mActivity, tmpPlayer.getPlayerName(), ActText);
                                //if 2 or 3 point made, show animation
                                
                                if(actionCode == 2 || actionCode == 4){
                                    // perform anim only in bktcourt be touched
                                    if(touch_flg){
                                        // add imageview for anim
                                        mBallAnim  = imageViewFactory(mBallAnim, R.layout.ball_animation, R.id.ballanim);
                                        animStart(v, mBall, mBallAnim, "mBallAnimLoc", lp.leftMargin, lp.topMargin);
                                    }
                                }

                            }
                        // MOVING DOWN
                        }else{
                            if(movingCheck(currentY)){
                                // update scroe & actionCode
                                Missed(lastPos);
                                PlayerObj tmpPlayer = PlayerObj.getInstance(mContext, actionCode, null, null,
                                                                            msPlayer.getPlayerNum(), msPlayer.getPlayerName(),
                                                                            true, false, true, actTime, tmpQuarter, DEFAULT_X, DEFAULT_Y);
                                tmpPlayer.setSummary(tmpPlayer, 1);
                                updateSingleRow(tmpPlayer);
                                updateFoul(tmpPlayer, 1, 0);
                                TeamObj.addTimeLine(tmpPlayer);
                                Utilities.CustomToast(mActivity, tmpPlayer.getPlayerName(), ActText);
                                // if 2 or 3 point missed, show animation
                                
                                if(actionCode == 3 || actionCode == 5){
                                    // perform anim only in bktcourt be touched
                                    if(touch_flg){
                                    	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBall.getLayoutParams();
                                        deleView(mRelativeBkt, R.id.ballIV);
                                        //add x icon
                                        missIcon = imageViewFactory(missIcon, R.layout.miss_icon, R.id.missIcon);
                                        //set the location of ballAnim
                                        RelativeLayout.LayoutParams missParam = new RelativeLayout.LayoutParams(WC, WC);
                                        missParam.leftMargin = lp.leftMargin;
                                        missParam.topMargin = lp.topMargin;
                                        mRelativeBkt.addView(missIcon, missParam);
                                        //after 0.5 sec, remove it
                                        Handler removeH = new Handler();
                                        removeH.postDelayed(new Runnable(){
                                            public void run(){
                                                mRelativeBkt.removeView(missIcon);
                                            }
                                        }, 500);
                                    }

                                }
                                
                            }
                        }
                    }
                    removeOverLay();
                    break;
                default:
                    removeOverLay();
            }
        }
    }

    
    /**
     * record engine for competitors
     * */
    public void doRecordC(View v, MotionEvent event, int touched, 
            WindowManager.LayoutParams params, CompetitorObj msPlayer){

        //no response to player item + ACTION_DOWN/ACTION_MOVE
        if(!((event.getAction() == 0 || event.getAction() == 2) && touched == 4)){
            switch (event.getAction()){
                //--------touch the screen--------//
                case MotionEvent.ACTION_DOWN://0
                    lastPos = touched;
                    lastTouchX = event.getX();
                    lastTouchY = event.getY();

                    // if rebound, 2p, 3p, free three, player, TO_n_foul is clicked
                    if(touched < 6){
                        //init the img by touched position
                        imageSetter(touched);
                        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                        // レイアウトファイルから重ね合わせするViewを作成する
                        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                        floatView = layoutInflater.inflate(R.layout.overlay, null);
                        // set the imageview in the overlay
                        mFloat = (ImageView) floatView.findViewById(R.id.float_image);
                        
                        mFloat.getLayoutParams().width = 300;
                        mFloat.getLayoutParams().height = 200;
                        
                        mFloat.setImageResource(imageSet[0]);
                        // attach overlay to the view
                        wm.addView(floatView, params);
                    }
                    break;
                    
                //--------finger moved--------//
                case MotionEvent.ACTION_MOVE://2
                    currentX = event.getX();
                    currentY = event.getY();
                    // identified moving direction(RIGHT or LEFT)
                    /*
                    if(currentX < lastTouchX){
                        // UP
                        if(movingCheck_RF(currentX)){
                            mFloat.setImageResource(imageSet[1]);
                        }
                    }else{
                        // DOWN
                        if(movingCheck_RF(currentX)){
                            mFloat.setImageResource(imageSet[2]);
                        }
                    }
                    */
                    // identified moving direction(UP or DOWN)
                    if(currentY < lastTouchY){
                        // UP
                        if(movingCheck(currentY)){
                            mFloat.setImageResource(imageSet[1]);
                        }
                    }else{
                        // DOWN
                        if(movingCheck(currentY)){
                            mFloat.setImageResource(imageSet[2]);
                        }
                    }
                    break;
                    
                //--------leave the screen--------//
                case MotionEvent.ACTION_UP://1
                    // get the player's number
                    currentY = event.getY();
                    actTime = strTime.getText().toString();
                    if(SummaryPage.getQp() == null){
                        tmpQuarter = 0;
                    }else{
                    	tmpQuarter = SummaryPage.getQp().getValue();
                    }
                    // if touched icon is block/steal/assist
                    if(lastPos > 5){
                        // update score & actionCode
                        MadeC(lastPos);
                        CompetitorObj tmpPlayer = CompetitorObj.getInstance(mContext, actionCode, null,null, 
                                                                    msPlayer.getPlayerNum(), msPlayer.getPlayerName(), 
                                                                    true, false,true, actTime, tmpQuarter, DEFAULT_X, DEFAULT_Y);
                        TeamObj.addTimeLine(tmpPlayer);
                        Utilities.CustomToast(mActivity, tmpPlayer.getPlayerName(), ActText);

                    // if touched icon are others
                    }else{
                        // identified moving direction(MOVING UP)
                        if(currentY < lastTouchY){
                            if(movingCheck(currentY)){
                                // update score & actionCode
                                MadeC(lastPos);
                                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBall.getLayoutParams();
                                CompetitorObj tmpPlayer = CompetitorObj.getInstance(mContext, actionCode, null, null,
                                                                            msPlayer.getPlayerNum(), msPlayer.getPlayerName(), 
                                                                            true, false,true, actTime, tmpQuarter, DEFAULT_X, DEFAULT_Y);

                                TeamObj.addTimeLine(tmpPlayer);
                                Utilities. CustomToast(mActivity, tmpPlayer.getPlayerName(), ActText);
                                //if 2 or 3 point made, show animation
                                
                                if(actionCode == 2 || actionCode == 4){
                                    // perform anim only in bktcourt be touched
                                    if(touch_flg){
                                        // add imageview for anim
                                        mBallAnim  = imageViewFactory(mBallAnim, R.layout.ball_animation, R.id.ballanim);
                                        animStart(v, mBall, mBallAnim, "mBallAnimLoc", lp.leftMargin, lp.topMargin);
                                    }
                                }

                            }
                        // MOVING DOWN
                        }else{
                            if(movingCheck(currentY)){
                                // update scroe & actionCode
                                Missed(lastPos);
                                CompetitorObj tmpPlayer = CompetitorObj.getInstance(mContext, actionCode, null, null,
                                                                            msPlayer.getPlayerNum(), msPlayer.getPlayerName(),
                                                                            true, false, true, actTime, tmpQuarter, DEFAULT_X, DEFAULT_Y);
                                updateFoul(tmpPlayer, 0, 1);
                                TeamObj.addTimeLine(tmpPlayer);
                                Utilities.CustomToast(mActivity, tmpPlayer.getPlayerName(), ActText);
                                // if 2 or 3 point missed, show animation
                                
                                if(actionCode == 3 || actionCode == 5){
                                    // perform anim only in bktcourt be touched
                                    if(touch_flg){
                                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBall.getLayoutParams();
                                        deleView(mRelativeBkt, R.id.ballIV);
                                        //add x icon
                                        missIcon = imageViewFactory(missIcon, R.layout.miss_icon, R.id.missIcon);
                                        //set the location of ballAnim
                                        RelativeLayout.LayoutParams missParam = new RelativeLayout.LayoutParams(WC, WC);
                                        missParam.leftMargin = lp.leftMargin;
                                        missParam.topMargin = lp.topMargin;
                                        mRelativeBkt.addView(missIcon, missParam);
                                        //after 0.5 sec, remove it
                                        Handler removeH = new Handler();
                                        removeH.postDelayed(new Runnable(){
                                            public void run(){
                                                mRelativeBkt.removeView(missIcon);
                                            }
                                        }, 500);
                                    }

                                }
                                
                            }
                        }
                    }
                    removeOverLay();
                    break;
                default:
                    removeOverLay();
            }
        }
    }
    
    /**
     * initialize the plus and minus images
     * */
    private void imageSetter(int pos) {
        switch(pos){
            case 0: imageSet = rebound; break;
            case 1: imageSet = twoPoint; break;
            case 2: imageSet = threePoint; break;
            case 3: imageSet = freeThrow; break;
            case 5: imageSet = to_n_foul; break;
            default:
        }
    }
    
    /**
    *
    * */
    private void removeOverLay() {
       if(floatView != null){
           wm.removeViewImmediate(floatView);
           floatView = null;
       }
   }
   
   /**
    * translate touched item to action code/action text
    * */
    private void Missed(int mLastPos) {
       switch(String.valueOf(mLastPos)){
       case "0":
           actionCode = ActionDef.ACT_OR;
           ActText = ActionDef.ACT_strOR;
           break;
       case "1":
           actionCode = ActionDef.ACT_TWOP_MI;
           ActText = ActionDef.ACT_strTWOP_MI;
           break;
       case "2":
           actionCode = ActionDef.ACT_THREEP_MI;
           ActText = ActionDef.ACT_strTHREEP_MI;
           break;
       case "3":
           actionCode = ActionDef.ACT_FTMI;
           ActText = ActionDef.ACT_strFTMI;
           break;
       case "5":
           actionCode = ActionDef.ACT_FOUL;
           ActText = ActionDef.ACT_strFOUL;
           break;
       case "6":
           actionCode = ActionDef.ACT_BS;
           ActText = ActionDef.ACT_strBS;
           break;
       case "7":
           actionCode = ActionDef.ACT_ST;
           ActText = ActionDef.ACT_strST;
           break;
       case "8":
           actionCode = ActionDef.ACT_AS;
           ActText = ActionDef.ACT_strAS;
           break;
       default:
      }
   }
   
   /**
   *
   * */
    private void deleView(ViewGroup v, int imageId) {
        ImageView touchedView = (ImageView) v.findViewById(imageId);
        int isExist = v.indexOfChild(touchedView);
        if(isExist != -1){
            v.removeView(touchedView);
        }
    }
  
  /**
   * set the score to update the score board & scorekeeper
   * */
    private void setScore(int l, int r) {
        String tmpScore[] = strScore.getText().toString().split(":");
        int leftScore = Integer.valueOf(tmpScore[0]);
        int rightScore = Integer.valueOf(tmpScore[1]);

        String newRightScore = String.valueOf(rightScore + r);
        String newLeftScore = String.valueOf(leftScore + l);

        strScore.setText(newLeftScore + ":" + newRightScore);
        
    }

    /**
     * setQuarterScore for formal pattern
     * */
    private void setQuarterScore(int l, int r){
        // get the quarter info of that time
        NumberPicker tmpQp = SummaryPage.getQp();
        if(l != 0){
            // update home team
            if(tmpQp == null || tmpQp.getValue() == 0){
                // add to 1st quarter if not setted
                TeamObj.scoreKeeper[0][0] += l ;
            }else if(tmpQp.getValue() == 1){
                // 2nd half
                TeamObj.scoreKeeper[0][1] += l ;
            }else if(tmpQp.getValue() == 2){
                // 1st
                TeamObj.scoreKeeper[0][0] += l ;
            }else if(tmpQp.getValue() == 3){
                // 2nd
                TeamObj.scoreKeeper[0][1] += l ;
            }else if(tmpQp.getValue() == 4){
                // 3rd
                TeamObj.scoreKeeper[0][2] += l ;
            }else if(tmpQp.getValue() == 5){
                // 4th
                TeamObj.scoreKeeper[0][3] += l ;
            }
        }else{
            // update away team
            if(tmpQp == null || tmpQp.getValue() == 0){
                // add to 1st half if not setted
                TeamObj.scoreKeeper[1][0] += r ;
            }else if(tmpQp.getValue() == 1){
                // 2nd half
                TeamObj.scoreKeeper[1][1] += r ;
            }else if(tmpQp.getValue() == 2){
                // 1st
                TeamObj.scoreKeeper[1][0] += r ;
            }else if(tmpQp.getValue() == 3){
                // 2nd
                TeamObj.scoreKeeper[1][1] += r ;
            }else if(tmpQp.getValue() == 4){
                // 3rd
                TeamObj.scoreKeeper[1][2] += r ;
            }else if(tmpQp.getValue() == 5){
                // 4th
                TeamObj.scoreKeeper[1][3] += r ;
            }
        }
    }
    
    /**
     * setQuarterScore for undo
     * */
    private void undoQuarterScore(int l, int r, Player mPlayer){
        // update the score keeper array
        int tmpQp = mPlayer.getQuarter();
        if(l != 0){
            // update home team
            if(tmpQp == 0){
                // add to 1st quarter if not setted
                TeamObj.scoreKeeper[0][0] += l ;
            }else if(tmpQp == 1){
                TeamObj.scoreKeeper[0][1] += l ;
            }else if(tmpQp == 2){
                TeamObj.scoreKeeper[0][0] += l ;
            }else if(tmpQp == 3){
                TeamObj.scoreKeeper[0][1] += l ;
            }else if(tmpQp == 4){
                TeamObj.scoreKeeper[0][2] += l ;
            }else if(tmpQp == 5){
                TeamObj.scoreKeeper[0][3] += l ;
            }
        }else{
            // update away team
            if(tmpQp == 0){
                // add to 1st quarter if not setted
                TeamObj.scoreKeeper[1][0] += r ;
            }else if(tmpQp == 1){
                TeamObj.scoreKeeper[1][1] += r ;
            }else if(tmpQp == 2){
                TeamObj.scoreKeeper[1][0] += r ;
            }else if(tmpQp == 3){
                TeamObj.scoreKeeper[1][1] += r ;
            }else if(tmpQp == 4){
                TeamObj.scoreKeeper[1][2] += r ;
            }else if(tmpQp == 5){
                TeamObj.scoreKeeper[1][3] += r ;
            }
        }
    }
    /**
     * translate touched item to action code/action text
     * */
    private void MadeC(int mlastPos) {
        switch(String.valueOf(mlastPos)){
        case "0":
            actionCode = ActionDef.ACT_DR;
            ActText = ActionDef.ACT_strDR;
            break;
        case "1":
            actionCode = ActionDef.ACT_TWOP_MA;
            ActText = ActionDef.ACT_strTWOP_MA;
            setScore(0,2);
            setQuarterScore(0,2);
            break;
        case "2":
            actionCode = ActionDef.ACT_THREEP_MA;
            ActText = ActionDef.ACT_strTHREEP_MA;
            setScore(0,3);
            setQuarterScore(0,3);
            break;
        case "3":
            actionCode = ActionDef.ACT_FTMA;
            ActText = ActionDef.ACT_strFTMA;
            setScore(0,1);
            setQuarterScore(0,1);
            break;
        case "5":
            actionCode = ActionDef.ACT_TO;
            ActText = ActionDef.ACT_strTO;
            break;
        case "6":
            actionCode = ActionDef.ACT_BS;
            ActText = ActionDef.ACT_strBS;
            break;
        case "7":
            actionCode = ActionDef.ACT_ST;
            ActText = ActionDef.ACT_strST;
            break;
        case "8":
            actionCode = ActionDef.ACT_AS;
            ActText = ActionDef.ACT_strAS;
            break;
        default:
        
        }
  }
  
  /**
   * translate touched item to action code/action text
   * */
    private void Made(int mlastPos) {
      switch(String.valueOf(mlastPos)){
      case "0":
          actionCode = ActionDef.ACT_DR;
          ActText = ActionDef.ACT_strDR;
          break;
      case "1":
          actionCode = ActionDef.ACT_TWOP_MA;
          ActText = ActionDef.ACT_strTWOP_MA;
          setScore(2,0);
          setQuarterScore(2,0);
          break;
      case "2":
          actionCode = ActionDef.ACT_THREEP_MA;
          ActText = ActionDef.ACT_strTHREEP_MA;
          setScore(3,0);
          setQuarterScore(3,0);
          break;
      case "3":
          actionCode = ActionDef.ACT_FTMA;
          ActText = ActionDef.ACT_strFTMA;
          setScore(1,0);
          setQuarterScore(1,0);
          break;
      case "5":
          actionCode = ActionDef.ACT_TO;
          ActText = ActionDef.ACT_strTO;
          break;
      case "6":
          actionCode = ActionDef.ACT_BS;
          ActText = ActionDef.ACT_strBS;
          break;
      case "7":
          actionCode = ActionDef.ACT_ST;
          ActText = ActionDef.ACT_strST;
          break;
      case "8":
          actionCode = ActionDef.ACT_AS;
          ActText = ActionDef.ACT_strAS;
          break;
      default:
      }
  }
  
  
  /**
   * check the view has been added and move distance is far enough
   * */
    public boolean movingCheck(float cY){
      // if moveing up
      if(cY < lastTouchY){
          float diff = lastTouchY - cY;
          Boolean isValid = (diff > DISTANCE) ? true : false;
          if(isValid && floatView != null){
              return true;
          }else{
              return false;
          }
      // if moving down
      }else{
          float diff = cY - lastTouchY;
          Boolean isValid = (diff > DISTANCE) ? true : false;
          if(isValid && floatView != null){
              return true;
          }else{
              return false;
          }
      }
  }
  /**
   * perform animation
   * */
    private void animStart(View v, ImageView mB, ImageView mBAnim,
          String getterStr, int x, int y) {
      // add one more ball image to bktcourt for animation
      int isExist = mRelativeBkt.indexOfChild(mBAnim);
      if(isExist != -1){
          mRelativeBkt.removeView(mBAnim);
      }
      //set the location of ballAnim
      RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(WC, WC);
      params.leftMargin = x;
      params.topMargin = y;
      mRelativeBkt.addView(mBAnim, params);
      // delete the ball imageview which created by touch
      deleView(mRelativeBkt, R.id.ballIV);
      
      //set path of animation
      path = new AnimatorPath();
      path.moveTo(0, 0);
      path.curveTo(0, 0, midX, midY, disX, disY);
      // Set up the animation
      final ObjectAnimator anim = ObjectAnimator.ofObject(this, getterStr,
              new PathEvaluator(), path.getPoints().toArray());
      
      anim.setInterpolator(new AccelerateInterpolator());
      anim.setDuration(500);
      anim.start();
      
      //del the ball image which created for animation
      Handler afterAnim = new Handler();
      afterAnim.postDelayed(new Runnable(){
          public void run() {
              deleView(mRelativeBkt, R.id.ballanim);}
      }, 1000);
      //reset variables
      touch_flg = false;
      midX = 0;
      midY = 0;
      disX = 0;
      disY = 0;

  }
  
  /*
   * have to match the animStart(v, mBall, mBallAnim, "mBallAnimLoc", lp.leftMargin, lp.topMargin); 
   * without this class, curve animation does not work
   * */
    public void setMBallAnimLoc(PathPoint newLoc) {
      mBallAnim.setTranslationX(newLoc.mX);
      mBallAnim.setTranslationY(newLoc.mY);
  }
  
  /**
   * refresh single row in player's listview
   * */
    private void updateSingleRow(PlayerObj tPlayer) {
      // get touched player's number
      String touchedNumString = tPlayer.getPlayerNum();
      // check the visible range of the listview
      int start = mListView.getFirstVisiblePosition();
      // loop the onplay players(except banner)
      for(int i=start, j=mListView.getLastVisiblePosition(); i <= j; i++){
          // the touched player's row
          View vi = mListView.getChildAt(i-start);
          TextView tmpView = (TextView)vi.findViewById(R.id.number);
          // verify the row( bench row or not)
          if(tmpView != null){
              String numString = (String) tmpView.getText();
              // get the player's number in listview
              if(numString.indexOf(touchedNumString) != -1){
                  // update all record for that player
                  ((TextView)vi.findViewById(R.id.twomade)).setText(tPlayer.recordsArray[2]);
                  ((TextView)vi.findViewById(R.id.twotried)).setText(tPlayer.recordsArray[3]);
                  ((TextView)vi.findViewById(R.id.threemade)).setText(tPlayer.recordsArray[4]);
                  ((TextView)vi.findViewById(R.id.threetried)).setText(tPlayer.recordsArray[5]);
                  ((TextView)vi.findViewById(R.id.ftmade)).setText(tPlayer.recordsArray[6]);
                  ((TextView)vi.findViewById(R.id.fttried)).setText(tPlayer.recordsArray[7]);
                  ((TextView)vi.findViewById(R.id.defrebound)).setText(tPlayer.recordsArray[8]);
                  ((TextView)vi.findViewById(R.id.offrebound)).setText(tPlayer.recordsArray[9]);
                  ((TextView)vi.findViewById(R.id.assist)).setText(tPlayer.recordsArray[10]);
                  ((TextView)vi.findViewById(R.id.block)).setText(tPlayer.recordsArray[11]);
                  ((TextView)vi.findViewById(R.id.steal)).setText(tPlayer.recordsArray[12]);
                  ((TextView)vi.findViewById(R.id.turnover)).setText(tPlayer.recordsArray[13]);
                  ((TextView)vi.findViewById(R.id.foul)).setText(tPlayer.recordsArray[14]);
                  ((TextView)vi.findViewById(R.id.point)).setText(tPlayer.recordsArray[15]);
                  // update the view
                  mListView.getAdapter().getView(i, vi, mListView);	
              }
          }
          
      }
  }
  /**
   * refresh foul textview in header.xml
   * */
    private void updateFoul(Player tPlayer, int l, int r){
      if(tPlayer.playerAct == ActionDef.ACT_FOUL){
          String tmpFoul[] = strFoul.getText().toString().split(":");
          int leftScore = Integer.valueOf(tmpFoul[0]);
          int rightScore = Integer.valueOf(tmpFoul[1]);

          String newLeftScore = String.valueOf(leftScore + l);
          String newRightScore = String.valueOf(rightScore + r);

          strFoul.setText(newLeftScore + ":" + newRightScore);
      }
      
    }

    /**
     * transform number to act
     **/
    public void undo(View mainView){
        // rival
        int rightUndo = 0;
        // our team
        int leftUndo = 0;
        // seperate competitor and our player
        if (TeamObj.undoStack.lastElement() instanceof PlayerObj) {
            PlayerObj mPlayerObj = (PlayerObj) TeamObj.undoStack.pop();
            
            switch(mPlayerObj.playerAct){
            case ActionDef.ACT_TWOP_MA:
                leftUndo = -2;
              break;
            case ActionDef.ACT_THREEP_MA:
                leftUndo = -3;
                break;
            case ActionDef.ACT_FTMA:
                leftUndo = -1;
                break;
            case ActionDef.ACT_FOUL:
                // decrement foul counter in header.xml
                updateFoul(mPlayerObj, -1, 0);
                break;
            default:
                break;
            }
            //undo score board(summary board)
            mPlayerObj.setSummary(mPlayerObj, -1);
            //undo score board
            setScore(leftUndo, rightUndo);
            undoQuarterScore(leftUndo,rightUndo, mPlayerObj);
            // update UI view
            updateSingleRow(mPlayerObj);
            Utilities.CustomToast(mActivity, mPlayerObj.getPlayerName(), actionToText(mPlayerObj.playerAct, "取消"));
        }else{
            // if target of undo is CompetitorObj
            CompetitorObj CPObj = (CompetitorObj) TeamObj.undoStack.pop();

            switch(CPObj.playerAct){
            case ActionDef.ACT_TWOP_MA:
                rightUndo = -2;
                break;
            case ActionDef.ACT_THREEP_MA:
                rightUndo = -3;
                break;
            case ActionDef.ACT_FTMA:
                rightUndo = -1;
                break;
            case ActionDef.ACT_FOUL:
                // decrement foul counter in header.xml
                updateFoul(CPObj, 0, -1);
                break;
            default:
                break;
            }
            //undo score board(summary board)
            //mPlayerObj.setSummary(mPlayerObj, -1);
            //undo score board
            setScore(leftUndo, rightUndo);
            undoQuarterScore(leftUndo,rightUndo, CPObj);
            // update UI view
            //updateSingleRow(mPlayerObj);
            Utilities.CustomToast(mActivity, CPObj.getPlayerName(), actionToText(CPObj.playerAct, "取消"));

        }
    }

    /**
     * translate playerAct to text
     * */
    public static String actionToText(int playerAct, String append) {
        String text = "";
        switch(playerAct){
            case ActionDef.ACT_TWOP_MA:
                text = "兩分進帳";
                break;
            case ActionDef.ACT_TWOP_MI:
                text = "兩分沒進";
                break;
            case ActionDef.ACT_THREEP_MA:
                text = "三分進帳";
                break;
            case ActionDef.ACT_THREEP_MI:
                text = "三分沒進";
                break;
            case ActionDef.ACT_FTMA:
                text = "穩罰中一";
                break;
            case ActionDef.ACT_FTMI:
                text = "罰球不進";
                break;
            case ActionDef.ACT_DR:
                text = "怒拉一防守籃板";
                break;
            case ActionDef.ACT_OR:
                text = "怒拉一進攻籃板";
                break;
            case ActionDef.ACT_AS:
                text = "妙傳助攻";
                break;
            case ActionDef.ACT_BS:
                text = "送出火鍋一次";
                break;
            case ActionDef.ACT_ST:
                text = "抄截加一";
                break;
            case ActionDef.ACT_TO:
                text = "失誤一次";
                break;
            case ActionDef.ACT_FOUL:
                text = "犯規一次";
                break;
            default:
                text = "DEFAULT";
                break;
        }
        text = text + append;
        return text;
    }
}
