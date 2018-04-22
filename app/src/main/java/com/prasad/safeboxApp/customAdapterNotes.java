package com.prasad.safeboxApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Prasad on 3/10/17.
 */

public class customAdapterNotes extends BaseAdapter {

    private Context context;
    private List<notesGetterSetter> list;

    protected customAdapterNotes() {
    }

    protected customAdapterNotes(Context context, List<notesGetterSetter> list) {
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
        if (view==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.notes_custom_listview, null);
        }
        TextView header=(TextView)view.findViewById(R.id.header);
        TextView date=(TextView)view.findViewById(R.id.date);

        header.setText(list.get(position).getHeader());
        date.setText(list.get(position).getBody());
        view.setId(list.get(position).getId());

        return view;
    }
}
