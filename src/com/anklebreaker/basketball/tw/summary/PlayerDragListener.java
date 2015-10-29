package com.anklebreaker.basketball.tw.summary;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.recordboard.RivalPlayerObj;
import com.anklebreaker.basketball.tw.recordboard.TeamObj;
import com.anklebreaker.basketball.tw.tab.BasketFragment;
import com.anklebreaker.basketball.tw.util.Utilities;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerDragListener implements OnDragListener{

    Context mContext = null;
    private final String REGEX_STRING = "號";
    private final String CLIP_LABEL = "TEAM_A";
   
    public PlayerDragListener(Activity c){
        mContext = c;
    }
    
    @Override
    public boolean onDrag(View v, DragEvent event) {

        // Handles each of the expected events
        //Drawable normalShape = mContext.getResources().getDrawable(R.drawable.normal_color);
        //Drawable targetShape = mContext.getResources().getDrawable(R.drawable.target_shape);
        
        TextView draggedV = (TextView)event.getLocalState();
        //View dragPv = (View) draggedV.getParent();
        
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
                ClipData clipData = event.getClipData();
                // retrieve the LABEL info from dragged item
                String Label = clipData.getDescription().toString();
                // retrieve the data from dragged item
                String num = (String) clipData.getItemAt(0).getText();
                num = regEx(num);
                
                // retrieve the dragged player
                int drag = posFinder(num, Label);
                
                // retrieve the data from dropped location
                String droppedNum = (String) destinationV.getText();
                droppedNum = regEx(droppedNum);
                
                // retrieve the dropped player
                int drop = posFinder(droppedNum, Label);

                playerChange(drag, drop, draggedV, destinationV, Label);
                Toast.makeText(mContext, num + " 換 " + droppedNum, Toast.LENGTH_SHORT).show();
                
                // restore background color
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
            //the drag and drop operation has concluded.
            case DragEvent.ACTION_DRAG_ENDED:
                break;
            default:
                break;
        }
        return true;
    }
    
    /**
     * detect whether the String contains REGEX_STRING
     * */
    public String regEx(String num){
        Pattern p = Pattern.compile(REGEX_STRING);
        Matcher m = p.matcher(num);
        if(!m.find()){
            num = num + REGEX_STRING;
        }
        
        return num;
    }
    
    /**
     * retrieve the dragged player
     * */
    public int posFinder(String num, String mLabel){
        
        int cnt = 0;
        // if the passed info comes from team A
        if(mLabel.indexOf(CLIP_LABEL) != -1){
             for(; cnt< PlayerObj.playerMap.size(); cnt++){
                String pNum = PlayerObj.playerMap.get(cnt).getPlayerNum();
                if(pNum.equals(num)){
                    break;
                }
            }
        }else{
            for(; cnt< RivalPlayerObj.rivalPlayerMap.size(); cnt++){
                String pNum = RivalPlayerObj.rivalPlayerMap.get(cnt).getPlayerNum();
                if(pNum.equals(num)){
                    break;
                }
            }
        }
        
        
    	return cnt;
    }
    
    /**
     * change the player to starter(or bench)
     * */
    public void playerChange(int mDrag, int mDrop, TextView dragV, TextView desV, String mLabel){
        // change the player of team A
        if(mLabel.indexOf(CLIP_LABEL) != -1){
            if(PlayerObj.playerMap.get(mDrop).getIsOnPlay() != 
                PlayerObj.playerMap.get(mDrag).getIsOnPlay()){

                // change the status
                PlayerObj.playerReplace(mDrop);
                PlayerObj.playerReplace(mDrag);
                
                // change the number in layout
                desV.setText(PlayerObj.playerMap.get(mDrag).getPlayerNum());
                dragV.setText(PlayerObj.playerMap.get(mDrop).getPlayerNum());
                
                // swap the position
                Collections.swap(PlayerObj.playerMap, mDrag, mDrop);
                
                // update the layout
                //View rootView = (View) desV.getRootView();
                //ListView mListView = (ListView) rootView.findViewById(R.id.player_list);
                
                BasketFragment.listViewA.setAdapter(TeamObj.mPlayerListAdapter);
                TeamObj.mPlayerListAdapter.notifyDataSetChanged();

            }else{
                Toast.makeText(mContext, "這樣子換不了人喔", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(RivalPlayerObj.rivalPlayerMap.get(mDrop).getIsOnPlay() != 
                RivalPlayerObj.rivalPlayerMap.get(mDrag).getIsOnPlay()){

                // change the status
                RivalPlayerObj.playerReplace(mDrop);
                RivalPlayerObj.playerReplace(mDrag);
                    
                // change the number in layout
                desV.setText(RivalPlayerObj.rivalPlayerMap.get(mDrag).getPlayerNum());
                dragV.setText(RivalPlayerObj.rivalPlayerMap.get(mDrop).getPlayerNum());
                
                // swap the position
                Collections.swap(RivalPlayerObj.rivalPlayerMap, mDrag, mDrop);
                
                // update the layout
                //View rootView = (View) desV.getRootView();
                //ListView mListView = (ListView) rootView.findViewById(R.id.player_list);
                
                BasketFragment.listViewB.setAdapter(TeamObj.mRivalPlayerListAdapter);
                TeamObj.mRivalPlayerListAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(mContext, "這樣子換不了人喔", Toast.LENGTH_SHORT).show();
            }
        }
        
    }
}
