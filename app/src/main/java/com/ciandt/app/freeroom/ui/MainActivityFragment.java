package com.ciandt.app.freeroom.ui;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ciandt.app.freeroom.R;
import com.ciandt.app.freeroom.util.ReloadWebView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment{
    private static final String LOG = "Log";

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private String mUrl;
    private long mRefresh;

    private static final String ARG_URL = "url";
    private static final String ARG_REFRESH = "refresh";
    private ReloadWebView mReloadWebView;

    public static MainActivityFragment newInstance(String url, long refresh) {
        Log.d(LOG, "MainActivityFragment.newInstance");

        MainActivityFragment fragment = new MainActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putLong(ARG_REFRESH, refresh);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mUrl = getArguments().getString(ARG_URL);
        mRefresh = getArguments().getLong(ARG_REFRESH);

        initWebView(view);
        return view;
    }

    private void initWebView(View view) {
        mWebView = (WebView) view.findViewById(R.id.webview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptThirdPartyCookies(mWebView, true);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.INVISIBLE);

                if (mReloadWebView != null) {
                    mReloadWebView.cancel();
                }

                mReloadWebView = new ReloadWebView(MainActivityFragment.this.getActivity(), mWebView, mRefresh);
                mReloadWebView.start();
            }
        });
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        mWebView.setLongClickable(false);
        mWebView.loadUrl(mUrl);

        Log.d(LOG, mUrl);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mReloadWebView != null) {
            mReloadWebView.cancel();
        }
    }
}
