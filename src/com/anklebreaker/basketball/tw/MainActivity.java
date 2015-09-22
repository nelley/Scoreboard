package com.anklebreaker.basketball.tw;

import com.anklebreaker.basketball.tw.recordboard.TeamObj;
import com.anklebreaker.basketball.tw.tab.BasketBallAdapter;
import com.anklebreaker.basketball.tw.tab.CustomTab;
import com.anklebreaker.basketball.tw.util.LogOutput;
import com.anklebreaker.basketball.tw.util.MultiDevInit;
import com.anklebreaker.basketball.tw.util.ViewServer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "Scoreboard.MainActivity";

    private ViewPager pager;
    private BasketBallAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.simple_tabs);
        ViewServer.get(this).addWindow(this);

        Log.i(TAG, "onCreate");
        LogOutput.run();

        // initilize the layout param for multi screen
        new MultiDevInit(this.getApplicationContext());
        // initialize teamobj
        TeamObj.getInstance(this.getApplicationContext());

        CookieSyncManager.createInstance(this);
        
        adapter = new BasketBallAdapter(getSupportFragmentManager(), this);
        pager = (ViewPager)findViewById(R.id.pager);

        Log.i(TAG, "set pager");
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        //set the initial page
        Log.i(TAG, "set indicator");
        CustomTab indicator = (CustomTab)findViewById(R.id.indicator);
        indicator.getLayoutParams().height = MultiDevInit.IndicatorH;
        indicator.setViewPager(pager, 0);
    }

    /**
     * Called when the activity will start interacting with the user.
     * */
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        ViewServer.get(this).setFocusedWindow(this);
    }
    
    /**
     * Called when the activity is no longer visible to the user.
     * */
    protected void onStop() {
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onStop");
        super.onStop();
    }
    
    /**
     * Called after your activity has been stopped, prior to it being started again.
     * */
    protected void onRestart(){
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onRestart");
        super.onRestart();
    }
    
    /**
     * Called when the activity is becoming visible to the user.
     * */
    protected void OnStart(){
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onStart");
        super.onStart();
    }

    /**
     * Called when the activity is becoming visible to the user.
     * */
    protected void OnDestroy(){
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onDestroy");
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    /**
     * 管理所有從intent返回的activity
     * CheckInPage裡,經startActivityForResult()啟動的activity結束後的回傳值
     * CheckInActivity裡可設定resultCode
     * CheckInPage裡可設定requestCode
     * CheckInActivity可設定data
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();
        switch (requestCode) {
            //CheckInActivity
            case 999:
                if (resultCode == Activity.RESULT_OK) {

                }else{

                }
                break;
                //SummaryActivity
            case 998:
                if(resultCode == Activity.RESULT_OK){

                }else{

                }
                break;
            default:
                break;
        }
    }
    // BACKボタンが押された時の処理
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            // rewrite by using DialogFragments???
            showDialog(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    /**
     * close app dailog
     * */
    @Override
    public Dialog onCreateDialog(int id) {
        switch (id) {
        case 0:
            return new AlertDialog.Builder(MainActivity.this)
            .setMessage("要關閉球經救星嗎?")
            .setCancelable(false)
            .setPositiveButton("結束", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // close the app
                    MainActivity.this.finish();
                    System.exit(0);
                }
            })
            // 「終了しない」が押された時の処理
            .setNegativeButton("繼續使用", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            })
            .create();
        }
        return null;
    }

    
}