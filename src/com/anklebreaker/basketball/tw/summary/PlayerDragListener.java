package com.anklebreaker.basketball.tw.summary;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.recordboard.TeamObj;
import com.anklebreaker.basketball.tw.util.Utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerDragListener implements OnDragListener{

    Context mContext = null;
   
    public PlayerDragListener(Activity c){
        mContext = c;
    }
    
    @Override
    public boolean onDrag(View v, DragEvent event) {

        // Handles each of the expected events
        //Drawable normalShape = mContext.getResources().getDrawable(R.drawable.normal_color);
        //Drawable targetShape = mContext.getResources().getDrawable(R.drawable.target_shape);
        
        TextView draggedV = (TextView)event.getLocalState();
        View dragPv = (View) draggedV.getParent();
        
        TextView destinationV = (TextView)v;
        View desPv = (View) destinationV.getParent();
        TextView nameView = (TextView) desPv.findViewById(R.id.name);
        
        switch (event.getAction()) {
        //signal for the start of a drag and drop operation.
            case DragEvent.ACTION_DRAG_STARTED:
                Utilities.CustomToast(((Activity)mContext), "請拖曳到板凳區或背號上以進行更換");
                
                break;
                
                //the drag point has entered the bounding box of the View
            case DragEvent.ACTION_DRAG_ENTERED:
                // have to handle different layout object(textview to tablerow)
                v.setBackgroundColor(Color.argb(142, 142, 142, 142));
                if(nameView != null){
                    nameView.setBackgroundColor(Color.argb(142, 142, 142, 142));
                }
                // change the color(#8e8e8e) of the view
                break;

                //the user has moved the drag shadow outside the bounding box of the View
            case DragEvent.ACTION_DRAG_EXITED:
                if(desPv instanceof RelativeLayout){
                    // bench players
                    v.setBackgroundColor(Color.TRANSPARENT);
                }else{
                    // change starters's background to white
                    v.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    if(nameView != null){
                        nameView.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    }
                }
                
                break;
                
            //drag shadow has been released,the drag point is within the bounding box of the View
            case DragEvent.ACTION_DROP:
                // retrieve the data from dragged item
                String num = (String) event.getClipData().getItemAt(0).getText();
                
                String pstr = "號"; 
                Pattern p = Pattern.compile(pstr);
                
                Matcher m = p.matcher(num);
                if(!m.find()){
                    num = num + "號";
                }

                // retrieve the dragged player
                int drag = 0;
                for(; drag< PlayerObj.playerMap.size(); drag++){
                    String pNum = PlayerObj.playerMap.get(drag).getPlayerNum();
                    if(pNum.equals(num)){
                        break;
                    }
                }
                
                // retrieve the data from dropped location
                String droppedNum = (String) destinationV.getText();
                m = p.matcher(droppedNum);
                if(!m.find()){
                    droppedNum = droppedNum  + "號";
                }
                int drop = 0;
                for(; drop< PlayerObj.playerMap.size(); drop++){
                    String pNum = PlayerObj.playerMap.get(drop).getPlayerNum();
                    if(pNum.equals(droppedNum)){
                        break;
                    }
                }

                if(PlayerObj.playerMap.get(drop).getIsOnPlay() != 
                        PlayerObj.playerMap.get(drag).getIsOnPlay()){

                    // change the status
                    PlayerObj.playerReplace(drop);
                    PlayerObj.playerReplace(drag);
                    
                    // change the number in layout
                    destinationV.setText(PlayerObj.playerMap.get(drag).getPlayerNum());
                    draggedV.setText(PlayerObj.playerMap.get(drop).getPlayerNum());
                    
                    // swap the position
                    Collections.swap(PlayerObj.playerMap, drag, drop);
                    
                    // update the layout
                    View rootView = (View) destinationV.getRootView();
                    ListView mListView = (ListView) rootView.findViewById(R.id.player_list);
                    
                    mListView.setAdapter(TeamObj.mPlayerListAdapter);
                    TeamObj.mPlayerListAdapter.notifyDataSetChanged();
                    /*
                    mListView.setAdapter(SummaryPage.select_list_adapter);
                    SummaryPage.select_list_adapter.notifyDataSetChanged();
                    */
                    Toast.makeText(mContext, "change player " + num + " to " + droppedNum, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext, "這樣子換不了人喔", Toast.LENGTH_SHORT).show();
                }
                
                // restore background color
                if(desPv instanceof RelativeLayout){
                    // bench players
                    v.setBackgroundColor(Color.TRANSPARENT);
                }else{
                    // change starters's background to white
                    v.setBackgroundColor(Color.argb(255, 255, 255, 255));
                }
                
                break;
            //the drag and drop operation has concluded.
            case DragEvent.ACTION_DRAG_ENDED:
                //v.setBackground(normalShape);    //go back to normal shape
                break;
            default:
                break;
        }
        return true;
    }
}
