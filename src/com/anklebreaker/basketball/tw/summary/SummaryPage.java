package com.anklebreaker.basketball.tw.summary;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.def.ActionDef;
import com.anklebreaker.basketball.tw.recordboard.Item;


public class SummaryPage {
	
	private static final String TAG = "Scoreboard.SummaryPage";
	
	Activity mActivity = null;
	private ListView mListView;
	private PlayerListAdapter list_adapter = null;
	ArrayList<Item> playerList = new ArrayList<Item>();
	
    /**
     * constructor
     * */
    public SummaryPage(Activity a){
        mActivity = a;
    }
	/**
     * create the view
     * */
    public View createSummaryPage(LayoutInflater inflater){
        Log.i(TAG, "createSummaryPage S");
        final View mixedView = inflater.inflate(R.layout.summary_layout, null);
        mListView = (ListView) mixedView.findViewById(R.id.list_item);
        
        
        list_adapter = new PlayerListAdapter(mActivity, ActionDef.defaultStarters);
        mListView.setAdapter(list_adapter);
        
        
        Log.i(TAG, "createSummaryPage E");
        return mixedView;
    }
}
