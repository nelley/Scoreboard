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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * adapter for showing players in the main listview
 * */

public class PlayerListAdapter extends BaseAdapter{


    private static final String TAG = "ScoreBoard.PlayerListAdapter";

    private static final int TYPE_MAX_COUNT = 3;
    private static Activity activity;
    private static ArrayList<PlayerObj> playerData;

    private LayoutInflater mInflater;

    private static final int TYPE_STARTER = 0;
    private static final int TYPE_BENCH = 1;
    private static final int TYPE_EXPAND = 2;

    /**
     * init the data
     * @a activity
     * @player players(all players OR bench players)
     * */
    public PlayerListAdapter(Activity a, ArrayList<PlayerObj> player) {
        super();
        activity = a;
        playerData = player;
        this.mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * generate the view
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
                holder.dummyimage = (ImageView) convertView.findViewById(R.id.dummyimage);
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

                // update the layout
                layoutSetting(holder);

                break;
            case TYPE_BENCH:
                // update the data
                holder.number.setText(playerData.get(position).getPlayerNum());
                holder.name.setText(playerData.get(position).getPlayerName());

                // update the layout
                layoutSetting(holder);

                break;
            case TYPE_EXPAND:
                // update the data

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
        ImageView dummyimage;
    }

	/**
	 * control the size in the listview
	 * */
	@Override
    public int getCount() {
        // contain the expand banner for user click
        return playerData.size();
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
