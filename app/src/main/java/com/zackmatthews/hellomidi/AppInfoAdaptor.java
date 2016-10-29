package com.zackmatthews.hellomidi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cfhowes on 10/28/16.
 */

class AppInfoAdapter extends ArrayAdapter<EventTriggerHelper.AppInfo> {
    Context context;
    int layoutResourceId;
    ArrayList<EventTriggerHelper.AppInfo> data = null;

    public AppInfoAdapter(Context context, int layoutResourceId, ArrayList<EventTriggerHelper.AppInfo> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AppInfoHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new AppInfoHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            row.setTag(holder);
        }
        else
        {
            holder = (AppInfoHolder)row.getTag();
        }

        EventTriggerHelper.AppInfo ai = data.get(position);
        if(ai.appname != null) {
            holder.txtTitle.setText(ai.appname + "-" + String.valueOf(ai.mappedKey));
        }
        if(ai.taskName != null){
            holder.txtTitle.setText(ai.taskName + "-" + String.valueOf(ai.mappedKey));
        }
        if(ai.icon != null) {
            holder.imgIcon.setImageDrawable(ai.icon);
        }
        return row;
    }

    class AppInfoHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
