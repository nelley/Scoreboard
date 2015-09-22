package com.anklebreaker.basketball.tw.util;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.def.ActionDef;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.recordboard.TeamObj;
import com.anklebreaker.basketball.tw.summary.PlayerListAdapter;
import com.anklebreaker.basketball.tw.summary.SummaryPage;
import com.anklebreaker.basketball.tw.tab.BasketFragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ListView;

/**
 * class for restart dialog
 * */
public class Custom_alert_Dialog extends Dialog implements android.view.View.OnClickListener {

    private static final String TAG = "util.Custom_alert_Dialog";
    
    private Activity mActivity;
    private Button yesBtn, noBtn;
    private PlayerListAdapter def_list_adapter;
    private ListView mListView;
    
    public Custom_alert_Dialog(Activity c, ListView lv) {
        super(c);
        this.mActivity = c;
        this.mListView = lv;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.custom_alert_dialog);
        LayoutParams params = getWindow().getAttributes();
        params.width = MultiDevInit.xPIXEL/2;
        params.height = MultiDevInit.yPIXEL/3;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        
        
        yesBtn = (Button) findViewById(R.id.btn_yes);
        noBtn = (Button) findViewById(R.id.btn_no);
        yesBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);
    }

    /**
     * handling the click actions
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                // enable player select dialog
                BasketFragment.setMenu_flg = false;
                // reset listview
                SummaryPage.IS_EXPAND = false;
                
                PlayerObj.playerMap.clear();
                TeamObj.undoStack.clear();
                SummaryPage.resetFoul();
                SummaryPage.resetScore();
                SummaryPage.resetTimer();
                Log.i(TAG, "cleared!!");
                Log.i(TAG, "playerMap: " + PlayerObj.playerMap.toString());
                Log.i(TAG, "undo stack: " + TeamObj.undoStack.toString());
                
                // re-locate default players()
                def_list_adapter = new PlayerListAdapter(mActivity, ActionDef.defaultTotalPlayer, false);
                mListView.setAdapter(def_list_adapter);	
                def_list_adapter.notifyDataSetChanged();
                
                // re-select players
                PlayerSelectDialog PSDialog = new PlayerSelectDialog(mActivity);
                PSDialog.setCanceledOnTouchOutside(false);
                PSDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                PSDialog.show();
                
                // dissmiss previous dialog
                SummaryPage.dismissDialog("setting");
                
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}