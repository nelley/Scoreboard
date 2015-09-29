package com.anklebreaker.basketball.tw.summary;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.recordboard.TeamObj;
import com.anklebreaker.basketball.tw.util.MultiDevInit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GameSummaryActivity extends Activity{

    ListView mListView = null;
    ListAdapter mAdapter = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_summary_layout);
        if(mListView == null){
            mListView = (ListView)findViewById(R.id.playhistory);	
        }
        if(mAdapter == null){
            mAdapter = new GameSummaryAdapter(this, TeamObj.undoStack);
        }
        mListView.setAdapter(mAdapter);
        
        // row 1
        TextView title = (TextView) findViewById(R.id.title);
        TextView setting = (TextView) findViewById(R.id.setting);
        title.getLayoutParams().width = MultiDevInit.cellW*14;
        setting.getLayoutParams().width = MultiDevInit.cellW*2;
        // row 2
        TextView mMyteam = (TextView) findViewById(R.id.myteam);
        
        TextView mQuarter = (TextView) findViewById(R.id.quarter);
        TextView mTime = (TextView) findViewById(R.id.time);
        
        TextView mCompetitor = (TextView) findViewById(R.id.competitor);

        TextView mNotes = (TextView) findViewById(R.id.notes);
        
        mMyteam.getLayoutParams().width = MultiDevInit.cellW*4;
        int stdHeight = mMyteam.getLayoutParams().height;
        
        mQuarter.getLayoutParams().width = MultiDevInit.cellW*2;
        mQuarter.getLayoutParams().height = stdHeight/2;
        mTime.getLayoutParams().width = MultiDevInit.cellW*2;
        mTime.getLayoutParams().height = stdHeight/2;
        
        mCompetitor.getLayoutParams().width = MultiDevInit.cellW*4;
        //mCompetitor.getLayoutParams().height = MultiDevInit.recordRowH*2;
        
        mNotes.getLayoutParams().width = MultiDevInit.cellW*6;
        //mNotes.getLayoutParams().height = MultiDevInit.recordRowH*2;
        
        //sortingBtn.getLayoutParams().width = MultiDevInit.cellW*2;
        //sortingBtn.getLayoutParams().height = stdHeight;
        
    }

}
