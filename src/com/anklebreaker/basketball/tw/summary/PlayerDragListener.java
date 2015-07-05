package com.anklebreaker.basketball.tw.summary;

import java.util.ArrayList;

import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.recordboard.PlayerObj;
import com.anklebreaker.basketball.tw.recordboard.RecordGridViewAdapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.Toast;

public class PlayerDragListener implements OnDragListener{

    Context mContext = null;
   
    public PlayerDragListener(Activity c){
        mContext = c;
    }
    
    @Override
    public boolean onDrag(View v, DragEvent event) {

        // Handles each of the expected events
        Drawable normalShape = mContext.getResources().getDrawable(R.drawable.normal_color);
        Drawable targetShape = mContext.getResources().getDrawable(R.drawable.target_shape);
        
        switch (event.getAction()) {
        //signal for the start of a drag and drop operation.
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                //if false, go to drag_ended
                break;
                
                //the drag point has entered the bounding box of the View
            case DragEvent.ACTION_DRAG_ENTERED:
                //Toast.makeText(mContext, "ENTERED", Toast.LENGTH_LONG).show();
                // have to handle different layout object(textview to tablerow)
                v.setBackgroundColor(Color.YELLOW);    //change the shape of the view

                break;
                
                //the user has moved the drag shadow outside the bounding box of the View
            case DragEvent.ACTION_DRAG_EXITED:
                //Toast.makeText(mContext, "EXITED", Toast.LENGTH_LONG).show();
                v.setBackgroundColor(Color.BLUE);    //change the shape of the view back to normal
                break;
                
            //drag shadow has been released,the drag point is within the bounding box of the View
            case DragEvent.ACTION_DROP:
                // if the view is the bottomlinear, we accept the drag item
                String num = (String) event.getClipData().getItemAt(0).getText();
                //GridView currentGV = (GridView)v;
                //prepare for swaping
                //PlayerObj tmp = (PlayerObj)currentGV.getItemAtPosition(PLAYER_POSITION);
                //Bitmap benchImg = tmp.getImage();
                //String benchPlayer = tmp.getPlayerNum();
                
                //update the value and GridView
                //get info from dragged view
                //PlayerView pV = (PlayerView)event.getLocalState();
                //PlayerObj mItem = new PlayerObj(pV.getPlayerImg(), pV.getPlayerNum());
                //mGridArray.set(PLAYER_POSITION, mItem);
                //mGridAdapter.notifyDataSetChanged();
                //exchange the player info to menu's view
                //PlayerObj newMenu = new PlayerObj(benchImg, benchPlayer);
                //update the menu's view
                //TeamObj.getBenchArray().set(pV.getmPosition(), newMenu);
                //pV.getBenchAdapter().notifyDataSetChanged();
                
                Toast.makeText(mContext, num, Toast.LENGTH_SHORT).show();
            
                
                
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
