package com.ciandt.app.freeroom.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.ciandt.app.freeroom.R;
import com.ciandt.app.freeroom.util.ReloadWebView;
import com.ciandt.app.freeroom.util.Util;
import com.ciandt.app.freeroom.adapter.RoomAdapter;
import com.ciandt.app.freeroom.constants.Constants;
import com.ciandt.app.freeroom.model.Room;
import com.ciandt.app.freeroom.util.GridAutofitLayoutManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements RoomAdapter.OnItemClickListener {
    private WebView mWebView;
    private RecyclerView mRecyclerView;
    private RoomAdapter mAdapter;
    private Timer timer;
    private List<Room> mDataSet;
    private Firebase mFirebaseUrl;
    private Firebase mFirebaseRoom;
    private int mCurrentIndex;
    private ImageView mButtonSettings;

    public static MainActivityFragment newInstance() {
        return new MainActivityFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initFirebase();
        initWebView(view);
        initRecyclerView(view);
        initButton(view);

        return view;
    }

    private void initFirebase() {
        Firebase.setAndroidContext(this.getActivity());
        mFirebaseUrl = new Firebase(Constants.URL_FIREBASE);
        mFirebaseUrl.child(Constants.URL_FREE_ROOM).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Activity activity = MainActivityFragment.this.getActivity();

                Log.d("Log", dataSnapshot.getValue().toString());

                if (dataSnapshot.getValue() != null && activity != null && activity.getPackageName() != null) {
                    Util.saveURL(activity, dataSnapshot.getValue().toString());
                    loadUrl();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Log", "onCancelled");
                loadUrl();
            }
        });

        mFirebaseRoom = new Firebase(Constants.URL_FIREBASE);
        mFirebaseRoom.child(Constants.ROOM).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Activity activity = MainActivityFragment.this.getActivity();

                Log.d("Log", dataSnapshot.getValue().toString());

                if (dataSnapshot.getValue() != null && activity != null && activity.getPackageName() != null) {
                    Util.saveRooms(activity, dataSnapshot.getValue().toString());
                    loadRooms();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Log", "onCancelled");
            }
        });
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new ReloadWebView(this.getActivity(), mWebView);
    }

    private void initButton(View view) {
        mButtonSettings = (ImageView) view.findViewById(R.id.imageView);

        mButtonSettings.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }

    private void initRecyclerView(View layoutView) {
        mRecyclerView = (RecyclerView) layoutView.findViewById(R.id.recycler_view);

        mDataSet = new ArrayList<>();
        mAdapter = new RoomAdapter(mDataSet);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new GridAutofitLayoutManager(getActivity(), 100));
        mRecyclerView.setAdapter(mAdapter);

        loadRooms();
    }

    private void initWebView(View view) {
        mWebView = (WebView) view.findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        mWebView.setLongClickable(false);

    }

    private void loadRooms() {
        Log.d("Log", "loadRooms");

        List<Room> rooms = Util.convertJsonToList(Util.getRooms(this.getActivity()));
        mDataSet.clear();
        mDataSet.addAll(rooms);

        loadUrl(0);
    }

    private void loadUrl() {
        String url = mDataSet.size() > mCurrentIndex ? "+" + mDataSet.get(mCurrentIndex).getParameter() : "";
        url = Util.getUrl(this.getActivity()) + url;

        Log.d("Log", url);
        mWebView.loadUrl("");
        mWebView.loadUrl(url);
    }

    private void loadUrl(int index) {
        mCurrentIndex = index;
        mAdapter.setItemSelected(mCurrentIndex);
        loadUrl();
    }

    @Override
    public void onItemClick(View view, int index, Room room) {

        Log.d("Log", "onItemClick");
        loadUrl(index);
    }
}
