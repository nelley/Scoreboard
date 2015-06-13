package com.anklebreaker.basketball.tw.summary;

import java.util.ArrayList;
import android.animation.ObjectAnimator;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.animation.AnimatorPath;
import com.anklebreaker.basketball.tw.animation.PathEvaluator;
import com.anklebreaker.basketball.tw.def.ActionDef;
import com.anklebreaker.basketball.tw.recordboard.Item;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.recordboard.TeamObj;
import com.anklebreaker.basketball.tw.util.MultiDevInit;


public class SummaryPage {

    private static final String TAG = "Scoreboard.SummaryPage";

    final int DEFAULT_ACTION = 999;
    final int TOUCH_OK = 1;
    final int DISTANCE = 40;
    final int RIVAL_ACTION = 17;
    final int DEFAULT_X = 0;
    final int DEFAULT_Y = 0;

    final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    final int MP = ViewGroup.LayoutParams.MATCH_PARENT;

    private ImageView mFloat ;
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
    private float lastTouchY;
    private float currentY;
    int lastPos = -1;

    private int actionCode = 999;
    private String name = "player";
    private String ActText = "720度轉身扣籃";

    Context mContext = null;
    Activity mActivity = null;
    private ListView mListView;
    private PlayerListAdapter list_adapter = null;
    ArrayList<Item> playerList = new ArrayList<Item>();
    // new add
    final GridView[] pPanel = new GridView[5];

    ImageView bktCourt, benchBtn, summary, rival, undo, mBall, mBallAnim, mBallAna, missIcon, testBtn;
    private String actTime;
    TextView strTime, strTimeTitle, strScore, strScoreTitle;
    private float midX, midY, disX, disY;
    //for animation
    AnimatorPath path = null;

    WindowManager wm;
    View floatView = null;


    /**
     * constructor
     * */
    public SummaryPage(Context c, Activity a){
        mActivity = a;
        mContext = c;
    }

	/**
     * create the view
     * */
    public View createSummaryPage(LayoutInflater inflater){
        Log.i(TAG, "createSummaryPage S");

        final View mixedView = inflater.inflate(R.layout.summary_layout, null);
        mListView = (ListView) mixedView.findViewById(R.id.list_item);

        mListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // init overlay's view param object
                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                        PixelFormat.TRANSLUCENT);

                // create alertDialog to show the 9*9 panel
                customDialogInit("test", R.layout.summarypage_record_board, mixedView, params);


            }
        });


        // long press for change player
        mListView.setOnItemLongClickListener(new OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id) {
                //Toast.makeText(mActivity, "long test", Toast.LENGTH_SHORT).show();
                // show the player changing panel
                customDialogInit("change", R.layout.summarypage_player_change, R.string.change);



                // if return false, it fires the OnItemClickListener
                return true;
            }
        });

        list_adapter = new PlayerListAdapter(mActivity, ActionDef.defaultStarters);
        mListView.setAdapter(list_adapter);

        Log.i(TAG, "createSummaryPage E");
        return mixedView;
    }

    /**
     * initialize engine of record board
     * */
    public void customDialogInit(String mTitle, int layoutId, final View mixedV, final WindowManager.LayoutParams xParams){
    	TextView title = new TextView(mActivity);
        title.setText(mTitle);
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(0, 0, 0, 0);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        final View rootView = LayoutInflater.from(mActivity).inflate(layoutId, null);
        final GridView rb = (GridView) rootView.findViewById(R.id.setplayer);
        //rb.getLayoutParams().height = MultiDevInit.bktCourtH;
        //rb.getLayoutParams().width = MultiDevInit.bktCourtW;
        rb.setVerticalSpacing(10);
        rb.setAdapter(TeamObj.RecordGVAdapter[0]);

        rb.setOnTouchListener(new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                doRecord(mixedV, event, rb, xParams);//false⇒独自Viewの下にいるViewにTouchEventを渡す。true⇒独自Viewの下にいるViewにTouchEventを渡さない
                return false;
            }
        });

        final AlertDialog defBuilder = new AlertDialog.Builder(mActivity)
        .setView(rootView)
        .setCustomTitle(title)
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
        ImageView bktCourt = (ImageView) rootView.findViewById(R.id.bktCourt);
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

    public void doRecord(View v, MotionEvent event, GridView player, WindowManager.LayoutParams params){
        //identified which image in gridview was touched
        int pos = player.pointToPosition((int) event.getX(), (int) event.getY());

        //no response to player item + ACTION_DOWN/ACTION_MOVE
        if(!((event.getAction() == 0 || event.getAction() == 2) && pos == 4)){
            switch (event.getAction()){
                //--------touch the screen--------//
                case MotionEvent.ACTION_DOWN://0
                    lastPos = pos;
                    lastTouchY = event.getY();
                    if(pos < 6){
                        //initialize the img by touched position
                        imageSetter(pos);

                        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                        // レイアウトファイルから重ね合わせするViewを作成する
                        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                        floatView = layoutInflater.inflate(R.layout.overlay, null);
                        // set the imageview in the overlay
                        mFloat = (ImageView) floatView.findViewById(R.id.float_image);
                        mFloat.setImageResource(imageSet[0]);
                        // attach overlay to the view
                        wm.addView(floatView, params);
                    }
                    break;
                //--------finger moved--------//
                case MotionEvent.ACTION_MOVE://2
                    currentY = event.getY();
                    //identified moving direction
                    if(currentY < lastTouchY){
                        //UP
                        float diff = lastTouchY - currentY;
                        Boolean isValid = (diff > DISTANCE) ? true : false;
                        if(isValid){
                            mFloat.setImageResource(imageSet[1]);
                        }
                    }else{
                        //DOWN
                        float diff = currentY - lastTouchY;
                        Boolean isValid = (diff > DISTANCE) ? true : false;
                        if(isValid){
                            mFloat.setImageResource(imageSet[2]);
                        }
                    }
                    break;
                    //--------leave the screen--------//
                case MotionEvent.ACTION_UP://1
                    //get the player's number
                    //Item playerInfo = mGridArray.get(4);
                    name = "test";//(playerInfo.getTitle() == "") ? "沒有人" : playerInfo.getTitle();
                    currentY = event.getY();
                    //if touched icon is block/steal/assist
                    if(lastPos > 5){
                        isMade(lastPos);
                        if(actionCode != DEFAULT_ACTION){
                            //actTime = strTime.getText().toString();
                            PlayerObj tmpPlayer = PlayerObj.getInstance(mActivity, actionCode, name, name, actTime, DEFAULT_X, DEFAULT_Y);
                            tmpPlayer.setSummary(tmpPlayer, 1);
                            TeamObj.addTimeLine(tmpPlayer);
                            CustomToast(name, ActText);
                        }else{
                            Toast.makeText(mActivity, "player touched", Toast.LENGTH_SHORT).show();
                        }
                    //if touched icon are others
                    }else{
                        //identified moving direction(MOVING UP)
                        if(currentY < lastTouchY){
                            float diff = lastTouchY - currentY;
                            Boolean isValid = (diff > DISTANCE) ? true : false;
                            if(isValid){
                                //check made or miss, get the player's action
                                isMade(lastPos);
                                if(actionCode != DEFAULT_ACTION){
                                    //actTime = strTime.getText().toString();
                                    //RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBall.getLayoutParams();
                                    PlayerObj tmpPlayer = PlayerObj.getInstance(mActivity, actionCode, name, name, actTime, DEFAULT_X, DEFAULT_Y);
                                    tmpPlayer.setSummary(tmpPlayer, 1);
                                    TeamObj.addTimeLine(tmpPlayer);
                                    CustomToast(name, ActText);
                                    //if 2 or 3 point made, show animation
                                    /*
                                    if(actionCode == 2 || actionCode == 4){
                                        // perform anim only in bktcourt be touched
                                        if(touch_flg){
                                            // add imageview for anim
                                            mBallAnim  = imageViewFactory(mBallAnim, R.layout.ball_animation, R.id.ballanim);
                                            animStart(v, mBall, mBallAnim, "mBallAnimLoc", lp.leftMargin, lp.topMargin);
                                        }
                                    }*/

                                }else{
                                    Toast.makeText(mActivity, "player touched", Toast.LENGTH_SHORT).show();
                                }
                            }
                        // MOVING DOWN
                        }else{
                            float diff = currentY - lastTouchY;
                            Boolean isValid = (diff > DISTANCE) ? true : false;
                            if(isValid){
                                // check made or miss, get the player's action
                                isMissed(lastPos);
                                if(actionCode != DEFAULT_ACTION){
                                    //actTime = strTime.getText().toString();
                                    //RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBall.getLayoutParams();
                                    PlayerObj tmpPlayer = PlayerObj.getInstance(mActivity, actionCode, name, name, actTime, DEFAULT_X, DEFAULT_Y);
                                    tmpPlayer.setSummary(tmpPlayer, 1);
                                    TeamObj.addTimeLine(tmpPlayer);
                                    CustomToast(name, ActText);
                                    // if 2 or 3 point missed, show animation
                                    /*
                                    if(actionCode == 3 || actionCode == 5){
                                        // perform anim only in bktcourt be touched
                                        if(touch_flg){
                                            deleView(mRelative, R.id.ballIV);
                                            //add x icon
                                            missIcon = imageViewFactory(missIcon, R.layout.miss_icon, R.id.missIcon);
                                            //set the location of ballAnim
                                            RelativeLayout.LayoutParams missParam = new RelativeLayout.LayoutParams(WC, WC);
                                            missParam.leftMargin = lp.leftMargin;
                                            missParam.topMargin = lp.topMargin;
                                            mRelative.addView(missIcon, missParam);
                                            //after 0.5 sec, remove it
                                            Handler removeH = new Handler();
                                            removeH.postDelayed(new Runnable(){
                                                public void run(){
                                                    mRelative.removeView(missIcon);
                                                }
                                            }, 500);
                                        }

                                    }
                                    */
                                }else{
                                    Toast.makeText(mActivity, "player touched", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    removeOverLay();

                    //wm.removeViewImmediate(floatView);
                    //deleView(mRelative, R.id.ballIV);
                    //reset(mRelative, ChildView, curView, mBall);
                    break;
                default:
                    removeOverLay();
                    //wm.removeViewImmediate(floatView);
                    //deleView(mRelative, R.id.ballIV);
                    //reset(mRelative, ChildView, curView, mBall);
            }
        }
    }

    /**
     *
     * */
    private void removeOverLay() {
        if(floatView != null){
            wm.removeViewImmediate(floatView);
        }
    }

	/**
     * translate touched item to action code/action text
     * */
    private void isMade(int mlastPos) {
        switch(String.valueOf(mlastPos)){
        case "0":
            actionCode = ActionDef.ACT_DR;
            ActText = ActionDef.ACT_strDR;
            break;
        case "1":
            actionCode = ActionDef.ACT_TWOP_MA;
            ActText = ActionDef.ACT_strTWOP_MA;
            //setScore(2,0);
            break;
        case "2":
            actionCode = ActionDef.ACT_THREEP_MA;
            ActText = ActionDef.ACT_strTHREEP_MA;
            //setScore(3,0);
            break;
        case "3":
            actionCode = ActionDef.ACT_FTMA;
            ActText = ActionDef.ACT_strFTMA;
            //setScore(1,0);
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
    private void isMissed(int mLastPos) {
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
        ImageView touchedView = (ImageView) mActivity.findViewById(imageId);
        int isExist = v.indexOfChild(touchedView);
        if(isExist != -1){
            v.removeView(touchedView);
        }
    }

    /**
     * toast show in 0.5 second method
     * */
    private void CustomToast(String str, String value) {
        final Toast toast = Toast.makeText(mActivity, str + value, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, MultiDevInit.bktCourtH);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {toast.cancel();}
        }, 500);
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
     * perform animation
     * */
    private void animStart(View v, ImageView mB, ImageView mBAnim,
            String getterStr, int x, int y) {
        final ViewGroup mParentView = (ViewGroup) v.getParent();
        int isExist = mParentView.indexOfChild(mBAnim);
        if(isExist != -1){
            mParentView.removeView(mBAnim);
        }
        //set the location of ballAnim
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(WC, WC);
        params.leftMargin = x;
        params.topMargin = y;
        mParentView.addView(mBAnim, params);
        deleView(mParentView, R.id.ballIV);
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
        //del the view
        Handler afterAnim = new Handler();
        afterAnim.postDelayed(new Runnable(){
            public void run() {
                deleView(mParentView, R.id.ballanim);}
        }, 1000);
        //reset variables
        touch_flg = false;
        midX = 0;
        midY = 0;
        disX = 0;
        disY = 0;

    }


    /**
     * set the score to update the score board
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
     *
     * */



}
