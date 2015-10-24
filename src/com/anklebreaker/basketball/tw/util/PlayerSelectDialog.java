package com.anklebreaker.basketball.tw.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.recordboard.TeamObj;
import com.anklebreaker.basketball.tw.summary.PlayerGridViewAdapter;
import com.anklebreaker.basketball.tw.tab.BasketFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class PlayerSelectDialog extends Dialog implements android.view.View.OnClickListener{

    private static final String TAG = "PlayerSelectDialog";
    
    // initial players for selection
    private static PlayerGridViewAdapter mInitialAdapter;
    
    private Context mContext;
    private Button yesBtn;
    public static final String DEFAUT_STRING = "";
    
    public PlayerSelectDialog(Context c) {
        super(c);
        this.mContext = c;
    }

    /**
     * init player selection dailog
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // set dialog's size
        setContentView(R.layout.player_select_dialog);
        LayoutParams params = getWindow().getAttributes();
        params.width = MultiDevInit.xPIXEL*3/4;
        params.height = MultiDevInit.yPIXEL*9/10;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        
        // set gridview's size
        GridView setPlayers = (GridView) this.findViewById(R.id.playergrid);
        setPlayers.getLayoutParams().height = MultiDevInit.yPIXEL*6/10;
        
        Button yesBtn = (Button)findViewById(R.id.btn_yes);
        yesBtn.setBackground(mContext.getResources().getDrawable(R.drawable.btn_selector));
        
        yesBtn.setOnClickListener(this);
        //yesBtn.setOnKeyListener(this);
        
        // get player list when last time using
        SharedPreferences pref = mContext.getSharedPreferences(TeamObj.PLAYER_FILE_NAME, Context.MODE_PRIVATE);
        Map<String,?> keys = pref.getAll();
        
        BasketFragment.player_settingGrid.clear();
        int mapSize = keys.size();
        if(mapSize == 0){
            for(int i=0; i<10; i++){// first time use this app
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
        }else{// get info of players from pref
            List<String> player_array = new ArrayList<String>();
            for(Map.Entry<String,?> entry : keys.entrySet()){
                player_array.add(pref.getString(entry.getKey(), DEFAUT_STRING));
            }
            Collections.sort(player_array);
            
            for(int i=0; i<player_array.size(); i++){
                BasketFragment.player_settingGrid.add(new PlayerObj(mContext,
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.unchecked),
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.basketball_player),
                        player_array.get(i),
                        /*default name*/player_array.get(i),
                        false,
                        false,
                        false));
            }
            
            BasketFragment.player_settingGrid.add(new PlayerObj(mContext,
                    BitmapFactory.decodeResource(mContext.getResources(), R.drawable.unchecked),
                    BitmapFactory.decodeResource(mContext.getResources(), R.drawable.plus),
                    "新增球員",
                    "name",
                    false,
                    false,
                    false));
        	
        }
        
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

    public static PlayerGridViewAdapter getmInitialAdapter() {
        return mInitialAdapter;
    }
}
