package com.anklebreaker.basketball.tw.recordboard;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class PlayerChangeListener implements OnItemLongClickListener{
	Activity mActivity = null;
	RecordGridViewAdapter mBenchAdapter = null;
	
	public PlayerChangeListener(Activity m, RecordGridViewAdapter BGA){
		this.mActivity = m;
		this.mBenchAdapter = BGA;
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		//get the value from item which been long clicked
		ClipData.Item item = new ClipData.Item((CharSequence)"player changed");
		PlayerObj playerInfo = (PlayerObj)parent.getItemAtPosition(position);
		
		String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
		//第一引数にClipDataのラベル名、第二引数に格納するコンテンツのタイプ、第三引数に格納するデータを指定します。
		ClipData data = new ClipData("NELLEY", mimeTypes, item);
		
		DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
		PlayerView pV = new PlayerView(view, playerInfo.getImage(), playerInfo.getPlayerNum(), mBenchAdapter, position);
		
		view.startDrag( data, //data to be dragged
			shadowBuilder, //drag shadow
			pV, //local data about the drag and drop operation
			0//no needed flags
		);
		
		//view.setVisibility(View.INVISIBLE);
		return true;
	}

}
