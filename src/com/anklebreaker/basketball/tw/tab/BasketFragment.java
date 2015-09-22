package com.anklebreaker.basketball.tw.tab;

import java.util.ArrayList;
import java.util.Map;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.recordboard.TeamObj;
import com.anklebreaker.basketball.tw.summary.PlayerGridViewAdapter;
import com.anklebreaker.basketball.tw.summary.SummaryPage;
import com.anklebreaker.basketball.tw.util.PlayerSelectDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
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
    
    // player list(GridView) for init
    public static ArrayList<PlayerObj> player_settingGrid = new ArrayList<PlayerObj>();
    private static PlayerGridViewAdapter InitialAdapter;
    public static Boolean setMenu_flg = false;
    public static final String DEFAUT_STRING = "";
    
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
        if(mContent.equals("記錄板")){
            Log.i(TAG, "onCreateView summary");
            SummaryPage mSummaryPage = new SummaryPage(mContext, getActivity());
            return mSummaryPage.createSummaryPage(inflater);
        }else if(mContent.equals("比賽數據")){
            Log.i(TAG, "onCreateView 4");
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
        if (visible && mContent == "記錄板" && setMenu_flg == false) {
        	// re-select players
            PlayerSelectDialog PSDialog = new PlayerSelectDialog(mContext);
            PSDialog.setCanceledOnTouchOutside(false);
            PSDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            PSDialog.show();
        }
        Log.i(TAG, "setMenuVisibility E");
    }
    
    
    /**
     * getter and setter
     * */
    public ArrayList<PlayerObj> getPlayer_settingGrid() {
         return player_settingGrid;
    }

    public void setPlayer_settingGrid(ArrayList<PlayerObj> pg) {
        player_settingGrid = pg;
    }

    public static PlayerGridViewAdapter getInitialAdapter() {
        return InitialAdapter;
    }

    public static void setInitialAdapter(PlayerGridViewAdapter initialAdapter) {
        InitialAdapter = initialAdapter;
    }
}

