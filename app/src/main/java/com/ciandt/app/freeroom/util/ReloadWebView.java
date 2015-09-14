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
    private Activity context;
    private Timer timer;
    private WebView wv;

    public ReloadWebView(Activity context, WebView wv) {
        this.context = context;
        this.wv = wv;

        timer = new Timer();
        timer.schedule(this,
                Constants.TIME_RELOAD * 1000,  // initial delay
                Constants.TIME_RELOAD * 1000); // subsequent rate
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
                Log.d(ReloadWebView.class.getName(), "reload");
                wv.loadUrl(Util.getUrl(context));
            }
        });
    }
}

