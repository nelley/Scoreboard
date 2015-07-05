package com.anklebreaker.basketball.tw.summary;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

public class DragNDropTouchListener implements OnLongClickListener {
    Activity mActivity = null;
    //RecordGridViewAdapter mBenchAdapter = null;
    
    public DragNDropTouchListener(Activity m){
        this.mActivity = m;
    }
    
    @Override
    public boolean onLongClick(View v) {
        //get the player number from item which been long clicked
        ClipData.Item item = new ClipData.Item(((TextView)v).getText());
        String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
        //第一引数にClipDataのラベル名、第二引数に格納するコンテンツのタイプ、第三引数に格納するデータを指定します。
        ClipData data = new ClipData("PLAYER_CHANGE", mimeTypes, item);
        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
        //playerView pV = new playerView(view, playerInfo.getImage(), playerInfo.getTitle(), mBenchAdapter, position);
        
        v.startDrag( data, //data to be dragged
            shadowBuilder, //drag shadow
            null/*pv*/, //local data about the drag and drop operation
            0//no needed flags
            );
        return true;
    }


}
