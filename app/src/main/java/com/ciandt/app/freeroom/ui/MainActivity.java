package com.ciandt.app.freeroom.ui;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.ciandt.app.freeroom.R;
import com.ciandt.app.freeroom.adapter.RoomAdapter;
import com.ciandt.app.freeroom.constants.Constants;
import com.ciandt.app.freeroom.model.Building;
import com.ciandt.app.freeroom.service.AlarmReceiver;
import com.ciandt.app.freeroom.util.GridAutofitLayoutManager;
import com.ciandt.app.freeroom.util.Util;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//"http://ciandt-i.dev.smartcanvas.com/free+room"
public class MainActivity extends Activity implements RoomAdapter.OnItemClickListener, ValueEventListener {
    AlarmReceiver alarm = new AlarmReceiver();
    private RecyclerView mRecyclerView;
    private RoomAdapter mAdapter;
    private Firebase mFirebase;
    private ImageView mButtonSettings;
    private int mCurrentIndex;
    private List<Building> mDataSet;
    private boolean isResumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initHideMenuNavigation();
        alarm.setAlarm(this);

        initFirebase();
        initRecyclerView();
        initButton();
    }

    private void initButton() {
        mButtonSettings = (ImageView) findViewById(R.id.imageView);

        mButtonSettings.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent, 1);
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1  && resultCode == RESULT_OK){
            if(alarm != null) alarm.cancelAlarm(this);
            finish();
        }
    }

    private void initFirebase() {
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase(Constants.URL_FIREBASE);
        mFirebase.child(Constants.URL_FREE_ROOM).addValueEventListener(this);
        mFirebase.child(Constants.URL_BUILDING).addValueEventListener(this);
        mFirebase.child(Constants.URL_TIME_RELOAD).addValueEventListener(this);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mDataSet = new ArrayList<>();
        mAdapter = new RoomAdapter(mDataSet);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new GridAutofitLayoutManager(this, 100));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initHideMenuNavigation() {
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(flags);
                }
            }
        });
    }

    private void loadBuildings() {
        Log.d("Log", "loadRooms");

        if(isResumed) {
            List<Building> buildings = Util.convertJsonToList(Util.getBuildings(this));
            mDataSet.clear();
            mDataSet.addAll(buildings);
            mCurrentIndex = 0;
            mAdapter.setItemSelected(mCurrentIndex);
            loadPage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        loadBuildings();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus)
            initHideMenuNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int index, Building building) {
        mCurrentIndex = index;
        mAdapter.setItemSelected(mCurrentIndex);
        loadPage();
    }

    private void loadPage(){
        if(isResumed) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            String parameter = mDataSet.get(mCurrentIndex).getParameter();
            long refresh = Util.getTimeReload(MainActivity.this);
            String url =  Util.getUrl(MainActivity.this) + "+" + parameter;
            ft.replace(R.id.fragment, MainActivityFragment.newInstance(url, 10000));
            ft.commit();
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Activity activity = MainActivity.this;
        if(activity.getPackageName() == null) return;
        if (dataSnapshot.getValue() == null) return;

        if(dataSnapshot.getKey().equals(Constants.URL_FREE_ROOM)){
            Log.d("Log", "URL_FREE_ROOM" + dataSnapshot.getValue().toString());
            Util.saveURL(activity, dataSnapshot.getValue().toString());
            loadPage();
        }else if(dataSnapshot.getKey().equals(Constants.URL_BUILDING)){
            Log.d("Log",  "URL_BUILDING" + dataSnapshot.getValue().toString());
            Util.saveBuildings(activity, dataSnapshot.getValue().toString());
            loadBuildings();
        }else if(dataSnapshot.getKey().equals(Constants.URL_TIME_RELOAD)){
            Log.d("Log", "URL_TIME_RELOAD" + dataSnapshot.getValue().toString());
            Util.saveTimeReload(activity, dataSnapshot.getValue().toString());
            loadPage();
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        Log.d("Log", "onCancelled");
    }
}
