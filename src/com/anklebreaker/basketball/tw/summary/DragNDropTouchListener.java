package com.anklebreaker.basketball.tw.summary;

import java.util.ArrayList;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.recordboard.Player;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

public class DragNDropTouchListener implements OnLongClickListener {

    private Activity mActivity = null;
    private TextView vNumber = null;
    private Player player = null;
    private String CLIP_LABEL = "";
    private final String TEAM_A = "TEAM_A_CHANGE";
    private final String TEAM_B = "TEAM_B_CHANGE";
    
    public DragNDropTouchListener(Activity m, TextView n, Player p){
        this.mActivity = m;
        this.vNumber = n;
        this.player = p;
    }
    
    @Override
    public boolean onLongClick(View v) {
        //get the player number from item which been long clicked
        ClipData.Item item = new ClipData.Item(((TextView)v).getText());
        String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
        if(player instanceof PlayerObj){
            CLIP_LABEL = TEAM_A;
        }else{
            CLIP_LABEL = TEAM_B;
        }
        
        //第一引数にClipDataのラベル名、第二引数に格納するコンテンツのタイプ、第三引数に格納するデータを指定します。
        ClipData data = new ClipData(CLIP_LABEL, mimeTypes, item);
        //DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
        DragShadowBuilder shadowBuilder = new CustomDragShadowBuilder(mActivity, v);
        //playerView pV = new playerView(view, playerInfo.getImage(), playerInfo.getTitle(), mBenchAdapter, position);
        
        v.startDrag( data, //data to be dragged
            shadowBuilder, //drag shadow
            vNumber,       //local data about the drag and drop operation
            0              //no needed flags
            );
        return true;
    }


}
