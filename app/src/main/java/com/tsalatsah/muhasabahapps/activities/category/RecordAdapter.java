package com.tsalatsah.muhasabahapps.activities.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tsalatsah.muhasabahapps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ibnujakaria on 28/04/16.
 */
public class RecordAdapter extends BaseAdapter {

    private Context context;
    private JSONArray records;
    private LayoutInflater inflater;

    public RecordAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setJSONResponse(JSONArray records) {
        this.records = records;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return records.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            JSONObject record = new JSONObject(records.getString(position));
            return (Object) record;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_sub_category, null);

        try {
            JSONObject record = (JSONObject) getItem(position);
            TextView textView = (TextView) view.findViewById(R.id.textView);
            textView.setText(record.getString("name") + " | " + record.getString("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
