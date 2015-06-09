package com.anklebreaker.basketball.tw.summary;

import java.util.ArrayList;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.recordboard.Item;
import com.anklebreaker.basketball.tw.util.MultiDevInit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

public class PlayerListAdapter extends BaseAdapter{

	private static Activity activity;
	private static ArrayList<Item> data;
	private LayoutInflater mInflater;
	
	public PlayerListAdapter(Activity a, ArrayList<Item> d) {
        super();
        activity = a;
        data = d;
        this.mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = this.mInflater.inflate(R.layout.list_item, null);
		}
		// set the height of every row
		TextView number = (TextView)convertView.findViewById(R.id.number);
		TextView name = (TextView)convertView.findViewById(R.id.name);
		
		TextView twomade = (TextView)convertView.findViewById(R.id.twomade);
		TextView twotried = (TextView)convertView.findViewById(R.id.twotried);
		TextView threemade = (TextView)convertView.findViewById(R.id.threemade);
		TextView threetried = (TextView)convertView.findViewById(R.id.threetried);
		TextView ftmade = (TextView)convertView.findViewById(R.id.ftmade);
		TextView fttried = (TextView)convertView.findViewById(R.id.fttried);
		TextView defrebound = (TextView)convertView.findViewById(R.id.defrebound);
		TextView offrebound = (TextView)convertView.findViewById(R.id.offrebound);
		TextView assist = (TextView)convertView.findViewById(R.id.assist);
		TextView block = (TextView)convertView.findViewById(R.id.block);
		TextView steal = (TextView)convertView.findViewById(R.id.steal);
		TextView turnover = (TextView)convertView.findViewById(R.id.turnover);
		TextView foul = (TextView)convertView.findViewById(R.id.foul);
		TextView point = (TextView)convertView.findViewById(R.id.point);
		
		// set the width of every cell 
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
		
		
		number.setText(data.get(position).getTitle());
		name.setText(data.get(position).getTitle());
		/*
		twomade.setText("0");
		twotried.setText("0");
		threemade.setText("0");
		threetried.setText("0");
		ftmade.setText("0");
		fttried.setText("0");
		defrebound.setText("0");
		offrebound.setText("0");
		assist.setText("0");
		block.setText("0");
		steal.setText("0");
		turnover.setText("0");
		foul.setText("0");
		point.setText("0");
		*/
		
		
		return convertView;
	}
	
	@Override
    public int getCount() {
	    return data.size();
    }

	@Override
    public Object getItem(int position) {
	    return null;
    }

	@Override
    public long getItemId(int position) {
	    return 0;
    }
}
