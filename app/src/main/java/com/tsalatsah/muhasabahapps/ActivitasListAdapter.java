package com.tsalatsah.muhasabahapps;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by root on 02/04/16.
 */
public class ActivitasListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Activitas> mActivitasList;

    public ActivitasListAdapter(Context mContext, List<Activitas> mActivitasList) {
        this.mContext = mContext;
        this.mActivitasList = mActivitasList;
    }

    @Override
    public int getCount() {
        return mActivitasList.size();
    }

    @Override
    public Object getItem(int position) {
        return mActivitasList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_activitas_list,null);
        TextView activitasName = (TextView) v.findViewById(R.id.activitas_name);
        activitasName.setText(mActivitasList.get(position).getName());

        v.setTag(mActivitasList.get(position).getId());
        return v;
    }
}
