package com.anklebreaker.basketball.tw.util;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.recordboard.TeamObj;
import com.anklebreaker.basketball.tw.summary.PlayerGridViewAdapter;
import com.anklebreaker.basketball.tw.tab.BasketFragment;
import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerSelectDialog extends Dialog implements android.view.View.OnClickListener{

    private static final String TAG = "PlayerSelectDialog";
    
    private PlayerGridViewAdapter mInitialAdapter;
    private Context mContext;
    private Button yesBtn;
    
    public PlayerSelectDialog(Context c) {
        super(c);
        this.mContext = c;
    }

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.player_select_dialog);
        LayoutParams params = getWindow().getAttributes();
        params.width = MultiDevInit.xPIXEL*3/4;
        params.height = MultiDevInit.yPIXEL*9/10;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        
        GridView setPlayers = (GridView) this.findViewById(R.id.playergrid);
        Button yesBtn = (Button)findViewById(R.id.btn_yes);
        
        yesBtn.setOnClickListener(this);
        //yesBtn.setOnKeyListener(this);
        
        BasketFragment.player_settingGrid.clear();
        for(int i=0; i<10; i++){
            if(i<9){
                // mContext, image_check, image, playerNo, playerName, isStarter, isBench, isOnplay
                BasketFragment.player_settingGrid.add(new PlayerObj(mContext,
                                                BitmapFactory.decodeResource(mContext.getResources(), R.drawable.unchecked),
                                                BitmapFactory.decodeResource(mContext.getResources(), R.drawable.basketball_player),
                                                String.valueOf(i)+"號",
                                                /*default name*/String.valueOf(i)+"號",
                                                false,
                                                false,
                                                false));
            }else{//「+」icon
                BasketFragment.player_settingGrid.add(new PlayerObj(mContext,
                                                BitmapFactory.decodeResource(mContext.getResources(), R.drawable.unchecked),
                                                BitmapFactory.decodeResource(mContext.getResources(), R.drawable.plus),
                                                "新增球員",
                                                "name",
                                                false,
                                                false,
                                                false));
            }
        }
        
        //mInitialAdapter = BasketFragment.getInitialAdapter();
        mInitialAdapter = new PlayerGridViewAdapter(mContext, R.layout.player_grid, BasketFragment.player_settingGrid);
        setPlayers.setAdapter(mInitialAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_yes:
            String set_text = TeamObj.setByUser(v, mContext);
            if(set_text=="ok"){
                BasketFragment.setMenu_flg = true;
                dismiss();
            }else{
                Toast.makeText(mContext, set_text, Toast.LENGTH_SHORT).show();
            }
            break;
        default:
            break;
        }
    }


	@Override
    public void onBackPressed() {
        //super.onBackPressed();
         Toast.makeText(mContext, "請重新選擇球員", Toast.LENGTH_SHORT).show();
    }
    

}
