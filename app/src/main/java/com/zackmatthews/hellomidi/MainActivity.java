package com.zackmatthews.hellomidi;

import android.graphics.Color;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MidiHelper.MidiHelperEventListener{
    private TextView statusTextView;
    private ScrollView scrollView;
    private FloatingActionButton fab;
    private View.OnClickListener connectToDeviceClickEvent, openMidiMapperClickEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor(Color.RED);
        connectToDeviceClickEvent = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventTriggerHelper.instance().getTaskerTasks(MainActivity.this);
                MidiHelper.instance(MainActivity.this).presentDevices();
            }
        };
        openMidiMapperClickEvent = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IntentMapperActivity.class);
                startActivity(intent);
            }
        };
        //fab.setOnClickListener(connectToDeviceClickEvent);
        fab.setOnClickListener(openMidiMapperClickEvent);

        MidiHelper.instance(MainActivity.this).registerMidiHelperEventListener(this);

        statusTextView = (TextView)findViewById(R.id.statusText);
        scrollView = (ScrollView)findViewById(R.id.scrollview);
    }

    @Override
    public void onMidiHelperStatusEvent(final String statusText) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusTextView.append("\n" + statusText + "\n");
                scrollView.fullScroll(View.FOCUS_DOWN);

                if(statusText.contains(EventTriggerHelper.LAUNCH_APP_EVENT_TRIGGER)){
                    EventTriggerHelper.instance().openApp(MainActivity.this, "com.google.android.talk");
                }
            }
        });
    }

    @Override
    public void onDeviceStateChange(boolean isConnected) {
        if(isConnected){
            fab.setOnClickListener(openMidiMapperClickEvent);
            fab.setBackgroundColor(Color.GREEN);
        }
        else{
            fab.setOnClickListener(connectToDeviceClickEvent);
            fab.setBackgroundColor(Color.RED);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
