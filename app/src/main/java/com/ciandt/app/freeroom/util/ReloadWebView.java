package com.ciandt.app.freeroom.util;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;

import com.ciandt.app.freeroom.constants.Constants;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by thales on 26/08/2015.
 */
public class ReloadWebView extends TimerTask {
    private static final String LOG = "log";
    private Activity context;
    private Timer timer;
    private WebView wv;
    private long mTimeReload;

    public ReloadWebView(Activity context, WebView wv, long timeReload) {
        this.context = context;
        this.wv = wv;
        this.mTimeReload = timeReload;
    }

    @Override
    public void run() {
        if(context == null || context.isFinishing()) {
            // Activity killed
            this.cancel();
            return;
        }

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG, "ReloadWebView.reload");
                wv.loadUrl(Util.getUrl(context));
            }
        });
    }

    public void start(){
        Log.d(LOG, "ReloadWebView.start: " + mTimeReload);

        if(timer != null) timer.cancel();
        if(mTimeReload <= 0) mTimeReload =  Constants.TIME_RELOAD;

        timer = new Timer();
        timer.schedule(this,
                mTimeReload * 1000,  // initial delay
                mTimeReload * 1000); // subsequent rate
    }

    @Override
    public boolean cancel() {
        if(timer != null) timer.cancel();
        return super.cancel();
    }
}

