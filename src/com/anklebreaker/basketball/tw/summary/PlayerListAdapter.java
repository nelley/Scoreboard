package com.anklebreaker.basketball.tw.summary;

import java.util.ArrayList;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.util.MultiDevInit;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * adapter for showing players in the main listview
 * */
public class PlayerListAdapter extends BaseAdapter{

    private static final String TAG = "ScoreBoard.PlayerListAdapter";

    private static final int TYPE_MAX_COUNT = 3;
    private static Activity activity;
    private static ArrayList<PlayerObj> playerData;

    private LayoutInflater mInflater;
    private boolean isExpand;
    
    private static final int TYPE_STARTER = 0;
    private static final int TYPE_BENCH = 1;
    private static final int TYPE_EXPAND = 2;
    
    private final int BASE_ID = 1000;

    /**
     * init the data
     * @a activity
     * @player players(all players OR bench players)
     * */
    public PlayerListAdapter(Activity a, ArrayList<PlayerObj> player, boolean f) {
        super();
        activity = a;
        playerData = player;
        isExpand = f;
        this.mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * generate the view
     * @convertView each row in listview
     * @parent listview
     * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView Start");
        // check row's attribute
        PlayerHolder holder = null;
        int type = getItemViewType(position);

        //---------------------------------------------------
        //------------- set the data to holder-------------
        //---------------------------------------------------
        if (convertView == null) {
            holder = new PlayerHolder();
            switch (type) {
            case TYPE_STARTER:
                convertView = this.mInflater.inflate(R.layout.playerlistadapter_list_player, parent, false);
                holder.number = (TextView) convertView.findViewById(R.id.number);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.twomade = (TextView) convertView.findViewById(R.id.twomade);
                holder.twotried = (TextView) convertView.findViewById(R.id.twotried);
                holder.threemade = (TextView) convertView.findViewById(R.id.threemade);
                holder.threetried = (TextView) convertView.findViewById(R.id.threetried);
                holder.ftmade = (TextView) convertView.findViewById(R.id.ftmade);
                holder.fttried = (TextView) convertView.findViewById(R.id.fttried);
                holder.defrebound = (TextView) convertView.findViewById(R.id.defrebound);
                holder.offrebound = (TextView) convertView.findViewById(R.id.offrebound);
                holder.assist = (TextView) convertView.findViewById(R.id.assist);
                holder.block = (TextView) convertView.findViewById(R.id.block);
                holder.steal = (TextView) convertView.findViewById(R.id.steal);
                holder.turnover = (TextView) convertView.findViewById(R.id.turnover);
                holder.foul = (TextView) convertView.findViewById(R.id.foul);
                holder.point = (TextView) convertView.findViewById(R.id.point);

                break;
            case TYPE_BENCH:
                convertView = this.mInflater.inflate(R.layout.playerlistadapter_list_player, parent, false);
                holder.number = (TextView) convertView.findViewById(R.id.number);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.twomade = (TextView) convertView.findViewById(R.id.twomade);
                holder.twotried = (TextView) convertView.findViewById(R.id.twotried);
                holder.threemade = (TextView) convertView.findViewById(R.id.threemade);
                holder.threetried = (TextView) convertView.findViewById(R.id.threetried);
                holder.ftmade = (TextView) convertView.findViewById(R.id.ftmade);
                holder.fttried = (TextView) convertView.findViewById(R.id.fttried);
                holder.defrebound = (TextView) convertView.findViewById(R.id.defrebound);
                holder.offrebound = (TextView) convertView.findViewById(R.id.offrebound);
                holder.assist = (TextView) convertView.findViewById(R.id.assist);
                holder.block = (TextView) convertView.findViewById(R.id.block);
                holder.steal = (TextView) convertView.findViewById(R.id.steal);
                holder.turnover = (TextView) convertView.findViewById(R.id.turnover);
                holder.foul = (TextView) convertView.findViewById(R.id.foul);
                holder.point = (TextView) convertView.findViewById(R.id.point);

                break;
            case TYPE_EXPAND:
                convertView = this.mInflater.inflate(R.layout.playerlistadapter_list_dummy, parent, false);
                holder.dummytext = (TextView) convertView.findViewById(R.id.dummytext);
                holder.bcoach = (ImageView) convertView.findViewById(R.id.coach);
                holder.bmanager = (ImageView) convertView.findViewById(R.id.manager);
                holder.bgatorade = (ImageView) convertView.findViewById(R.id.gatorade);
                
                break;
            }
            convertView.setTag(holder);
        }else{
            // to speed up the listview process
            holder = (PlayerHolder)convertView.getTag();
        }

        //---------------------------------------------------
        //------------- set the data to listview-------------
        //---------------------------------------------------
        switch (type) {
            case TYPE_STARTER:
                // update the data
                holder.number.setText(playerData.get(position).getPlayerNum());
                holder.name.setText(playerData.get(position).getPlayerName());
                holder.twomade.setText(playerData.get(position).recordsArray[2]);
                holder.twotried.setText(playerData.get(position).recordsArray[3]);
                holder.threemade.setText(playerData.get(position).recordsArray[4]);
                holder.threetried.setText(playerData.get(position).recordsArray[5]);
                holder.ftmade.setText(playerData.get(position).recordsArray[6]);
                holder.fttried.setText(playerData.get(position).recordsArray[7]);
                holder.defrebound.setText(playerData.get(position).recordsArray[8]);
                holder.offrebound.setText(playerData.get(position).recordsArray[9]);
                holder.assist.setText(playerData.get(position).recordsArray[10]);
                holder.block.setText(playerData.get(position).recordsArray[11]);
                holder.steal.setText(playerData.get(position).recordsArray[12]);
                holder.turnover.setText(playerData.get(position).recordsArray[13]);
                holder.foul.setText(playerData.get(position).recordsArray[14]);
                holder.point.setText(playerData.get(position).recordsArray[15]);
                // update the layout
                layoutSetting(holder);

                break;
            case TYPE_BENCH:
                // update the data
                holder.number.setText(playerData.get(position).getPlayerNum());
                holder.name.setText(playerData.get(position).getPlayerName());
                holder.twomade.setText(playerData.get(position).recordsArray[2]);
                holder.twotried.setText(playerData.get(position).recordsArray[3]);
                holder.threemade.setText(playerData.get(position).recordsArray[4]);
                holder.threetried.setText(playerData.get(position).recordsArray[5]);
                holder.ftmade.setText(playerData.get(position).recordsArray[6]);
                holder.fttried.setText(playerData.get(position).recordsArray[7]);
                holder.defrebound.setText(playerData.get(position).recordsArray[8]);
                holder.offrebound.setText(playerData.get(position).recordsArray[9]);
                holder.assist.setText(playerData.get(position).recordsArray[10]);
                holder.block.setText(playerData.get(position).recordsArray[11]);
                holder.steal.setText(playerData.get(position).recordsArray[12]);
                holder.turnover.setText(playerData.get(position).recordsArray[13]);
                holder.foul.setText(playerData.get(position).recordsArray[14]);
                holder.point.setText(playerData.get(position).recordsArray[15]);
                // update the layout
                layoutSetting(holder);

                break;
            case TYPE_EXPAND:
                // update the data
                if(!isExpand){
                    // hide the bench players when expanding 
                    holder.bcoach.setVisibility(View.GONE);
                    holder.bmanager.setVisibility(View.GONE);
                    holder.bgatorade.setVisibility(View.GONE);
                }else{
                    int totalplayers = playerData.size();
                    // show the bench players when folding
                    int lastId = holder.bmanager.getId();
                    /*include dummy row*/
                    for(int i=0; i<totalplayers-6; i++){
                        final ImageView iv = new ImageView(activity);
                        iv.setId( BASE_ID + i);
                        RelativeLayout.LayoutParams lay = 
                                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                                                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        if(i==0){
                            // first bench player located in benchstart's right side
                            lay.addRule(RelativeLayout.RIGHT_OF, R.id.manager);	
                        }else{
                            // others located in the new added player's right side 
                            lay.addRule(RelativeLayout.RIGHT_OF, lastId);
                        }
                        lay.addRule(RelativeLayout.CENTER_VERTICAL);
                        iv.setLayoutParams(lay);
                        // change icon's image here
                        iv.setImageDrawable(activity.getResources().getDrawable(R.drawable.home));
                        // set ondragndrop listener for changing players
                        iv.setOnClickListener(new OnClickListener(){
                            @Override
                            public void onClick(View v) {
	                            // TODO Auto-generated method stub
                                Toast.makeText(activity, String.valueOf(iv.getId()), Toast.LENGTH_SHORT).show();
                                // 
                            }
                        });
                        ((ViewGroup) convertView).addView(iv);
                        // update the lastId
                        lastId = iv.getId();
                    }
                    // in order to set the player in the rightmost, get the item's params object
                    RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) holder.bgatorade.getLayoutParams();
                    params.addRule(RelativeLayout.RIGHT_OF, lastId);
                    
                   // set coach's desicion
                    holder.bcoach.setOnClickListener(new OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(activity, "coach!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // set manager's function
                    holder.bmanager.setOnClickListener(new OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(activity, "manager!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // set Gatorade's function
                    holder.bgatorade.setOnClickListener(new OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(activity, "gatorade!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    
                }
                
                // update the layout
                break;
        }
        return convertView;
    }

    /**
     * holder for player row
     * */
    static class PlayerHolder {
        // for players
        TextView number,name,
                 twomade,twotried,threemade,threetried,ftmade, fttried,
                 defrebound,offrebound,assist,block,steal,turnover,foul,
                 point;
        
        // for expand
        TextView dummytext;
        ImageView bcoach,bmanager, bgatorade;
    }

    /**
     * control the size in the listview
     * */
   @Override
    public int getCount() {
        // contain the expand banner for user click
        if(isExpand){
            // starters only
            return 6;
        }else{
            // all players
            return playerData.size();
        }
        
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

	@Override
    public Object getItem(int position) {
        return playerData.get(position);
    }

	@Override
    public long getItemId(int position) {
	    return 0;
    }

	@Override
    public int getItemViewType(int position) {
        if(playerData.get(position).getIsStarter()){
            return TYPE_STARTER;
        }

        if(playerData.get(position).getIsBench()){
            return TYPE_BENCH;
        }else{
            return TYPE_EXPAND;
        }
    }

    /**
     * enable/disable clickable for every row
     **/
    @Override
    public boolean isEnabled(int position) {
        //Log.i(TAG, "position enabled:" + position);
        // bench players can not click
        if(position >5){
            return false;
        }
        return super.isEnabled(position);
    }

    /**
     * set the cell's width & height
     * */
    public void layoutSetting(PlayerHolder mHolder){
        // update the layout width
        mHolder.number.getLayoutParams().width = MultiDevInit.cellW;
        mHolder.name.getLayoutParams().width = MultiDevInit.cellW;
        mHolder.twomade.getLayoutParams().width = MultiDevInit.cellW;
        mHolder.twotried.getLayoutParams().width = MultiDevInit.cellW;
        mHolder.threemade.getLayoutParams().width = MultiDevInit.cellW;
        mHolder.threetried.getLayoutParams().width = MultiDevInit.cellW;
        mHolder.ftmade.getLayoutParams().width = MultiDevInit.cellW;
        mHolder.fttried.getLayoutParams().width = MultiDevInit.cellW;
        mHolder.defrebound.getLayoutParams().width = MultiDevInit.cellW;
        mHolder.offrebound.getLayoutParams().width = MultiDevInit.cellW;
        mHolder.assist.getLayoutParams().width = MultiDevInit.cellW;
        mHolder.block.getLayoutParams().width = MultiDevInit.cellW;
        mHolder.steal.getLayoutParams().width = MultiDevInit.cellW;
        mHolder.turnover.getLayoutParams().width = MultiDevInit.cellW;
        mHolder.foul.getLayoutParams().width = MultiDevInit.cellW;
        mHolder.point.getLayoutParams().width = MultiDevInit.cellW;
        // update the layout height
        mHolder.number.getLayoutParams().height = MultiDevInit.IndicatorH;
    }
}
