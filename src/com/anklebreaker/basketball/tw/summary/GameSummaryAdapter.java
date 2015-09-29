package com.anklebreaker.basketball.tw.summary;

import java.util.Stack;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.engine.RecordEngine;
import com.anklebreaker.basketball.tw.recordboard.CompetitorObj;
import com.anklebreaker.basketball.tw.recordboard.Player;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.recordboard.TeamObj;
import com.anklebreaker.basketball.tw.util.MultiDevInit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameSummaryAdapter extends BaseAdapter{

    private final String TAG = "ScoreBoard.Game_Summary_Adapter";

    private Context mContext;
    private Stack<Player> mPlayerList;
    private LayoutInflater mInflater;
    
    public GameSummaryAdapter(Context c, Stack<Player> playerList){
        this.mContext = c;
        this.mPlayerList = playerList;
        this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView Start");
        convertView = this.mInflater.inflate(R.layout.game_summary_listview, parent, false);
        
        TextView mtContent = (TextView) convertView.findViewById(R.id.myteam_content);
        TextView qContent = (TextView) convertView.findViewById(R.id.quarter);
        TextView tContent = (TextView) convertView.findViewById(R.id.time);
        TextView cContent = (TextView) convertView.findViewById(R.id.competitor_content);
        
        RelativeLayout row = (RelativeLayout) convertView.findViewById(R.id.game_summary_content);
        row.getLayoutParams().height = MultiDevInit.recordRowH;
        
        mtContent.getLayoutParams().width = MultiDevInit.cellW*4;
        qContent.getLayoutParams().width = MultiDevInit.cellW*2;
        tContent.getLayoutParams().width = MultiDevInit.cellW*2;
        cContent.getLayoutParams().width = MultiDevInit.cellW*4;
        
        qContent.getLayoutParams().height = MultiDevInit.recordRowH/2;
        tContent.getLayoutParams().height = MultiDevInit.recordRowH/2;
        
        if (TeamObj.undoStack.get(position) instanceof PlayerObj) {
            // get the player obj
            PlayerObj mPlayerObj = (PlayerObj) TeamObj.undoStack.get(position);
            // get & set the player action
            StringBuffer actionText = new StringBuffer(mPlayerObj.getPlayerNum());
            // translate the int to action text
            actionText.append(RecordEngine.actionToText(mPlayerObj.playerAct, ""));
            mtContent.setText(actionText.toString());
            
            // set the time of that action
            tContent.setText(mPlayerObj.actTime);
            
            
            // set null to competitor's action
            //cContent.setText(String.valueOf("NA"));
            
        }else{
            // get the competitor obj
            CompetitorObj CPObj = (CompetitorObj) TeamObj.undoStack.get(position);
            // get & set the player action
            StringBuffer actionText = new StringBuffer(CPObj.getPlayerNum());
            // translate the int to action text
            actionText.append(RecordEngine.actionToText(CPObj.playerAct, ""));
            cContent.setText(actionText.toString());
            // set the time of that action
            tContent.setText(CPObj.actTime);
            
            // set null to player obj's action
            //mtContent.setText(String.valueOf("NA"));
            
            

            
        }
        
        // set the quarter
        qContent.setText(TeamObj.qString[mPlayerList.get(position).getQuarter()]);
        
        // set the width & hieght
        
        
        return convertView;
    }
    
    @Override
    public int getCount() {
        return mPlayerList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPlayerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}