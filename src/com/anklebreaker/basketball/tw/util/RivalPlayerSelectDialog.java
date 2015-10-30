package com.anklebreaker.basketball.tw.util;

import java.util.ArrayList;
import java.util.Collections;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RivalPlayerSelectDialog extends Dialog implements android.view.View.OnClickListener{

    private static final String TAG = "RivalPlayerSelectDialog";
    
    private Context mContext;
    private ListView mListView;
    private Button yesBtn;
    public static final String DEFAUT_STRING = "";
    
    /**
     * @mListView the target of update listview
     * */
    public RivalPlayerSelectDialog(Context c, ListView mListView) {
        super(c);
        this.mContext = c;
        this.mListView = mListView;
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
        
        // set color list's size
        for(int i=0; i<TeamObj.teamColor.length; i++){
            final ImageView tmpIV = (ImageView) findViewById(TeamObj.teamColor[i]);
            tmpIV.getLayoutParams().width = MultiDevInit.IndicatorH;
            tmpIV.getLayoutParams().height = MultiDevInit.IndicatorH;
            tmpIV.setTag(TeamObj.teamColorCode[i]);
            tmpIV.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                	// remove selected color
                    TeamObj.teamColorKeeper[1] = -1;
                    // remove all the select icon
                    for(int j=0; j<TeamObj.teamColor.length; j++){
                        ((ImageView)findViewById(TeamObj.teamColor[j])).setImageResource(android.R.color.transparent);	
                    }
                    // set the image icon to the selected one
                    tmpIV.setImageResource(R.drawable.select_icon);
                    // keep the select color
                    TeamObj.teamColorKeeper[1] = (int) v.getTag();
                }
            });
        }
        
        // set gridview's size
        GridView setPlayers = (GridView) this.findViewById(R.id.playergrid);
        setPlayers.getLayoutParams().height = MultiDevInit.yPIXEL*6/10;
        
        Button yesBtn = (Button)findViewById(R.id.btn_yes);
        yesBtn.setBackground(mContext.getResources().getDrawable(R.drawable.btn_selector));
        
        // change the title's text
        TextView title = (TextView) this.findViewById(R.id.txt_title);
        title.setText("請設定B隊的球衣顏色,比賽球員");
        
        yesBtn.setOnClickListener(this);
        //yesBtn.setOnKeyListener(this);
        
        // get player list when last time using
        SharedPreferences pref = mContext.getSharedPreferences(TeamObj.RIVAL_PLAYER_FILE_NAME, Context.MODE_PRIVATE);
        Map<String,?> keys = pref.getAll();
        
        // reset player_settingGrid
        TeamObj.player_settingGrid.clear();
        int mapSize = keys.size();
        if(mapSize == 0){
            for(int i=0; i<10; i++){// first time use this app
                if(i<9){
                    // mContext, image_check, image, playerNo, playerName, isStarter, isBench, isOnplay
                    TeamObj.player_settingGrid.add(new PlayerObj(mContext,
                                                    BitmapFactory.decodeResource(mContext.getResources(), R.drawable.unchecked),
                                                    BitmapFactory.decodeResource(mContext.getResources(), R.drawable.basketball_player),
                                                    String.valueOf(i)+"號",
                                                    /*default name*/String.valueOf(i)+"號",
                                                    false,
                                                    false,
                                                    false));
                }else{//「+」icon
                    TeamObj.player_settingGrid.add(new PlayerObj(mContext,
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
                TeamObj.player_settingGrid.add(new PlayerObj(mContext,
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.unchecked),
                        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.basketball_player),
                        player_array.get(i),
                        /*default name*/player_array.get(i),
                        false,
                        false,
                        false));
            }
            
            TeamObj.player_settingGrid.add(new PlayerObj(mContext,
                    BitmapFactory.decodeResource(mContext.getResources(), R.drawable.unchecked),
                    BitmapFactory.decodeResource(mContext.getResources(), R.drawable.plus),
                    "新增球員",
                    "name",
                    false,
                    false,
                    false));
        
        }
        
        TeamObj.mInitialAdapter = new PlayerGridViewAdapter(mContext, R.layout.player_grid, TeamObj.player_settingGrid);
        setPlayers.setAdapter(TeamObj.mInitialAdapter);
    }


    @Override
    public void onClick(View v) {
        String set_text = null;
        switch (v.getId()) {
        case R.id.btn_yes:
            // set the players
            set_text = TeamObj.setByUserRival(v, mContext, mListView);
            if(set_text=="ok"){
                BasketFragment.setMenu_flg = true;
                // update color of score's underline
                BasketFragment.setScoreTextBorder(1);
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
