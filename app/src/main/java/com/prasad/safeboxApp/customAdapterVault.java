package com.prasad.safeboxApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Prasad on 1/5/17.
 */

public class customAdapterVault extends BaseAdapter {

    private Context context;
    private List<vaultGetterSetter> list;

    protected customAdapterVault() {

    }

    protected customAdapterVault(Context context, List<vaultGetterSetter> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        try {

        if (view==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.vault_show_custom_listview, null);
        }


        TextView top=(TextView)view.findViewById(R.id.top);
        TextView mid=(TextView)view.findViewById(R.id.mid);
        TextView bottom=(TextView)view.findViewById(R.id.low);

        top.setText(list.get(position).getDesc());
        mid.setText(list.get(position).getUname());
        bottom.setText(list.get(position).getPwd());
        view.setId(list.get(position).getId());

        }catch (Exception ex){

        }
        return view;
    }

}
