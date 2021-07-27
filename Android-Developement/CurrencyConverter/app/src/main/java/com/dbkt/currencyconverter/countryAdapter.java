package com.dbkt.currencyconverter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class countryAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> countrynames;

    public countryAdapter(Context context, List<String> countrynames){
        this.context = context;
        this.countrynames = countrynames;
    }
    @Override
    public int getCount() {
        return countrynames!=null?countrynames.size():0;
    }

    @Override
    public Object getItem(int position) {
        return (Object) countrynames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_country, parent, false);
            rootView=convertView;
        }
        else{
            rootView = convertView;
        }

        TextView txtName = rootView.findViewById(R.id.sp_selector);
        txtName.setText(countrynames.get(position));
        return convertView;
    }
}
