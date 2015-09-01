package com.anklebreaker.basketball.tw.summary;

import java.lang.ref.WeakReference;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.View;

public class CustomDragShadowBuilder extends View.DragShadowBuilder {

    private final WeakReference<View> mView;
    Bitmap tmpBitmap = null;
    Activity mActivity;
    CustomDragShadowBuilder(Activity aa, View view) {
        super();
        view.setDrawingCacheEnabled(true);
        tmpBitmap = view.getDrawingCache();
        mView = new WeakReference<View>(view);
    }
    
    @Override
    public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
        final View view = mView.get();
        if (view != null) {
            shadowSize.set(view.getWidth(), view.getHeight());
            shadowTouchPoint.set(shadowSize.x, shadowSize.y*2);
        } else {
            
        }
    }

    
    @Override
    public void onDrawShadow(Canvas canvas) {
        final View view = mView.get();
        if (view != null) {
            canvas.drawBitmap(tmpBitmap, 0, 0, null);
        } else {
            
        }
    }
}