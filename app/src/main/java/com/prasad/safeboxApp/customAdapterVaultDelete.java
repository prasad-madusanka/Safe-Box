package com.prasad.safeboxApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Prasad on 3/20/17.
 */

public class customAdapterVaultDelete extends BaseAdapter {


    private Context context;
    private List<vaultDeleteGetterSetter> list;

    protected customAdapterVaultDelete() {
    }

    protected customAdapterVaultDelete(Context context, List<vaultDeleteGetterSetter> list) {
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
            view = inflater.inflate(R.layout.vault_delete_custom_listview, null);
        }


        TextView desc=(TextView)view.findViewById(R.id.vaultDesc);
        TextView uname=(TextView)view.findViewById(R.id.vaultUname);

        desc.setText(list.get(position).getDescription());
        uname.setText(list.get(position).getUname());
        view.setId(list.get(position).getId());

        return view;
    }
}
