package com.gkpoter.voiceShare.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.gkpoter.voiceShare.R;

import java.util.List;

/**
 * Created by dy on 2016/10/22.
 */
public class InformationAdapter extends BaseAdapter {
    private List<String> data;
    private Context context;

    public InformationAdapter(List<String> data,Context context){
        this.data=data;
        this.context=context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.listview_adapter_information,null);

        return view;
    }
}
