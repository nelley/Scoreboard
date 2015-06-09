package com.anklebreaker.basketball.tw.recordboard;

import com.anklebreaker.basketball.tw.util.Timer;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

public class GameTimer extends Timer{

    public GameTimer(long millisOnTimer, long countDownInterval,
            boolean runAtStart) {
	    super(millisOnTimer, countDownInterval, runAtStart);
	    // TODO Auto-generated constructor stub
    }

	Context mContext;
    TextView timerView;
    public static GameTimer gtInstance = null;
    
    
    public static GameTimer getInstance(Context c, TextView t, long millisInFuture, long countDownInterval){
        if(gtInstance == null){
            gtInstance = new GameTimer(c, t, millisInFuture, countDownInterval);
        }
        return gtInstance;
    }
    public GameTimer(Context c, TextView t, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval, false);
        mContext = c;
        timerView = t;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        // インターバル(countDownInterval)毎に呼ばれる
        timerView.setText(Long.toString(millisUntilFinished/1000/60) + ":" + 
                          Long.toString(millisUntilFinished/1000%60) + ":" + 
                          Long.toString(millisUntilFinished/100%10));
    }

    @Override
    public void onFinish() {
        timerView.setText("00:00:0");
        Toast.makeText(mContext, "比賽結束!!!", Toast.LENGTH_SHORT).show();
    }

    public void update(long millisInFuture, long countDownInterval){
        gtInstance.cancel();
        gtInstance = new GameTimer(mContext, timerView, millisInFuture, countDownInterval);
    }
}
