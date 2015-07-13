package com.anklebreaker.basketball.tw.util;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

public class MultiDevInit {

    private static final String TAG = "Scoreboard.MultiDevInit";
    
    public static final int LDPIX = 240;
    public static final int LDPIY = 320;
    public static final int MDPIX = 320;
    public static final int MDPIY = 480;
    public static final int HDPIX = 480;
    public static final int HDPIY = 800;
    public static final int XHDPIX = 720;
    public static final int XHDPIY = 1280;
    public static final int XXHDPIX = 1080;
    public static final int XXHDPIY = 1920;
    
    public static int xDPI = 0;
    public static int yDPI = 0;
    
    public static int xPIXEL = 0;
    public static int yPIXEL = 0;
    // main activity's indicator
    public static int IndicatorH = 0;
    // record cell
    public static int cellW = 0;
    // record row
    public static int recordRowH = 0;
    // header
    public static int headerH = 0;
    //panel in gridview
    public static int pH = 0;
    public static int pW = 0;
    //location of benchlist
    public static int mLeftMargin = 0;
    public static int mTopMargin = 0;
    //score board textview 
    public static int titleH = 0;
    public static int titleW = 0;
    //android's STATUS BAR
    public static int STATUS_BAR_H = 0;
    //player selection
    public static int playerSecH = 0;
    public static int playerSecW = 0;
    //text size
    public static float titleSize= 0;
    public static float contextSize= 0;
    //tab text size
    public static float tabSize = 0;
    //dot radius
    public static float mRadius = 0;
    
    /**
     * Constructor
     * */
    public MultiDevInit(Context mContext){
        Log.i(TAG, "MultiDevInit S");
        //get the layout information(screen size) in dp
        xDPI = (int)Utilities.getScreenDpiX(mContext);
        yDPI = (int)Utilities.getScreenDpiY(mContext);

        //convert screen size from dp to pixel
        xPIXEL = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, xDPI , mContext.getResources().getDisplayMetrics());
        yPIXEL = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, yDPI , mContext.getResources().getDisplayMetrics());
        
        IndicatorH = yPIXEL/10;
        // for record table's row
        recordRowH = yPIXEL/11;
        
        // for 16 record columns
        cellW = xPIXEL/16;
        
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            STATUS_BAR_H = mContext.getResources().getDimensionPixelSize(resourceId);
        } 
        
        headerH = (yPIXEL - IndicatorH - STATUS_BAR_H)/7;
        
        mLeftMargin = 0;//xPIXEL*(2/3);
        mTopMargin = 0;//GVH/4;
        
        playerSecH = pH;
        playerSecW = xPIXEL/2;
        
        // set the text font size
        if(xPIXEL <= LDPIX && yPIXEL <= LDPIY){
            titleSize = 10;
            contextSize = 18;
            tabSize = 4;
            mRadius = 4;
        }else if((xPIXEL >= LDPIX && xPIXEL <= MDPIX) && 
                 (yPIXEL >= LDPIY && yPIXEL <= MDPIY)){
            titleSize = 12;
            contextSize = 24;
            tabSize = 7;
            mRadius = 4;
        }else if((xPIXEL >= MDPIX && xPIXEL <= HDPIX) &&
                 (yPIXEL >=MDPIY && yPIXEL <= HDPIY)){
            titleSize = 14;
            contextSize = 28;
            tabSize = 10;
            mRadius = 6;
        }else if((xPIXEL >= HDPIX && xPIXEL <= XHDPIX) && 
                 (yPIXEL >= HDPIY && yPIXEL <= XHDPIY)){
            titleSize = 16;
            contextSize = 32;
            tabSize = 13;
            mRadius = 10;
        }else if((xPIXEL >= XHDPIX &&xPIXEL <= XXHDPIX) && 
                 (yPIXEL >= XHDPIY && yPIXEL <= XXHDPIY)){
            titleSize = 18;
            contextSize = 36;
            tabSize = 16;
            mRadius = 12;
        }else{
            titleSize = 12;
            contextSize = 18;
            tabSize = 10;
            mRadius = 6;
        }
        
    }
}
