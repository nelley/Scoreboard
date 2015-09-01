package com.anklebreaker.basketball.tw.summary;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Point;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

public class DragNDropTouchListener implements OnLongClickListener {
    Activity mActivity = null;
    TextView vNumber = null;
    
    public DragNDropTouchListener(Activity m, TextView n){
        this.mActivity = m;
        this.vNumber = n;
    }
    
    @Override
    public boolean onLongClick(View v) {
        //get the player number from item which been long clicked
        ClipData.Item item = new ClipData.Item(((TextView)v).getText());
        String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
        //第一引数にClipDataのラベル名、第二引数に格納するコンテンツのタイプ、第三引数に格納するデータを指定します。
        ClipData data = new ClipData("PLAYER_CHANGE", mimeTypes, item);
        //DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
        DragShadowBuilder shadowBuilder = new CustomDragShadowBuilder(mActivity, v);
        //playerView pV = new playerView(view, playerInfo.getImage(), playerInfo.getTitle(), mBenchAdapter, position);
        
        v.startDrag( data, //data to be dragged
            shadowBuilder, //drag shadow
            vNumber,       //local data about the drag and drop operation
            0//no needed flags
            );
        return true;
    }


}
