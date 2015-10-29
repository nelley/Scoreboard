package com.anklebreaker.basketball.tw.tab;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.summary.SummaryPage;
import com.anklebreaker.basketball.tw.util.PlayerSelectDialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.*;
import android.view.*;
import android.view.ViewGroup.*;
import android.widget.*;

public final class BasketFragment extends Fragment{

    private static final String TAG = "Scoreboard.BasketFragment";
    private static final String KEY_CONTENT = "BasketFragment:Content";
    private String mContent = "???";
    private static Context mContext = null;
    
    public static Boolean setMenu_flg = false;
    public static final String DEFAUT_STRING = "";
    
    // team A listview
    public static ListView listViewA;
    // team B listview
    public static ListView listViewB;
    
    // team A score textview
    private static TextView strScoreA, strFoulA, strTimeA, strTimeTitleA;
    // team B score textview
    private static TextView strScoreB, strFoulB, strTimeB, strTimeTitleB;
    
    PlayerSelectDialog PSDialog;
    
    //Constructor:TestFragment初始化,傳進陣列裡定義好的東西進來
    public static BasketFragment newInstance(String content, Context c) {
        BasketFragment fragment = new BasketFragment();
        fragment.mContent = content;
        mContext = c;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "oncreate");
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    /*
     * 這裡選擇在哪個頁面生成哪些資訊
     * */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        LinearLayout layout = null;
        if(mContent.equals(BasketBallAdapter.CONTENT[0])){
            Log.i(TAG, "team A scoreboard init");
            SummaryPage mSummaryPage = new SummaryPage(mContext, getActivity());
            View mixedView = mSummaryPage.createSummaryPage(inflater);
            listViewA = (ListView) mixedView.findViewById(R.id.player_list);
            strTimeTitleA = (TextView) mixedView.findViewById(R.id.timeTitle);
            strScoreA = (TextView) mixedView.findViewById(R.id.score);
            strFoulA = (TextView) mixedView.findViewById(R.id.foul);
            strTimeA = (TextView) mixedView.findViewById(R.id.time);
            return mixedView;
        }else if(mContent.equals(BasketBallAdapter.CONTENT[1])){
            Log.i(TAG, "team B scoreboard init");
            SummaryPage mSummaryPage = new SummaryPage(mContext, getActivity());
            View mixedView = mSummaryPage.createSummaryPage(inflater);
            listViewB = (ListView) mixedView.findViewById(R.id.player_list);
            strTimeTitleB = (TextView) mixedView.findViewById(R.id.timeTitle);
            strScoreB = (TextView) mixedView.findViewById(R.id.score);
            strFoulB = (TextView) mixedView.findViewById(R.id.foul);
            strTimeB = (TextView) mixedView.findViewById(R.id.time);
            return mixedView;
        }else{
            Log.i(TAG, "onCreateView else");
            layout = new LinearLayout(getActivity());
            TextView text = new TextView(getActivity());
            text.setGravity(Gravity.CENTER);
            text.setText(mContent);
            text.setTextSize(20 * getResources().getDisplayMetrics().density);
            text.setPadding(20, 20, 20, 20);
            layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            layout.setGravity(Gravity.CENTER);
            layout.addView(text);
            return layout;
        }
    }

	@Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView");
    }
	
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
    
    /**
     * method to control which action is activated when the pager is visible to user
     * */
    @Override
    public void setMenuVisibility(boolean visible) {
        super.setMenuVisibility(visible);
        Log.i(TAG, "setMenuVisibility S");
        // prevent activity leak
        if(PSDialog != null){
            PSDialog = null;
        }
        
        // show player selection dialog
        if (visible && mContent == BasketBallAdapter.CONTENT[0] && setMenu_flg == false) {
            // re-select players(listViewA will be null at first time)
            PSDialog = new PlayerSelectDialog(mContext, listViewA);
            PSDialog.setCanceledOnTouchOutside(false);
            PSDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            PSDialog.show();
        }
        Log.i(TAG, "setMenuVisibility E");
    }
    
    /**
     * reset foul record
     * */
    public static void resetScore(){
        strScoreA.setText("0:0");
        strScoreB.setText("0:0");
    }
    
    /**
     * reset score record
     * */
    public static void resetFoul(){
        strFoulA.setText("0:0");
        strFoulB.setText("0:0");
    }

    public static TextView getStrScoreA() {
        return strScoreA;
    }

    public static TextView getStrFoulA() {
        return strFoulA;
    }

    public static TextView getStrScoreB() {
        return strScoreB;
    }

    public static TextView getStrFoulB() {
        return strFoulB;
    }
}

