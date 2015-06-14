package com.anklebreaker.basketball.tw.summary;

import java.util.ArrayList;
import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.util.MultiDevInit;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayerListAdapter extends BaseAdapter{
	
	private static final String TAG = "ScoreBoard.PlayerListAdapter";
	
	private static Activity activity;
	private static ArrayList<PlayerObj> playerData;
	private LayoutInflater mInflater;
	private boolean blank_flag = true; 
	
	private static final int TYPE_STARTER = 0;
    private static final int TYPE_BENCH = 1;
    private static final int TYPE_BLANK = 2;
    

	public PlayerListAdapter(Activity a, ArrayList<PlayerObj> player) {
        super();
        activity = a;
        playerData = player;
        this.mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if(convertView == null){
            switch (type) {
            case TYPE_STARTER:
                convertView = this.mInflater.inflate(R.layout.playerlistadapter_list_player, null);
                rowCellInit(convertView, position);
                break;
            case TYPE_BENCH:
                convertView = this.mInflater.inflate(R.layout.playerlistadapter_list_bench, null);
                break;
            }
        }
        return convertView;
    }

	/**
	 * init the row and cell of every player
	 * */
	private void rowCellInit(View cV, int pos) {
		// all attribute in the score board(16 records)
		TextView number = (TextView)cV.findViewById(R.id.number);
		TextView name = (TextView)cV.findViewById(R.id.name);
		TextView twomade = (TextView)cV.findViewById(R.id.twomade);
		TextView twotried = (TextView)cV.findViewById(R.id.twotried);
		TextView threemade = (TextView)cV.findViewById(R.id.threemade);
		TextView threetried = (TextView)cV.findViewById(R.id.threetried);
		TextView ftmade = (TextView)cV.findViewById(R.id.ftmade);
		TextView fttried = (TextView)cV.findViewById(R.id.fttried);
		TextView defrebound = (TextView)cV.findViewById(R.id.defrebound);
		TextView offrebound = (TextView)cV.findViewById(R.id.offrebound);
		TextView assist = (TextView)cV.findViewById(R.id.assist);
		TextView block = (TextView)cV.findViewById(R.id.block);
		TextView steal = (TextView)cV.findViewById(R.id.steal);
		TextView turnover = (TextView)cV.findViewById(R.id.turnover);
		TextView foul = (TextView)cV.findViewById(R.id.foul);
		TextView point = (TextView)cV.findViewById(R.id.point);

		// set the width of every cell(16 cells)
		number.getLayoutParams().width = MultiDevInit.cellW;
		name.getLayoutParams().width = MultiDevInit.cellW;
		twomade.getLayoutParams().width = MultiDevInit.cellW;
		twotried.getLayoutParams().width = MultiDevInit.cellW;
		threemade.getLayoutParams().width = MultiDevInit.cellW;
		threetried.getLayoutParams().width = MultiDevInit.cellW;
		ftmade.getLayoutParams().width = MultiDevInit.cellW;
		fttried.getLayoutParams().width = MultiDevInit.cellW;
		defrebound.getLayoutParams().width = MultiDevInit.cellW;
		offrebound.getLayoutParams().width = MultiDevInit.cellW;
		assist.getLayoutParams().width = MultiDevInit.cellW;
		block.getLayoutParams().width = MultiDevInit.cellW;
		steal.getLayoutParams().width = MultiDevInit.cellW;
		turnover.getLayoutParams().width = MultiDevInit.cellW;
		foul.getLayoutParams().width = MultiDevInit.cellW;
		point.getLayoutParams().width = MultiDevInit.cellW;

		// set the height of the cell
		number.getLayoutParams().height = MultiDevInit.IndicatorH;

		// set the content of the player's number and name
		number.setText(playerData.get(pos).getPlayerNum());
		name.setText(playerData.get(pos).getPlayerNum());
	}

	@Override
    public int getCount() {
	    return playerData.size();
    }

	@Override
    public Object getItem(int position) {
	    return null;
    }

	@Override
    public long getItemId(int position) {
	    return 0;
    }
	
	@Override
	public int getItemViewType(int position) {
		return playerData.get(position).getIsStarter()?TYPE_STARTER : TYPE_BENCH;
	}
}
