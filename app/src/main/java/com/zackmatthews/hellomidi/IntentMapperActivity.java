package com.zackmatthews.hellomidi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class IntentMapperActivity extends Activity {

    public static final int TYPE_TASKER=10;
    public static final int TYPE_APPS=11;
    int TYPE;

    private ListView mIntentListView;
    private Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_mapper);

        mContext = this;


        Intent sender = getIntent();
        if(sender != null) {
            this.TYPE =sender.getIntExtra("TYPE", TYPE_TASKER);
        }

        mIntentListView = (ListView) findViewById(R.id.intent_list);
        final ArrayList<EventTriggerHelper.AppInfo> appInfos =
                (this.TYPE == TYPE_APPS) ?EventTriggerHelper.instance().getInstalledApps(this)
                : EventTriggerHelper.instance().getTaskerTasks(this);

        // Create a List from String Array elements
        /*List<String> appsList = new ArrayList<String>();
        for(EventTriggerHelper.AppInfo info:appInfos) {
            appsList.add(info.appname);
        }*/

        // Create an ArrayAdapter from List
        final ArrayAdapter<EventTriggerHelper.AppInfo> arrayAdapter = new AppInfoAdapter
                (this, R.layout.intent_list_item_row, appInfos);

        // DataBind ListView with items from ArrayAdapter
        mIntentListView.setAdapter(arrayAdapter);
                mIntentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                if(TYPE == TYPE_APPS){
                    Log.i("Clicked", appInfos.get(position).appname + "::" + appInfos.get(position).pname);
                    MidiHelper.instance(mContext).pickNote(EventTriggerHelper.LAUNCH_APP_KEY_PREFIX, appInfos.get(position).pname);
                }
                else if(TYPE == TYPE_TASKER){
                    Log.i("Clicked", appInfos.get(position).taskName);
                    MidiHelper.instance(mContext).pickNote(EventTriggerHelper.TASKER_TASK_KEY_PREFIX, appInfos.get(position).taskName);
                }
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
